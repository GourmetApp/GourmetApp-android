package com.jugarte.gourmet.domine.gourmet;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jugarte.gourmet.domine.beans.Gourmet;
import com.jugarte.gourmet.exceptions.NotFoundException;

import java.util.List;

public class GetGourmetFirebase {

    public interface OnFirebaseResponse {
        void success(Gourmet gourmet);
        void error(Exception exception);
    }

    public void execute(final String user, final OnFirebaseResponse response) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference reference = database.getReference().child("users");
        final ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Gourmet gourmet = child.getValue(Gourmet.class);
                    if (gourmet.getCardNumber().endsWith(user)) {
                        response.success(gourmet);
                        return;
                    }
                }
                response.error(new NotFoundException());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                response.error(new NotFoundException());
            }

        };

        reference.addListenerForSingleValueEvent(postListener);
    }
}
