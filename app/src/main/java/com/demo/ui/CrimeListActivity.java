package com.demo.ui;

import android.support.v4.app.Fragment;

import com.demo.abstr.SingleFragmentActivity;
import com.demo.fragment.CrimeListFragment;

/**
 * Created by hc on 2016/7/16.
 */
public class CrimeListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }
}
