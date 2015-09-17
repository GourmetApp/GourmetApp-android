package com.jugarte.gourmet.builders;

import android.content.Context;

import com.jugarte.gourmet.beans.Gourmet;
import com.jugarte.gourmet.beans.Operation;
import com.jugarte.gourmet.helpers.CredentialsLogin;
import com.jugarte.gourmet.helpers.GourmetSqliteHelper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class GourmetInternalBuilder extends BaseBuilder {

	private String _data = "";
	private final Context context;

	public GourmetInternalBuilder(Context context) {
		this.context = context;
	}

    private Gourmet returnError(String errorCode) {
        Gourmet gourmet = new Gourmet();
        gourmet.errorCode = errorCode;
		gourmet.operations = null;
        return gourmet;
    }

	private Gourmet getGourmetCacheData() {
		GourmetSqliteHelper sqliteHelper = new GourmetSqliteHelper(this.context);
		if (sqliteHelper.getCurrentBalance() == null || sqliteHelper.getCurrentBalance().length() == 0) {
			return returnError("3");
		}

		Gourmet gourmet = new Gourmet();
		gourmet.cardNumber = CredentialsLogin.getUserCredential();
		gourmet.currentBalance = sqliteHelper.getCurrentBalance();
		gourmet.modificationDate = sqliteHelper.getModificationDate();
		gourmet.operations = sqliteHelper.getOperations();
		gourmet.errorCode = "0";

		return gourmet;
	}

	public String removeLastWord(String text) {
		String regex = "((\\s\\w)\\b)+$";
		text = text.replaceAll(regex, "");
		return cleanString(text);
	}

	public String cleanString(String text) {
		text = text.replace("\n" , "");
		text = text.replace("\t" , "");
		text = text.replace("\r" , "");
		text = text.replace("Saldo: " , "");
		text = text.trim();
		return text;
	}

	@Override
	public Gourmet build() throws Exception {
		if (this._data == null || this._data.length() == 0) {
			return getGourmetCacheData();
		}
		Gourmet gourmet = new Gourmet();
		gourmet.errorCode = "0";

		Document doc = Jsoup.parse(this._data);

		if (doc.getElementById("dato1") != null) {
			return this.returnError("2");
		}

		Element currentBalanceElement = doc.getElementById("TotalSaldo");
		if (currentBalanceElement == null) {
			return getGourmetCacheData();
		}
		gourmet.currentBalance = this.cleanString(currentBalanceElement.text());

		Elements operationsElement = doc.getElementsByTag("tr");
		for (Element operationElement : operationsElement) {
			Operation operation = new Operation();
			operation.name = this.removeLastWord(operationElement.getElementById("operacion").text());
			operation.price = operationElement.getElementById("importe").text();
			operation.date = operationElement.getElementById("fecha").text();
			operation.hour = operationElement.getElementById("horaOperacion").text();
			gourmet.operations.add(operation);
		}

		if (gourmet.operations.size() > 0) {
			Operation a = gourmet.operations.get(gourmet.operations.size() - 1);
			if (a.price.equalsIgnoreCase("fin")) {
				gourmet.operations.remove(gourmet.operations.size() - 1);
			}
		} else {
			gourmet.operations = null;
		}

		gourmet.cardNumber = CredentialsLogin.getUserCredential();

		GourmetSqliteHelper sqliteHelper = new GourmetSqliteHelper(this.context);
		sqliteHelper.updateElementsWithDatas(gourmet);
		gourmet.operations = sqliteHelper.getOperations();

		return gourmet;
	}

	@Override
	public void append(String type, Object data) {
		if(type.equals(BaseBuilder.DATA_JSON)) {
			this._data = (String) data;
		}
	}
}