package com.jugarte.gourmet.domine.gourmet;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jugarte.gourmet.domine.beans.Gourmet;
import com.jugarte.gourmet.exceptions.NotFoundException;

class GetGourmetFirebase {

    interface OnFirebaseResponse {
        void success(Gourmet gourmet);
        void error(Exception exception);
    }

    void execute(String user, final OnFirebaseResponse response) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("users/" + user);
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Gourmet gourmet = dataSnapshot.getValue(Gourmet.class);
                response.success(gourmet);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                response.error(new NotFoundException());
            }

        };

        reference.addListenerForSingleValueEvent(postListener);
    }
}
