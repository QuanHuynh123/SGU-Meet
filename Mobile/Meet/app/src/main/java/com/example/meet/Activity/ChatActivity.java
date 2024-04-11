package com.example.meet.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.meet.R;
import com.example.meet.adapter.ChatRecyclerAdapter;
import com.example.meet.adapter.SearchUserRecyclerAdapter;
import com.example.meet.fragment.ChatFragment;
import com.example.meet.model.ChatMessageModel;
import com.example.meet.model.ChatroomModel;
import com.example.meet.model.UserModel;
import com.example.meet.utils.Firebaseutil;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Arrays;

public class ChatActivity extends AppCompatActivity {

    String chatroomId;
    UserModel  otherUser;
    ChatroomModel chatroomModel;
    TextView username;
    EditText contentChat;
    ImageButton btnSend;
    ImageButton btnPrevious;
    ImageView avatar;
    RecyclerView recyclerView;

    ChatRecyclerAdapter chatRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        otherUser = new UserModel();
        Intent intent = getIntent();
        otherUser.setName(intent.getStringExtra("username"));
        otherUser.setUserId(intent.getStringExtra("userid"));

        String userId1= FirebaseAuth.getInstance().getUid(), userId2 = otherUser.getUserId();
        if(userId1.hashCode() < userId2.hashCode()){
            chatroomId =  userId1+"_" + userId2;
        }else {
            chatroomId = userId2+"_"+userId1;
        }

        username = findViewById(R.id.user_name);
        contentChat = findViewById(R.id.content_chat);
        btnSend = findViewById(R.id.btn_send);
        btnPrevious = findViewById(R.id.previous_icon);
        avatar = findViewById(R.id.avatar);
        recyclerView = findViewById(R.id.chat_recycler_view);

        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(ChatActivity.this, MainActivity.class);
                intent1.putExtra("returnToSearchFragment", true);
                startActivity(intent1);
            }
        });
        username.setText(otherUser.getName());

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message  = contentChat.getText().toString().trim();
                if (message.isEmpty())
                    return;
                else sendMessageToUser(message);
            }
        });

        getOrCreateChatroomModel();
        setupChatRecyclerView();
    }

    private void setupChatRecyclerView() {

        Query query = Firebaseutil.getChatroomMessageReference(chatroomId)
                .orderBy("timestamp",Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<ChatMessageModel> options = new FirestoreRecyclerOptions.Builder<ChatMessageModel>()
                .setQuery((com.google.firebase.firestore.Query) query, ChatMessageModel.class).build();


        chatRecyclerAdapter  = new ChatRecyclerAdapter(options, getApplicationContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(chatRecyclerAdapter);
        chatRecyclerAdapter.startListening();
        chatRecyclerAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver(){
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount){
                super.onItemRangeInserted(positionStart,itemCount);
                recyclerView.smoothScrollToPosition(0);
            }
        });
    }

    private void sendMessageToUser(String message) {
        chatroomModel.setLastMessageTimestamp(Timestamp.now());
        chatroomModel.setLastMessageSenderId(FirebaseAuth.getInstance().getUid());
        chatroomModel.setLastMessage(message);
        FirebaseFirestore.getInstance().collection("chatrooms").document(chatroomId).set(chatroomModel);

        ChatMessageModel chatMessageModel = new ChatMessageModel(message,FirebaseAuth.getInstance().getUid(),Timestamp.now());

        Task<DocumentReference> CollectionReference = FirebaseFirestore.getInstance()
                .collection("chatrooms")
                .document(chatroomId).collection("chats").add(chatMessageModel)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()){
                            contentChat.setText("");
                        }
                    }
                });
    }

    private void getOrCreateChatroomModel() {
        FirebaseFirestore.getInstance().collection("chatrooms").document(chatroomId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    chatroomModel = task.getResult().toObject(ChatroomModel.class);
                    if(chatroomModel == null){
                        chatroomModel = new ChatroomModel(
                                chatroomId,
                                Arrays.asList(FirebaseAuth.getInstance().getUid(),otherUser.getUserId()),
                                Timestamp.now(),
                                "",
                                ""
                        );
                        FirebaseFirestore.getInstance().collection("chatrooms").document(chatroomId).set(chatroomModel);
                    }
                }
            }
        });

    }
}