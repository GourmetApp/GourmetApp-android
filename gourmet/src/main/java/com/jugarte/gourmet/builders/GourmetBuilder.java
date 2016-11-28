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

	private String dataJSON = "";
    private Context context;

    public GourmetBuilder(Context context) {
        this.context = context;
    }

    private Gourmet returnError(String errorCode) {
        Gourmet gourmet = new Gourmet();
        gourmet.setErrorCode(errorCode);
        return gourmet;
    }

    public Gourmet getGourmetCacheData() {
        GourmetSqliteHelper sqliteHelper = new GourmetSqliteHelper(this.context);
        if (sqliteHelper.getCurrentBalance() == null || sqliteHelper.getCurrentBalance().length() == 0) {
            return returnError("2");
        }

        Gourmet gourmet = new Gourmet();
        gourmet.setCardNumber(CredentialsLogin.getUserCredential(this.context));
        gourmet.setCurrentBalance(sqliteHelper.getCurrentBalance());
        gourmet.setModificationDate(sqliteHelper.getModificationDate());
        gourmet.setOperations(sqliteHelper.getOperations());
        gourmet.setErrorCode("0");

        return gourmet;
    }

    public Gourmet updateGourmetDataWithCache(Gourmet gourmet) {
        GourmetSqliteHelper sqliteHelper = new GourmetSqliteHelper(this.context);
        sqliteHelper.updateElementsWithDatas(gourmet);
        gourmet.setOperations(sqliteHelper.getOperations());
        return gourmet;
    }

	@Override
	public Gourmet build() throws Exception {
		Gourmet gourmet = new Gourmet();
		JSONObject data;
		JSONArray operations = null;

        if (this.dataJSON == null) {
            return getGourmetCacheData();
        }

		try {
			data = new JSONObject(this.dataJSON);
		} catch (JSONException e) {
            return getGourmetCacheData();
		}

        if (!data.getString("errorCode").equalsIgnoreCase("0")) {
            return returnError(data.getString("errorCode"));
        }

        try {
            operations = new JSONArray(data.getString("operations"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (operations != null) {
            Operation operation = null;
            for (int i = 0; i < operations.length(); i++) {
                operation = new Operation();
                JSONObject jsonItem = (JSONObject) operations.get(i);

                operation.setName(jsonItem.getString("name"));
                operation.setDate(jsonItem.getString("date"));
                operation.setHour(jsonItem.getString("hour"));
                operation.setPrice(jsonItem.getString("price"));

                gourmet.addOperation(operation);
            }
        }

        gourmet.setCardNumber(CredentialsLogin.getUserCredential(context));
        gourmet.setCurrentBalance(data.getString("currentBalance"));
        gourmet.setErrorCode(data.getString("errorCode"));

		return gourmet;
	}

	@Override
	public void append(String type, Object data) {
		if(type.equals(BaseBuilder.DATA_JSON)) {
			this.dataJSON = (String) data;
		}
	}
}