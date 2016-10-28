package com.typeAdapter;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.demo.R;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_main);
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

        Mson sa = new Mson();
        Map map2 = new HashMap();
        map2.put("name","a");
        List list = new ArrayList();
        list.add(2);
        list.add(3);
        Map<String,String> map = sa.beanToMap(b);
        for (Map.Entry<String, String> entry : map.entrySet()) {
            Log.d("ok","Key : " + entry.getKey() + ", Value : " + entry.getValue().getClass());
        }


		Iterator<Map.Entry<String, String>> iter = map.entrySet().iterator();

		Map.Entry<String, String> entry;

		while (iter.hasNext()){
			entry = iter.next();
			Object value = entry.getValue();
			Log.d("ok1", value.getClass().getName());
		}
        Log.d("ok1", map.toString());
    }

    public enum Weather{
        sunny,rainy,cloudy
    }
}
