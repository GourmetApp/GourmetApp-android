package com.jugarte.gourmet.data.chequegourmet;

import androidx.annotation.VisibleForTesting;

import com.jugarte.gourmet.domine.beans.Operation;
import com.jugarte.gourmet.exceptions.ConnectionException;
import com.jugarte.gourmet.exceptions.EmptyException;
import com.jugarte.gourmet.exceptions.NotFoundException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ChequeGourmetBuilder {

    public String buildCards(String response) throws JSONException, EmptyException, ConnectionException {
        if (response == null) {
            throw new ConnectionException();
        }

        if (response.isEmpty()) {
            throw new EmptyException();
        }

        JSONArray jsonArray = new JSONArray(response);

        if (jsonArray.length() > 0) {
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            return jsonObject.optString("info", null);
        }
        return null;
    }

    public String buildBalance(String response) throws JSONException, EmptyException, ConnectionException {
        if (response == null) {
            throw new ConnectionException();
        }

        if (response.isEmpty()) {
            throw new EmptyException();
        }

        JSONObject jsonObject = new JSONObject(response);

        String balance = jsonObject.optString("balance");
        if (balance != null) {
            balance = balance.replace(".", ",");
        }

        return balance;
    }

    public ChequeGourmet build(String response, String balance, String cardNumber)
            throws NotFoundException, ConnectionException, EmptyException, JSONException {

        if (response == null) {
            throw new ConnectionException();
        }

        if (response.isEmpty()) {
            throw new EmptyException();
        }

        JSONObject json;
        try {
            json = new JSONObject(response);
        } catch (JSONException e) {
            throw new NotFoundException();
        }

        ArrayList<Operation> operationArrayList = new ArrayList<>();

        JSONArray array = json.getJSONArray("payments").optJSONArray(0);
        if (array == null) { throw new EmptyException(); }

        for (int i = 0; i < array.length(); i++) {
            JSONObject payment = array.getJSONObject(i);

            Operation operation = new Operation();
            String operationName = payment.getJSONObject("restaurant").getString("name");
            operationName = cleanString(operationName);
            operation.setName(operationName);
            operation.setPrice(payment.getString("dsAmount"));
            String[] dateCreated = payment.getString("dateCreated").split(" ");
            operation.setDate(dateCreated[0]);
            if (dateCreated.length > 1) {
                operation.setHour(dateCreated[1]);
            } else {
                operation.setHour("00:00");
            }
            operationArrayList.add(operation);
        }

        return new ChequeGourmet(cardNumber, balance, operationArrayList);
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    public String cleanString(String text) {
        text = text.replace("\n", "");
        text = text.replace("\t", "");
        text = text.replace("\r", "");
        text = text.replace("¥", "Ñ");
        text = text.replace("#", "Ñ");
        text = text.replace("ï", "'");
        text = text.replace("WWW.JUST-EAT.ES", "JUST-EAT");
        text = text.replace("ALCASAL", "Wetaca");
        text = text.replace("Saldo: ", "");
        text = text.trim();
        return text;
    }

}
