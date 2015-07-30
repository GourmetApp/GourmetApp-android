package com.jugarte.gourmet.builders;

import com.jugarte.gourmet.beans.Gourmet;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class GourmetInternalBuilder extends BaseBuilder {

	private String _data = "";

    private Gourmet returnError() {
        return this.returnError("1", "Error");
    }

    private Gourmet returnError(String errorCode, String errorMessage) {
        Gourmet gourmet = new Gourmet();
        gourmet.errorCode = errorCode;
        gourmet.errorMessage = errorMessage;
        return gourmet;
    }

	public GourmetInternalBuilder(String deviceType) {
		super(deviceType);
	}

	@Override
	public Gourmet build() throws Exception {
		Gourmet gourmet = new Gourmet();
		
		return gourmet;
	}

	@Override
	public void append(String type, Object data) {
		if(type.equals(GourmetInternalBuilder.DATA_JSON)) {
			this._data = (String) data;
		}
	}
}