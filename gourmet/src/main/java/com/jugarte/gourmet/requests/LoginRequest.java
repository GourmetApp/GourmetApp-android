package com.jugarte.gourmet.requests;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jugarte.gourmet.beans.Gourmet;
import com.jugarte.gourmet.beans.Operation;
import com.jugarte.gourmet.builders.GourmetBuilder;
import com.jugarte.gourmet.helpers.CredentialsLogin;
import com.jugarte.gourmet.helpers.DateHelper;
import com.jugarte.gourmet.helpers.VolleySingleton;
import com.jugarte.gourmet.internal.Constants;

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
                final GourmetBuilder gourmetBuilder = new GourmetBuilder();
                gourmetBuilder.append(GourmetBuilder.DATA_JSON, response);
                gourmetBuilder.append(GourmetBuilder.DATA_CARD_NUMBER, CredentialsLogin.getUserCredential(mContext));
                gourmetBuilder.append(GourmetBuilder.DATA_MODIFICATION_DATE, DateHelper.getCurrentDateTime());

                Gourmet gourmet = null;
                try {
                    gourmet = gourmetBuilder.build();
                } catch (Exception e) {
                    e.printStackTrace();
                    if (mErrorListener != null) {
                        mErrorListener.onErrorResponse(new VolleyError("The object could not be built"));
                    }
                }

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference reference = database.getReference("users/" + gourmet.getCardNumber());

                final Gourmet finalGourmet = gourmet;

                final Listener<Gourmet> finalResponse = mResponseListener;
                ValueEventListener postListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Gourmet fireBaseGourmet = dataSnapshot.getValue(Gourmet.class);
                        Gourmet resultGourmet = mergeGourmetDataWithFirebase(finalGourmet, fireBaseGourmet);

                        resultGourmet.orderOperations();

                        reference.setValue(resultGourmet);

                        if (finalResponse != null) {
                            finalResponse.onResponse(resultGourmet);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        finalResponse.onResponse(null);
                    }

                };
                reference.addListenerForSingleValueEvent(postListener);

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference reference = database.getReference("users/" + CredentialsLogin.getUserCredential(mContext));

                final Listener<Gourmet> finalResponse = mResponseListener;
                ValueEventListener postListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Gourmet fireBaseGourmet = dataSnapshot.getValue(Gourmet.class);
                        if (fireBaseGourmet != null) {
                            fireBaseGourmet.setOfflineMode(true);
                        }

                        if (finalResponse != null) {
                            finalResponse.onResponse(fireBaseGourmet);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        finalResponse.onResponse(null);
                    }

                };
                reference.addListenerForSingleValueEvent(postListener);
            }

        }) {
            @Override
            protected Map<String, String> getParams() {
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
