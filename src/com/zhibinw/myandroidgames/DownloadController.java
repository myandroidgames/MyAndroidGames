package com.zhibinw.myandroidgames;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.Volley;

import android.content.Context;

public class DownloadController {
    private static DownloadController mInstance;
    private RequestQueue mRequestQueue;
    private static Context mContext;

    public DownloadController(Context context) {
        mContext = context;
    }

    public static synchronized DownloadController getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DownloadController(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
//            Cache cache = new DiskBasedCache(mContext.getCacheDir(), 5*1024 * 1024);
//            Network network = new BasicNetwork(new HurlStack());
//            mRequestQueue = new RequestQueue(cache, network);
//            mRequestQueue.start();
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

}
