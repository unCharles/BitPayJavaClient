package model;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import controller.BitPay;


/**
 * 
 * @author Chaz Ferguson
 * @date 11.11.2013
 *
 */
public class Rates {

	private JSONArray rates;
	private BitPay bp;

	/**
	 * 
	 * @param The Raw HTTP Response from BitPay api/rates call.
	 * @param bp - used to update self
	 */
	public Rates(JSONArray rates, BitPay bp) {
			this.rates = rates;
			this.bp = bp;
	}
	
	/**
	 * 
	 * @return Bitcoin Exchange rates in a JSONArray.
	 */
	public JSONArray getRates() {
		return this.rates;
	}
	
	/**
	 * Updates the rates from the BitPay api.
	 */
	public void update() {
		this.rates = this.bp.getRates().getRates();
	}
	
	/**
	 * Returns the Bitcoin exchange rate for the given currency code.
	 * Ensure that the currency code is valid, and in ALL CAPS.
	 * @param 3 letter currency code in all caps.
	 * @return String of the exchange rate.
	 */
	public double getRate(String currencyCode) {
		double val = 0;
		for (Object rate : this.rates) {
			JSONObject obj = (JSONObject) rate;
			if (obj.get("code").equals(currencyCode)) {
				try {
					val = (Double) obj.get("rate");
				} catch(ClassCastException e) {
					val = ((Long)obj.get("rate")).doubleValue();
				}
			}
		}
		return val;
	}
	
}
