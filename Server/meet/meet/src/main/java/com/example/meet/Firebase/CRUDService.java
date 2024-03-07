package com.example.meet.Firebase;

import com.example.meet.Firebase.CRUD;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.cloud.FirestoreClient;

import java.util.concurrent.ExecutionException;
public class CRUDService {
    public String createCRUD(CRUD crud) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        // Lấy getDocumentId làm id
        ApiFuture<WriteResult> collectionApiFuture = dbFirestore.collection("user").document(crud.getDocumentId()).set(crud);
        return collectionApiFuture.get().getUpdateTime().toString();
    }

    public CRUD getCRUD(String documentId) throws ExecutionException, InterruptedException {
        Firestore dbFirestore  = FirestoreClient.getFirestore();
        DocumentReference documentReference = dbFirestore.collection("user").document(documentId);
        ApiFuture<DocumentSnapshot> future = documentReference.get();
        DocumentSnapshot document = future.get();
        CRUD crud ;
        if(document.exists()){
            crud = document.toObject(CRUD.class);
            return crud;
        }
        return null;
    }
    public String updateCRUD(CRUD crud) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> collectionApiFuture = dbFirestore.collection("user").document(crud.getDocumentId()).set(crud);

        return collectionApiFuture.get().getUpdateTime().toString();
    }

    public String deleteCRUD(String documentId){
        Firestore dbFirestore  = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> writeResult = dbFirestore.collection("user").document(documentId).delete();
        return "Successfully delete " + documentId ;
        
    }

    public void check (){
        FirebaseAuth auth = FirebaseAuth.getInstance();
    }
}
