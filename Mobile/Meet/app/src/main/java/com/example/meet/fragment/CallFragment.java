package com.example.meet.fragment;

import android.annotation.SuppressLint;
import android.app.Application;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.meet.R;
import com.example.meet.adapter.FriendListAdapter;
import com.example.meet.adapter.SearchUserRecyclerAdapter;
import com.example.meet.configApi.ApiConfig;
import com.example.meet.interfaceApiService.ProfileService;
import com.example.meet.model.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.zegocloud.uikit.prebuilt.call.ZegoUIKitPrebuiltCallService;
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationConfig;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CallFragment extends Fragment {


    private RecyclerView recyclerView;
    private FriendListAdapter adapter;
    private UserModel myAccount;
    private List<UserModel> listFriends;

    public CallFragment() {
        // Required empty public constructor
    }

    public static CallFragment newInstance() {
        return new CallFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_call, container, false);
        recyclerView = view.findViewById(R.id.recycler_view_call);
        listFriends = new ArrayList<>();

        startService(FirebaseAuth.getInstance().getUid());
        setupSearchRecyclerView();

        adapter = new FriendListAdapter(requireActivity(), listFriends);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        recyclerView.setAdapter(adapter);
        adapter.startListening();
        return view;
    }

    @SuppressLint("RestrictedApi")
    private void setupSearchRecyclerView() {
        Retrofit retrofit = ApiConfig.getRetrofit();
        ProfileService profileService = retrofit.create(ProfileService.class);
        Call<UserModel> call = profileService.getProfileUser(FirebaseAuth.getInstance().getUid());
        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                myAccount = response.body();
                if (myAccount != null && myAccount.getFriendRequests() != null) {
                    getDataFriend(myAccount.getFriendList());
                    if (listFriends == null) System.out.println("Danh sách bạn bè null");
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

    private void getDataFriend(List<String> idFriends) {
        Retrofit retrofit = ApiConfig.getRetrofit();
        ProfileService profileService = retrofit.create(ProfileService.class);
        Call<List<UserModel>> call = profileService.getFriend(idFriends);

        call.enqueue(new Callback<List<UserModel>>() {
            @Override
            public void onResponse(Call<List<UserModel>> call, Response<List<UserModel>> response) {
                if (response.isSuccessful()) {
                    List<UserModel> friends = response.body();
                    if (friends != null) {
                        listFriends.addAll(friends); // Thêm danh sách mới
                        adapter.notifyDataSetChanged(); // Cập nhật adapter sau khi thêm dữ liệu
                        System.out.println("Có danh sách");
                    } else {
                        System.out.println("Danh sách bạn trống!");
                    }
                } else {
                    System.out.println("Response not successful");
                }
            }

            @Override
            public void onFailure(Call<List<UserModel>> call, Throwable t) {
                System.out.println("Call API failed: " + t.getMessage());
            }
        });
    }

    void startService(String userID){
        Application application = requireActivity().getApplication();
        long appID = 1419731492;   // yourAppID
        String appSign ="4f2ca855301945d6c2a6b21a73d2ba926eb9bb9e24a226f8fe2d2b975f83cc3f";  // yourAppSign
        //String userID =; // yourUserID, userID should only contain numbers, English characters, and '_'.
        String userName = userID;   // yourUserName

        ZegoUIKitPrebuiltCallInvitationConfig callInvitationConfig = new ZegoUIKitPrebuiltCallInvitationConfig();

        ZegoUIKitPrebuiltCallService.init(application, appID, appSign, userID, userName,callInvitationConfig);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Lưu trạng thái của fragment
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Khôi phục trạng thái của fragment
        if (savedInstanceState != null) {
            // Thực hiện các xử lý khi fragment được khôi phục sau khi bị hủy
        }
    }

}
