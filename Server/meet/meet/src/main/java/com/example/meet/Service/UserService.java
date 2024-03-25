package com.example.meet.Service;

import com.example.meet.Model.User;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
public class UserService {

    public String createUser(User user) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        // Lấy getDocumentId làm id
        ApiFuture<WriteResult> collectionApiFuture = dbFirestore.collection("user").document(user.getDocumentId()).set(user);
        return collectionApiFuture.get().getUpdateTime().toString();
    }
}
