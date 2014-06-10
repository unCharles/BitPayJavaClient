package model;

public class PayoutInstruction {
	private double amount;
	private String address;
	private String label;
	
	public PayoutInstruction(double amount, String address) {
		this.amount = amount;
		this.address = address;
		this.label = "";
	}
	
	public PayoutInstruction(double amount, String address, String label) {
		this.amount = amount;
		this.address = address;
		this.label = label;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
	public String toJsonString() {
		//TO DO
		return "";
	}
}
