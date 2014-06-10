package model;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;

public class PayoutRequest {
	private ArrayList<PayoutInstruction> instructions;
	double amount;
	String currency;
	Date effectiveDate;
	String reference;
	String pricingMethod;
	String notificationURL;
	String notificationEmail;
	
	public PayoutRequest() {
		instructions = new ArrayList<PayoutInstruction>();
	}
	
	public void addInstruction(double amount, String address) {
		this.instructions.add(new PayoutInstruction(amount, address));
	}
	
	public void addInstruction(double amount, String address, String label) {
		this.instructions.add(new PayoutInstruction(amount, address, label));
	}
	
	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Date getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getNotificationURL() {
		return notificationURL;
	}

	public void setNotificationURL(String notificationURL) {
		this.notificationURL = notificationURL;
	}

	public String getNotificationEmail() {
		return notificationEmail;
	}

	public void setNotificationEmail(String notificationEmail) {
		this.notificationEmail = notificationEmail;
	}
	
	public List<NameValuePair> getParams() {
		// TODO
		List<NameValuePair> params = new ArrayList<NameValuePair>(2);
		return params;
	}
	
	public String toJsonString() {
		// TODO
		return "";
	}
}
