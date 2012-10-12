package com.kovalenych.tables;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;
import com.kovalenych.R;

import java.util.HashMap;


public class SoundManager implements Soundable {
    private SoundPool mSoundPool;
    private HashMap mSoundPoolMap;
    private AudioManager mAudioManager;
    private Context mContext;
    public static volatile float volume = 25;


    public SoundManager(Context context) {
        mContext = context;
        initSounds();
    }

    public void initSounds() {

        mSoundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
        mSoundPoolMap = new HashMap<Integer, Integer>();
        mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        addSound(TO_START_2_MIN, R.raw.to2min);
        addSound(TO_START_1_MIN, R.raw.to1min);
        addSound(TO_START_30_SEC, R.raw.to30sec);
        addSound(TO_START_10_SEC, R.raw.to10sec);
        addSound(TO_START_5_SEC, R.raw.to5sec);
        addSound(START, R.raw.start);
        addSound(AFTER_START_1, R.raw.after1min);
        addSound(AFTER_START_2, R.raw.after2min);
        addSound(AFTER_START_3, R.raw.after3min);
        addSound(AFTER_START_4, R.raw.after4min);
        addSound(AFTER_START_5, R.raw.after5min);
        addSound(BREATHE, R.raw.breathe);
        addSound(LIST_DROP, R.raw.list_drop);      //todo test landscape
    }

    public void addSound(int index, int SoundID) {
        mSoundPoolMap.put(index, mSoundPool.load(mContext, SoundID, 1));
    }

    public void playSound(int index) {
        float streamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC) * volume * 10;
        streamVolume = streamVolume / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        if (mSoundPool != null && mSoundPoolMap != null) {
            mSoundPool.play((Integer) mSoundPoolMap.get(index), streamVolume, streamVolume, 1, 0, 1f);
            Log.d("UnaeroApplication ",""+streamVolume + " vol " + volume);
        }
    }


}
