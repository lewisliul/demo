package com.demo.base;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.demo.bean.Sound;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hc on 2016/7/23.
 */
public class BeatBox {
    private static final String TAG = "BeatBox";
    private static final String SOUND_FOLDER = "sample_sounds";
    private AssetManager mAssetsManager;
    private List<Sound> mSoundList = new ArrayList<>();

    public BeatBox(Context context){
        mAssetsManager = context.getAssets();
    }

    /**
     * 获取assets中的资源
     */
    private void loadSounds(){
        String[] soundNames;
        try{
            soundNames = mAssetsManager.list(SOUND_FOLDER);
            Log.i(TAG, "loadSounds: " + soundNames.length + "sounds");
        }catch (Exception ioe){
            Log.e(TAG,"could not list assets",ioe);
            return;
        }

        for(String filename : soundNames){
            String path = SOUND_FOLDER + "/" + filename;
            Sound sound = new Sound(path);
            mSoundList.add(sound);
        }
    }

    public List<Sound> getSoundList() {
        return mSoundList;
    }
}
