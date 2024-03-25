package com.example.meet.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.meet.R;
import com.example.meet.configApi.ApiConfig;
import com.example.meet.enumCustom.RegistrationStatus;
import com.example.meet.interfaceApiService.RegisterService;
import com.example.meet.model.AccountUserModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RegisterActivity extends AppCompatActivity {

    private EditText edtEmail, edtPassword,edtConfirmPsswd,edtFullName;
    private Button btnSignUp;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initUi();
        initListener();
    }

    private void initUi(){
        edtEmail = findViewById(R.id.edt_email);
        edtConfirmPsswd = findViewById(R.id.edt_confirm_psswd);
        edtFullName = findViewById(R.id.edt_fullname);
        edtPassword = findViewById(R.id.edt_password);
        btnSignUp = findViewById(R.id.btn_sign_up);
        progressDialog = new ProgressDialog(this);
    }

    private  void initListener(){
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickRegister();
            }
        });
    }

    private void onClickRegister() {
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        String name = edtFullName.getText().toString().trim();
        String confirmPsswd = edtConfirmPsswd.getText().toString().trim();

        AccountUserModel accountUser  ;

        progressDialog.show();
        if(password.equals(confirmPsswd)){
            accountUser = new AccountUserModel(email,password,true,name,false);
            Retrofit retrofit = ApiConfig.getRetrofit();
            RegisterService registerService = retrofit.create(RegisterService.class);
            Call<RegistrationStatus> call = registerService.registerAccount(accountUser);

            call.enqueue(new Callback<RegistrationStatus>() {
                @Override
                public void onResponse(Call<RegistrationStatus> call, Response<RegistrationStatus> response) {
                    if (response.isSuccessful()) {
                        RegistrationStatus registrationStatus = response.body();
                        if (registrationStatus != null ){
                            if (registrationStatus.equals(RegistrationStatus.SUCCESS)){
                                Log.v("Success","Create account succces");
                            }else if(registrationStatus.equals(RegistrationStatus.EMAIL_ALREADY_EXISTS)){
                                Log.v("EmailExists","Email is already exists");
                            }else {
                                Log.v("Failed",".............");
                            }
                        }
                    } else {
                        Log.v("Failed","Create account failed");
                    }
                }

                @Override
                public void onFailure(Call<RegistrationStatus> call, Throwable t) {
                    Log.v("Failed","Get cookie to failed!");
                }
            });
        }
        else Toast.makeText(RegisterActivity.this,"Authentication failed.",Toast.LENGTH_SHORT).show();
    }
}