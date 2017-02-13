package com.appsolutions.moviebot;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.LruCache;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageLoader;

/**
 * Created by eric on 2/13/17.
 */
public class VolleyImageRequest {

    private static VolleyImageRequest mInstance;
    private static Context mContext;
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;

    private VolleyImageRequest (Context context){
        mContext = context;
        requestQueue = getRequestQueue();

        imageLoader = new ImageLoader(requestQueue, new ImageLoader.ImageCache() {

            private final LruCache<String, Bitmap> cache = new LruCache<>(20);

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

    public static synchronized VolleyImageRequest getinstance(Context context){
         if(mInstance == null){
             mInstance = new VolleyImageRequest(context);
         }
        return mInstance;
    }

    public RequestQueue getRequestQueue(){
        if(requestQueue == null){
            Cache cache = new DiskBasedCache(mContext.getCacheDir(), 10 * 1024 * 1024);
            Network network = new BasicNetwork(new HurlStack());
            requestQueue = new RequestQueue(cache, network);
            requestQueue.start();
        }
        return requestQueue;
    }

    public ImageLoader getImageLoader(){
        return imageLoader;
    }
}
