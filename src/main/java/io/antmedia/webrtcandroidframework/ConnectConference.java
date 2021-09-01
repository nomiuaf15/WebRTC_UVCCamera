package io.antmedia.webrtcandroidframework;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import org.webrtc.DataChannel;
import org.webrtc.SurfaceViewRenderer;

import java.util.ArrayList;

import de.tavendo.autobahn.WebSocket;

public class ConnectConference implements IWebRTCListener, IDataChannelObserver {

    ConferenceManager conferenceManager;
    String[] Permissions = new String[]{
            Manifest.permission.CAMERA, Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.MODIFY_AUDIO_SETTINGS, Manifest.permission.RECORD_AUDIO,
            Manifest.permission.BLUETOOTH, Manifest.permission.INTERNET,
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.READ_PHONE_STATE
    };


    IWebRTCListener webRTCListener;
    Context context;
    Intent intent;
    String roomId, SERVER_URL;
    String streamId = null;
    SurfaceViewRenderer surfaceView;
    ArrayList<SurfaceViewRenderer> othersViewSurfaceView;
    IDataChannelObserver dataChannelObserver;

    public ConnectConference(IWebRTCListener webRTCListener, Context context, Intent intent, String roomId, String SERVER_URL, SurfaceViewRenderer surfaceView, ArrayList<SurfaceViewRenderer> othersViewSurfaceView, IDataChannelObserver dataChannelObserver) {
        this.webRTCListener = webRTCListener;
        this.context = context;
        this.intent = intent;
        this.roomId = roomId;
        this.SERVER_URL = SERVER_URL;
        this.surfaceView = surfaceView;
        this.othersViewSurfaceView = othersViewSurfaceView;
        this.dataChannelObserver = dataChannelObserver;

    }

    public void initializeConferenceManager() {
        if (roomId == null) {
            Toast.makeText(context, "RoomId Required", Toast.LENGTH_SHORT).show();
        } else {
            conferenceManager = new ConferenceManager(context, webRTCListener, intent,
                    SERVER_URL, roomId, surfaceView, othersViewSurfaceView, streamId, dataChannelObserver);
            conferenceManager.setPlayOnlyMode(false);
            conferenceManager.setOpenFrontCamera(true);
        }
    }

    public void joinConference() {
        if (conferenceManager == null) {
            showToast(context, "Initialize Conference Manager First");
        } else {
            conferenceManager.joinTheConference();
        }
    }

    public void leaveConference() {
        if (conferenceManager == null) {
            showToast(context, "Initialize Conference Manager First");
        } else {
            conferenceManager.leaveFromConference();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void disableVideo() {
        conferenceManager.disableVideo();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void muteAudio() {
        conferenceManager.disableAudio();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void enableVideo() {
        conferenceManager.enableVideo();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void unMuteAudio() {
        conferenceManager.enableAudio();
    }


    public void checkPermissionsForConference() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
                + ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                + ContextCompat.checkSelfPermission(context, Manifest.permission.MODIFY_AUDIO_SETTINGS)
                + ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO)
                + ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            showToast(context, "Permission Needed");
        }
    }

    public static void showToast(Context context, String message) {
        if (BuildConfig.DEBUG) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDisconnected(String streamId) {
        showToast(context, "Disconnected");
    }

    @Override
    public void onPublishFinished(String streamId) {
        showToast(context, "Publish Finished");
    }

    @Override
    public void onPlayFinished(String streamId) {
        showToast(context, "Play Finished");
    }

    @Override
    public void onPublishStarted(String streamId) {
        showToast(context, "Publish Started");
    }

    @Override
    public void onPlayStarted(String streamId) {
        showToast(context, "Play Started");
    }

    @Override
    public void noStreamExistsToPlay(String streamId) {
        showToast(context, "No Stream Exist to Play");
    }

    @Override
    public void onError(String description, String streamId) {
        showToast(context, description);
    }

    @Override
    public void onSignalChannelClosed(WebSocket.WebSocketConnectionObserver.WebSocketCloseNotification code, String streamId) {
        showToast(context, "Signal Channel Closed with code " + code);
    }

    @Override
    public void streamIdInUse(String streamId) {
        showToast(context, "Stream in Use " + streamId);
    }

    @Override
    public void onIceConnected(String streamId) {

    }

    @Override
    public void onIceDisconnected(String streamId) {
        showToast(context, "Conference manager publish stream id left" + streamId);
    }

    @Override
    public void onTrackList(String[] tracks) {

    }

    @Override
    public void onBitrateMeasurement(String streamId, int targetBitrate, int videoBitrate, int audioBitrate) {

    }

    @Override
    public void onStreamInfoList(String streamId, ArrayList<StreamInfo> streamInfoList) {

    }

    @Override
    public void onBufferedAmountChange(long previousAmount, String dataChannelLabel) {

    }

    @Override
    public void onStateChange(DataChannel.State state, String dataChannelLabel) {

    }

    @Override
    public void onMessage(DataChannel.Buffer buffer, String dataChannelLabel) {
//        ByteBuffer data = buffer.data;
//        String strDataJson = new String(data.array(), StandardCharsets.UTF_8);
//        try {
//            JSONObject json = new JSONObject(strDataJson);
//            String eventType = json.getString("eventType");
//            String streamId = json.getString("streamId");
//            Toast.makeText(this, eventType + " : " + streamId, Toast.LENGTH_LONG).show();
//        } catch (Exception e) {
//            Log.e(getClass().getSimpleName(), e.getMessage());
//        }
    }

    @Override
    public void onMessageSent(DataChannel.Buffer buffer, boolean successful) {
//        ByteBuffer data = buffer.data;
//        String strDataJson = new String(data.array(), StandardCharsets.UTF_8);
//
//        Log.e(getClass().getSimpleName(), "SentEvent: " + strDataJson);
    }
}
