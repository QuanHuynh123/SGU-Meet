package com.example.meet.adapter;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meet.R;

public class FriendRequestViewHolder extends RecyclerView.ViewHolder {

    ImageView avatar;
    TextView user_name;
    Button btnAccept, btnDecline;

    public FriendRequestViewHolder(@NonNull View itemView) {
        super(itemView);
        avatar = itemView.findViewById(R.id.avatar);
        user_name = itemView.findViewById(R.id.user_name);
        btnAccept = itemView.findViewById(R.id.btnAccept);
        btnDecline = itemView.findViewById(R.id.btnDecline);
    }
}