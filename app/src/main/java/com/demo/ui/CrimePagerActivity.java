package com.demo.ui;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.demo.R;
import com.demo.data.CrimeLab;
import com.demo.bean.Crime;
import com.demo.fragment.CrimeFragment;

import java.util.List;
import java.util.UUID;
/**
 * Created by hc on 2016/7/17.
 */
public class CrimePagerActivity extends AppCompatActivity {
    private static final String EXTRA_CRIME_ID = "extra_crime_id";
    private ViewPager mViewPager;
    private List<Crime> mCrimes;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crimepager_main);

        UUID id = (UUID)getIntent().getSerializableExtra(EXTRA_CRIME_ID);

        mViewPager = (ViewPager)findViewById(R.id.id_activity_crimepager_viewpager);
        mCrimes = CrimeLab.getInstance(this).getCrimeList();
        FragmentManager manager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(manager) {
            @Override
            public Fragment getItem(int position) {
                Crime crime = mCrimes.get(position);
                return CrimeFragment.newInstace(crime.getId());
            }
            @Override
            public int getCount() {
                return mCrimes.size();
            }
        });
        //点击当前列表item，跳转相应的view页面
        for(int i=0;i<mCrimes.size();i++){
            if(mCrimes.get(i).getId().equals(id)){
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }
    //创建跳转的intent
    public static Intent newIntent(Context context, UUID id){
        Intent intent = new Intent(context,CrimePagerActivity.class);
        intent.putExtra(EXTRA_CRIME_ID,id);
        return intent;
    }
}
