package com.jugarte.gourmet.builders;

/**
 * Created by javiergon on 03/08/15.
 */
public class LastVersionBuilder extends BaseBuilder {

    private String _data = "";

    public LastVersionBuilder(String deviceType) {
        super(deviceType);
    }

    @Override
    public Object build() throws Exception {
        return null;
    }

    @Override
    public void append(String type, Object data) {
        if(type.equals(BaseBuilder.DATA_JSON)) {
            this._data = (String) data;
        }
    }
}
