package com.example.meet.Service;

import com.google.firebase.auth.FirebaseAuth;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

@Service
public class LoginService {

//    PostMapping("")
    public void login(String email, String password){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        System.out.println(auth);
    }
}
