package com.jugarte.gourmet.ui.base;

public class BasePresenter<V> implements Presenter<V> {

    private V screen;

    @Override
    public void onAttach(V screen) {
        this.screen = screen;
    }

    @Override
    public void onDetach() {
        screen = null;
    }

    public V getScreen() {
        return screen;
    }
}
