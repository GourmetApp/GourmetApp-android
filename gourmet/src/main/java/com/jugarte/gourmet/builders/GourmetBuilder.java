package com.jugarte.gourmet.builders;

import android.content.Context;

import com.jugarte.gourmet.beans.Gourmet;
import com.jugarte.gourmet.beans.Operation;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class GourmetBuilder extends BaseBuilder {

	public static final String DATA_CARD_NUMBER = "cardNumber";
	public static final String DATA_MODIFICATION_DATE = "modificationDate";

	private String _data = "";
	private String cardNumber = "";
    private String modificationDate = "";
	private final Context context;

    public GourmetBuilder(Context context) {
		this.context = context;
	}

    private Gourmet returnError(String errorCode) {
        Gourmet gourmet = new Gourmet();
        gourmet.setErrorCode(errorCode);
		gourmet.setOperations(null);
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
			return null;
		}
		Gourmet gourmet = new Gourmet();
		gourmet.setErrorCode("0");

		Document doc = Jsoup.parse(this._data);

		if (doc.getElementById("dato1") != null) {
			return this.returnError("2");
		}

		Element currentBalanceElement = doc.getElementById("TotalSaldo");
		if (currentBalanceElement == null) {
			return null;
		}
		gourmet.setCurrentBalance(this.cleanString(currentBalanceElement.text()));

		Elements operationsElement = doc.getElementsByTag("tr");
		for (Element operationElement : operationsElement) {
			Operation operation = new Operation();
			operation.setName(this.removeLastWord(operationElement.getElementById("operacion").text()));
			operation.setPrice(operationElement.getElementById("importe").text());
			operation.setDate(operationElement.getElementById("fecha").text());
			operation.setHour(operationElement.getElementById("horaOperacion").text());
			gourmet.addOperation(operation);
		}

		if (gourmet.getOperations() != null && gourmet.getOperations().size() > 0) {
			Operation o = gourmet.getOperations().get(gourmet.getOperations().size() - 1);
			if (o.getPrice().equalsIgnoreCase("fin")) {
				gourmet.getOperations().remove(gourmet.getOperations().size() - 1);
			}
		} else {
			gourmet.setOperations(null);
		}

		gourmet.setCardNumber(cardNumber);
		gourmet.setModificationDate(modificationDate);

		return gourmet;
	}

	@Override
	public void append(String type, Object data) {
		if(type.equalsIgnoreCase(BaseBuilder.DATA_JSON)) {
			_data = (String) data;
		} else if (type.equalsIgnoreCase(DATA_CARD_NUMBER)) {
			cardNumber = (String) data;
		} else if (type.equalsIgnoreCase(DATA_MODIFICATION_DATE)) {
            modificationDate = (String) data;
        }
	}
}