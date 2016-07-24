package com.demo.ui;

import android.support.v4.app.Fragment;

import com.demo.abstr.SingleFragmentActivity;
import com.demo.fragment.PhotoGalleryFragment;

/**
 * Created by hc on 2016/7/24.
 */
public class PhotoGalleryActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return PhotoGalleryFragment.newInstance();
    }
}
