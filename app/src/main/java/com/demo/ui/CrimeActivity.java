package com.demo.ui;

import android.support.v4.app.Fragment;

import com.demo.abstr.SingleFragmentActivity;
import com.demo.fragment.CrimeFragment;


public class CrimeActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new CrimeFragment();
    }
}
