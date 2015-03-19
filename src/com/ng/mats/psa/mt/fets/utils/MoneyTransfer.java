package com.ng.mats.psa.mt.fets.utils;

public class MoneyTransfer {
	private String transactionId;
	private long payerWalletId;
	private String payerNumber;
	private String transactionPin;
	private long channelId;
	private long billerMerchantId;
	private long billerProductId;
	private String recieverNumber;
	private String remarks;
	private double amount;
	private long charge;
	private String billerTransactionRef;
	private String accountNumber;
	private String bankCode;
	private String accountName;
	private String url;
	private String reference;

	public MoneyTransfer() {
		this.transactionId = "";
		this.payerNumber = "";
		this.transactionPin = "";
		this.recieverNumber = "";
		this.remarks = "";
		this.billerTransactionRef = "";
		this.accountNumber = "";
		this.bankCode = "";
		this.accountName = "";
		this.payerWalletId = 0l;
		this.channelId = 0l;
		this.billerMerchantId = 0l;
		this.billerProductId = 0l;
		this.charge = 0l;
		this.amount = 0.0;
		this.url = "";
		this.reference = "";
	}

	public String toString() {
		return "TransactionId  : ".concat(transactionId)
				.concat("\nPayer Wallet Id : ")
				.concat(String.valueOf(payerWalletId))
				.concat("\nPayer Number : ").concat(payerNumber)
				.concat("\nTransaction Pin : ").concat(transactionPin)
				.concat("\nChannel Id : ").concat(String.valueOf(channelId))
				.concat("\nBiller Merchant Id : ")
				.concat(String.valueOf(billerMerchantId))
				.concat("\nBiller Product Id : ")
				.concat(String.valueOf(billerProductId))
				.concat("\nReciever Number : ").concat(recieverNumber)
				.concat("\nRemarks : ").concat(remarks).concat("\nAmount : ")
				.concat(String.valueOf(amount)).concat("\nCharge : ")
				.concat(String.valueOf(charge))
				.concat("\nBiller Transaction Ref : ")
				.concat(billerTransactionRef).concat("\nAccount Number : ")
				.concat(accountNumber).concat("\nAccount Name : ")
				.concat(accountName).concat("\nBank Code : ").concat(bankCode)
				.concat("\nURL : ").concat(url).concat("\nReference : ")
				.concat(reference);

	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public long getPayerWalletId() {
		return payerWalletId;
	}

	public void setPayerWalletId(long payerWalletId) {
		this.payerWalletId = payerWalletId;
	}

	public String getPayerNumber() {
		return payerNumber;
	}

	public void setPayerNumber(String payerNumber) {
		this.payerNumber = payerNumber;
	}

	public String getTransactionPin() {
		return transactionPin;
	}

	public void setTransactionPin(String transactionPin) {
		this.transactionPin = transactionPin;
	}

	public long getChannelId() {
		return channelId;
	}

	public void setChannelId(long channelId) {
		this.channelId = channelId;
	}

	public long getBillerMerchantId() {
		return billerMerchantId;
	}

	public void setBillerMerchantId(long billerMerchantId) {
		this.billerMerchantId = billerMerchantId;
	}

	public long getBillerProductId() {
		return billerProductId;
	}

	public void setBillerProductId(long billerProductId) {
		this.billerProductId = billerProductId;
	}

	public String getRecieverNumber() {
		return recieverNumber;
	}

	public void setRecieverNumber(String recieverNumber) {
		this.recieverNumber = recieverNumber;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public long getCharge() {
		return charge;
	}

	public void setCharge(long charge) {
		this.charge = charge;
	}

	public String getBillerTransactionRef() {
		return billerTransactionRef;
	}

	public void setBillerTransactionRef(String billerTransactionRef) {
		this.billerTransactionRef = billerTransactionRef;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

}
