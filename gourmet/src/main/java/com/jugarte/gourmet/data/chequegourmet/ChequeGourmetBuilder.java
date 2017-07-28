package com.jugarte.gourmet.data.chequegourmet;

import android.support.annotation.VisibleForTesting;

import com.jugarte.gourmet.domine.beans.Operation;
import com.jugarte.gourmet.exceptions.ConnectionException;
import com.jugarte.gourmet.exceptions.EmptyException;
import com.jugarte.gourmet.exceptions.NotFoundException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class ChequeGourmetBuilder {

    public ChequeGourmet build(String response, String cardNumber)
            throws NotFoundException, ConnectionException, EmptyException {

        if (response == null) {
            throw new ConnectionException();
        }

        if (response.isEmpty()) {
            throw new EmptyException();
        }

        Document doc = Jsoup.parse(response);

        if (doc.getElementById("dato1") != null) {
            throw new NotFoundException();
        }

        Element currentBalanceElement = doc.getElementById("TotalSaldo");
        if (currentBalanceElement == null) {
            throw new EmptyException();
        }
        String balance = cleanString(currentBalanceElement.text());

        ArrayList<Operation> operationArrayList = null;
        Elements operationsElement = doc.getElementsByTag("tr");

        if (operationsElement != null && !operationsElement.isEmpty()) {
            operationArrayList = new ArrayList<>();

            for (Element operationElement : operationsElement) {
                Operation operation = new Operation();
                operation.setName(removeLastWord(operationElement.getElementById("operacion").text()));
                operation.setPrice(operationElement.getElementById("importe").text());
                operation.setDate(operationElement.getElementById("fecha").text());
                operation.setHour(operationElement.getElementById("horaOperacion").text());
                operationArrayList.add(operation);
            }
        }

        if (operationArrayList != null && operationArrayList.size() > 0) {
            Operation lastOperation = operationArrayList.get(operationArrayList.size() - 1);

            if (lastOperation.getPrice().equalsIgnoreCase("fin")) {
                operationArrayList.remove(lastOperation);
            }

        }

        return new ChequeGourmet(cardNumber, balance, operationArrayList);
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    public String removeLastWord(String text) {
        String regex = "((\\s\\w)\\b)?+$";
        text = text.replaceAll(regex, "");
        return cleanString(text);
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
