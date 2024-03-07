package com.example.meet.Firebase;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@SpringBootApplication
public class CRUDRunner {
    public static void main(String[] args) throws IOException {

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
}