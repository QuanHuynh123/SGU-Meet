package com.example.meet.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meet.Activity.ChatActivity;
import com.example.meet.R;
import com.example.meet.configApi.ApiConfig;
import com.example.meet.interfaceApiService.ProfileService;
import com.example.meet.model.UserModel;
import com.example.meet.utils.Firebaseutil;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SearchUserRecyclerAdapter extends FirestoreRecyclerAdapter<UserModel,SearchUserRecyclerAdapter.UserModelViewHolder > {

    Context context ;
    UserModel myAccount;

    public SearchUserRecyclerAdapter(@NonNull FirestoreRecyclerOptions<UserModel> options, Context context, UserModel myAccount) {
        super(options);
        this.context = context;
        this.myAccount = myAccount;
    }

    @Override
    protected void onBindViewHolder(@NonNull UserModelViewHolder holder, int position, @NonNull UserModel userModel) {
        if (myAccount == null )
            System.out.println("NULL mẹ trong hàm search");
            boolean isFriend = false;
            holder.username.setText(userModel.getName());
            if (userModel.getUserId().equals(Firebaseutil.currenUserId())){
                holder.username.setText(userModel.getName()+" (Me)");
                isFriend = true;
            }

            List<String> friendList = myAccount.getFriendList();
            if (friendList != null && !friendList.isEmpty()) {
                for (String friendId : friendList) {
                    if (friendId.equals(userModel.getUserId())) {
                        // Nếu userModel có trong danh sách bạn, ẩn button kết bạn
                        isFriend = true;
                        break;
                    }
                }
            }

        if (isFriend) {
            System.out.println("Là bạn bè");
            holder.btnRequestFriend.setText("Bạn bè");
            holder.btnRequestFriend.setEnabled(false); // Tắt khả năng click vào nút
        } else {
            holder.btnRequestFriend.setVisibility(View.VISIBLE);
            holder.btnRequestFriend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Retrofit retrofit = ApiConfig.getRetrofit();
                    ProfileService profileService = retrofit.create(ProfileService.class);
                    Call<Boolean> call = profileService.requestAddFriend(Firebaseutil.currenUserId(), userModel.getUserId());
                    call.enqueue(new Callback<Boolean>() {
                        @Override
                        public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(context, "Sent friend request to " + userModel.getName(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Boolean> call, Throwable t) {
                            // Xử lý lỗi
                        }
                    });
                }
            });
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!userModel.getUserId().equals(Firebaseutil.currenUserId())) {
                    Intent intent = new Intent(context, ChatActivity.class);
                    intent.putExtra("username", userModel.getName());
                    intent.putExtra("userid", userModel.getUserId());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } else {
                    // Optional: Show a message or perform an action when clicking on yourself
                    Toast.makeText(context, "You cannot chat with yourself.", Toast.LENGTH_SHORT).show();
                }
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
        Button btnRequestFriend;
        public UserModelViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAvt = itemView.findViewById(R.id.avatar);
            username = itemView.findViewById(R.id.name_user);
            btnRequestFriend = itemView.findViewById(R.id.request_friend);
        }
    }
}
