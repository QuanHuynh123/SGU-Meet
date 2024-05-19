package com.ducku.myvideocallapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.ducku.myvideocallapp.databinding.ActivityIncomingBinding;
import com.stringee.call.StringeeCall2;
import com.stringee.common.StringeeAudioManager;
import com.stringee.listener.StatusListener;
import com.stringee.video.StringeeVideoTrack;

import org.json.JSONObject;

import java.util.Set;

public class IncomingActivity extends AppCompatActivity {
    ActivityIncomingBinding views;
    FrameLayout vLocal;
    FrameLayout vRemote;
    TextView tvStatus;
    Button btnCancel;
    Button btnAnswer;
    Button btnReject;

    StringeeCall2 stringeeCall2;
    private StringeeAudioManager audioManager;


    StringeeCall2.SignalingState state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        views = ActivityIncomingBinding.inflate(getLayoutInflater());
        setContentView(views.getRoot());

        initView();
        initAnswer();


        btnAnswer.setOnClickListener(v -> {
            if (stringeeCall2 != null) {
                stringeeCall2.answer(null);
                btnAnswer.setVisibility(View.GONE);
                btnReject.setVisibility(View.GONE);
                btnCancel.setVisibility(View.VISIBLE);
            }
        });

        btnReject.setOnClickListener(v -> {
            if (stringeeCall2 != null) {
                stringeeCall2.reject(null);
            }
        });

        btnCancel.setOnClickListener(v -> {
            if (stringeeCall2 != null) {
                stringeeCall2.hangup(null);
                finish();
            }
        });
    }

    private void initView() {
        vLocal = views.vLocal;
        vRemote = views.vRemote;
        tvStatus = views.tvStatus;
        btnCancel = views.btnCancel;
        btnAnswer = views.btnAnswer;
        btnReject = views.btnReject;
    }

    private void initAnswer() {
        String callId = getIntent().getStringExtra("call_id");
        stringeeCall2 = MainActivity.callMap.get(callId);
        Log.d("call_id", callId);
        stringeeCall2.enableVideo(true);
        stringeeCall2.setQuality(StringeeCall2.VideoQuality.QUALITY_1080P);

        audioManager = StringeeAudioManager.create(this);
        audioManager.start(new StringeeAudioManager.AudioManagerEvents() {
            @Override
            public void onAudioDeviceChanged(StringeeAudioManager.AudioDevice selectedAudioDevice, Set<StringeeAudioManager.AudioDevice> availableAudioDevices) {
            }
        });
        audioManager.setSpeakerphoneOn(true); // false: Audio Call, true: Video Call
        stringeeCall2.ringing(new StatusListener() {
            @Override
            public void onSuccess() {
            }
        });


        stringeeCall2.setCallListener(new StringeeCall2.StringeeCallListener() {
            @Override
            public void onSignalingStateChange(StringeeCall2 stringeeCall2, StringeeCall2.SignalingState signalingState, String s, int i, String s1) {

            }

            @Override
            public void onError(StringeeCall2 stringeeCall2, int i, String s) {

            }

            @Override
            public void onHandledOnAnotherDevice(StringeeCall2 stringeeCall2, StringeeCall2.SignalingState signalingState, String s) {

            }

            @Override
            public void onMediaStateChange(StringeeCall2 stringeeCall2, StringeeCall2.MediaState mediaState) {

            }

            @Override
            public void onLocalStream(StringeeCall2 stringeeCall2) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        vLocal.addView(stringeeCall2.getLocalView());
                        stringeeCall2.renderLocalView(true);
                    }
                });
            }

            @Override
            public void onRemoteStream(StringeeCall2 stringeeCall2) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        vRemote.addView(stringeeCall2.getRemoteView2());
                        stringeeCall2.renderRemoteView(false);
                    }
                });
            }

            @Override
            public void onVideoTrackAdded(StringeeVideoTrack stringeeVideoTrack) {

            }

            @Override
            public void onVideoTrackRemoved(StringeeVideoTrack stringeeVideoTrack) {

            }

            @Override
            public void onCallInfo(StringeeCall2 stringeeCall2, JSONObject jsonObject) {

            }

            @Override
            public void onTrackMediaStateChange(String s, StringeeVideoTrack.MediaType mediaType, boolean b) {

            }
        });
        stringeeCall2.answer(null);
    }
}