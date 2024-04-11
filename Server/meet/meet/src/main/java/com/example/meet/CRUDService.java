package com.example.meet;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
@Service
public class CRUDService {
    public String createCRUD(CRUD crud) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        // Lấy getDocumentId làm id
        ApiFuture<DocumentReference> collectionApiFuture = dbFirestore.collection("user").add(crud);
        return collectionApiFuture.get().getId();
    }

    public CRUD getCRUD(String name) throws ExecutionException, InterruptedException {
        Firestore dbFirestore  = FirestoreClient.getFirestore();
        Query query = dbFirestore.collection("user")
                .whereEqualTo("name", "quan");
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
//        DocumentReference documentReference = dbFirestore.collection("user").document(name);
//        ApiFuture<DocumentSnapshot> future = documentReference.get();
//        DocumentSnapshot document = future.get();
//        CRUD crud ;
//        if(document.exists()){
//            crud = document.toObject(CRUD.class);
//            return crud;
//        }
//        return null;
        CRUD crud = new CRUD();
        for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
            // Lấy dữ liệu từ tài liệu và gán vào đối tượng CRUD
            crud = document.toObject(CRUD.class);

            // In ra thông tin của đối tượng CRUD
            System.out.println("Name: " + crud.getName());
            System.out.println("Gender: " + crud.getGender());
            System.out.println("Email: " + crud.getEmail());
            System.out.println("Age: " + crud.getAge());
        }

        return crud;
    }
    public String updateCRUD(CRUD crud) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        DocumentReference  collectionApiFuture = dbFirestore.collection("user").document("cc");
        ApiFuture<WriteResult> writeResult = collectionApiFuture.update("createdTimestamp", FieldValue.serverTimestamp());
        return collectionApiFuture.get().toString();
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
