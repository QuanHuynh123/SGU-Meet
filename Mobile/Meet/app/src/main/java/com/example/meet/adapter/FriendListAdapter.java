package com.example.meet.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zegocloud.uikit.prebuilt.call.invite.widget.ZegoSendCallInvitationButton;
import com.zegocloud.uikit.service.defines.ZegoUIKitUser;
import com.example.meet.R;
import com.example.meet.model.UserModel;

import java.util.Collections;
import java.util.List;

public class FriendListAdapter extends RecyclerView.Adapter<FriendListAdapter.FriendViewHolder> {

    private Context context;
    private List<UserModel> friendList;

    public FriendListAdapter(Context context, List<UserModel> friendList) {
        this.context = context;
        this.friendList = friendList;
    }

    @NonNull
    @Override
    public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.friend_list_item, parent, false);
        return new FriendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendViewHolder holder, int position) {
        UserModel friend = friendList.get(position);
        holder.textFriendName.setText(friend.getName());

        holder.voiceCallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.voiceCallBtn.setIsVideoCall(false);
                holder.voiceCallBtn.setResourceID("zego_uikit_call"); // Please fill in the resource ID name that has been configured in the ZEGOCLOUD's console here.
                holder.voiceCallBtn.setInvitees(Collections.singletonList(new ZegoUIKitUser(friend.getUserId())));
            }
        });

        holder.videoCallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.videoCallBtn.setIsVideoCall(true);
                holder.videoCallBtn.setResourceID("zego_uikit_call"); // Please fill in the resource ID name that has been configured in the ZEGOCLOUD's console here.
                holder.videoCallBtn.setInvitees(Collections.singletonList(new ZegoUIKitUser(friend.getUserId())));
            }
        });
    }

    @Override
    public int getItemCount() {
        return friendList.size();
    }

    public void startListening() {
    }
    static class FriendViewHolder extends RecyclerView.ViewHolder {
        TextView textFriendName;
        ImageView avatar;
        ZegoSendCallInvitationButton voiceCallBtn, videoCallBtn;

        public FriendViewHolder(@NonNull View itemView) {
            super(itemView);
            textFriendName = itemView.findViewById(R.id.user_name);
            avatar = itemView.findViewById(R.id.avatar);
            voiceCallBtn = itemView.findViewById(R.id.btn_call);
            videoCallBtn = itemView.findViewById(R.id.btn_video_call);
            // Ánh xạ các thành phần khác của item nếu cần
        }
    }
}
