package com.jugarte.gourmet.builders;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jugarte.gourmet.beans.Gourmet;
import com.jugarte.gourmet.beans.Operation;
import com.jugarte.gourmet.helpers.CredentialsLogin;
import com.jugarte.gourmet.helpers.GourmetSqliteHelper;

public class GourmetBuilder extends BaseBuilder {

	private String _dataJSON = "";
    private Context context;

    public GourmetBuilder(Context context) {
        this.context = context;
    }

    private Gourmet returnError(String errorCode) {
        Gourmet gourmet = new Gourmet();
        gourmet.errorCode = errorCode;
        return gourmet;
    }

    public Gourmet getGourmetCacheData() {
        GourmetSqliteHelper sqliteHelper = new GourmetSqliteHelper(this.context);
        if (sqliteHelper.getCurrentBalance() == null || sqliteHelper.getCurrentBalance().length() == 0) {
            return returnError("2");
        }

        Gourmet gourmet = new Gourmet();
        gourmet.cardNumber = CredentialsLogin.getUserCredential();
        gourmet.currentBalance = sqliteHelper.getCurrentBalance();
        gourmet.modificationDate = sqliteHelper.getModificationDate();
        gourmet.operations = sqliteHelper.getOperations();
        gourmet.errorCode = "0";

        return gourmet;
    }

    public Gourmet updateGourmetDataWithCache(Gourmet gourmet) {
        GourmetSqliteHelper sqliteHelper = new GourmetSqliteHelper(this.context);
        sqliteHelper.updateElementsWithDatas(gourmet);
        gourmet.operations = sqliteHelper.getOperations();
        return gourmet;
    }

	@Override
	public Gourmet build() throws Exception {
		Gourmet gourmet = new Gourmet();
		JSONObject data;
		JSONArray operations = null;

        if (this._dataJSON == null) {
            return getGourmetCacheData();
        }

		try {
			data = new JSONObject(this._dataJSON);
		} catch (JSONException e) {
            return getGourmetCacheData();
		}

        if (data != null && !data.getString("errorCode").equalsIgnoreCase("0")) {
            return returnError(data.getString("errorCode"));
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

        gourmet.cardNumber = CredentialsLogin.getUserCredential();
        gourmet.currentBalance = data.getString("currentBalance");
        gourmet.errorCode = data.getString("errorCode");

		return gourmet;
	}

	@Override
	public void append(String type, Object data) {
		if(type.equals(BaseBuilder.DATA_JSON)) {
			this._dataJSON = (String) data;
		}
	}
}