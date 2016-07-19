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

    /**
     * 添加crime对象
     */
    public void addCrime(Crime c){
        mCrimeList.add(c);
    }
}
