package com.demo.base;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

import com.demo.bean.Sound;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hc on 2016/7/23.
 */
public class BeatBox {
    private static final String TAG = "BeatBox";
    private static final String SOUND_FOLDER = "sample_sounds";
    private static final int MAX_SOUNDS = 5;
    private AssetManager mAssetsManager;
    private List<Sound> mSoundList = new ArrayList<>();
    private SoundPool mSoundPool;

    public BeatBox(Context context){
        mAssetsManager = context.getAssets();
        mSoundPool = new SoundPool(MAX_SOUNDS,AudioManager.STREAM_MUSIC,0);
        loadSounds();
    }

    /**
     * 获取assets中的资源
     */
    private void loadSounds(){
        String[] soundNames;
        try{
            soundNames = mAssetsManager.list(SOUND_FOLDER);
            Log.d(TAG, "loadSounds: " + soundNames.length + "sounds");
        }catch (Exception ioe){
            Log.e(TAG,"could not list assets",ioe);
            return;
        }

        for(String filename : soundNames){
            String path = SOUND_FOLDER + "/" + filename;
            Sound sound = new Sound(path);
            try {
                load(sound);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mSoundList.add(sound);
        }
    }

    /**
     * 载入音频
     */
    private void load(Sound sound) throws IOException{
        AssetFileDescriptor afd = mAssetsManager.openFd(sound.getAssetPath());
        int soundId = mSoundPool.load(afd,1);
        sound.setSoundId(soundId);
    }

    /**
     * 播放音频
     * @param sound
     */
    public void play(Sound sound){
        Integer soundId = sound.getSoundId();
        if(soundId == null){
            return;
        }
        mSoundPool.play(soundId,1.0f,1.0f,1,0,1.0f);

    }

    public void release(){
        mSoundPool.release();
    }
    public List<Sound> getSoundList() {
        return mSoundList;
    }
}
