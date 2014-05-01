package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.json.JSONException;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import java.security.*;  

/**
 * 
 * @author Chaz Ferguson
 * @date 11.11.2013
 *
 */
public class BitPay {
	
	private static final String BASE_URL = "https://test.bp/";
	
	private HttpClient client;
	Signature signature;
	long nonce;
	
	/**
	 * Constructor.
	 * @param apiKey
	 * Generated at BitPay.com. Merchant account required.
	 * 
	 * @param currency
	 * default currency code
	 */
	public BitPay(PrivateKey privateKey) {
		this.nonce = new Date().getTime();
		try {
			this.signature = Signature.getInstance("SHA256withRSA");
			signature.initSign(privateKey);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} 
		client = HttpClientBuilder.create().build();
	}
	
	/**
	 *  Creates an invoice using the BitPay Payment Gateway API
	 * @param price - set in this.currency
	 * @return Invoice
	 */
	public Invoice createInvoice(double price, String currency) {
		if(currency.length() > 3) {
			throw new IllegalArgumentException("Must be a valid currency code");
		}
		
		String url = BASE_URL + "invoice";
		
		try {
			HttpPost post = new HttpPost(url);
			
			List<NameValuePair> params = this.getParams(price, currency);
			post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			String signature = signData(url, params);
			post.addHeader("x-signature", signature);
			post.addHeader("x-pubkey", ""); // TODO
			
			HttpResponse response = this.client.execute(post);
			
            return createInvoiceObjectFromResponse(response);
            
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Creates an invoice using the BitPay Payment Gateway API
	 * 
	 * @param price - set in this.currency
	 * @param params - optional invoice parameters
	 * @return Invoice
	 */
	public Invoice createInvoice(double price, String currency, InvoiceParams params) {
		if(currency.length() > 3) {
			throw new IllegalArgumentException("Must be a valid currency code");
		}
		
		String url = BASE_URL + "invoice";
		
		try {
			HttpPost post = new HttpPost(url);
			
			post.addHeader("X-BitPay-Plugin-Info", "Javalib0.1.0");
			List<NameValuePair> body = this.getParams(price, currency, params);
			post.setEntity(new UrlEncodedFormEntity(body, "UTF-8"));
			String signature = signData(url, body);
			post.addHeader("x-signature", signature);
			post.addHeader("x-pubkey", ""); // TODO
			
			HttpResponse response = this.client.execute(post);
			
            return createInvoiceObjectFromResponse(response);
            
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return null;
	}
	
	/**
	 * Get an existing Invoice by it's ID. The ID is used in the url:
	 * "http://bitpay.com/invoice?id=<ID>"
	 * @param invoiceId
	 * @return Invoice
	 */
	public Invoice getInvoice(String invoiceId) {
		String url = BASE_URL + "invoice/" + invoiceId;
		
		HttpGet get = new HttpGet(url);
		
		List<NameValuePair> body = this.getParams();
		String sig = signData(url, body);
		get.addHeader("x-signature", sig);
		get.addHeader("x-pubkey", ""); // TODO
		
		try {
			HttpResponse response = client.execute(get);
			
	        return createInvoiceObjectFromResponse(response);

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return null;
	}
	
	/**
	 * Submit a payout request.
	 * "http://bitpay.com/invoice?id=<ID>"
	 * @param invoiceId
	 * @return Invoice
	 */
	public String submitPayoutRequest(PayoutRequest payoutRequest) {
		String url = BASE_URL + "payouts";
		
		HttpPost post = new HttpPost(url);
		
		List<NameValuePair> body = this.getParams();
		String sig = signData(url, body);
		post.addHeader("x-signature", sig);
		post.addHeader("x-pubkey", ""); // TODO
		
		try {
			HttpResponse response = client.execute(post);
	        return response.toString();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}
	
	/**
	 * Get the current Bitcoin Exchange rates in dozens of currencies based on several exchanges.
	 * @return Rates object.
	 */
	public Rates getRates() {
		String url = BASE_URL + "rates";
		
		HttpGet get = new HttpGet(url);
		
		try {
			HttpResponse response = client.execute(get);
			
	        return new Rates(response, this);

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}
	
	private String signData(String url, List<NameValuePair> body) {
		String data = url + "{";
		for(NameValuePair param : body) {
			data += "'" + param.getName() + "':'" + param.getValue() + "',";
		}
		data = data.substring(0,data.length() - 1);
		data += "}";
		byte[] dataInBytes = data.getBytes();  
        try {
			this.signature.update(dataInBytes);
	        byte[] signedInfo = signature.sign();
	        return signedInfo.toString();
		} catch (SignatureException e) {
			e.printStackTrace();
		}
        return "";       
	}

	private List<NameValuePair> getParams() {
		List<NameValuePair> params = new ArrayList<NameValuePair>(2);
		params.add(new BasicNameValuePair("nonce", this.nonce + ""));
		this.nonce++;
		return params;
	}
	
	private List<NameValuePair> getParams(double price,
			String currency) {
		List<NameValuePair> params = new ArrayList<NameValuePair>(2);
		params.add(new BasicNameValuePair("price", price + ""));
		params.add(new BasicNameValuePair("currency", currency));
		params.add(new BasicNameValuePair("nonce", this.nonce + ""));
		this.nonce++;
		return params;
	}
	
	private List<NameValuePair> getParams(double price,
			String currency, InvoiceParams optionalParams) {
		List<NameValuePair> params = new ArrayList<NameValuePair>(2);
		for (BasicNameValuePair param : optionalParams.getNameValuePairs()) {
			params.add(param);
		}
		params.add(new BasicNameValuePair("price", price + ""));
		params.add(new BasicNameValuePair("currency", currency));
		params.add(new BasicNameValuePair("nonce", this.nonce + ""));
		this.nonce++;
		return params;
	}

	private Invoice createInvoiceObjectFromResponse(HttpResponse response) throws IOException, JSONException {
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		
		StringBuilder content = new StringBuilder();
		String line;
		
		while (null != (line = rd.readLine())) {
		    content.append(line);
		}
		
		Object obj=JSONValue.parse(content.toString());
		JSONObject finalResult = (JSONObject)obj;
		
		if(finalResult.get("error") != null ){
			System.out.println("Error: " + finalResult.get("error"));
		}
		
		return new Invoice(finalResult);
	};

}
