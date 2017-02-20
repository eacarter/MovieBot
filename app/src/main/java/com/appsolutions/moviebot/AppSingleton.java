package com.appsolutions.moviebot;

import android.content.Context;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;


public class AppSingleton {

    private static Context mContext;
    private RequestQueue mRequestQueue;
    private static AppSingleton ourInstance;

    private AppSingleton(View view) {
        mContext = view.getContext();
        mRequestQueue = getRequestQueue();
    }

    public static synchronized AppSingleton getInstance(View view) {
        if(ourInstance == null){
            ourInstance = new AppSingleton(view);
        }
        return ourInstance;
    }

    public RequestQueue getRequestQueue(){
        if(mRequestQueue == null){
            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> request){
        getRequestQueue().add(request);
    }

    public void cancelPendingRequest(Object tag){
        if(mRequestQueue != null){
            mRequestQueue.cancelAll(tag);
        }
    }


}
