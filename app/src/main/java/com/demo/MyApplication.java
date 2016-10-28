package com.demo;

import android.app.Application;

import com.elvishew.xlog.LogConfiguration;
import com.elvishew.xlog.LogLevel;
import com.elvishew.xlog.XLog;
import com.elvishew.xlog.formatter.message.json.DefaultJsonFormatter;
import com.elvishew.xlog.formatter.message.throwable.DefaultThrowableFormatter;
import com.elvishew.xlog.formatter.message.xml.DefaultXmlFormatter;
import com.elvishew.xlog.formatter.stacktrace.DefaultStackTraceFormatter;
import com.elvishew.xlog.formatter.thread.DefaultThreadFormatter;
import com.elvishew.xlog.printer.AndroidPrinter;
import com.elvishew.xlog.printer.SystemPrinter;
import com.elvishew.xlog.printer.file.FilePrinter;
import com.elvishew.xlog.printer.file.backup.FileSizeBackupStrategy;
import com.elvishew.xlog.printer.file.naming.DateFileNameGenerator;
import com.elvishew.xlog.printer.flattener.DefaultLogFlattener;
import com.elvishew.xlog.formatter.border.DefaultBorderFormatter;

/**
 * Created by hc on 2016/10/27.
 */
public class MyApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();

//		XLog.init(LogLevel.ALL);


		XLog.init(LogLevel.ALL,
				new LogConfiguration                                             // 如果没有指定 LogConfiguration，会默认使用 new LogConfiguration.Builder().build()
						.Builder()                                               // 打印日志时会用到的配置
						.tag("ok")                                           // 默认: "XLOG"
						.t()                                                     // 允许打印线程信息，默认禁止
						.st(1)                                                   // 允许打印调用栈信息，默认禁止
						.b()                                                     // Enable border, disabled by default
						.jsonFormatter(new DefaultJsonFormatter())               // 默认: DefaultJsonFormatter
						.xmlFormatter(new DefaultXmlFormatter())                 // 默认: DefaultXmlFormatter
						.throwableFormatter(new DefaultThrowableFormatter())     // 默认: DefaultThrowableFormatter
						.threadFormatter(new DefaultThreadFormatter())           // 默认: DefaultThreadFormatter
						.stackTraceFormatter(new DefaultStackTraceFormatter())   // 默认: DefaultStackTraceFormatter
						.borderFormatter(new DefaultBorderFormatter())            // 默认: DefaultBorderFormatter
						.build(),
				new AndroidPrinter(),                                            // 通过 android.util.Log 打印 log。如果没有指定任何 Printer，会默认使用 AndroidPrinter
				new SystemPrinter(),                                             // 通过 System.out.println 打印日志。如果没有指定，则不会使用
				new FilePrinter                                                  // 打印日志到文件。如果没有指定，则不会使用
						.Builder("/sdcard/xlog/")                                // 保存日志文件的路径
						.fileNameGenerator(new DateFileNameGenerator())          // 默认: ChangelessFileNameGenerator("log")
						.backupStrategy(new FileSizeBackupStrategy(1024 * 1024)) // 默认: FileSizeBackupStrategy(1024 * 1024)
						.logFormatter(new DefaultLogFlattener())                 // 默认: DefaultLogFlattener
						.build());
	}
}
