package com.ducku.conferenceapp.repository;

import static com.ducku.conferenceapp.models.DataModelType.Answer;
import static com.ducku.conferenceapp.models.DataModelType.Offer;
import static com.ducku.conferenceapp.models.DataModelType.StartCall;

import android.content.Context;
import android.util.Log;

import com.ducku.conferenceapp.firebase.FirebaseClient;
import com.ducku.conferenceapp.models.DataModel;
import com.ducku.conferenceapp.models.DataModelType;
import com.ducku.conferenceapp.utils.Constants;
import com.ducku.conferenceapp.utils.ErrorCallBack;
import com.ducku.conferenceapp.utils.NewEventCallBack;
import com.ducku.conferenceapp.utils.PreferenceManager;
import com.ducku.conferenceapp.utils.SuccessCallBack;
import com.ducku.conferenceapp.webrtc.MyPeerConnectionObserver;
import com.ducku.conferenceapp.webrtc.WebRTCClient;
import com.google.gson.Gson;

import org.webrtc.IceCandidate;
import org.webrtc.MediaStream;
import org.webrtc.PeerConnection;
import org.webrtc.SessionDescription;
import org.webrtc.SurfaceViewRenderer;

public class MainRepository implements WebRTCClient.Listener {

    public Listener listener;
    private final Gson gson = new Gson();
    private final FirebaseClient firebaseClient;

    private WebRTCClient webRTCClient;

    private String currentUsername;

    private SurfaceViewRenderer remoteView;

    private String target;

    public void updateCurrentUsername(String username) {
        this.currentUsername = username;
    }

    private MainRepository() {
        this.firebaseClient = new FirebaseClient();

    }

    private static MainRepository instance;


    public static MainRepository getInstance() {
        if (instance == null) {
            instance = new MainRepository();
        }

        return instance;
    }


    public void login(String username, Context context, SuccessCallBack callBack) {
        firebaseClient.login(username, () -> {
            updateCurrentUsername(username);
            this.webRTCClient = new WebRTCClient(context, new MyPeerConnectionObserver() {
                @Override
                public void onAddStream(MediaStream mediaStream) {
                    super.onAddStream(mediaStream);
                    try {
                        mediaStream.videoTracks.get(0).addSink(remoteView);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onConnectionChange(PeerConnection.PeerConnectionState newState) {
                    Log.d("TAG", "onConnectionChange: " + newState);
                    super.onConnectionChange(newState);
                    if (newState == PeerConnection.PeerConnectionState.CONNECTED && listener != null) {
                        listener.webrtcConnected();
                    }

                    if (newState == PeerConnection.PeerConnectionState.CLOSED ||
                            newState == PeerConnection.PeerConnectionState.DISCONNECTED) {
                        if (listener != null) {
                            listener.webrtcClosed();
                        }
                    }
                }

                @Override
                public void onIceCandidate(IceCandidate iceCandidate) {
                    super.onIceCandidate(iceCandidate);
                    webRTCClient.sendIceCandidate(iceCandidate, target);
                }
            }, username);
            webRTCClient.listener = this;
            callBack.onSuccess();
        });
    }

    public void call(String username, Context context, SuccessCallBack callBack) {

        this.webRTCClient = new WebRTCClient(context, new MyPeerConnectionObserver() {
            @Override
            public void onAddStream(MediaStream mediaStream) {
                super.onAddStream(mediaStream);
                try {
                    mediaStream.videoTracks.get(0).addSink(remoteView);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onConnectionChange(PeerConnection.PeerConnectionState newState) {
                Log.d("TAG", "onConnectionChange: " + newState);
                super.onConnectionChange(newState);
                if (newState == PeerConnection.PeerConnectionState.CONNECTED && listener != null) {
                    listener.webrtcConnected();
                }

                if (newState == PeerConnection.PeerConnectionState.CLOSED ||
                        newState == PeerConnection.PeerConnectionState.DISCONNECTED) {
                    if (listener != null) {
                        listener.webrtcClosed();
                    }
                }
            }

            @Override
            public void onIceCandidate(IceCandidate iceCandidate) {
                super.onIceCandidate(iceCandidate);
                webRTCClient.sendIceCandidate(iceCandidate, target);
            }
        }, username);
        webRTCClient.listener = this;
        callBack.onSuccess();
    }


    public void initLocalView(SurfaceViewRenderer view) {
        Log.d("MainRepo", view.toString());
        webRTCClient.initLocalSurfaceView(view);
    }

    public void initRemoteView(SurfaceViewRenderer view) {
        webRTCClient.initRemoteSurfaceView(view);
        this.remoteView = view;
    }

    public void startCall(String target) {
        subscribeForLatestEvent((data) -> {
        });
        webRTCClient.call(target);

    }

    public void switchCamera() {
        webRTCClient.switchCamera();
    }

    public void toggleAudio(Boolean shouldBeMuted) {
        webRTCClient.toggleAudio(shouldBeMuted);
    }

    public void toggleVideo(Boolean shouldBeMuted) {
        webRTCClient.toggleVideo(shouldBeMuted);
    }

    public void sendCallRequest(String target, ErrorCallBack errorCallBack) {

        DataModel dataModel = new DataModel(target, currentUsername, null, StartCall);
        firebaseClient.sendMessageToOtherUser(dataModel
                , errorCallBack
        );
        Log.d("DataModel", dataModel.toString());
    }

    public void endCall() {
        webRTCClient.closeConnection();
    }

    public void subscribeForLatestEvent(NewEventCallBack callBack) {
        firebaseClient.observeIncomingLatestEvent(currentUsername, model -> {
            switch (model.getType()) {

                case Offer:
                    this.target = model.getSender();
                    webRTCClient.onRemoteSessionReceived(new SessionDescription(
                            SessionDescription.Type.OFFER, model.getData()
                    ));
                    webRTCClient.answer(model.getSender());
                    break;
                case Answer:
                    this.target = model.getSender();
                    webRTCClient.onRemoteSessionReceived(new SessionDescription(
                            SessionDescription.Type.ANSWER, model.getData()
                    ));
                    break;
                case IceCandidate:
                    try {
                        IceCandidate candidate = gson.fromJson(model.getData(), IceCandidate.class);
                        webRTCClient.addIceCandidate(candidate);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case StartCall:

                    this.target = model.getSender();
                    //callBack.onNewEventReceived(model);
                    break;
            }

        });
    }

    @Override
    public void onTransferDataToOtherPeer(DataModel model) {
        firebaseClient.sendMessageToOtherUser(model, () -> {
        });
    }

    public interface Listener {
        void webrtcConnected();

        void webrtcClosed();
    }
}
