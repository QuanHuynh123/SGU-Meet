package com.example.meet.Service;

import com.example.meet.Enum.RegistrationStatus;
import com.example.meet.Model.AccountUser;
import com.example.meet.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegisterService {

    @Autowired
    UserService userService;

    public RegistrationStatus registerAccountUser( AccountUser accountUser) throws FirebaseAuthException {
        try {
            UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                    .setEmail(accountUser.getEmail())
                    .setEmailVerified(accountUser.isEmailVerified())
                    .setPassword(accountUser.getPassword())
                    .setDisplayName(accountUser.getName())
                    .setDisabled(accountUser.isDisabled());
            UserRecord userRecord = FirebaseAuth.getInstance().createUser(request);
            System.out.println("Successfully created new user: " + userRecord.getUid());

            //User user = new User(null,userRecord.getEmail(),null,userRecord.getDisplayName());

            //userService.createUser();
            return RegistrationStatus.SUCCESS;
        } catch (FirebaseAuthException e) {
            // Xử lý các trường hợp lỗi khác nhau
            if ("EMAIL_EXISTS".equals(e.getErrorCode())) {
                return RegistrationStatus.EMAIL_ALREADY_EXISTS;
            } else {
                // Xử lý các trường hợp lỗi khác
                return RegistrationStatus.FAILURE;
            }
        } catch (IllegalArgumentException e) {
            // Xử lý lỗi về mật khẩu không đủ dài
            return RegistrationStatus.INVALID_PASSWORD;
        }
    }

    public RegistrationStatus updateAccountUser( AccountUser accountUser) throws FirebaseAuthException {
        String uid = getUidUser(accountUser.getEmail());
        try {
            UserRecord.UpdateRequest request = new UserRecord.UpdateRequest(uid)
                    .setEmail(accountUser.getEmail())
                    .setEmailVerified(accountUser.isEmailVerified())
                    .setPassword(accountUser.getPassword())
                    .setDisplayName(accountUser.getName())
                    .setDisabled(accountUser.isDisabled());
            UserRecord userRecord = FirebaseAuth.getInstance().updateUser(request);
            return RegistrationStatus.SUCCESS;
        }catch (FirebaseAuthException e) {
            // Xử lý các trường hợp lỗi khác nhau
            if ("EMAIL_EXISTS".equals(e.getErrorCode())) {
                return RegistrationStatus.EMAIL_ALREADY_EXISTS;
            } else {
                // Xử lý các trường hợp lỗi khác
                return RegistrationStatus.FAILURE;
            }
        }
    }

    public String getUidUser(String email) throws FirebaseAuthException {
        UserRecord userRecord = FirebaseAuth.getInstance().getUserByEmail(email);
        return userRecord.getUid();
    }


}