package net.dbuck.twowaystreaming;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import net.majorkernelpanic.streaming.SessionBuilder;
import net.majorkernelpanic.streaming.rtsp.RtspServer;
import net.majorkernelpanic.streaming.video.VideoQuality;


/**
 * Created by Dillon on 5/8/2015.
 */
public class Settings extends PreferenceActivity implements View.OnClickListener {

    EditText port, resolutionX, resolutionY, bitrate, framerate;
    Button apply;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        init();

    }

    private void init() {
        port = (EditText) findViewById(R.id.port);
        resolutionX = (EditText) findViewById(R.id.resolutionX);
        resolutionY = (EditText) findViewById(R.id.resolutionY);
        bitrate = (EditText) findViewById(R.id.bitrate);
        framerate = (EditText) findViewById(R.id.framerate);
        apply = (Button) findViewById(R.id.apply);

        apply.setOnClickListener(this);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        resolutionX.setText(prefs.getString("video_resX", "176"));
        resolutionY.setText(prefs.getString("video_resY", "144"));
        framerate.setText(prefs.getString("video_framerate", "30"));
        bitrate.setText(prefs.getString("video_bitrate", "500000"));

    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.apply){
            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
            editor.putString(RtspServer.KEY_PORT, String.valueOf(Integer.parseInt(port.getText().toString()))); // save port
            editor.putString("video_resX", resolutionX.getText().toString()); // save resolution
            editor.putString("video_resY", resolutionY.getText().toString()); // save resolution
            editor.putString("video_framerate", framerate.getText().toString()); // save framerate
            editor.putString("video_bitrate", bitrate.getText().toString()); // save bitrate

            editor.apply();
        }
    }
}
