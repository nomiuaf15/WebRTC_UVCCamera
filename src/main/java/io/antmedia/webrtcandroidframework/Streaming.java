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
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import de.tavendo.autobahn.WebSocket;

public class Streaming implements IWebRTCListener, IDataChannelObserver {
    String TokenId = "tokenId";

    WebRTCClient webRTCClient;
    String SERVER_URL, StreamId, STREAM_MODE;
    Context context;

    SurfaceViewRenderer publisherSurfaceView, viewerSurfaceView;
    IWebRTCListener webRTCListener;
    IDataChannelObserver observer;
    Intent intent;
    int cameraFront;


    public Streaming(String SERVER_URL, String streamId, String STREAM_MODE, Context context, SurfaceViewRenderer publisherSurfaceView, SurfaceViewRenderer viewerSurfaceView, IWebRTCListener webRTCListener, IDataChannelObserver observer, Intent intent) {
        this.SERVER_URL = SERVER_URL;
        this.StreamId = streamId;
        this.STREAM_MODE = STREAM_MODE;
        this.context = context;
        this.publisherSurfaceView = publisherSurfaceView;
        this.viewerSurfaceView = viewerSurfaceView;
        this.webRTCListener = webRTCListener;
        this.observer = observer;
        this.intent = intent;
    }
    public Streaming(String SERVER_URL, String streamId, String STREAM_MODE, Context context, SurfaceViewRenderer publisherSurfaceView, SurfaceViewRenderer viewerSurfaceView, IWebRTCListener webRTCListener, IDataChannelObserver observer, Intent intent,int cameraFront) {
        this.SERVER_URL = SERVER_URL;
        this.StreamId = streamId;
        this.STREAM_MODE = STREAM_MODE;
        this.context = context;
        this.publisherSurfaceView = publisherSurfaceView;
        this.viewerSurfaceView = viewerSurfaceView;
        this.webRTCListener = webRTCListener;
        this.observer = observer;
        this.intent = intent;
        this.cameraFront = cameraFront;
    }
    public void initialize() {
        webRTCClient = new WebRTCClient(webRTCListener, context);
        if (cameraFront == 1 ) {
            webRTCClient.setOpenFrontCamera(false);
        } else if(cameraFront == 2) {
            webRTCClient.setOpenFrontCamera(true);
        }
//        else {
//            showToast(context,"Now USB Camera is Selected");
//        }
        webRTCClient.setVideoRenderers(viewerSurfaceView, publisherSurfaceView);
        webRTCClient.setDataChannelObserver(observer);
        webRTCClient.init(SERVER_URL, StreamId, STREAM_MODE, TokenId, intent);
    }

    public void startStreaming() {
        if (StreamId == null || webRTCClient == null) {
            showToast(context, "Check StreamId or initialize webRTCClient");
        } else {
            webRTCClient.startStream();
        }
    }

    public void stopStreaming() {
        if (StreamId == null || webRTCClient == null) {
            showToast(context, "Check StreamId or initialize webRTCClient");
        } else {
            webRTCClient.stopStream();
        }
    }

    public void muteAudio() {
        webRTCClient.disableAudio();
    }

    public void unMuteAudio() {
        webRTCClient.enableAudio();
    }

    public void disableVideo() {
        webRTCClient.disableVideo();
    }

    public void enableVideo() {
        webRTCClient.enableVideo();
    }

    public void swapCamera() {
        webRTCClient.switchCamera();
    }

    public void toggleMic() {
        webRTCClient.toggleMic();
    }


    public void checkPermissionsForStreaming() {
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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void sendTextMessage(String messageToSend) {
        final ByteBuffer buffer = ByteBuffer.wrap(messageToSend.getBytes(StandardCharsets.UTF_8));
        DataChannel.Buffer buf = new DataChannel.Buffer(buffer, false);
        webRTCClient.sendMessageViaDataChannel(buf);

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
        showToast(context, "No Stream Exists To Play");
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

    }

    @Override
    public void onMessageSent(DataChannel.Buffer buffer, boolean successful) {

    }
}
