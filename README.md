BitPayJavaClient
=============

This is a Java client library for the BitPay Payment Gateway

Dependencies
------------
BitPay merchant account

ApacheHttpClient

Jackson Annotations, Core, Databind (download and install this package)

Getting Started
---------------

Log into your BitPay merchant account and generate a pairing code. Then all you need to do is instantiate a BitPay object, and call authorizeClient() passing your pairing code.  The first time the BitPay object is instantiated a new BitPay identity for the client is created and stored locally on the client in a file 'bitpay_private.key' (this file is encrypted).

```java
bitpay = new BitPay(clientName);
if (!bitpay.clientIsAuthorized(BitPay.FACADE_POS))
{
    bitpay.authorizeClient(pairingCode);
}
```

####Create an invoice
```java
Invoice invoice = new Invoice(100.0, "USD");

invoice = bitpay.createInvoice(invoice);

String invoiceUrl = invoice.getURL();

String status = invoice.getStatus();
```

####Retreive an invoice
```java
Invoice invoice = bitpay.getInvoice(invoice.getId());
```
####Exchange Rates

You can also get BitPay's exchange rates.
```java
Rates rates = this.bitpay.getRates();

double rate = rates.getRate("USD");

rates.update();
```
####Advanced Invoices

You can add additional params to the invoice object prior to calling createInvoice(). You don't have to set all of the advanced parameters. It will only use the ones you do set.  createInvoice() updates your original invoice object.
```java
Invoice invoice = new Invoice(100.0, "USD");
invoice.setBuyerName("Satoshi");
invoice.setBuyerEmail("satoshi@bitpay.com");
invoice.setFullNotifications(true);
invoice.setNotificationEmail("satoshi@bitpay.com");
invoice.setPosData("ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890");
invoice = bitpay.createInvoice(invoice);
```
