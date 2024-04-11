package com.example.meet.Controller;

import com.example.meet.Enum.RegistrationStatus;
import com.example.meet.Model.AccountUser;
import com.example.meet.Service.RegisterService;
import com.google.firebase.auth.FirebaseAuthException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.*;

@RestController
public class RegisterController {
    @Autowired
    RegisterService registerService;

    @PostMapping("/register")
    public RegistrationStatus registerAccount(@RequestBody AccountUser accountUser) throws FirebaseAuthException {
        System.out.println("Call register");
        return registerService.registerAccountUser(accountUser);
    }

//    @PutMapping("/register/updatePassword")
//    public RegistrationStatus updateAccount(@RequestBody AccountUser accountUser) throws FirebaseAuthException {
//        return registerService.updateAccountUser(accountUser);
//    }
}
