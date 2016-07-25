package com.demo.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by hc on 2016/7/25.
 */
public class FlickrFetchr {

    public byte[] getUrlBytes(String urlS) throws IOException{
        URL url = new URL(urlS);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        try{
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = httpURLConnection.getInputStream();
            if(httpURLConnection.getResponseCode() != HttpURLConnection.HTTP_OK){
                throw new IOException(httpURLConnection.getResponseMessage() + ":with" + urlS);
            }
            int bytes = 0;
            byte[] buffer = new byte[1024];
            while((bytes = in.read(buffer)) > 0){
                out.write(buffer,0,bytes);
            }
            out.close();
            return out.toByteArray();
        }finally {
            httpURLConnection.disconnect();
        }
    }

    public String getUrlString(String url) throws IOException{
        return new String(getUrlBytes(url));
    }
}
