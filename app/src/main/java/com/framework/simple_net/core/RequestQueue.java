package com.framework.simple_net.core;

import android.util.Log;

import com.framework.simple_net.base.Request;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by hc on 2016/11/28.
 */
public class RequestQueue {

	private BlockingQueue<Request<?>> mRequestQueue = new PriorityBlockingQueue<Request<?>>();//请求队列

	private AtomicInteger mSerialNumGenerator = new AtomicInteger(0);//请求的序列化生成器

	public static int DEFAULT_CORE_NUMS = Runtime.getRuntime().availableProcessors() + 1;//默认的核心数

	private int mDispatcherNums = DEFAULT_CORE_NUMS;//CPU核心数 + 1个分发线程数

	private NetworkExecutor[] mDispatchers = null;//NetworkExecutor,执行网络请求的线程

	private HttpStack mHttpStack; // Http请求的真正执行者

	public RequestQueue(int coreNums, HttpStack httpStack) {
		mDispatcherNums = coreNums;
		mHttpStack = httpStack != null ? httpStack : HttpStackFactory.createHttpStack();
	}

	/**
	 * 启动NetworkExecutor
	 */
	private final void startNetworkExecutors(){
		mDispatchers = new NetworkExecutor[mDispatcherNums];
		for(int i = 0; i < mDispatcherNums; i++){
			mDispatchers[i] = new NetworkExecutor(mRequestQueue, mHttpStack);
			mDispatchers[i].start();
		}
	}

	public void start(){
		stop();
		startNetworkExecutors();
	}

	/**
	 * 停止NetworkExecutor
	 */
	public void stop(){
		if(mDispatchers != null && mDispatchers.length > 0){
			for(int i = 0; i < mDispatchers.length; i++){
				mDispatchers[i].quit();
			}
		}
	}


	/**
	 * 不能重复添加请求
	 * @param request
	 */
	public void addRequest(Request<?> request){
		if(!mRequestQueue.contains(request)){
			request.setSerialNumber(this.generateSerialNumber());
			mRequestQueue.add(request);
			Log.d("ok", "### 请求添加到队列成功");
		}else{
			Log.d("ok", "### 请求队列中已经含有");
		}
	}


	/**
	 * 为每个请求生成一个系列号
	 * @return 序列号
	 */
	private int generateSerialNumber() {
		return mSerialNumGenerator.incrementAndGet();
	}

	public void clear(){
		mRequestQueue.clear();
	}

	public BlockingQueue<Request<?>> getAllRequest(){
		return mRequestQueue;
	}

}
