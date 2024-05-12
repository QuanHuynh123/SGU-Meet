package com.example.meet.Service;

import com.example.meet.Model.ChatroomModel;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class testService {

    public List<ChatroomModel> getAllChatrooms() throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();

        // Tạo truy vấn để lấy tất cả các tài liệu từ collection "chatrooms"
        Query query = dbFirestore.collection("chatrooms");

        try {
            QuerySnapshot querySnapshot = query.get().get();
            List<ChatroomModel> chatrooms = new ArrayList<>();
            for (DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()) {
                ChatroomModel chatroom = documentSnapshot.toObject(ChatroomModel.class);
                chatrooms.add(chatroom);
            }
            return chatrooms;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<ChatroomModel> get1ChatRoom(String userId) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();

        // Tạo truy vấn để lấy tất cả các tài liệu từ collection "chatrooms"
        Query query = dbFirestore.collection("chatrooms");

        try {
            QuerySnapshot querySnapshot = query.get().get();
            List<ChatroomModel> chatrooms = new ArrayList<>();
            for (DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()) {
                ChatroomModel chatroom = documentSnapshot.toObject(ChatroomModel.class);
                if (chatroom.getUserIds().contains(userId)) {
                    chatrooms.add(chatroom);
                }
            }
            return chatrooms;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return null;
    }

}
