package com.demo.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.demo.bean.Crime;
import com.demo.db.CrimeBaseHelper;
import com.demo.db.CrimeCursorWrapper;
import com.demo.db.CrimeDb.CrimeTable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by hc on 2016/7/16.
 */
public class CrimeLab {
    private volatile static CrimeLab sCrimeLab;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    private CrimeLab(Context context){
        mContext = context.getApplicationContext();
        mDatabase = new CrimeBaseHelper(mContext).getWritableDatabase();
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
        List<Crime> crimeList = new ArrayList<>();
        CrimeCursorWrapper cursor = queryCrimes(null,null);
        try{
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                crimeList.add(cursor.getCrime());
                cursor.moveToNext();
            }
        }finally {
            cursor.close();
        }
        return crimeList;
    }

    /**
     * 获取具体的crime对象
     * @param id
     * @return
     */
    public Crime getCrime(UUID id) {
        CrimeCursorWrapper cursor = queryCrimes(CrimeTable.Cols.UUID + " = ?", new String[]{id.toString()});
        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getCrime();
        } finally {
            cursor.close();
        }
    }
    /**
     * 更新crime对象
     */
    public void updateCrime(Crime crime){
        String uuidString = crime.getId().toString();
        ContentValues values = getContentValues(crime);
        mDatabase.update(CrimeTable.NAME,values,CrimeTable.Cols.UUID + " = ?", new String[]{uuidString});
    }

    /**
     * 添加crime对象
     */
    public void addCrime(Crime c){
        ContentValues values = getContentValues(c);
        mDatabase.insert(CrimeTable.NAME,null,values);
    }

    /**
     * 创建contentvalues对象
     * @param crime
     * @return
     */
    private static ContentValues getContentValues(Crime crime){
        ContentValues values = new ContentValues();
        values.put(CrimeTable.Cols.UUID,crime.getId().toString());
        values.put(CrimeTable.Cols.TITLE,crime.getTitle());
        values.put(CrimeTable.Cols.DATE,crime.getDate().getTime());
        values.put(CrimeTable.Cols.SOLVED,crime.isSolved() ? 1 : 0);
        values.put(CrimeTable.Cols.SUSPECT,crime.getSuspect());
        return values;
    }

    /**
     * 查询记录
     */
    private CrimeCursorWrapper queryCrimes(String whereClause, String[] args){
        Cursor cursor = mDatabase.query(CrimeTable.NAME,
                null,
                whereClause,
                args,
                null,null,null);

        return new CrimeCursorWrapper(cursor);
    }

    /**
     * 删除记录
     */
    public void deleteCrime(){
        mDatabase.delete(CrimeTable.NAME,null,null);
    }

    /**
     * 定位图片文件的目录
     */
    public File getPhotoFile(Crime crime){
        File fileDir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if(fileDir == null){
            return null;
        }
        File path = new File(fileDir,crime.getPhotoFilename());
        return path;
    }
}
