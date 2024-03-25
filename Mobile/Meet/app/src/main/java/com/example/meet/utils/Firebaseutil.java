package com.example.meet.utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class Firebaseutil {

    public static String currenUserId(){
        return FirebaseAuth.getInstance().getUid();
    }
    public static DocumentReference documentReference(){
        return FirebaseFirestore.getInstance().collection("user").document(currenUserId());
    }

    public static CollectionReference allUserCollectionReference(){
        return FirebaseFirestore.getInstance().collection("user");
    }
}
