package test;

import static org.junit.Assert.*;

import java.security.PrivateKey;


import model.BitPay;
import model.Invoice;
import model.InvoiceParams;
import model.KeyUtils;
import model.Rates;

import org.json.simple.JSONArray;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class BitPayTest {

	private BitPay bitpay;
	private Invoice basicInvoice;
	private static double BTC_EPSILON = .000000001;
	private static double EPSILON = .001;
	
	@Before
	public void setUp() throws Exception {
		String key64 = "";
		String SIN = "";
		PrivateKey privateKey = KeyUtils.loadPrivateKey(key64);
		this.bitpay = new BitPay(privateKey, SIN);
		basicInvoice = this.bitpay.createInvoice(100, "USD");
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testShouldCreateInvoice100BTC() {
		Invoice invoice = this.bitpay.createInvoice(100, "BTC");
		assertEquals(invoice.getBtcPrice(), 100.0, BTC_EPSILON);
	}
	
	@Test
	public void testShouldCreateInvoice100USD() {
		Invoice invoice = this.bitpay.createInvoice(100, "USD");
		assertEquals(invoice.getPrice(), 100.0, EPSILON);
	}
	
	@Test
	public void testShouldGetInvoiceId() {
		assertNotNull(basicInvoice.getId());
	}
	
	@Test
	public void testShouldGetInvoiceURL() {
		assertNotNull(basicInvoice.getUrl());
	}
	
	@Test
	public void testShouldGetInvoiceStatusL() {
		assertNotNull(basicInvoice.getStatus());
	}
	
	@Test
	public void testShouldGetInvoiceBTCPrice() {
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
	public void testShouldCreateInvoiceWithAdditionalParams() {
		InvoiceParams params = new InvoiceParams();
		params.setBuyerName("Satoshi");
		params.setBuyerEmail("satoshi@bitpay.com");
		params.setFullNotifications(true);
		params.setNotificationEmail("satoshi@bitpay.com");
		
		Invoice invoice = this.bitpay.createInvoice(100, "USD", params);
		
		//Print to verify the information is on the invoice.
		System.out.println(invoice.getUrl());
		
		assertNotNull(invoice);
	}

	
	@Test
	public void testShouldGetExchangeRates() {
		Rates rates = this.bitpay.getRates();
		
		JSONArray arrayRates = rates.getRates();
		
		System.out.println("Exchange Rates: " + arrayRates);
		
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
