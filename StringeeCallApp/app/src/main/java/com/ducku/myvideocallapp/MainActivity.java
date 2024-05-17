package com.ducku.myvideocallapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.stringee.StringeeClient;
import com.stringee.call.StringeeCall;
import com.stringee.call.StringeeCall2;
import com.stringee.exception.StringeeError;
import com.stringee.listener.StatusListener;
import com.stringee.listener.StringeeConnectionListener;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    //user2
    private String token = "eyJjdHkiOiJzdHJpbmdlZS1hcGk7dj0xIiwidHlwIjoiSldUIiwiYWxnIjoiSFMyNTYifQ.eyJqdGkiOiJTSy4wLlBVS0RXSTRrTHVaWm5xVmN4TWxTdlRjT2dHd3ZkVmNlLTE3MTU1Mjc1ODgiLCJpc3MiOiJTSy4wLlBVS0RXSTRrTHVaWm5xVmN4TWxTdlRjT2dHd3ZkVmNlIiwiZXhwIjoxNzE4MTE5NTg4LCJ1c2VySWQiOiJ1c2VyMiJ9.ynfYE-7kUCjV7jGZu5Fwep4bNVKXJJgavuEWTm6KEkM";
    //private final String token = "eyJjdHkiOiJzdHJpbmdlZS1hcGk7dj0xIiwidHlwIjoiSldUIiwiYWxnIjoiSFMyNTYifQ.eyJqdGkiOiJTSy4wLlBVS0RXSTRrTHVaWm5xVmN4TWxTdlRjT2dHd3ZkVmNlLTE3MTU1MjMwMTciLCJpc3MiOiJTSy4wLlBVS0RXSTRrTHVaWm5xVmN4TWxTdlRjT2dHd3ZkVmNlIiwiZXhwIjoxNzE4MTE1MDE3LCJ1c2VySWQiOiJ1c2VyMSJ9.xAEmXnVfN_OZJ4VMRrlmlzXJjlaIMULTA7ffZcKCAbs";
    static StringeeClient client;
    public static Map<String, StringeeCall2> callMap = new HashMap<>();

    String firebaseToken;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initStringee();

        ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO
        }, 1);

        TextView textView = findViewById(R.id.tv_user);
        Button button = findViewById(R.id.button);
        button.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, OutGoingActivity.class);
            intent.putExtra("from", client.getUserId());
            intent.putExtra("to", textView.getText().toString());
            startActivity(intent);
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
        return;
    }

    private void initStringee() {
        client = new StringeeClient(this);
        client.setConnectionListener(new StringeeConnectionListener() {
            @Override
            public void onConnectionConnected(final StringeeClient stringeeClient, boolean isReconnecting) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView textView = findViewById(R.id.tv_user);
                        textView.setText(client.getUserId());
                        firebaseToken = String.valueOf(FirebaseMessaging.getInstance().getToken());
                        stringeeClient.registerPushToken(firebaseToken, new StatusListener() {
                            @Override
                            public void onSuccess() {
                                Toast.makeText(MainActivity.this, "Register success", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError(StringeeError stringeeError) {
                                super.onError(stringeeError);
                                Toast.makeText(MainActivity.this, "Register failed", Toast.LENGTH_SHORT).show();
                            }


                        });
                    }
                });
            }

            @Override
            public void onConnectionDisconnected(StringeeClient stringeeClient, boolean isReconnecting) {
            }

            @Override
            public void onIncomingCall(final StringeeCall stringeeCall) {

            }

            @Override
            public void onIncomingCall2(StringeeCall2 stringeeCall2) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        callMap.put(stringeeCall2.getCallId(), stringeeCall2);
                        Intent intent = new Intent(MainActivity.this, IncomingActivity.class);
                        intent.putExtra("call_id", stringeeCall2.getCallId());
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onConnectionError(StringeeClient stringeeClient, final StringeeError stringeeError) {
            }

            @Override
            public void onRequestNewToken(StringeeClient stringeeClient) {
                // Get new token here and connect to Stringe server
            }

            @Override
            public void onCustomMessage(String s, JSONObject jsonObject) {
            }

            @Override
            public void onTopicMessage(String s, JSONObject jsonObject) {
            }


        });
        client.connect(token);
    }
}