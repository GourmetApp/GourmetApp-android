package com.jugarte.gourmet.datamanagers;

import com.jugarte.gourmet.beans.Gourmet;
import com.jugarte.gourmet.builders.GourmetBuilder;
import com.jugarte.gourmet.utils.LogUtils;
import com.jugarte.gourmet.internal.Constants;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by javiergon on 15/05/15.
 */
public class DataManager {

    public Gourmet login(HashMap<String, Object> params) {
        String response = this.launchPostUrl(Constants.getUrlLoginService(), params);

        GourmetBuilder gourmetBuilder = new GourmetBuilder(null);
        gourmetBuilder.append(GourmetBuilder.DATA_JSON, response);
        try {
            return gourmetBuilder.build();
        } catch (Exception e) {
            return null;
        }
    }
    public Gourmet login(String user, String pass) {
        if (user != null && pass != null) {
            HashMap<String, Object> params = new HashMap<String, Object>();
            params.put(Constants.SERVICE_PARAM_USER, user);
            params.put(Constants.SERVICE_PARAM_PASS, pass);
            return this.login(params);
        }
        return null;
    }

    public String launchPostUrl(String url, HashMap<String, Object> bodyParams) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url);

        try {
            LogUtils.LOGD("GOURMET", "------------------------------------------------");
            LogUtils.LOGD("GOURMET", "URL " + url);
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            Iterator it = bodyParams.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry e = (Map.Entry) it.next();
                nameValuePairs.add(new BasicNameValuePair(e.getKey().toString(), e.getValue().toString()));

                LogUtils.LOGD("GOURMET", "Post Data : " + "{k:" + e.getKey().toString() +
                        ", \t\tv:" + e.getValue().toString() +
                        ", \t\tenc:" + URLEncoder.encode(e.getValue().toString(), "UTF-8") + "}");
            }
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            HttpResponse response = httpclient.execute(httppost);
            return EntityUtils.toString(response.getEntity());

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
