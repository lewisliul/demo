package com.framework.simple_net.base;

import org.apache.http.HttpEntity;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Created by hc on 2016/11/28.
 */
public class Response extends BasicHttpResponse {

	public byte[] rawData = new byte[0];

	public Response(StatusLine statusline) {
		super(statusline);
	}

	public Response(ProtocolVersion ver, int code, String reason) {
		super(ver, code, reason);
	}

	public byte[] getRawData() {
		return rawData;
	}

	@Override
	public void setEntity(HttpEntity entity) {
		super.setEntity(entity);
		rawData = entityToBytes(getEntity());
	}

	public int getStatusCode() {
		return getStatusLine().getStatusCode();
	}

	public String getMessage() {
		return getStatusLine().getReasonPhrase();
	}

	private byte[] entityToBytes(HttpEntity entity) {
		try {
			return EntityUtils.toByteArray(entity);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new byte[0];
	}
}
