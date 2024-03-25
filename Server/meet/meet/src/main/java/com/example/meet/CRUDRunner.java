package com.example.meet;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

@SpringBootApplication
public class CRUDRunner {


    public static void main(String[] args) throws IOException, FirebaseAuthException {

        // 2 dạng lấy tài nguyên khác !
        //InputStream serviceAccount = CRUDRunner.class.getResourceAsStream("/serviceAccountKey.json");
        /*ClassLoader classLoader = ClassLoader.class.getClassLoader();
        File file = new File(Objects.requireNonNull(classLoader.getResource("serviceAccountKey.json")).getFile());*/

        File file = new File("src/main/resources/serviceAccountKey.json");
        FileInputStream serviceAccount = new FileInputStream(file.getAbsoluteFile());

        // Kết nối Firebase
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://sgu-meet-default-rtdb.firebaseio.com")
                .build();
        FirebaseApp.initializeApp(options);

        SpringApplication.run(CRUDRunner.class,args);
    }

    public void login() throws FirebaseAuthException {

        // Truy xuất dữ liệu người dùng
        UserRecord userRecord = FirebaseAuth.getInstance().getUser("csipNVYFD0g7fDVq25MGGWXx6KB3");
        System.out.println("Successfully fetched user data: " + userRecord.getUid());

        //Tra cứu bằng email
        userRecord = FirebaseAuth.getInstance().getUserByEmail("huynhminhquan07072002@gmail.com");
        System.out.println("Successfully fetched user data: " + userRecord.getEmail());
    }
}