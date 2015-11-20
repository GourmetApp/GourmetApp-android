package com.jugarte.gourmet.datamanagers;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.jugarte.gourmet.beans.Gourmet;
import com.jugarte.gourmet.builders.GourmetInternalBuilder;
import com.jugarte.gourmet.helpers.CredentialsLogin;
import com.jugarte.gourmet.helpers.DateHelper;
import com.jugarte.gourmet.helpers.VolleySingleton;
import com.jugarte.gourmet.internal.Constants;

import java.util.Map;

public class LoginRequest extends ServiceRequest<Gourmet> {

    @Override
    public void launchConnection() {
        
        if (Constants.FAKE_SERVICES) {
            mResponseListener.onResponse(new DataManagerFake().login(null, null));
        }

        String url = Constants.getUrlLoginService();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                GourmetInternalBuilder gourmetBuilder = new GourmetInternalBuilder(mContext);
                gourmetBuilder.append(GourmetInternalBuilder.DATA_JSON, response);
                gourmetBuilder.append(GourmetInternalBuilder.DATA_CARD_NUMBER, CredentialsLogin.getUserCredential());
                gourmetBuilder.append(GourmetInternalBuilder.DATA_MODIFICATION_DATE, DateHelper.getCurrentDateTime());

                Gourmet gourmet = null;
                try {
                    gourmet = gourmetBuilder.build();
                } catch (Exception e) {
                    e.printStackTrace();
                    if (mErrorListener != null) {
                        mErrorListener.onErrorResponse(new VolleyError("The object could not be built"));
                    }
                }

                if (gourmet == null) {
                    gourmet = gourmetBuilder.getGourmetCacheData();
                }

                if (gourmet.operations == null) {
                    gourmet = gourmetBuilder.getGourmetCacheData();
                }

                gourmet = gourmetBuilder.updateGourmetDataWithCache(gourmet);

                if (mResponseListener != null) {
                    mResponseListener.onResponse(gourmet);
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (mErrorListener != null) {
                    mErrorListener.onErrorResponse(error);
                }

            }

        })
        {
            @Override
            protected Map<String,String> getParams() {
                return mQueryParams;
            }
        };

        VolleySingleton.getVolleyLoader().getRequestQueue().add(stringRequest);

    }

}
