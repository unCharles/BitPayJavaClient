package model;

import org.json.JSONException;
import org.json.simple.JSONObject;

/**
 * 
 * @author Chaz Ferguson
 * @date 11.11.2013
 *
 */
public class Invoice {
	private String id;
	private String url;
	private String status;
	private String btcPrice;
	private Long price;
	private String currency;
	private String token;
	
	public Invoice(JSONObject invoiceData) throws JSONException{
		this.id = (String) invoiceData.get("id");
		this.url = (String) invoiceData.get("url");
		this.status = (String) invoiceData.get("status");
		this.btcPrice = (String) invoiceData.get("btcPrice");
		this.price = (Long) invoiceData.get("price");
		this.currency = (String) invoiceData.get("currency");
		this.token = (String) invoiceData.get("token");
	}
	
	public String getId() {
		return id;
	}

	public String getUrl() {
		return url;
	}

	public String getStatus() {
		return status;
	}

	public String getBtcPrice() {
		return btcPrice;
	}

	public Long getPrice() {
		return price;
	}

	public String getCurrency() {
		return currency;
	}

	public String getToken() {
		return this.token;
	}

}
