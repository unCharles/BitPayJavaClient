package test;

import static org.junit.Assert.*;

import java.util.List;

import model.Invoice;
import model.Rates;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.google.bitcoin.core.ECKey;

import controller.BitPay;
import controller.KeyUtils;

/**
 * In order for tests to pass, the SIN must be approved, 
 * and associated to an account with access to the 
 * merchant resource.
 */
public class BitPayTest {

	private BitPay bitpay;
	private Invoice basicInvoice;
	//private static double BTC_EPSILON = .000000001;
	private static double EPSILON = .001;
	
	private static String SIN = "TfGVVuQeW2p2eazrpTC825nwWgXywXSv6ux";
	private static String privKeyFile = "TfGVVuQeW2p2eazrpTC825nwWgXywXSv6ux.priv";
	private static String pubKeyFile = "TfGVVuQeW2p2eazrpTC825nwWgXywXSv6ux.pub";
	private static String accountEmail = "chaz@bitpay.com";
	
	
	@Before
	public void setUp() throws Exception {
		String privateKey = KeyUtils.readKeyFromFile(privKeyFile);
		String publicKey = KeyUtils.readKeyFromFile(pubKeyFile);
		ECKey privKey = KeyUtils.loadKeys(privateKey, publicKey);
		
		this.bitpay = new BitPay(privKey, SIN);
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testShouldGetTokens() {
		JSONObject tokens = this.bitpay.getTokens();
		assertTrue(tokens.get("data") instanceof JSONArray);
	}

	@Test
	public void testShouldSubmitKey() {
		JSONObject response = this.bitpay.submitKey(accountEmail, SIN, "test");
		assertTrue(response.get("data") instanceof JSONObject);
	}

	@Test
	public void testShouldCreateInvoice100BTC() {
		Invoice invoice = this.bitpay.createInvoice(100, "BTC");
		assertEquals("BTC", invoice.getCurrency());
	}

	@Test
	public void testShouldCreateInvoice100USD() {
		Invoice invoice = this.bitpay.createInvoice(100, "USD");
		assertEquals(invoice.getPrice(), 100.0, EPSILON);
	}
	
	@Test
	public void testShouldGetInvoiceId() {
		basicInvoice = bitpay.createInvoice(50, "USD");
		assertNotNull(basicInvoice.getId());
	}
	
	@Test
	public void testShouldGetInvoiceURL() {
		basicInvoice = bitpay.createInvoice(50, "USD");
		assertNotNull(basicInvoice.getUrl());
	}
	
	@Test
	public void testShouldGetInvoiceStatus() {
		basicInvoice = bitpay.createInvoice(50, "USD");
		assertNotNull(basicInvoice.getStatus());
	}
	
	@Test
	public void testShouldGetInvoiceBTCPrice() {
		basicInvoice = bitpay.createInvoice(50, "USD");
		assertNotNull(basicInvoice.getBtcPrice());
	}

	@Test
	public void testShouldCreateInvoice100EUR() {
		Invoice invoice = this.bitpay.createInvoice(100, "EUR");
		assertEquals(invoice.getPrice(), 100.0, EPSILON);
	}
	
	@Test
	public void testShouldGetInvoice() {
		Invoice invoice = this.bitpay.createInvoice(100, "EUR");
		Invoice retreivedInvoice = this.bitpay.getInvoice(invoice.getId(), invoice.getToken());
		assertEquals(invoice.getId(), retreivedInvoice.getId());
	}
	
	@Test
	public void testShouldGetInvoices() {
		List<Invoice> invoices = this.bitpay.getInvoices("2014-01-01");
		assertTrue(invoices.size() > 0);
	}
	
	@Test
	public void testShouldGetExchangeRates() {
		Rates rates = this.bitpay.getRates();
		JSONArray arrayRates = rates.getRates();		
		assertNotNull(arrayRates);
	}
	
	@Test
	public void testShouldGetUSDExchangeRate() {
		Rates rates = this.bitpay.getRates();
		double rate = rates.getRate("USD");
		assertTrue(rate != 0);
	}
	
	@Test
	public void testShouldGetEURExchangeRate() {
		Rates rates = this.bitpay.getRates();
		double rate = rates.getRate("EUR");
		assertTrue(rate != 0);
	}
	
	@Test
	public void testShouldGetCNYExchangeRate() {
		Rates rates = this.bitpay.getRates();
		double rate = rates.getRate("CNY");
		assertTrue(rate != 0);
	}
	
	@Test
	public void testShouldUpdateExchangeRates() {
		Rates rates = this.bitpay.getRates();
		rates.update();
		JSONArray arrayRates = rates.getRates();
		assertNotNull(arrayRates);
	}

}
