package test;

import static org.junit.Assert.*;

import java.util.List;

import model.Invoice;
import model.PayoutRequest;
import model.Rates;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import controller.BitPay;


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
	private static String accountEmail = "chaz@bitpay.com";
	
	
	@Before
	public void setUp() throws Exception {		
		this.bitpay = new BitPay();
	}

	@After
	public void tearDown() throws Exception {
	}
	
	
	@Test
	public void testShouldGenerateAndSubmitValidKey(){
		BitPay bp = new BitPay();
		assertNotNull(bp.getSIN());
	}
	
	@Test
	public void testShouldGenerateAndSaveValidPrivateKey() {
		
	}

	@Test
	public void testShouldSubmitKey() {
		BitPay bp = new BitPay();
		JSONObject response = bp.submitKey(accountEmail, "test");
		assertTrue(response.get("data") instanceof JSONObject);
	}
	
	@Test
	public void testShouldGetTokens() {
		JSONObject tokens = this.bitpay.getTokens();
		assertTrue(tokens.get("data") instanceof JSONArray);
	}
/*

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
		Invoice retreivedInvoice = this.bitpay.getInvoice(invoice.getId());
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

	@Test
	public void testShouldCreatePayoutRequest() {
		PayoutRequest payoutRequest = new PayoutRequest();
		payoutRequest.addInstruction(1, "n1cf9z1dpf5GANHpJ2tNefYByvBsCFELae");
		
		JSONObject response = this.bitpay.submitPayoutRequest(payoutRequest);
		assertNotNull(response);
	}
*/
}
