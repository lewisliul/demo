package com.demo.bean;

/**
 * Created by hc on 2016/7/23.
 */
public class Sound {
    private String mAssetPath;
    private String mName;

    public Sound(String mAssetPath) {
        this.mAssetPath = mAssetPath;
        String[] components = mAssetPath.split("/");
        String filename = components[components.length-1];
        mName = filename.replace(".wav","");
    }

    public String getName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }
}
