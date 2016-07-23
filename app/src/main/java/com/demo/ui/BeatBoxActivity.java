package com.demo.ui;

import android.support.v4.app.Fragment;

import com.demo.abstr.SingleFragmentActivity;
import com.demo.fragment.BeatBoxFragment;

/**
 * Created by hc on 2016/7/23.
 */
public class BeatBoxActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return BeatBoxFragment.newIntance();
    }
}
