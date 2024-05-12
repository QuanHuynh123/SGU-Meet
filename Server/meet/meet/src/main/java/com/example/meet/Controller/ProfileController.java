package com.example.meet.Controller;

import com.example.meet.Enum.RegistrationStatus;
import com.example.meet.Model.AccountUser;
import com.example.meet.Model.ChatroomModel;
import com.example.meet.Model.User;
import com.example.meet.Service.RegisterService;
import com.example.meet.Service.UserService;
import com.example.meet.Service.*;
import com.google.firebase.auth.FirebaseAuthException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
public class ProfileController {

    @Autowired
    UserService userService;
    @Autowired
    testService testService;

    @Autowired
    RegisterService registerService;

    @GetMapping("/profile")
    public User getProfileUser(@RequestParam String userId) throws ExecutionException, InterruptedException {
        return userService.getProfileUser(userId);
    }


    @PutMapping("/updateProfile")
    public RegistrationStatus updateProfileUser(@RequestBody User user) throws ExecutionException, InterruptedException, FirebaseAuthException {
        System.out.println("update profile");
        return registerService.updateAccountUser(user);
    }

}
