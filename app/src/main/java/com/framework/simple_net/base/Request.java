package com.framework.simple_net.base;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hc on 2016/11/28.
 */
public abstract class Request<T> implements Comparable<Request<T>> {

	public static final String DEFAULT_PARAMS_ENCODING = "UTF-8";
	public final static String HEADER_CONTENT_TYPE = "Content-Type";
	protected int mSerialNum = 0;    //请求序列号
	protected Priority mPriority = Priority.NORMAL; //优先级默认设置为Normal
	protected boolean isCancel = false; //是否取消该请求
	private boolean mShouldCache = true; //该请求是否应该缓存
	protected RequestListener<T> mRequestListener; //请求Listener
	private String mUrl = "";
	HttpMethod mHttpMethod = HttpMethod.GET; //请求的方法
	private Map<String, String> mHeaders = new HashMap<String, String>();//请求的header
	private Map<String, String> mBodyParams = new HashMap<String, String>();//请求参数

	public static interface RequestListener<T>{
		//请求完成回调
		public void onComplete(int stCode, T response, String errMsg);
	}

	public Request(HttpMethod method, String url, RequestListener<T> listener) {
		mHttpMethod = method;
		mUrl = url;
		mRequestListener = listener;
	}

	//从原生的网络请求中解析结果
	public abstract T parseResponse(Response response);

	public final void deliveryResponse(Response response) {
		T result = parseResponse(response);
		if (mRequestListener != null) {
			int stCode = response != null ? response.getStatusCode() : -1;
			String msg = response != null ? response.getMessage() : "unkown error";
			Log.e("ok", "### 执行回调 : stCode = " + stCode + ", result : " + result + ", err : " + msg);
			mRequestListener.onComplete(stCode, result, msg);
		}
	}


	public void addHeader(String name, String value) {
		mHeaders.put(name, value);
	}

	//返回原生的请求体
	public byte[] getBody(){

		Map<String,String> params = getParams();

		if(params != null && params.size() > 0){
			return encodeParameters(params, getParamsEncoding());
		}
		return null;
	}

	//Converts params into an application/x-www-form-urlencoded
	private byte[] encodeParameters(Map<String, String> params, String paramsEncoding) {
		StringBuilder encodedParams = new StringBuilder();
		try{
			for (Map.Entry<String, String> entry : params.entrySet()) {
				encodedParams.append(URLEncoder.encode(entry.getKey(), paramsEncoding));
				encodedParams.append('=');
				encodedParams.append(URLEncoder.encode(entry.getValue(),paramsEncoding));
				encodedParams.append('&');
			}
			return encodedParams.toString().getBytes(paramsEncoding);

		}catch (UnsupportedEncodingException uee){
			throw new RuntimeException("Encoding not supported: " + paramsEncoding, uee);
		}
	}

	@Override
	public int compareTo(Request<T> tRequest) {
		Priority myPriority = this.getPriority();
		Priority anotherPriority = tRequest.getPriority();
		// 如果优先级相等,那么按照添加到队列的序列号顺序来执行
		return myPriority.equals(anotherPriority) ? this.getSerialNumber()
				- tRequest.getSerialNumber()
				: myPriority.ordinal() - anotherPriority.ordinal();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((mHeaders == null) ? 0 : mHeaders.hashCode());
		result = prime * result + ((mHttpMethod == null) ? 0 : mHttpMethod.hashCode());
		result = prime * result + ((mBodyParams == null) ? 0 : mBodyParams.hashCode());
		result = prime * result + ((mPriority == null) ? 0 : mPriority.hashCode());
		result = prime * result + (mShouldCache ? 1231 : 1237);
		result = prime * result + ((mUrl == null) ? 0 : mUrl.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Request<?> other = (Request<?>) obj;
		if (mHeaders == null) {
			if (other.mHeaders != null)
				return false;
		} else if (!mHeaders.equals(other.mHeaders))
			return false;
		if (mHttpMethod != other.mHttpMethod)
			return false;
		if (mBodyParams == null) {
			if (other.mBodyParams != null)
				return false;
		} else if (!mBodyParams.equals(other.mBodyParams))
			return false;
		if (mPriority != other.mPriority)
			return false;
		if (mShouldCache != other.mShouldCache)
			return false;
		if (mUrl == null) {
			if (other.mUrl != null)
				return false;
		} else if (!mUrl.equals(other.mUrl))
			return false;
		return true;
	}

	public String getUrl() {
		return mUrl;
	}

	public RequestListener<T> getRequestListener() {
		return mRequestListener;
	}

	public int getSerialNumber() {
		return mSerialNum;
	}

	public void setSerialNumber(int mSerialNum) {
		this.mSerialNum = mSerialNum;
	}

	public Priority getPriority() {
		return mPriority;
	}

	public void setPriority(Priority mPriority) {
		this.mPriority = mPriority;
	}

	protected String getParamsEncoding() {
		return DEFAULT_PARAMS_ENCODING;
	}

	public String getBodyContentType() {
		return "application/x-www-form-urlencoded; charset=" + getParamsEncoding();
	}

	public HttpMethod getHttpMethod() {
		return mHttpMethod;
	}

	public Map<String, String> getHeaders() {
		return mHeaders;
	}

	public Map<String, String> getParams() {
		return mBodyParams;
	}

	public boolean isHttps() {
		return mUrl.startsWith("https");
	}

	public void setShouldCache(boolean shouldCache) {
		this.mShouldCache = shouldCache;
	}

	public boolean shouldCache() {
		return mShouldCache;
	}

	public void cancel() {
		isCancel = true;
	}

	public boolean isCanceled() {
		return isCancel;
	}

	public static enum Priority{
		LOW,
		NORMAL,
		HIGH,
		IMMEDIATE
	}

	public static enum HttpMethod{
		GET("GET"),
		POST("POST"),
		PUT("PUT"),
		DELETE("DELETE");

		private String mHttpMethod = "";

		private HttpMethod(String method){
			mHttpMethod = method;
		}

		@Override
		public String toString() {
			return mHttpMethod;
		}
	}


}
