package com.jugarte.gourmet.ui.base;

public interface Presenter<V> {

    void onAttach(V screen);

    void onDetach();

}
