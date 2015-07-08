package com.jugarte.gourmet.builders;

public abstract class BaseBuilder {

	public static final String DATA_JSON = "datajson";

	private String _deviceType = null;
		
	public BaseBuilder(String deviceType) {
		this._deviceType = deviceType;
	}
	
	abstract public Object build() throws Exception;	
	abstract public void append(String type, Object data);

	public String getDeviceType() {
		return _deviceType;
	}

}
