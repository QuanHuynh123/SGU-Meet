package com.example.meet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.meet.model.ListChat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<ListChat> listChatFriend;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.idListView);
        listChatFriend = new ArrayList<>();
        listChatFriend.add(new ListChat(1,"Quan","Hello anh ban","2h30pm"));
        listChatFriend.add(new ListChat(2,"Tai","Choi game khong","2h30pm"));
        listChatFriend.add(new ListChat(3,"Wang","Tft ne bro","2h30pm"));
        listChatFriend.add(new ListChat(4,"Loc","Tft ne bro","2h30pm"));
        listChatFriend.add(new ListChat(5,"Thai","Tft ne bro","2h30pm"));
        listChatFriend.add(new ListChat(6,"Quan Pham","Tft ne bro","2h30pm"));
        listChatFriend.add(new ListChat(7,"Quan Tan","Tft ne bro","2h30pm"));
        listChatFriend.add(new ListChat(8,"Cieu","Tft ne bro","2h30pm"));
        listChatFriend.add(new ListChat(9,"Tommy","Tft ne bro","2h30pm"));

        ListAdapter adapter = new ListAdapter(this,R.layout.item_custom_listview,listChatFriend);
        listView.setAdapter(adapter);

    }

    private void userInformation(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null){
            return;
        }

        String name  = user.getDisplayName();
        String email = user.getEmail();

//        if(name == null) textName.setVisibility(View.GONE);
//        else {
//            textName.setVisibility(View.VISIBLE);
//            textName.setText(name);
//        }
//
//        Uri imgUrl = user.getPhotoUrl();
//        textName.setText(email);
//
//        Glide.with(this).load(imgUrl).error(R.drawable.avatar).into(imgAvatar);
//
//        System.out.println(" cc " + Glide.with(this).load(R.drawable.avatar));
    }
}