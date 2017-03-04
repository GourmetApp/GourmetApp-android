package com.jugarte.gourmet.helpers;

import android.content.Context;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;

import java.io.File;

public class VolleySingleton {

    private static VolleySingleton sInstance;

    private static final int CACHE_SIZE = 5 * 1024 * 1024;
    private static final String CACHE_LOCATION = "/cache";

    private RequestQueue mRequestQueue;

    private void initializeRequestQueue(Context context) {
        String cachePath = context.getCacheDir().getAbsolutePath() + CACHE_LOCATION;
        File cacheDir = new File(cachePath);

        Cache cache = new DiskBasedCache(cacheDir, CACHE_SIZE);
        Network network = new BasicNetwork(new HurlStack());
        mRequestQueue = new RequestQueue(cache, network);
        mRequestQueue.start();
    }

    /**
     * <p>Return the image loader</p>
     *
     * @return image loader
     */
    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }

    /**
     * Initialize Volley Helper
     */
    public void initializeVolley(Context context) {
        if (context == null) {
            return;
        }

        sInstance.initializeRequestQueue(context);
    }

    /**
     * <p>Return a singleton instance to access Volley helper features</p>
     *
     * @return singleton helper
     */
    public static synchronized VolleySingleton getVolleyLoader() {
        if (sInstance == null) {
            sInstance = new VolleySingleton();
        }

        return sInstance;
    }
}
