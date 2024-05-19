package com.example.meet.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meet.R;
import com.example.meet.adapter.FriendRequestAdapter;
import com.example.meet.configApi.ApiConfig;
import com.example.meet.interfaceApiService.ProfileService;
import com.example.meet.model.UserModel;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AddFriendActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ImageButton btnPrevious;
    private FriendRequestAdapter adapter;
    private List<UserModel> friendRequests;
    private UserModel userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_friend);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnPrevious = findViewById(R.id.previous_icon);
        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddFriendActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        recyclerView = findViewById(R.id.recyclerview_add_friend);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        friendRequests = new ArrayList<>();
        adapter = new FriendRequestAdapter(friendRequests, this);
        recyclerView.setAdapter(adapter);

        loadFriendRequests();
    }

    private void loadFriendRequests() {
        String userId = FirebaseAuth.getInstance().getUid();
        if (userId == null) {
            System.out.println("User is not logged in");
            return;
        }

        Retrofit retrofit = ApiConfig.getRetrofit();
        ProfileService profileService = retrofit.create(ProfileService.class);
        Call<UserModel> call = profileService.getProfileUser(userId);
        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                userModel = response.body();
                if (userModel != null && userModel.getFriendRequests() != null) {
                    getDataFriend(userModel.getFriendRequests());
                } else {
                    System.out.println("User model or friend requests are null");
                }
            }
            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                System.out.println("Call API failed: " + t.getMessage());
            }
        });
    }

    private void getDataFriend(List<String> idFriends){
        Retrofit retrofit = ApiConfig.getRetrofit();
        ProfileService profileService = retrofit.create(ProfileService.class);
        Call<List<UserModel>> call = profileService.getFriend(idFriends);

        call.enqueue(new Callback<List<UserModel>>() {
            @Override
            public void onResponse(Call<List<UserModel>> call, Response<List<UserModel>> response) {
                List<UserModel> listAddRequest  = response.body();
                if (listAddRequest != null ) {
                    friendRequests.clear(); // Xóa danh sách hiện tại
                    friendRequests.addAll(listAddRequest); // Thêm danh sách mới
                    adapter.notifyDataSetChanged(); // Cập nhật RecyclerView
                }else {
                    System.out.println("Không có ai gửi lời mời kết bạn !");
                }
            }

            @Override
            public void onFailure(Call<List<UserModel>> call, Throwable t) {
                System.out.println("Call API failed: " + t.getMessage());
            }
        });
    }
}
