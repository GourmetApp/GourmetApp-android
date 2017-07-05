package com.jugarte.gourmet.ui.search;

import android.support.annotation.NonNull;

import com.github.wrdlbrnft.sortedlistadapter.SortedListAdapter;

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
        return name;
    }

    String getDate() {
        return date;
    }

    String getPrice() {
        return price;
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
}
