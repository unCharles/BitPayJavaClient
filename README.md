BitPayJavaAPI
=============

This is a Java client library for the BitPay Payment Gateway

Dependencies
------------
BitPay merchant account

ApacheHttpClient

java-json

json-simple

Getting Started
---------------

Log into your BitPay merchant account and generate an API key. Then all you need to do is instantiate a BitPay object, and pass in your API key and the default currency code.

```java
Bitpay bitpay = new Bitpay("API_KEY");
```

####Create an invoice
```java
Invoice invoice = bitpay.createInvoice(100, "USD");

String invoiceUrl = invoice.getURL();

String status = invoice.getStatus();
```

####Retreive an invoice
```java
invoice = bitpay.getInvoice(invoice.getId());
```
####Exchange Rates

You can also get BitPay's exchange rates.
```java
Rates rates = this.bitpay.getRates();

double rate = rates.getRate("USD");

rates.update();
```
####Advanced Invoices

You can add additional params to the invoice by passing an InvoiceParams object. You don't have to set all of the advanced parameters. It will only use the ones you do set.
```java
InvoiceParams params = new InvoiceParams();

params.setBuyerName("Satoshi");
params.setBuyerEmail("satoshi@bitpay.com");
params.setFullNotifications(true);
params.setNotificationEmail("satoshi@bitpay.com");
		
Invoice invoice = this.bitpay.createInvoice(100, params);
```
