package com.example.meet.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.Firebase;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.zegocloud.uikit.prebuilt.call.ZegoUIKitPrebuiltCallService;
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationConfig;
import com.zegocloud.uikit.prebuilt.call.invite.widget.ZegoSendCallInvitationButton;
import com.zegocloud.uikit.service.defines.ZegoUIKitUser;

import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.ExecutionException;

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
    String name ;
    ChatRecyclerAdapter chatRecyclerAdapter;

    ImageView showCurrentPos;

    ZegoSendCallInvitationButton btnCall , btnVideoCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        otherUser = new UserModel();
        Intent intent = getIntent();
        otherUser.setUserId(intent.getStringExtra("userid"));

        getNameOtherUser(otherUser.getUserId(), new UserNameCallback() {
            @Override
            public void onUserNameReceived(String userName) {
                otherUser.setName(userName);
                username.setText(otherUser.getName());
            }
        });

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
        showCurrentPos = findViewById(R.id.btn_google_map);
        btnCall = findViewById(R.id.btn_phone);
        btnVideoCall = findViewById(R.id.btn_video_call);

        startService(FirebaseAuth.getInstance().getUid());
        setVoiceCall(otherUser.getUserId());
        setVideoCall(otherUser.getUserId());

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

        showCurrentPos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatActivity.this,GoogleMapActivity.class);
                startActivity(intent);
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

    public void getNameOtherUser(String idUser, UserNameCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Query query = db.collection("user").whereEqualTo("userId", idUser);

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (DocumentSnapshot document : task.getResult().getDocuments()) {
                    UserModel otherUser = document.toObject(UserModel.class);
                    if (otherUser != null) {
                        String userName = otherUser.getName();
                        callback.onUserNameReceived(userName);
                    }
                }
            }
        });
    }

    public interface UserNameCallback {
        void onUserNameReceived(String userName);
    }

    void startService(String userID){
        Application application = getApplication(); // Android's application context
        long appID = 1419731492;   // yourAppID
        String appSign ="4f2ca855301945d6c2a6b21a73d2ba926eb9bb9e24a226f8fe2d2b975f83cc3f";  // yourAppSign
        //String userID =; // yourUserID, userID should only contain numbers, English characters, and '_'.
        String userName =userID;   // yourUserName

        ZegoUIKitPrebuiltCallInvitationConfig callInvitationConfig = new ZegoUIKitPrebuiltCallInvitationConfig();

        ZegoUIKitPrebuiltCallService.init(getApplication(), appID, appSign, userID, userName,callInvitationConfig);
    }

    void setVoiceCall(String targetUserID){
        btnCall.setIsVideoCall(false);
        btnCall.setResourceID("zego_uikit_call"); // Please fill in the resource ID name that has been configured in the ZEGOCLOUD's console here.
        btnCall.setInvitees(Collections.singletonList(new ZegoUIKitUser(targetUserID)));
    }

    void setVideoCall(String targetUserID){
        btnVideoCall.setIsVideoCall(true);
        btnVideoCall.setResourceID("zego_uikit_call"); // Please fill in the resource ID name that has been configured in the ZEGOCLOUD's console here.
        btnVideoCall.setInvitees(Collections.singletonList(new ZegoUIKitUser(targetUserID)));
    }

}