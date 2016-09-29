package com.moretv;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        LogHelper.debugLog("ok","test main activity 你的的は啊我aawegawwwwwwwwwwwwww" +
//                "wwwwwwwwwghah哈哈哈哈哈哈哈哈哈哈哈哈");
//        LogHelper.debugLog("test main activity aabbblllllllllllllbbbb");
//        LogHelper.debugLog("");
//        LogHelper.debugLog("test main activity 你的的は啊" +
//                "哈哈哈哈哈哈哈哈我aawegawwwwwwwwwwwwwwwwwwwwwwwghah哈哈哈哈哈哈哈哈哈哈哈哈" +
//                "哈哈哈哈哈哈哈哈我aawegawwwwwwwwwwwwwwwwwwwwwwwghah哈哈哈哈" +
//                "哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈我aawegawwwwwwwwwwwwwwwwwwwwww" +
//                "wghah哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈我a" +
//                "awegawwwwwwwwwwwwwwwwwwwwwwwghah哈哈哈哈wghah哈哈" +
//                "哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈我aawega" +
//                "wwwwwwwwwwwwwwwwwwwwwwwghah哈哈哈哈wghah哈哈哈哈哈哈哈哈哈哈" +
//                "哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈我aawegawwwwwwwwwwwwwwwwwwwwwwwghah哈" +
//                "哈哈哈wghah哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈我" +
//                "aawegawwwwwwwwwwwwwwwwwwwwwwwghah哈哈哈哈wghah哈哈哈哈哈" +
//                "哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈我aawegawwwwwwwwwwwwwwwwwwwwwwwghah哈哈哈哈");
//        LogHelper.debugLog("test main activity 你的的は啊我aawegawww");

        Bean b = new Bean();
        Bean b2 = null;
        Gson gson = new Gson();
        String s = gson.toJson(b);
//        Log.d("ok",b.toString());
//        Log.d("ok",s);
        Boolean f = false;
        boolean ff = true;
        Date date = new Date();
        Long a = 1212412l;
        char c = 'a';
        float d = 123.236f;
        String str = "aaaaaa";
        Calendar calendar = Calendar.getInstance();

        Mson sa = new Mson();
        Map map2 = new HashMap();
        map2.put("name","a");
        List list = new ArrayList();
        list.add(2);
        list.add(3);
        Map<String,Object> map = sa.beanToMap(b);
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            Log.d("ok","Key : " + entry.getKey() + ", Value : " + entry.getValue());
        }
        Log.d("ok1", map.toString());
    }

    public enum Weather{
        sunny,rainy,cloudy
    }
}
