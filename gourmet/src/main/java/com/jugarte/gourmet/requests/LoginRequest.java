package com.jugarte.gourmet.requests;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jugarte.gourmet.beans.Gourmet;
import com.jugarte.gourmet.beans.Operation;
import com.jugarte.gourmet.builders.GourmetInternalBuilder;
import com.jugarte.gourmet.helpers.CredentialsLogin;
import com.jugarte.gourmet.helpers.DateHelper;
import com.jugarte.gourmet.helpers.VolleySingleton;
import com.jugarte.gourmet.internal.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;


public class LoginRequest extends ServiceRequest<Gourmet> {

    @Override
    public void launchConnection() {
        
        if (Constants.FAKE_SERVICES) {
            mResponseListener.onResponse(new RequestFake().login(null, null));
        }

        String url = Constants.getUrlLoginService();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                final GourmetInternalBuilder gourmetBuilder = new GourmetInternalBuilder(mContext);
                gourmetBuilder.append(GourmetInternalBuilder.DATA_JSON, response);
                gourmetBuilder.append(GourmetInternalBuilder.DATA_CARD_NUMBER, CredentialsLogin.getUserCredential(mContext));
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

                if (gourmet.getOperations() == null) {
                    gourmet = gourmetBuilder.getGourmetCacheData();
                }

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference reference = database.getReference("users/" + gourmet.getCardNumber());

                final Gourmet finalGourmet = gourmet;

                final Listener<Gourmet> finalResponse = mResponseListener;
                ValueEventListener postListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Gourmet firebaseGourmet = dataSnapshot.getValue(Gourmet.class);
                        Gourmet resultGourmet = mergeGourmetDataWithFirebase(finalGourmet, firebaseGourmet);

                        reference.setValue(resultGourmet);

                        if (finalResponse != null) {
                            finalResponse.onResponse(resultGourmet);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }

                };
                reference.addListenerForSingleValueEvent(postListener);

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference reference = database.getReference(CredentialsLogin.getUserCredential(mContext));

                final Listener<Gourmet> finalResponse = mResponseListener;
                ValueEventListener postListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Gourmet firebaseGourmet = dataSnapshot.getValue(Gourmet.class);
                        firebaseGourmet.setOfflineMode(true);

                        if (finalResponse != null) {
                            finalResponse.onResponse(firebaseGourmet);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }

                };
                reference.addListenerForSingleValueEvent(postListener);
            }

        }) {
            @Override
            protected Map<String,String> getParams() {
                return mQueryParams;
            }
        };

        VolleySingleton.getVolleyLoader().getRequestQueue().add(stringRequest);

    }

    private Gourmet mergeGourmetDataWithFirebase(Gourmet localGourmet, Gourmet firebaseGourmet) {
        if (firebaseGourmet == null || firebaseGourmet.getOperations() == null) {
            return localGourmet;
        }

        Collections.reverse(localGourmet.getOperations());

        for (Operation operation : localGourmet.getOperations()) {
            if (!containsId(firebaseGourmet.getOperations(), operation.getId())) {
                firebaseGourmet.getOperations().add(0, operation);
            }
        }
        localGourmet.setOperations(firebaseGourmet.getOperations());
        return localGourmet;
    }

    private static boolean containsId(List<Operation> operations, String id) {
        for (Operation operation : operations) {
            if (operation.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

}
