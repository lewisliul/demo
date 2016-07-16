package com.demo.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.demo.R;
import com.demo.fragment.CrimeFragment;


public class CrimeActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_main);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.id_crime_fragmentcontainer_framelayout);
        if(fragment == null){
            fragment = new CrimeFragment();
            fm.beginTransaction().add(R.id.id_crime_fragmentcontainer_framelayout,fragment).commit();
        }
    }
}
