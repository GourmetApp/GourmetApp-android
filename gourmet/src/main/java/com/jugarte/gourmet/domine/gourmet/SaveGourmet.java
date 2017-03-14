package com.jugarte.gourmet.domine.gourmet;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jugarte.gourmet.beans.Gourmet;

public class SaveGourmet {

    public void execute(Gourmet gourmet) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("users/" + gourmet.getCardNumber());
        reference.setValue(gourmet);
    }
}
