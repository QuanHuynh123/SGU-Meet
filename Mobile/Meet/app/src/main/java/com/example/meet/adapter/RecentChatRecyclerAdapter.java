package com.example.meet.adapter;

import static com.example.meet.utils.Firebaseutil.documentReference;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meet.Activity.ChatActivity;
import com.example.meet.R;
import com.example.meet.model.ChatroomModel;
import com.example.meet.model.UserModel;
import com.example.meet.utils.Firebaseutil;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class RecentChatRecyclerAdapter extends FirestoreRecyclerAdapter<ChatroomModel,RecentChatRecyclerAdapter.ChatroomModelViewHolder > {

    Context context ;
    UserModel userModel = new UserModel();

    public RecentChatRecyclerAdapter(@NonNull FirestoreRecyclerOptions<ChatroomModel> options, Context context) {
        super(options);
        this.context = context;
    }

    @SuppressLint("RecyclerView")
    @Override
    protected void onBindViewHolder(@NonNull ChatroomModelViewHolder holder, int position, @NonNull ChatroomModel chatroomModel) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String a = "";

        if(chatroomModel.getUserIds().get(0).equals(FirebaseAuth.getInstance().getUid()))
            a = chatroomModel.getUserIds().get(1);
        else a = chatroomModel.getUserIds().get(0);

        Query query = db.collection("user").whereEqualTo("userId", a);

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                boolean lastMessageSentByMe = chatroomModel.getLastMessageSenderId().equals(Firebaseutil.currenUserId());
                for (DocumentSnapshot document : task.getResult().getDocuments()) {

                    userModel = document.toObject(UserModel.class);

                    if (userModel != null) {
                        holder.username.setText(userModel.getName());
                        if (lastMessageSentByMe)
                            holder.lastMessageText.setText("You: " + chatroomModel.getLastMessage());
                        else
                            holder.lastMessageText.setText(chatroomModel.getLastMessage());
                        //System.out.println("last message :" + chatroomModel.getLastMessage());
                        holder.lastMessageTime.setText(Firebaseutil.timestampToString(chatroomModel.getLastMessageTimestamp()));

                    } else {
                        System.out.println("User bị null :(((((((((((((((((((((");
                    }
                }
            }
        });

        final  String idUser = a ;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("vị trí : " + position);
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("userid", idUser);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @NonNull
    @Override
    public RecentChatRecyclerAdapter.ChatroomModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recent_chat_recycler_row, parent , false);
        return new RecentChatRecyclerAdapter.ChatroomModelViewHolder(view);
    }

    public void clear() {
        int size = getItemCount();
        notifyItemRangeRemoved(0, size);
    }



    class ChatroomModelViewHolder extends RecyclerView.ViewHolder{

        TextView username ;
        TextView lastMessageText;
        TextView lastMessageTime;

        public ChatroomModelViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.user_name_text);
            lastMessageText = itemView.findViewById(R.id.last_message_text);
            lastMessageTime = itemView.findViewById(R.id.last_message_time_text);
        }
    }
}