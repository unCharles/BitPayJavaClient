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
import com.google.bitcoin.core.ECKey;


/**
 * 
 * @author Chaz Ferguson
 * @date 11.11.2013
 *
 */
public class BitPay {
	
	private static final String BASE_URL = "https://test.bitpay.com/";
	
	private HttpClient client;
	ECKey privateKey;
	long nonce;
	String SIN;
	
	public BitPay(ECKey privateKey, String SIN) {
		this.SIN = SIN;
		this.nonce = new Date().getTime();
		this.privateKey = privateKey;
		client = HttpClientBuilder.create().build();
	}
	
	public String submitKey(String accountEmail, String sin, String label) {
		List<NameValuePair> params = this.getParams(accountEmail, sin, label);
		String url = BASE_URL + "keys";
		HttpResponse response = this.post(url, params);
		return response.toString();
	}
	
	public Invoice createInvoice(double price, String currency) {
		if(currency.length() > 3) {
			throw new IllegalArgumentException("Must be a valid currency code");
		}
		
		String url = BASE_URL + "invoice";
		List<NameValuePair> params = this.getParams(price, currency);
		HttpResponse response = this.post(url, params);
		return createInvoiceObjectFromResponse(response);
	}
	
	public Invoice createInvoice(double price, String currency, InvoiceParams optionalParams) {
		if(currency.length() > 3) {
			throw new IllegalArgumentException("Must be a valid currency code");
		}
		
		String url = BASE_URL + "invoice";
		List<NameValuePair> params = this.getParams(price, currency, optionalParams);
		HttpResponse response = this.post(url, params);
		return createInvoiceObjectFromResponse(response);
	}
	

	public Invoice getInvoice(String invoiceId) {
		String url = BASE_URL + "invoice/" + invoiceId;
		List<NameValuePair> params = this.getParams();
		
		HttpResponse response = this.get(url, params);
		return createInvoiceObjectFromResponse(response);
	}
	
	public String submitPayoutRequest(PayoutRequest payoutRequest) {
		// TODO
		String url = BASE_URL + "payouts";
		List<NameValuePair> params = this.getParams();
		HttpResponse response = this.post(url, params);
		return response.toString();
	}
	
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
		return KeyUtils.signString(privateKey, data);
	}

	private List<NameValuePair> getParams() {
		List<NameValuePair> params = new ArrayList<NameValuePair>(2);
		params.add(new BasicNameValuePair("nonce", this.nonce + ""));
		this.nonce++;
		return params;
	}
	
	private List<NameValuePair> getParams(String email, String sin, String label) {
		List<NameValuePair> params = new ArrayList<NameValuePair>(2);
		params.add(new BasicNameValuePair("nonce", this.nonce + ""));
		params.add(new BasicNameValuePair("sin", sin));
		params.add(new BasicNameValuePair("email", email));
		params.add(new BasicNameValuePair("label", label));
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

	private HttpResponse post(String url, List<NameValuePair> params) {
		try {
			HttpPost post = new HttpPost(url);
			post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			
			String signature = signData(url, params);
			post.addHeader("X-BitPay-Plugin-Info", "Javalib0.1.0");
			post.addHeader("x-signature", signature);
			post.addHeader("x-pubkey", SIN);
			
			HttpResponse response = this.client.execute(post);
			
	        return response;
	        
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	private HttpResponse get(String url, List<NameValuePair> params) {
		HttpGet get = new HttpGet(url);
		
		String sig = signData(url, params);
		get.addHeader("X-BitPay-Plugin-Info", "Javalib0.1.0");
		get.addHeader("x-signature", sig);
		get.addHeader("x-pubkey", SIN);
		
		try {
			HttpResponse response = client.execute(get);
			
	        return response;
	
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	
		return null;
	}

	private Invoice createInvoiceObjectFromResponse(HttpResponse response) {
		BufferedReader rd;
		try {
			rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
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
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return null;
	
	};

}
