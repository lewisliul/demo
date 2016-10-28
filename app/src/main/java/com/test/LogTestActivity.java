package com.test;

import android.app.Activity;
import android.os.Bundle;

import com.demo.R;
import com.elvishew.xlog.Logger;
import com.elvishew.xlog.XLog;

/**
 * Created by hc on 2016/10/27.
 */
public class LogTestActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_logtest);

		String jsonString = "{name:Elvis, age: 18}";
		String xmlString = "<Person name=\"Elvis\" age=\"18\" />";

		XLog.d("The message");
		XLog.d("The message with argument: age=%s", 18);
		XLog.json(jsonString);
		XLog.xml(xmlString);

		XLog.d("|||||||||||||||||||||||||||||||||");

		Logger logger = XLog.b().build();
		logger.d("The message");
		logger.d("The message with argument: age=%s", 18);
		logger.json(jsonString);
		logger.xml(xmlString);
	}
}
