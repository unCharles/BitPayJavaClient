package test;

import static org.junit.Assert.*;

import java.util.Date;
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
import controller.BitPayException;
import controller.KeyUtils;
import com.google.bitcoin.core.ECKey;


/**
 * In order for tests to pass, the SIN must be approved, 
 * and associated to an account with access to the 
 * merchant resource.
 */
public class BitPayTest {

	private BitPay bitpay;
	private Invoice basicInvoice;
	private static String privateKeyFile = "key.txt";
	private static String SIN = "SIN";
	//private static double BTC_EPSILON = .000000001;
	private static double EPSILON = .001;
	private static String accountEmail = "";
	
	
	@Before
	public void setUp() throws Exception {
		String privateKey = KeyUtils.readCompressedHexKey(privateKeyFile);
		this.bitpay = new BitPay(privateKey, SIN);
	}

	@After
	public void tearDown() throws Exception {
	}
	
	
	@Test
	public void testShouldGenerateAndSubmitValidKey(){
		assertNotNull(this.bitpay.getSIN());
	}
	
	@Test
	public void testShouldGenerateAndSaveValidPrivateKey() {
		
	}

	@Test
	public void testShouldSubmitKey() {
		JSONObject response = this.bitpay.submitKey(accountEmail, "test");
		assertTrue(response.get("data") instanceof JSONObject);
	}
	
	@Test
	public void testShouldGetTokens() {
		JSONArray tokens = null;
		tokens = this.bitpay.getTokens();
		assertTrue(tokens instanceof JSONArray);
		
	}

	@Test
	public void testShouldCreateInvoice100BTC() {
		Invoice invoice = null;
		try {
			invoice = this.bitpay.createInvoice(100, "BTC");
		} catch (BitPayException e) {
			e.printStackTrace();
		}
		assertEquals("BTC", invoice.getCurrency());
		
	}

	@Test
	public void testShouldCreateInvoice100USD() {
		Invoice invoice = null;
		try {
			invoice = this.bitpay.createInvoice(100, "USD");
		} catch (BitPayException e) {
			e.printStackTrace();
		}
		assertEquals(invoice.getPrice(), 100.0, EPSILON);
		
	}
	
	@Test
	public void testShouldGetInvoiceId() {
		try {
			basicInvoice = bitpay.createInvoice(50, "USD");
		} catch (BitPayException e) {
			e.printStackTrace();
		}
		assertNotNull(basicInvoice.getId());
	}
	
	@Test
	public void testShouldGetInvoiceURL() {
		try {
			basicInvoice = bitpay.createInvoice(50, "USD");
		} catch (BitPayException e) {
			e.printStackTrace();
		}
		assertNotNull(basicInvoice.getUrl());
		
	}
	
	@Test
	public void testShouldGetInvoiceStatus() {
		try {
			basicInvoice = bitpay.createInvoice(50, "USD");
		} catch (BitPayException e) {
			e.printStackTrace();
		}
		assertNotNull(basicInvoice.getStatus());
		
	}
	
	@Test
	public void testShouldGetInvoiceBTCPrice() {
		try {
			basicInvoice = bitpay.createInvoice(50, "USD");
		} catch (BitPayException e) {
			e.printStackTrace();
		}
		assertNotNull(basicInvoice.getBtcPrice());
		
	}

	@Test
	public void testShouldCreateInvoice100EUR() {
		Invoice invoice = null;
		try {
			invoice = this.bitpay.createInvoice(100, "EUR");
		} catch (BitPayException e) {
			e.printStackTrace();
		}
		assertEquals(invoice.getPrice(), 100.0, EPSILON);
		
	}
	
	@Test
	public void testShouldGetInvoice() {
		Invoice invoice = null;
		Invoice retreivedInvoice = null;
		try {
			invoice = this.bitpay.createInvoice(100, "EUR");
			retreivedInvoice = this.bitpay.getInvoice(invoice.getId());
		} catch (BitPayException e) {
			e.printStackTrace();
		}
		assertEquals(invoice.getId(), retreivedInvoice.getId());
		
	}
	
	@Test
	public void testShouldGetInvoices() {
		List<Invoice> invoices = null;
		try {
			invoices = this.bitpay.getInvoices("2014-01-01");
		} catch (BitPayException e) {
			e.printStackTrace();
		}
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
		payoutRequest.setAmount(1);
		payoutRequest.setCurrency("BTC");
		payoutRequest.setEffectiveDate(new Date());
		
		JSONObject response = null;
		try {
			response = this.bitpay.submitPayoutRequest(payoutRequest);
		} catch (BitPayException e) {
			e.printStackTrace();
		}
		assertNotNull(response);
	}

}
