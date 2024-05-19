package com.example.meet.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meet.Activity.LoginActivity;
import com.example.meet.Activity.RegisterActivity;
import com.example.meet.R;
import com.example.meet.configApi.ApiConfig;
import com.example.meet.enumCustom.RegistrationStatus;
import com.example.meet.interfaceApiService.ProfileService;
import com.example.meet.interfaceApiService.RegisterService;
import com.example.meet.model.UserModel;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FriendRequestAdapter extends RecyclerView.Adapter<FriendRequestViewHolder> {
    private List<UserModel> friendRequests;
    private Context context;

    public FriendRequestAdapter(List<UserModel> friendRequests, Context context) {
        this.friendRequests = friendRequests;
        this.context = context;
    }

    @NonNull
    @Override
    public FriendRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend_request, parent, false);
        return new FriendRequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendRequestViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if (friendRequests != null) {
            UserModel userModel = friendRequests.get(position);
            holder.user_name.setText(userModel.getName());

            holder.btnAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Xử lý chấp nhận lời mời kết bạn
                    acceptFriendRequest(FirebaseAuth.getInstance().getUid(), userModel.getUserId());
                    holder.btnAccept.setText("Bạn bè");
                    holder.btnAccept.setEnabled(false);
                }
            });

            holder.btnDecline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Xử lý từ chối lời mời kết bạn
                    declineFriendRequest(userModel);
                }
            });
        } else {
            // Xử lý khi danh sách là null, có thể thông báo cho người dùng hoặc thực hiện hành động phù hợp
        }
    }


    @Override
    public int getItemCount() {
        return friendRequests == null ? 0 : friendRequests.size();
    }

    private void acceptFriendRequest(String userId1 , String userId2) {
        Retrofit retrofit = ApiConfig.getRetrofit();
        ProfileService profileService = retrofit.create(ProfileService.class);

        Call<Boolean> call = profileService.addFriend(userId1,userId2);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context,"Kết bạn thành công", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                // Xử lý lỗi
            }
        });

    }

    private void declineFriendRequest(UserModel userModel) {
        // Thêm mã xử lý từ chối lời mời kết bạn
    }
}