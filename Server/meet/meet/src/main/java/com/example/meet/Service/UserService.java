package com.example.meet.Service;

import com.example.meet.Enum.RegistrationStatus;
import com.example.meet.Model.AccountUser;
import com.example.meet.Model.User;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
public class UserService {

    public User getProfileUser(String userId) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        System.out.println("USER ID LA : " + userId + " Client la : " + dbFirestore);

        // Tạo truy vấn để lấy người dùng dựa trên trường userId
        Query query = dbFirestore.collection("user").whereEqualTo("userId", userId);

        try {
            QuerySnapshot querySnapshot = query.get().get();
            if (!querySnapshot.isEmpty()) {
                DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);

                User user = documentSnapshot.toObject(User.class);

                return user;
            } else {
                System.out.println("Không tìm thấy người dùng với userId đã chỉ định.");
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return null;
    }

//    public RegistrationStatus updateAccountUser( AccountUser accountUser) throws FirebaseAuthException {
//        String uid = getUidUser(accountUser.getEmail());
//        try {
//            UserRecord.UpdateRequest request = new UserRecord.UpdateRequest(uid)
//                    .setEmail(accountUser.getEmail())
//                    .setEmailVerified(accountUser.isEmailVerified())
//                    .setPassword(accountUser.getPassword())
//                    .setDisplayName(accountUser.getName())
//                    .setDisabled(accountUser.isDisabled());
//            UserRecord userRecord = FirebaseAuth.getInstance().updateUser(request);
//            return RegistrationStatus.SUCCESS;
//        }catch (FirebaseAuthException e) {
//            // Xử lý các trường hợp lỗi khác nhau
//            if ("EMAIL_EXISTS".equals(e.getErrorCode())) {
//                return RegistrationStatus.EMAIL_ALREADY_EXISTS;
//            } else {
//                // Xử lý các trường hợp lỗi khác
//                return RegistrationStatus.FAILURE;
//            }
//        }
//    }

    public RegistrationStatus updateProfileUser(User user){

        return null;
    }


}
