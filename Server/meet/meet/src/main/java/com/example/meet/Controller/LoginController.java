package com.example.meet.Controller;

import com.example.meet.Model.LoginRequest;
import com.example.meet.Model.Test;
import com.example.meet.Service.LoginService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.SessionCookieOptions;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.CookieParam;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.NewCookie;
import java.net.URI;
import java.util.concurrent.TimeUnit;

@RestController
public class LoginController {

    @Autowired
    LoginService loginService;

    @GetMapping("/login")
    public void login(){
        // ...
    }

    @PostMapping("/sessionLogin")
    public ResponseEntity<String> createSessionCookie(@RequestBody LoginRequest request) {
        // Get the ID token sent by the client
        String idToken = request.getIdToken();
        // Set session expiration to 5 days.
        long expiresIn = TimeUnit.DAYS.toMillis(5);
        SessionCookieOptions options = SessionCookieOptions.builder()
                .setExpiresIn(expiresIn)
                .build();

        try {
            // Xác thực token từ phía firebase
            String sessionCookie = FirebaseAuth.getInstance().createSessionCookie(idToken, options);
            System.out.println("Session được cấp : " +sessionCookie);
            // Set cookie policy parameters as required.
            return ResponseEntity.ok()
                    .header("Set-Cookie", "session=" + sessionCookie)
                    .build();
        } catch (FirebaseAuthException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Failed to create a session cookie");
        }
    }

    @PostMapping("/profile")
    public ResponseEntity<String> verifySessionCookie(@RequestHeader("Cookie") String sessionCookieValue) {
        try {
            String prefix = "session=";
            String result = sessionCookieValue.substring(prefix.length());
            // Verify the session cookie. In this case an additional check is added to detect
            // if the user's Firebase session was revoked, user deleted/disabled, etc.
            final boolean checkRevoked = false;
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifySessionCookie(
                    result, checkRevoked);
            return ResponseEntity.ok("Xác minh thành công!");
        } catch (FirebaseAuthException e) {
            // Session cookie is unavailable, invalid or revoked. Force user to login.
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Session cookie is invalid or expired. Please log in again.");
        }
    }

    @PostMapping("/sessionLogout")
    public ResponseEntity<Void> clearSessionCookie(@CookieValue("session") Cookie cookie) {
        final int maxAge = 0;
        NewCookie newCookie = new NewCookie(cookie, null, maxAge, true);
        return ResponseEntity.ok().build();
    }


}
