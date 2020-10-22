package com.hengda.zwf.commonsettings;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

public class AlarmActivity extends Activity {

    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_MSG = "msg";
    public static final String EXTRA_CANCEL = "cancel";
    public static final String EXTRA_VOICE_ID = "voiceId";
    public static AlarmActivity sInstance;
    MediaPlayer mMediaPlayer;
    TextView tvTitle;
    TextView tvMsg;
    TextView tvCancel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        sInstance = this;
        setFinishOnTouchOutside(false);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvMsg = (TextView) findViewById(R.id.tvMsg);
        tvCancel = (TextView) findViewById(R.id.tvCancel);
        String title = getIntent().getExtras().getString(EXTRA_TITLE);
        String msg = getIntent().getExtras().getString(EXTRA_MSG);
        String cancel = getIntent().getExtras().getString(EXTRA_CANCEL);
        int voiceId = getIntent().getExtras().getInt(EXTRA_VOICE_ID);
        tvTitle.setText(title);
        tvMsg.setText(msg);
        tvCancel.setText(cancel);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sInstance.finish();
                sInstance = null;
            }
        });
        initPlayer(voiceId);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME) {
            //ignore
        }
        return false;
    }

    private void releasePlayer() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    private void initPlayer(int resid) {
        mMediaPlayer = MediaPlayer.create(this, resid);
        mMediaPlayer.setLooping(true);
        mMediaPlayer.start();
        setVolumeMax();
    }

    private void setVolumeMax() {
        AudioManager mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume, 0);
    }

}
