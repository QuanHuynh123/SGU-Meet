package com.example.meet.Service;

import com.example.meet.CRUD;
import com.example.meet.Model.User;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
public class FriendService {

    @Autowired
    UserService userService;
    public List<User> getFriend(List<String> idFriends) throws ExecutionException, InterruptedException {
        List<User> friendList = new ArrayList<>();
        for (String id : idFriends) {
            User user = userService.getProfileUser(id);
            friendList.add(user);
        }
        return friendList;
    }

    public void addFriend(String id1, String id2) throws ExecutionException, InterruptedException {
        // Thêm id2 vào danh sách bạn bè của người dùng có id1
        updateUserField(id1, "friendList", id2);
        // Thêm id1 vào danh sách bạn bè của người dùng có id2
        updateUserField(id2, "friendList", id1);
        remove(id1,id2);
    }

    private void updateUserField(String userId, String field, String value) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        Query query = dbFirestore.collection("user").whereEqualTo("userId", userId);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();

        for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
            DocumentReference docRef = document.getReference();
            ApiFuture<WriteResult> writeResult = docRef.update(field, FieldValue.arrayUnion(value));
            System.out.println("Updated " + field + " for userId " + userId + " at: " + writeResult.get().getUpdateTime());
        }
    }

    public void requestAddFriend(String id1, String id2) throws ExecutionException, InterruptedException {
        updateUserField(id2, "friendRequests", id1);
        updateUserField(id1, "sentFriendRequests", id2);
    }

    public void remove(String id1, String id2) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        Query query = dbFirestore.collection("user").whereEqualTo("userId", id1);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();

        for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
            DocumentReference docRef = document.getReference();
            ApiFuture<WriteResult> writeResult = docRef.update("friendRequests", FieldValue.arrayRemove(id2));

        }

        query = dbFirestore.collection("user").whereEqualTo("userId", id2);
        querySnapshot = query.get();
        for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
            DocumentReference docRef = document.getReference();
            ApiFuture<WriteResult> writeResult = docRef.update("sentFriendRequests", FieldValue.arrayRemove(id1));

        }
    }


}
