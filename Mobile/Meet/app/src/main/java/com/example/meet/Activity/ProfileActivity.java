package com.example.meet.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.meet.R;
import com.example.meet.configApi.ApiConfig;
import com.example.meet.interfaceApiService.ProfileService;
import com.example.meet.model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ProfileActivity extends AppCompatActivity {

    EditText edt_username, edt_email, edt_age ;
    TextView tv_logout;
    Spinner spinnerGender;
    ProgressBar progressBar;

    ImageView imageView;

    UserModel userModel;
    Button btnUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        edt_username = findViewById(R.id.edt_username);
        edt_email = findViewById(R.id.edt_email);
        edt_age = findViewById(R.id.edt_age);
        spinnerGender = findViewById(R.id.gender_spinner);
        imageView = findViewById(R.id.avatar);
        btnUpdate = findViewById(R.id.btn_update_profile);
        tv_logout = findViewById(R.id.tv_logout);
        progressBar = findViewById(R.id.progress_bar);

        String[] genders = {"Male", "Female"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, genders);
        spinnerGender.setAdapter(adapter);

        getUserData();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
            }
        });
        tv_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logout();
            }
        });


        spinnerGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Lấy dữ liệu của tùy chọn đã chọn
                String selectedGender = (String) parentView.getItemAtPosition(position);
                // Bạn có thể làm gì đó với dữ liệu đã chọn ở đây, ví dụ: hiển thị hoặc lưu trữ nó
                // Ví dụ: hiển thị dữ liệu đã chọn trong log
                Log.d("SelectedGender", selectedGender);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Được gọi khi không có tùy chọn nào được chọn (thường không cần thiết)
            }
        });
    }


    private void getUserData() {
        setInProgress(true);
        String userId = FirebaseAuth.getInstance().getUid();

        Retrofit retrofit = ApiConfig.getRetrofit();
        ProfileService profileService = retrofit.create(ProfileService.class);
        Call<UserModel> call = profileService.getProfileUser(userId);
        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                setInProgress(false);
                userModel = response.body();
                if (userModel != null){
                    edt_username.setText(userModel.getName());
                    edt_email.setText(userModel.getEmail());
                    edt_age.setText(userModel.getAge());
                }
                else System.out.println("user is null");
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                System.out.println("Call API failed !");
            }
        });
    }

    private void updateProfile() {
        String newUsername = edt_username.getText().toString();
        if(newUsername.isEmpty() || newUsername.length()<3){
            edt_username.setError("Username length should be at least 3 chars");
            return;
        }
        userModel.setName(newUsername);
        setInProgress(true);
    }

    private void Logout() {

    }
    void setInProgress(boolean inProgress){
        if(inProgress){
            progressBar.setVisibility(View.VISIBLE);
            btnUpdate.setVisibility(View.GONE);
        }else{
            progressBar.setVisibility(View.GONE);
            btnUpdate.setVisibility(View.VISIBLE);
        }
    }

}