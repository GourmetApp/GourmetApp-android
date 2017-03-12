package com.jugarte.gourmet.data.chequegourmet;

import com.jugarte.gourmet.exceptions.ConnectionException;
import com.jugarte.gourmet.exceptions.EmptyException;
import com.jugarte.gourmet.exceptions.NotFoundException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ChequeGourmetDataManager {

    private static final String URL = "http://tarjetagourmet.chequegourmet.com/processLogin_iphoneApp.jsp";

    public ChequeGourmet getChequeGourmet(String username, String password)
            throws ConnectionException, EmptyException, NotFoundException {

        Map<String, String> postParams = new HashMap<>();
        postParams.put("usuario", username);
        postParams.put("contrasena", password);
        postParams.put("token", "xAeSYsTQQTCVyPOGWLpR");

        ChequeGourmetRequest chequeGourmetRequest = new ChequeGourmetRequest();
        String response;
        try {
            response = chequeGourmetRequest.getLogin(URL, postParams);
        } catch (IOException e) {
            throw new ConnectionException();
        }

        ChequeGourmetBuilder chequeGourmetBuilder = new ChequeGourmetBuilder();
        return chequeGourmetBuilder.build(response);
    }
}
