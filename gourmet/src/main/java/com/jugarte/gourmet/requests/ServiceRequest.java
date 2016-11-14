package com.jugarte.gourmet.requests;

import android.content.Context;

import com.android.volley.Response;

import java.util.Map;

/**
 * ServiceRequest
 * Superclass service connection definition
 */
public class ServiceRequest<T> {

    protected Map<String, String> mQueryParams = null;
    protected Context mContext = null;

    protected Listener<T> mResponseListener = null;
    protected Response.ErrorListener mErrorListener = null;

    /**
     * <p>Response listener</p>
     * @param <Gourmet>
     */
    public interface Listener<Gourmet> {
        public void onResponse(Gourmet response);
    }

    /**********************
     * 					  *
     *	    INTERNAL	  *
     *					  *
     **********************/

    /**
     * <p>Set Query params that will be used to compose the http query</p>
     * @param params (could be null)
     */
    public void setQueryParams(Map<String, String> params) {
        this.mQueryParams = params;
    }

    /**
     * <p>Callback interface for delivering parsed responses</p>
     * @param listener
     */
    public void setResponseListener (Listener<T> listener) {
        this.mResponseListener = listener;
    }

    /**
     * <p>Callback method that an error has been occurred with the
     * provided error code and optional user-readable message.</p>
     * @param listener
     */
    public void setOnErrorListener (Response.ErrorListener listener) {
        this.mErrorListener = listener;
    }

    /**
     * <p>Set the context to execute</p>
     * @param context
     */
    public void setContext (Context context) {
        this.mContext = context;
    }

    /**
     * <p>Launch the request using Volley. The response will be received
     * through listener</p>
     */
    public void launchConnection () {

    }

    /**
     * <p>Stop the processing request</p>
     */
    public void stopConnection () {

    }

    /**********************
     * 					  *
     *	   LIFE CYCLE	  *
     *					  *
     **********************/
    public ServiceRequest() {

    }
}
