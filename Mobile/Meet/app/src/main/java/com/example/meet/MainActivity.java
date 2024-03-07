package com.example.meet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private Button buttonLogout ;
    private TextView textName ;
    private ImageView imgAvatar ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textName = findViewById(R.id.hello_name);
        imgAvatar = findViewById(R.id.avatar);
        buttonLogout = findViewById(R.id.logout);

        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                auth.signOut();
                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
        userInformation();
    }

    private void userInformation(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null){
            return;
        }

        String name  = user.getDisplayName();
        String email = user.getEmail();

        if(name == null) textName.setVisibility(View.GONE);
        else {
            textName.setVisibility(View.VISIBLE);
            textName.setText(name);
        }

        Uri imgUrl = user.getPhotoUrl();
        textName.setText(email);

        Glide.with(this).load(imgUrl).error(R.drawable.avatar).into(imgAvatar);
    }
}