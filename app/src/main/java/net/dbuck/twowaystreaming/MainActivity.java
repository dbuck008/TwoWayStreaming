package net.dbuck.twowaystreaming;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import net.majorkernelpanic.streaming.SessionBuilder;
import net.majorkernelpanic.streaming.gl.SurfaceView;
import net.majorkernelpanic.streaming.rtsp.RtspServer;
import net.majorkernelpanic.streaming.video.VideoQuality;


public class MainActivity extends Activity implements SharedPreferences.OnSharedPreferenceChangeListener {
    private final static String TAG = "MainActivity";

    private SurfaceView mSurfaceView;
    private VideoQuality videoQuality = new VideoQuality(320,240,7,2000000);
    private SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mSurfaceView = (SurfaceView) findViewById(R.id.surface);
        settings = PreferenceManager.getDefaultSharedPreferences(this);
        settings.registerOnSharedPreferenceChangeListener(this);

        // Sets the port of the RTSP server to 8086
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        editor.putString(RtspServer.KEY_PORT, String.valueOf(8086));
        editor.apply();

        // Read video quality settings from the preferences
//        videoQuality = new VideoQuality(
//                settings.getInt("video_resX", videoQuality.resX),
//                settings.getInt("video_resY", videoQuality.resY),
//                Integer.parseInt(settings.getString("video_framerate", String.valueOf(videoQuality.framerate))),
//                Integer.parseInt(settings.getString("video_bitrate", String.valueOf(videoQuality.bitrate/1000)))*1000);


        // Configures the SessionBuilder
        SessionBuilder.getInstance()
                .setSurfaceView(mSurfaceView)
                .setPreviewOrientation(0)
                .setContext(getApplicationContext())
                .setAudioEncoder(SessionBuilder.AUDIO_NONE)
                .setVideoQuality(videoQuality)
                .setVideoEncoder(SessionBuilder.VIDEO_H264);

        // Starts the RTSP server
        this.startService(new Intent(this,RtspServer.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(this, Settings.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("video_resX") || key.equals("video_resY")) {
            videoQuality.resX = sharedPreferences.getInt("video_resX", 0);
            videoQuality.resY = sharedPreferences.getInt("video_resY", 0);
        }

        else if (key.equals("video_framerate")) {
            videoQuality.framerate = Integer.parseInt(sharedPreferences.getString("video_framerate", "0"));
        }

        else if (key.equals("video_bitrate")) {
            videoQuality.bitrate = Integer.parseInt(sharedPreferences.getString("video_bitrate", "0"))*1000;
        }
    }
}
