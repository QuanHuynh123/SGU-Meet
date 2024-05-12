package com.ducku.conferenceapp.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.ducku.conferenceapp.R;
import com.ducku.conferenceapp.databinding.ActivityMyCallBinding;
import com.ducku.conferenceapp.models.DataModelType;
import com.ducku.conferenceapp.repository.MainRepository;
import com.ducku.conferenceapp.utils.Constants;
import com.ducku.conferenceapp.utils.PreferenceManager;
import com.ducku.conferenceapp.webrtc.MyPeerConnectionObserver;
import com.ducku.conferenceapp.webrtc.WebRTCClient;
import com.permissionx.guolindev.PermissionX;

import org.webrtc.EglBase;
import org.webrtc.IceCandidate;
import org.webrtc.MediaStream;
import org.webrtc.PeerConnection;

public class MyCallActivity extends AppCompatActivity implements MainRepository.Listener {
    ActivityMyCallBinding views;


    private MainRepository mainRepository;
    private Boolean isCameraMuted = false;
    private Boolean isMicrophoneMuted = false;

    private WebRTCClient webRTCClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        views = ActivityMyCallBinding.inflate(getLayoutInflater());
        setContentView(views.getRoot());
        mainRepository = MainRepository.getInstance();

        views.callLayout.setVisibility(View.VISIBLE);
        mainRepository.initLocalView(views.localView);
        mainRepository.initRemoteView(views.remoteView);
        PermissionX.init(this)
                .permissions(android.Manifest.permission.CAMERA, android.Manifest.permission.RECORD_AUDIO)
                .request((allGranted, grantedList, deniedList) ->
                {
                    if (allGranted) {
                        init();
                    } else {
                        Log.d("MyCallActivity", "Permission not granted");
                        onBackPressed();
                    }
                });

    }

    private void init() {

        mainRepository.listener = this;

        mainRepository.subscribeForLatestEvent(data -> {
            if (data.getType() == DataModelType.StartCall) {
                //star the call here
                //mainRepository.sendCallRequest(preferenceManager.getString("ku mufahasa"),() ->{});
                mainRepository.startCall(data.getSender());

            }
        });

        views.switchCameraButton.setOnClickListener(v -> {
            mainRepository.switchCamera();
        });

        views.micButton.setOnClickListener(v -> {
            if (isMicrophoneMuted) {
                views.micButton.setImageResource(R.drawable.ic_baseline_mic_off_24);
            } else {
                views.micButton.setImageResource(R.drawable.ic_baseline_mic_24);
            }
            mainRepository.toggleAudio(isMicrophoneMuted);
            isMicrophoneMuted = !isMicrophoneMuted;
        });

        views.videoButton.setOnClickListener(v -> {
            if (isCameraMuted) {
                views.videoButton.setImageResource(R.drawable.ic_baseline_videocam_off_24);
            } else {
                views.videoButton.setImageResource(R.drawable.ic_baseline_videocam_24);
            }
            mainRepository.toggleVideo(isCameraMuted);
            isCameraMuted = !isCameraMuted;
        });

        views.endCallButton.setOnClickListener(v -> {
            mainRepository.endCall();
            finish();
        });
    }

    @Override
    public void webrtcConnected() {
        runOnUiThread(() -> {
            views.callLayout.setVisibility(View.VISIBLE);
        });
    }

    @Override
    public void webrtcClosed() {
        runOnUiThread(this::finish);
    }
}