package com.example.meet.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meet.Activity.ChatActivity;
import com.example.meet.R;
import com.example.meet.model.UserModel;
import com.example.meet.utils.Firebaseutil;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class SearchUserRecyclerAdapter extends FirestoreRecyclerAdapter<UserModel,SearchUserRecyclerAdapter.UserModelViewHolder > {

    Context context ;

    public SearchUserRecyclerAdapter(@NonNull FirestoreRecyclerOptions<UserModel> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull UserModelViewHolder holder, int position, @NonNull UserModel userModel) {
            holder.username.setText(userModel.getName());
            if (userModel.getUserId().equals(Firebaseutil.currenUserId())){
                holder.username.setText(userModel.getName()+" (Me)");
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ChatActivity.class);
                    intent.putExtra("username",userModel.getName());
                    intent.putExtra("userid",userModel.getUserId());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
    }

    @NonNull
    @Override
    public UserModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_user_cecycler_row, parent , false);
        return new UserModelViewHolder(view);
    }

    public void clear() {
        int size = getItemCount();
        notifyItemRangeRemoved(0, size);
    }



    class UserModelViewHolder extends RecyclerView.ViewHolder{

        ImageView imgAvt ;
        TextView username ;
        public UserModelViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAvt = itemView.findViewById(R.id.avatar);
            username = itemView.findViewById(R.id.name_user);
        }
    }
}
