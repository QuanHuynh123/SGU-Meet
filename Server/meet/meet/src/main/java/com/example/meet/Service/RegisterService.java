package com.example.meet.Service;

import com.example.meet.Enum.RegistrationStatus;
import com.example.meet.Model.AccountUser;
import com.example.meet.Model.User;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.google.cloud.Timestamp;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

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

            saveUserFirestore(accountUser);

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
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveUserFirestore(AccountUser accountUser) throws ExecutionException, InterruptedException, FirebaseAuthException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        // Lấy getDocumentId làm idUser;
        String userUid = getUidUser(accountUser.getEmail());

        Date date = new Date();
        Timestamp timestamp = Timestamp.of(new java.sql.Timestamp(date.getTime()));
        String nameLowercase = accountUser.getName().toLowerCase();
        List<String> array = new ArrayList<>();
        User user = new User(accountUser.getEmail(),"",0,nameLowercase,array,array,array,array,timestamp,userUid);
        ApiFuture<DocumentReference> collectionApiFuture = dbFirestore.collection("user").add(user);
        System.out.println(collectionApiFuture.get().getId());
    }


    public String getUidUser(String email) throws FirebaseAuthException {
        UserRecord userRecord = FirebaseAuth.getInstance().getUserByEmail(email);
        return userRecord.getUid();
    }

    public RegistrationStatus updateAccountUser( User accountUser) throws FirebaseAuthException {
        String uid = getUidUser(accountUser.getEmail());
        try {
            UserRecord.UpdateRequest request = new UserRecord.UpdateRequest(uid)
                    .setEmail(accountUser.getEmail())
                    .setDisplayName(accountUser.getName());
            UserRecord userRecord = FirebaseAuth.getInstance().updateUser(request);
            System.out.println("Successfully created new user: " + userRecord.getUid());
            updateUserFirestore(accountUser);
            return RegistrationStatus.SUCCESS;
        }catch (FirebaseAuthException e) {
                // Xử lý các trường hợp lỗi khác
                return RegistrationStatus.FAILURE;
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateUserFirestore(User accountUser) throws ExecutionException, InterruptedException, FirebaseAuthException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        // Lấy getDocumentId làm idUser;
        String userUid = getUidUser(accountUser.getEmail());

        //User user = new User(accountUser.getEmail(),accountUser.getGender(),accountUser.getAge(),accountUser.getName(), accountUser.getCreatedTimestamp(),userUid);
        //DocumentReference userRef = dbFirestore.collection("user").document(userUid);
    }

}