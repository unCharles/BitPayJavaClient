package model;

import org.json.JSONException;
import org.json.simple.JSONObject;

/**
 * 
 * @author Andy Phillipson
 * @date 8.1.2014
 *
 */
public class LedgerEntry {
	private String entryDate;
	private String entryTime;
	private String orderId;
	private String txType;
	private String currency;
	private String amount;
	private String description;
	private String exchangeRate;
	private String buyerName;
	private String buyerEmail;
	private String token;
	
	public LedgerEntry(JSONObject ledgerEntryData) throws JSONException{
		this.entryDate = (String) ledgerEntryData.get("entryDate");
		this.entryTime = (String) ledgerEntryData.get("entryTime");
		this.orderId = (String) ledgerEntryData.get("orderId");
		this.txType = (String) ledgerEntryData.get("txType");
		this.currency = (String) ledgerEntryData.get("currency");
		this.amount = (String) ledgerEntryData.get("amount");
		this.description = (String) ledgerEntryData.get("description");
		this.exchangeRate = (String) ledgerEntryData.get("exchangeRate");
		this.buyerName = (String) ledgerEntryData.get("buyerName");
		this.buyerEmail = (String) ledgerEntryData.get("buyerEmail");
		this.token = (String) ledgerEntryData.get("token");
	}
	
	public String getEntryDate() {
		return entryDate;
	}

	public String getEntryTime() {
		return entryTime;
	}

	public String getOrderId() {
		return orderId;
	}

	public String getTxType() {
		return txType;
	}

	public String getCurrency() {
		return currency;
	}

	public String getAmount() {
		return amount;
	}

	public String getDescription() {
		return this.description;
	}
	
	public String getExchangeRate() {
		return this.exchangeRate;
	}
	
	public String getBuyerName() {
		return this.buyerName;
	}
	
	public String getBuyerEmail() {
		return this.buyerEmail;
	}

	public String getToken() {
		return this.token;
	}

}
