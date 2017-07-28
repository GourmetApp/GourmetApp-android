package com.jugarte.gourmet.ui.balance.model;

import com.brandongogetap.stickyheaders.exposed.StickyHeader;

public class MonthVM implements StickyHeader {

    private String month;

    public MonthVM(String month) {
        this.month = month;
    }

    public String getMonth() {
        return month;
    }
}
