package com.sophomoreventure.collegeconnect.Network;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.sophomoreventure.collegeconnect.NoSSLv3SocketFactory;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

/**
 * Created by Vikas Kumar on 30-12-2015.
 */
public class VolleySingleton {
    private static VolleySingleton sInstance = null;
    private ImageLoader mImageLoader;
    private RequestQueue mRequestQueue;

    private VolleySingleton(Context context) {

        SSLContext sslcontext = null;
        try {
            sslcontext = SSLContext.getInstance("TLSv1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        try {
            sslcontext.init(null, null, null);
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        SSLSocketFactory NoSSLv3Factory = new NoSSLv3SocketFactory(sslcontext.getSocketFactory());

        mRequestQueue = Volley.newRequestQueue(context, new HurlStack(null, NoSSLv3Factory));
        mImageLoader = new ImageLoader(mRequestQueue, new ImageLoader.ImageCache() {

            private LruCache<String, Bitmap> cache = new LruCache<>((int) (Runtime.getRuntime().maxMemory() / 1024) / 8); // 4mb

            @Override
            public Bitmap getBitmap(String url) {
                return cache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                cache.put(url, bitmap);
            }
        });
    }

    public static VolleySingleton getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new VolleySingleton(context);
        }
        return sInstance;
    }

    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }
}

