package com.jugarte.gourmet.ui.search;

import androidx.annotation.NonNull;

import com.github.wrdlbrnft.sortedlistadapter.SortedListAdapter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

class SearchViewModel implements SortedListAdapter.ViewModel {

    private String id;
    private String name;
    private String date;
    private String price;

    SearchViewModel(String id, String name, String date, String price) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return parseTitle(name);
    }

    public String getDate() {
        return parseDate(getDateObject());
    }

    public String getPrice() {
        return parsePrice(price, isPositive(name));
    }

    public boolean isPositive() {
        return isPositive(name);
    }

    Date getDateObject() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

        try {
            return formatter.parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    @Override
    public <T> boolean isSameModelAs(@NonNull T item) {
        if (item instanceof SearchViewModel) {
            final SearchViewModel other = (SearchViewModel) item;
            return other.getId().equals(this.id);
        }
        return false;
    }

    @Override
    public <T> boolean isContentTheSameAs(@NonNull T item) {
        if (item instanceof SearchViewModel) {
            final SearchViewModel other = (SearchViewModel) item;
            return name != null ? name.equals(other.name) : other.name == null;
        }
        return false;
    }

    private String parseTitle(String title) {
        if (isPositive(title))
            return title;
        return firstLetterInUpper(title);
    }

    private String parsePrice(String price, boolean positive) {
        return ((positive) ? "+" : "-") + price + "€";
    }

    private String parseDate(Date dateObject) {
        DateFormat df = new SimpleDateFormat("E dd MMMM yyyy HH:mm", Locale.getDefault());
        return firstLetterInUpper(removeDots(df.format(dateObject)));
    }

    private boolean isPositive(String name) {
        return name.equalsIgnoreCase("Actualización de saldo") ||
                name.equalsIgnoreCase("Recarga Saldo");
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

    private String removeDots(String text) {
        return text.replace(".", "");
    }

}
