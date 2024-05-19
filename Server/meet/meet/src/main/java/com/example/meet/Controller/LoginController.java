package com.example.meet.Controller;

import com.example.meet.Model.LoginRequest;
import com.example.meet.Service.LoginService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.SessionCookieOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.NewCookie;
import java.util.concurrent.TimeUnit;

@RestController
public class LoginController {

    @Autowired
    LoginService loginService;

    @PostMapping("/sessionLogin")
    public LoginRequest createSessionCookie(@RequestBody LoginRequest request) {
        System.out.println("Tạo sessionCookie");
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
            request = new LoginRequest(sessionCookie);
            // Set cookie policy parameters as required.
            return  request;
        } catch (FirebaseAuthException e) {
            return null;
        }
    }

    @PostMapping("/verifyCookie")
    public Boolean verifySessionCookie(@RequestHeader("cookie") String sessionCookieValue) {
        System.out.println("Xác thực cookie");
        if (sessionCookieValue == null ) System.out.println("Session Cookie is null");
        else System.out.println(sessionCookieValue);
        try {
            final boolean checkRevoked = false;
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifySessionCookie(
                    sessionCookieValue, checkRevoked);
            System.out.println(decodedToken);
            return true;
        } catch (FirebaseAuthException e) {
            // Session cookie is unavailable, invalid or revoked. Force user to login.
            System.out.println("Call verify Cookie failed");
            return false;
        }
    }


    @PostMapping("/sessionLogout")
    public Boolean clearSessionCookieAndRevoke(@RequestHeader("cookie") String sessionCookieValue) {
        try {
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifySessionCookie(sessionCookieValue);
            FirebaseAuth.getInstance().revokeRefreshTokens(decodedToken.getUid());

            System.out.println("Sau khi da logout " + decodedToken.getUid() + " " + decodedToken.getEmail());
            //final int maxAge = 0;
            //NewCookie newCookie = new NewCookie(cookie, null, maxAge, true);

            return true;
        } catch (FirebaseAuthException e) {
            return false;
        }
    }

}
