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

    @GetMapping("/register")
    public RegistrationStatus registerAccount(@RequestBody AccountUser accountUser) throws FirebaseAuthException {
        return registerService.registerAccountUser(accountUser);
    }

    @PutMapping("/register/update")
    public RegistrationStatus updateAccount(@RequestBody AccountUser accountUser) throws FirebaseAuthException {
        return registerService.updateAccountUser(accountUser);
    }
}
