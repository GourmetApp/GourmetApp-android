package com.jugarte.gourmet.builders;

public abstract class BaseBuilder {

    public static final String DATA_JSON = "datajson";

    abstract public Object build() throws Exception;

    abstract public void append(String type, Object data);

}
