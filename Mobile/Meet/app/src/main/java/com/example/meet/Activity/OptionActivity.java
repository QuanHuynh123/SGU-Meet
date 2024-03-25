package com.example.meet.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.meet.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class OptionActivity extends AppCompatActivity {

    private Button btnJoinMeeting, btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);

        nextActivity();
    }

    private void nextActivity(){
        btnLogin = findViewById(R.id.btn_login);
        btnJoinMeeting = findViewById(R.id.btn_join);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                FirebaseAuth auth = FirebaseAuth.getInstance();
                auth.signOut();
                if(user == null){
                    //chua login
                    Intent intent = new Intent(OptionActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    // da login
                    Intent intent = new Intent(OptionActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        btnJoinMeeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // nhap id
            }
        });

    }
}