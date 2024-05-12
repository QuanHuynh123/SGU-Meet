package com.ducku.conferenceapp.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.ducku.conferenceapp.R;
import com.ducku.conferenceapp.databinding.ActivityOutGoingInvitationBinding;
import com.ducku.conferenceapp.models.DataModel;
import com.ducku.conferenceapp.models.User;
import com.ducku.conferenceapp.network.ApiClient;
import com.ducku.conferenceapp.network.ApiService;
import com.ducku.conferenceapp.repository.MainRepository;
import com.ducku.conferenceapp.utils.Constants;
import com.ducku.conferenceapp.utils.PreferenceManager;
import com.ducku.conferenceapp.webrtc.MyPeerConnectionObserver;
import com.ducku.conferenceapp.webrtc.WebRTCClient;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONObject;
import org.webrtc.IceCandidate;
import org.webrtc.MediaStream;
import org.webrtc.PeerConnection;

import java.net.URL;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OutGoingInvitationActivity extends AppCompatActivity implements WebRTCClient.Listener {
    ActivityOutGoingInvitationBinding activityOutGoingInvitationBinding;
    private PreferenceManager preferenceManager;
    private String inviterToken = null;

    private MainRepository mainRepository;

    private String meetingType = null;
    private User user;

    private WebRTCClient webRTCClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityOutGoingInvitationBinding = ActivityOutGoingInvitationBinding.inflate(getLayoutInflater());
        setContentView(activityOutGoingInvitationBinding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());

        ImageView imageMeetingType = activityOutGoingInvitationBinding.imageMeetingType;
        TextView textFirstChar = activityOutGoingInvitationBinding.textFirstChar;
        TextView textViewUsername = activityOutGoingInvitationBinding.textUsername;
        TextView textEmail = activityOutGoingInvitationBinding.textEmail;
        meetingType = getIntent().getStringExtra("type");
        mainRepository = mainRepository.getInstance();

        if (meetingType != null) {
            if (meetingType.equals("video")) {
                imageMeetingType.setImageResource(R.drawable.ic_video);
            } else if (meetingType.equals("audio")) {
                imageMeetingType.setImageResource(R.drawable.ic_phone_call);
            }
        }

        user = (User) getIntent().getSerializableExtra("user");
        if (user != null) {
            textFirstChar.setText(user.getFirstName().substring(0, 1));
            textViewUsername.setText(String.format("%s %s", user.getFirstName(), user.getLastName()));
            textEmail.setText(user.getEmail());
        }

        ImageView imageStopInvitation = activityOutGoingInvitationBinding.imageStopInvitation;
        imageStopInvitation.setOnClickListener(v -> {
            if (user != null) {
                cancelInvitation(user.getToken());
            }
        });

        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                inviterToken = task.getResult();
                if (user != null && meetingType != null) {
                    initMeeting(meetingType, user.getToken());
                }
            }
        });


    }

    private void initMeeting(String meetingType, String receiverToken) {
        try {
            JSONArray tokens = new JSONArray();
            tokens.put(receiverToken);

            JSONObject body = new JSONObject();
            JSONObject data = new JSONObject();

            data.put(Constants.REMOTE_MESSAGE_TYPE, Constants.REMOTE_MESSAGE_INVITATION);
            data.put(Constants.REMOTE_MESSAGE_MEETING_TYPE, meetingType);
            data.put(Constants.KEY_FIRST_NAME, preferenceManager.getString(Constants.KEY_FIRST_NAME));
            data.put(Constants.KEY_LAST_NAME, preferenceManager.getString(Constants.KEY_LAST_NAME));
            data.put(Constants.KEY_EMAIL, preferenceManager.getString(Constants.KEY_EMAIL));
            data.put(Constants.REMOTE_MESSAGE_INVITER_TOKEN, inviterToken);


            body.put(Constants.REMOTE_MESSAGE_DATA, data);
            body.put(Constants.REMOTE_MESSAGE_REGISTRATION_IDS, tokens);

            sendRemoteMessage(body.toString(), Constants.REMOTE_MESSAGE_INVITATION);


        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void sendRemoteMessage(String remoteMessageBody, String type) {
        ApiClient.getClient().create(ApiService.class).sendRemoteMessage(
                Constants.getRemoteMessageHeaders(), remoteMessageBody
        ).enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.isSuccessful()) {
                    if (type.equals(Constants.REMOTE_MESSAGE_INVITATION)) {
                        Toast.makeText(OutGoingInvitationActivity.this, "Invitation sent successfully", Toast.LENGTH_SHORT).show();
                        mainRepository.getInstance().updateCurrentUsername(getIntent().getStringExtra("sender"));
                        mainRepository.sendCallRequest(getIntent().getStringExtra("target"), () -> {
                            Toast.makeText(OutGoingInvitationActivity.this, "Some error happened", Toast.LENGTH_SHORT).show();
                        });

                        mainRepository.subscribeForLatestEvent(data -> {
                            Log.d("Data in Outgoing Activity", data.getSender());
                        });
                    } else if (type.equals(Constants.REMOTE_MESSAGE_INVITATION_RESPONSE)) {
                        Toast.makeText(OutGoingInvitationActivity.this, "Invitation cancelled", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } else {
                    Toast.makeText(OutGoingInvitationActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                Toast.makeText(OutGoingInvitationActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void cancelInvitation(String receiverToken) {
        try {
            JSONArray tokens = new JSONArray();
            tokens.put(receiverToken);

            JSONObject body = new JSONObject();
            JSONObject data = new JSONObject();

            data.put(Constants.REMOTE_MESSAGE_TYPE, Constants.REMOTE_MESSAGE_INVITATION_RESPONSE);
            data.put(Constants.REMOTE_MESSAGE_INVITATION_RESPONSE, Constants.REMOTE_MESSAGE_INVITATION_CANCELLED);

            body.put(Constants.REMOTE_MESSAGE_DATA, data);
            body.put(Constants.REMOTE_MESSAGE_REGISTRATION_IDS, tokens);

            sendRemoteMessage(body.toString(), Constants.REMOTE_MESSAGE_INVITATION_RESPONSE);
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private BroadcastReceiver invitationResponseReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String type = intent.getStringExtra(Constants.REMOTE_MESSAGE_INVITATION_RESPONSE);
            if (type != null) {
                if (type.equals(Constants.REMOTE_MESSAGE_INVITATION_ACCEPTED)) {
                    try {
                        startActivity(new Intent(OutGoingInvitationActivity.this, MyCallActivity.class));
                    } catch (Exception e) {

                    }
                }
            }
        }

    };

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(
                invitationResponseReceiver,
                new IntentFilter(Constants.REMOTE_MESSAGE_INVITATION_RESPONSE)
        );
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(
                invitationResponseReceiver
        );
    }

    @Override
    public void onTransferDataToOtherPeer(DataModel model) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}