package com.jugarte.gourmet.ui.balance;

import com.jugarte.gourmet.domine.beans.Gourmet;
import com.jugarte.gourmet.domine.beans.Operation;
import com.jugarte.gourmet.ui.balance.model.BalanceVM;
import com.jugarte.gourmet.ui.balance.model.OperationVM;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BalanceMapper {

    private String lastMonth = "";

    public BalanceVM map(Gourmet gourmet) {
        BalanceVM.Builder balanceBuilder = new BalanceVM.Builder()
                .setCardNumber(gourmet.getCardNumber())
                .setCurrent(parseCurrent(gourmet.getCurrentBalance()));

        List<Object> operations = new ArrayList<>();
        for (Operation operation : gourmet.getOperations()) {
            boolean positive = isPositive(operation.getName());
            OperationVM operationVM = new OperationVM.Builder()
                    .setTitle(parseTitle(operation.getName()))
                    .setDate(parseDate(operation.getDateObject()))
                    .setMonth(parseMonth(operation.getDateObject()))
                    .setPrice(parsePrice(operation.getPrice(), positive))
                    .setState(positive ? OperationVM.State.POSITIVE : OperationVM.State.NEGATIVE)
                    .build();

            if (!lastMonth.equalsIgnoreCase(operationVM.getMonth())) {
                lastMonth = operationVM.getMonth();
                operations.add(operationVM.getMonth());
            }
            operations.add(operationVM);
        }

        balanceBuilder.setOperations(operations);
        return balanceBuilder.build();
    }

    private String parseCurrent(String currentBalance) {
        return currentBalance + "€";
    }

    private String parseTitle(String title) {
        if (isPositive(title))
            return title;
        return firstLetterInUpper(title);
    }

    private String parseDate(Date dateObject) {
        DateFormat df = new SimpleDateFormat("E dd MMM HH:mm", Locale.getDefault());
        return firstLetterInUpper(df.format(dateObject));
    }

    private String parseMonth(Date dateObject) {
        DateFormat df = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
        return firstLetterInUpper(df.format(dateObject));
    }

    private String parsePrice(String price, boolean positive) {
        return ((positive) ? "+" : "-") + price + "€";
    }

    private boolean isPositive(String name) {
        return name.equalsIgnoreCase("Actualización de saldo");
    }

    private String firstLetterInUpper(String text) {
        text = text.toLowerCase();
        StringBuilder res = new StringBuilder();

        String[] strArr = text.split(" ");
        for (String str : strArr) {
            char[] stringArray = str.trim().toCharArray();
            stringArray[0] = Character.toUpperCase(stringArray[0]);
            str = new String(stringArray);

            res.append(str).append(" ");
        }

        return res.toString().trim();
    }
}
