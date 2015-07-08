package com.jugarte.gourmet.builders;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jugarte.gourmet.beans.Gourmet;
import com.jugarte.gourmet.beans.Operation;

public class GourmetBuilder extends BaseBuilder {

	private String _dataJSON = "";

    private Gourmet returnError() {
        return this.returnError("1", "Error");
    }

    private Gourmet returnError(String errorCode, String errorMessage) {
        Gourmet gourmet = new Gourmet();
        gourmet.errorCode = errorCode;
        gourmet.errorMessage = errorMessage;
        return gourmet;
    }

	public GourmetBuilder(String deviceType) {
		super(deviceType);
	}

	@Override
	public Gourmet build() throws Exception {
		Gourmet gourmet = new Gourmet();
		JSONObject data = null;
		JSONArray operations = null;
		try {
			data = new JSONObject(this._dataJSON);
		} catch (JSONException e) {
            return returnError();
		}

        if (data != null && !data.getString("errorCode").equalsIgnoreCase("0")) {
            return returnError(data.getString("errorCode"), data.getString("errorMessage"));
        }

        try {
            operations = new JSONArray(data.getString("operations"));
        } catch (JSONException e) {
        }

        if (operations != null) {
            Operation operation = null;
            for (int i = 0; i < operations.length(); i++) {
                operation = new Operation();
                JSONObject _item = (JSONObject) operations.get(i);

                operation.name = _item.getString("name");
                operation.date = _item.getString("date");
                operation.hour = _item.getString("hour");
                operation.price = _item.getString("price");

                gourmet.operations.add(operation);
            }
        }

        gourmet.currentBalance = data.getString("currentBalance");
        gourmet.errorCode = data.getString("errorCode");
        gourmet.errorMessage= data.getString("errorMessage");

		return gourmet;
	}

	@Override
	public void append(String type, Object data) {
		if(type.equals(GourmetBuilder.DATA_JSON)) {
			this._dataJSON = (String) data;
		}
	}
}