package com.demo.base;

import android.content.Context;

import com.demo.bean.Crime;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by hc on 2016/7/16.
 */
public class CrimeLab {
    private volatile static CrimeLab sCrimeLab;
    private List<Crime> mCrimeList;
    private CrimeLab(Context context){
        mCrimeList = new ArrayList<>();
        for(int i=0;i<100;i++){
            Crime crime = new Crime();
            crime.setTitle("crime #" + i);
            crime.setSolved(i % 2 == 0);
            mCrimeList.add(crime);
        }
    }

    public static CrimeLab getInstance(Context context){
        if(sCrimeLab == null){
            synchronized (CrimeLab.class){
                if(sCrimeLab == null){
                    sCrimeLab = new CrimeLab(context.getApplicationContext());
                }
            }
        }
        return sCrimeLab;
    }
    /**
     * 获取crime列表
     */
    public List<Crime> getCrimeList(){
        return mCrimeList;
    }

    /**
     * 获取具体的crime对象
     * @param id
     * @return
     */
    public Crime getCrime(UUID id){
        for(Crime crime : mCrimeList){
            if(crime.getId().equals(id)){
                return crime;
            }
        }
        return null;
    }
}
