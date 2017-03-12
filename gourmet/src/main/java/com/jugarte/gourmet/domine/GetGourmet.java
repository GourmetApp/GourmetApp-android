package com.jugarte.gourmet.domine;

import com.jugarte.gourmet.beans.Gourmet;

public class GetGourmet {

    public interface OnGourmetResponse {

        void success(Gourmet gourmet);

        void error(Exception exception);
    }

    public void execute(String user, String password, OnGourmetResponse response) {


    }

}
