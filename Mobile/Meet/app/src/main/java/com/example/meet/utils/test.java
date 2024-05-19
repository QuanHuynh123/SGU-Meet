package com.example.meet.utils;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.meet.Activity.LoginActivity;
import com.example.meet.Activity.MainActivity;
import com.example.meet.Activity.RegisterActivity;
import com.example.meet.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

public class test {
//    package com.example.meet.Activity;
//
//import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;
//
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.example.meet.R;
//import com.example.meet.configApi.ApiConfig;
//import com.example.meet.interfaceApiService.LoginService;
//import com.example.meet.model.LoginRequest;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.AuthResult;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.auth.GetTokenResult;
//
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//import retrofit2.Retrofit;
//
//    public class LoginActivity extends AppCompatActivity {
//
//
//        private LinearLayout layoutSignUp;
//        private TextView edtUser ;
//        private TextView edtPassword;
//        private Button btnlogin;
//        private ProgressDialog progressDialog;
//
//        private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
//        private static final int LENGTH = 10; // Độ dài của chuỗi
//
//        @Override
//        protected void onCreate(Bundle savedInstanceState) {
//            super.onCreate(savedInstanceState);
//            setContentView(R.layout.activity_login);
//
//            initUi();
//            initListener();
//        }
//
//        private void initUi(){
//            layoutSignUp = findViewById(R.id.layout_sign_up);
//            edtUser = findViewById(R.id.edt_user);
//            edtPassword = findViewById(R.id.edt_password);
//            btnlogin = findViewById(R.id.btn_login);
//            progressDialog = new ProgressDialog(this);
//        }
//
//        private void initListener(){
//            layoutSignUp.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(com.example.meet.Activity.LoginActivity.this, RegisterActivity.class);
//                    startActivity(intent);
//                }
//            });
//
//            btnlogin.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    onClickLogin();
//                }
//            });
//        }
//
//        private void onClickLogin(){
//            FirebaseAuth auth = FirebaseAuth.getInstance();
//
//            String email = edtUser.getText().toString().trim();
//            String password = edtPassword.getText().toString().trim();
//            if(email == null || password == null)
//                Toast.makeText(com.example.meet.Activity.LoginActivity.this, "Vui lòng nhập đầy đủ", Toast.LENGTH_SHORT).show();
//            else {
//
//                progressDialog.show();
//
//                auth.signInWithEmailAndPassword(email, password)
//                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                            @Override
//                            public void onComplete(@NonNull Task<AuthResult> task) {
//                                System.out.println("Trước khi task thành công ");
//                                if (task.isSuccessful()) {
//                                    FirebaseUser user = auth.getCurrentUser();
//                                    System.out.println("Task thành công ");
//                                    if (user != null) {
//                                        user.getIdToken(true).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
//                                            @Override
//                                            public void onComplete(@NonNull Task<GetTokenResult> task) {
//                                                progressDialog.dismiss();
//                                                if (task.isSuccessful()) {
//                                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//                                                    if (user != null) {
//                                                        // Đăng nhập thành công, thực hiện các thao tác tiếp theo
//                                                        redirectToMain();
//                                                    } else {
//                                                        // FirebaseUser null, xử lý lỗi không xác định
//                                                        Toast.makeText(com.example.meet.Activity.LoginActivity.this, "Không thể xác thực người dùng.", Toast.LENGTH_SHORT).show();
//                                                    }
////                                                String idToken = task.getResult().getToken();
////                                                SharedPreferences sharedPreferences = getSharedPreferences(FirebaseAuth.getInstance().getUid(), Context.MODE_PRIVATE);
////                                                String retrievedSessionCookie = sharedPreferences.getString("sessionCookie", "");
////                                                if (retrievedSessionCookie.isEmpty()) {
////                                                    postIdTokenToSessionLogin(idToken);
////                                                }else {
////                                                    verifySession(retrievedSessionCookie, idToken);
////                                                    FirebaseAuth.getInstance().getUid();
////                                                }
//                                                }
//                                            }
//                                        });
//                                    }
//                                } else {
//                                    // If sign in fails, display a message to the user.
//                                    Log.w(TAG, "signInWithEmail:failure", task.getException());
//                                    Toast.makeText(com.example.meet.Activity.LoginActivity.this, "Authentication failed.",
//                                            Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        });
//            }
//        }
//
////    private void postIdTokenToSessionLogin( String idToken) {
////        LoginRequest loginRequest = new LoginRequest();
////        loginRequest.setIdToken(idToken);
////
////        Retrofit retrofit = ApiConfig.getRetrofit();
////        LoginService loginService = retrofit.create(LoginService.class);
////
////        Call<LoginRequest> call = loginService.createSessionCookie(loginRequest);
////        call.enqueue(new Callback<LoginRequest>() {
////            @Override
////            public void onResponse(Call<LoginRequest> call, Response<LoginRequest> response) {
////             String sessionCookie = response.body().getIdToken();
////             saveCookie(sessionCookie);
////             verifySession(sessionCookie,idToken);
////            }
////            @Override
////            public void onFailure(Call<LoginRequest> call, Throwable t) {
////                Log.v("FailedAPI","Call API cookie to failed");
////            }
////        });
////
////    }
////
////    private void verifySession(String sessionCookieValue, String idToken) {
////        Retrofit retrofit = ApiConfig.getRetrofit();
////        LoginService loginService = retrofit.create(LoginService.class);
////        System.out.println(sessionCookieValue);
////        Call<Boolean> profileCall = loginService.verifySessionCookie(sessionCookieValue);
////        profileCall.enqueue(new Callback<Boolean>() {
////            @Override
////            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
////                if (response.isSuccessful()) {
////                    Log.v("Success", "Xác minh thành công ");
////                    redirectToMain();
////                } else {
////                    postIdTokenToSessionLogin(idToken);
////                    Log.v("Failed","Xác minh thất bại");
////                }
////            }
////            @Override
////
////            public void onFailure(Call<Boolean> call, Throwable t) {
////                Log.v("FailedAPI","Call API verify to failed");
////            }
////        });
////    }
//
//        private void redirectToMain( ) {
//            //Sign in success, update UI with the signed-in user's information
//            Log.d(TAG, "signInWithEmail:success");
//            Intent intent = new Intent(com.example.meet.Activity.LoginActivity.this, MainActivity.class);
//            startActivity(intent);
//            finish();
//        }
//
////    private void saveCookie(String sessionCookie){
////        SharedPreferences sharedPreferences = getSharedPreferences(FirebaseAuth.getInstance().getUid(), Context.MODE_PRIVATE);
////        SharedPreferences.Editor editor = sharedPreferences.edit();
////        editor.putString("sessionCookie",sessionCookie);
////        editor.apply();
////    }
//
//    }
}
