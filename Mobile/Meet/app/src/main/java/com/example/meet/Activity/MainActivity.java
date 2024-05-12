package com.example.meet.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.meet.R;
import com.example.meet.adapter.AdapterViewPaper;
import com.example.meet.fragment.CallFragment;
import com.example.meet.fragment.ChatFragment;
import com.example.meet.fragment.SearchFragment;
import com.example.meet.fragment.SettingFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ViewPager2 viewPager;
    ArrayList<Fragment> fragmentArrayList = new ArrayList<>();

    private BottomNavigationView bottomNavigationView ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.paperMain);
        bottomNavigationView = findViewById(R.id.navigationbottom);
        fragmentArrayList.add(new ChatFragment());
        fragmentArrayList.add(new SearchFragment());
        fragmentArrayList.add(new CallFragment());
        fragmentArrayList.add(new SettingFragment());


        AdapterViewPaper adapterViewPaper = new AdapterViewPaper(this,fragmentArrayList);
        viewPager.setAdapter(adapterViewPaper);
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        bottomNavigationView.setSelectedItemId(R.id.navigation_chat);
                        break;
                    case 1:
                        bottomNavigationView.setSelectedItemId(R.id.navigation_search);
                        break;
                    case 2:
                        bottomNavigationView.setSelectedItemId(R.id.navigation_call);
                        break;
                    case 3:
                        bottomNavigationView.setSelectedItemId(R.id.navigation_setting);
                        break;
                }
                super.onPageSelected(position);
            }
        });

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if(itemId == R.id.navigation_chat){
                    viewPager.setCurrentItem(0);
                }else if(itemId == R.id.navigation_search){
                    viewPager.setCurrentItem(1);
                }else if(itemId == R.id.navigation_call){
                    viewPager.setCurrentItem(2);
                }else if(itemId == R.id.navigation_setting){
                    viewPager.setCurrentItem(3);
                }
                return true;
            }
        });

        boolean returnToSearchFragment = getIntent().getBooleanExtra("returnToSearchFragment", false);
        if (returnToSearchFragment) {
            viewPager.setCurrentItem(0); // 1 là index của ChatFragment trong fragmentArrayList
        }

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