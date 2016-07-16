package com.demo.abstr;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.demo.R;

/**
 * Created by hc on 2016/7/16.
 */
public abstract class SingleFragmentActivity extends FragmentActivity {
    protected abstract Fragment createFragment();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_main);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.id_crime_fragmentcontainer_framelayout);
        if(fragment == null){
            fragment = createFragment();
            fm.beginTransaction().add(R.id.id_crime_fragmentcontainer_framelayout,fragment).commit();
        }
    }
}
