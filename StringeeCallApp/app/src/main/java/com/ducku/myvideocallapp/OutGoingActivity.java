package com.ducku.myvideocallapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.ducku.myvideocallapp.databinding.ActivityOutGoingBinding;
import com.stringee.call.StringeeCall;
import com.stringee.call.StringeeCall2;
import com.stringee.common.StringeeAudioManager;
import com.stringee.video.StringeeVideoTrack;

import org.json.JSONObject;

import java.util.Set;

public class OutGoingActivity extends AppCompatActivity {
    private StringeeCall2 stringeeCall2;

    private StringeeCall2.SignalingState state;
    private StringeeAudioManager audioManager;

    ActivityOutGoingBinding views;

    String from;
    String to;

    boolean isVideoCall = true;

    FrameLayout vLocal;
    FrameLayout vRemote;
    TextView tvStatus;
    Button btnCancel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        views = ActivityOutGoingBinding.inflate(getLayoutInflater());
        setContentView(views.getRoot());

        from = getIntent().getStringExtra("from");
        to = getIntent().getStringExtra("to");
        initView();
        initStringeeCall();
    }

    private void initView() {
        vLocal = views.vLocal;
        vRemote = views.vRemote;
        tvStatus = views.tvStatus;
        btnCancel = views.btnCancel;

        btnCancel.setOnClickListener(v -> {
            if (stringeeCall2 != null) {
                stringeeCall2.hangup(null);
                finish();
            }
        });

    }

    private void initStringeeCall() {
        stringeeCall2 = new StringeeCall2(MainActivity.client, from, to);
// Initialize audio manager to manage the audio routing
        audioManager = StringeeAudioManager.create(this);
        audioManager.start(new StringeeAudioManager.AudioManagerEvents() {
            @Override
            public void onAudioDeviceChanged(StringeeAudioManager.AudioDevice selectedAudioDevice, Set<StringeeAudioManager.AudioDevice> availableAudioDevices) {
                // All change of audio devices will receive in here
            }
        });
        audioManager.setSpeakerphoneOn(isVideoCall); // false: Audio Call, true: Video Call
// Make a call
        stringeeCall2.setVideoCall(isVideoCall); // false: Audio Call, true: Video Call


        stringeeCall2.setCallListener(new StringeeCall2.StringeeCallListener() {
            @Override
            public void onSignalingStateChange(StringeeCall2 stringeeCall2, StringeeCall2.SignalingState signalingState, String s, int i, String s1) {
                tvStatus.setText(signalingState.toString());
                state = signalingState;
                if (state == StringeeCall2.SignalingState.ENDED) {
                    finish();
                }
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
        stringeeCall2.makeCall(null);
    }
}