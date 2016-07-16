package com.demo.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.demo.abstr.SingleFragmentActivity;
import com.demo.fragment.CrimeFragment;

import java.util.UUID;


public class CrimeActivity extends SingleFragmentActivity {
    private static final String CRIME_ID = "crime_id";

    public static Intent newIntent(Context context, UUID crimeId) {
        Intent intent = new Intent(context, CrimeActivity.class);
        intent.putExtra(CRIME_ID, crimeId);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        UUID id = (UUID) getIntent().getSerializableExtra(CRIME_ID);
        return CrimeFragment.newInstace(id);
    }
}
