package com.jugarte.gourmet.builders;

import com.jugarte.gourmet.beans.Gourmet;
import com.jugarte.gourmet.beans.Operation;

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
		gourmet.operations = null;
        return gourmet;
    }

	public String removeLastWord(String text) {
		text = cleanString(text);
		text = text.substring(0, text.length()-1);
		text = cleanString(text);
		return text;
	}

	public String cleanString(String text) {
		text = text.replace("\n" , "");
		text = text.replace("\t" , "");
		text = text.replace("\r" , "");
		text = text.replace("Saldo: " , "");
		text = text.trim();
		return text;
	}

	public GourmetInternalBuilder(String deviceType) {
		super(deviceType);
	}

	@Override
	public Gourmet build() throws Exception {
		if (this._data == null || this._data.length() == 0) {
			return this.returnError("3", "El servidor no responde");
		}
		Gourmet gourmet = new Gourmet();
		gourmet.errorCode = "0";
		gourmet.errorMessage = "";

		Document doc = Jsoup.parse(this._data);

		if (doc.getElementById("dato1") != null) {
			return this.returnError("2", "Usuario o contraseña incorrectos");
		}

		Element currentBalanceElement = doc.getElementById("TotalSaldo");
		if (currentBalanceElement == null) {
			return this.returnError("3", "El servidor no responde");
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

		return gourmet;
	}

	@Override
	public void append(String type, Object data) {
		if(type.equals(GourmetInternalBuilder.DATA_JSON)) {
			this._data = (String) data;
		}
	}
}