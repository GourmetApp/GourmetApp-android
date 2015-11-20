package com.jugarte.gourmet.requests;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.jugarte.gourmet.beans.LastVersion;
import com.jugarte.gourmet.builders.LastVersionBuilder;
import com.jugarte.gourmet.helpers.VolleySingleton;
import com.jugarte.gourmet.internal.Constants;

public class GitHubRequest  extends ServiceRequest<LastVersion> {

    @Override
    public void launchConnection() {
        
        if (Constants.FAKE_SERVICES) {
            mResponseListener.onResponse(new RequestFake().getLastPublishVersion());
        }

        String url = Constants.getUrlLastPublishVersion();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                LastVersionBuilder lastVersionBuilder = new LastVersionBuilder();
                lastVersionBuilder.append(LastVersionBuilder.DATA_JSON, response);
                LastVersion lastVersion = null;
                try {
                    lastVersion = (LastVersion) lastVersionBuilder.build();
                } catch (Exception e) {
                    e.printStackTrace();
                    if (mErrorListener != null) {
                        mErrorListener.onErrorResponse(new VolleyError("The object could not be built"));
                    }
                }

                if (mResponseListener != null) {
                    mResponseListener.onResponse(lastVersion);
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (mErrorListener != null) {
                    mErrorListener.onErrorResponse(error);
                }

            }

        });

        VolleySingleton.getVolleyLoader().getRequestQueue().add(stringRequest);

    }

}
