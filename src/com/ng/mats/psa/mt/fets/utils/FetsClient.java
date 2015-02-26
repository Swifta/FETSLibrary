package com.ng.mats.psa.mt.fets.utils;

import java.rmi.RemoteException;
import java.util.logging.Logger;

import com.fets.mm.soap.services.FetsServiceStub;
import com.fets.mm.soap.services.FetsServiceStub.Authenticate;
import com.fets.mm.soap.services.FetsServiceStub.AuthenticateResponse;
import com.fets.mm.soap.services.FetsServiceStub.CashOutRequest;
import com.fets.mm.soap.services.FetsServiceStub.P2BankTransfer;
import com.fets.mm.soap.services.FetsServiceStub.P2PTransfer;
import com.fets.mm.soap.services.FetsServiceStub.ServiceResponse;
import com.fets.mm.soap.services.FetsServiceStub.Wallet;

public class FetsClient {
	private static FetsServiceStub fetsStub;
	private static final Logger logger = Logger.getLogger(FetsClient.class
			.getName());

	public static ServiceResponse doCashIn(MoneyTransfer moneyTransfer) {
		ServiceResponse serviceResponse = new ServiceResponse(), requestServiceResponse = new ServiceResponse();

		logger.info("----------------------start of FETS cash in Type "
				+ moneyTransfer.getPayerNumber() + " ::: "
				+ moneyTransfer.getTransactionPin());
		AuthenticateResponse authenticationResponse = doAuthentication(
				moneyTransfer.getPayerNumber(),
				moneyTransfer.getTransactionPin());
		if (authenticationResponse != null) {
			Wallet[] walletArray = authenticationResponse.getWallets();
			logger.info("----------------------start of FETS cash in Type "
					+ walletArray);
			int count = 1;
			for (Wallet newWallet : walletArray) {
				logger.info("----------------------iterating through array of wallet : "
						+ count++);
				if (newWallet.getDefaultWallet()) {
					logger.info("----------------------Default wallet "
							+ newWallet.getId());
					moneyTransfer.setPayerWalletId(newWallet.getId());
					break;
				} else {
					logger.info("----------------------Wallet not default wallet");
				}
			}
			logger.info("----------------------Before setting the ServiceResponse parameters");
			P2PTransfer p2PTransfer = new P2PTransfer();
			// requestServiceResponse.setAccount_no(moneyTransfer.getPayerNumber());
			// requestServiceResponse.setAccountName(param);
			// requestServiceResponse.setAgent_id(Long.valueOf(moneyTransfer
			// .getTransactionPin()));
			// requestServiceResponse.setAgent_password(moneyTransfer
			// .getTransactionPin());
			requestServiceResponse.setAmount(moneyTransfer.getAmount());
			// requestServiceResponse.setBank_code(param);
			// requestServiceResponse.setBen_msisdn(moneyTransfer.getPayerNumber());
			requestServiceResponse.setChannel_id(moneyTransfer.getChannelId());
			// requestServiceResponse.setConfirm_password(param);
			// requestServiceResponse.setCustomer_id(param);
			// requestServiceResponse.setCustomer_msisdn(moneyTransfer
			// .getPayerNumber());
			// requestServiceResponse.setCustomerRefNum();
			requestServiceResponse.setDestination_msisdn(moneyTransfer
					.getRecieverNumber());
			// requestServiceResponse.setId(param);
			// requestServiceResponse.setMerchant_id(param);
			// requestServiceResponse.setMessage(param);
			requestServiceResponse.setMsisdn(moneyTransfer.getPayerNumber());
			requestServiceResponse.setNaration(moneyTransfer.getRemarks());
			// requestServiceResponse.setNew_password(param);
			// requestServiceResponse.setOld_password(param);
			requestServiceResponse.setPassword(moneyTransfer
					.getTransactionPin());
			// requestServiceResponse.setProduct_id(param);
			// requestServiceResponse.setRecipient_msisdn(param);
			// requestServiceResponse.setRedeemCode(param);
			// requestServiceResponse.setResponseCode(param);
			// requestServiceResponse.setSuccess(param);
			requestServiceResponse.setTnxRefNo(moneyTransfer
					.getBillerTransactionRef());
			requestServiceResponse.setTranRefNum(moneyTransfer
					.getBillerTransactionRef());
			requestServiceResponse.setWallet_id(moneyTransfer
					.getPayerWalletId());

			/*
			 * p2PTransfer.setArg0(moneyTransfer.getPayerNumber());
			 * p2PTransfer.setArg1(moneyTransfer.getPayerWalletId());
			 * p2PTransfer.setArg2(moneyTransfer.getTransactionPin());
			 * p2PTransfer.setArg3(moneyTransfer.getChannelId());
			 * p2PTransfer.setArg4(moneyTransfer.getAmount());
			 * p2PTransfer.setArg5(String.valueOf(moneyTransfer.getCharge()));
			 * p2PTransfer.setArg6(moneyTransfer.getRemarks());
			 */p2PTransfer.setP2PTransfer(requestServiceResponse);
			printResponseDetails(requestServiceResponse);
			logger.info("----------------------After setting p2ptransfer attributes\n"
					+ moneyTransfer.toString());
			P2PTransfer p2PTransferResponse = new P2PTransfer();
			try {

				fetsStub = new FetsServiceStub();
				logger.info("----------------------After calling fets stub");
				p2PTransferResponse = fetsStub.p2PTransfer(p2PTransfer);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				logger.info("----------------------Remote Exception after calling fets");
				e.printStackTrace();
			}
			if (p2PTransferResponse != null) {
				logger.info("----------------------p2PTransferResponse is not null");

				printResponseDetails(p2PTransferResponse.getP2PTransfer());
			} else {
				logger.info("----------------------p2PTransferResponse is null");
			}
		} else {
			logger.info("----------------------Authentication Response is null");
		}
		return serviceResponse;
	}

	public static String getBalance(MoneyTransfer moneyTransfer) {
		ServiceResponse serviceResponse = new ServiceResponse(), cashoutRequestResp = new ServiceResponse();
		String balance = "";
		logger.info("----------------------start of FETS cash out Type "
				+ moneyTransfer.getRecieverNumber() + " ::: "
				+ moneyTransfer.getTransactionPin());
		AuthenticateResponse authenticationResponse = doAuthentication(
				moneyTransfer.getRecieverNumber(),
				moneyTransfer.getTransactionPin());
		if (authenticationResponse != null) {
			Wallet[] walletArray = authenticationResponse.getWallets();
			// authenticationResponse.getw
			logger.info("----------------------start of FETS cashout Type ");
			int count = 1;
			for (Wallet newWallet : walletArray) {
				logger.info("----------------------iterating through array of wallet : "
						+ count++);
				if (newWallet.getDefaultWallet()) {
					logger.info("----------------------Default wallet "
							+ newWallet.getId());
					newWallet.getAvailableBalance();
					moneyTransfer.setPayerWalletId(newWallet.getId());
					break;
				} else {
					logger.info("----------------------Wallet not default wallet");
				}
			}
			logger.info("----------------------Before setting the ServiceResponse parameters");

			logger.info("----------------------After setting acceptPayment attributes\n"
					+ moneyTransfer.toString());
			balance = String.valueOf(authenticationResponse
					.getWalletAvailableBalance());
		} else {
			logger.info("----------------------Authentication response is null");
		}
		return balance;

	}

	public static void printResponseDetails(
			ServiceResponse requestServiceResponse) {
		if (requestServiceResponse != null)
			logger.info("---------------------- p2PTransfer Response is not null \nMessage:: \nAccount number::"
					+ requestServiceResponse.getAccount_no()
					+ "\n---------------------- Account Name::"
					+ requestServiceResponse.getAccountName()
					+ "\n---------------------- Agent ID::"
					+ requestServiceResponse.getAgent_id()
					+ "\n---------------------- Agent Password::"
					+ requestServiceResponse.getAgent_password()
					+ "\n---------------------- Amount::"
					+ requestServiceResponse.getAmount()
					+ "\n---------------------- Bank Code::"
					+ requestServiceResponse.getBank_code()
					+ "\n---------------------- Beneficiary MSISDN::"
					+ requestServiceResponse.getBen_msisdn()
					+ "\n---------------------- Channel ID::"
					+ requestServiceResponse.getChannel_id()
					+ "\n---------------------- Confirm Password::"
					+ requestServiceResponse.getConfirm_password()
					+ "\n---------------------- Customer ID::"
					+ requestServiceResponse.getCustomer_id()
					+ "\n---------------------- Customer MSISDN::"
					+ requestServiceResponse.getCustomer_msisdn()
					+ "\n---------------------- Customer reference number::"
					+ requestServiceResponse.getCustomerRefNum()
					+ "\n---------------------- Destination MSISDN::"
					+ requestServiceResponse.getDestination_msisdn()
					+ "\n---------------------- ID::"
					+ requestServiceResponse.getId()
					+ "\n---------------------- Merchant ID::"
					+ requestServiceResponse.getMerchant_id()
					+ "\n---------------------- Message::"
					+ requestServiceResponse.getMessage()
					+ "\n---------------------- MSISDN::"
					+ requestServiceResponse.getMsisdn()
					+ "\n---------------------- Narration::"
					+ requestServiceResponse.getNaration()
					+ "\n---------------------- New Password::"
					+ requestServiceResponse.getNew_password()
					+ "\n---------------------- Old Password::"
					+ requestServiceResponse.getOld_password()
					+ "\n---------------------- Password::"
					+ requestServiceResponse.getPassword()
					+ "\n---------------------- Product ID::"
					+ requestServiceResponse.getProduct_id()
					+ "\n---------------------- Recipient MSISDN::"
					+ requestServiceResponse.getRecipient_msisdn()
					+ "\n---------------------- Redeem Code::"
					+ requestServiceResponse.getRedeemCode()
					+ "\n---------------------- Response Code::"
					+ requestServiceResponse.getResponseCode()
					+ "\n---------------------- Success::"
					+ requestServiceResponse.getSuccess()
					+ "\n---------------------- Transaction Reference Number::"
					+ requestServiceResponse.getTnxRefNo()
					+ "\n---------------------- Transfer Reference Number::"
					+ requestServiceResponse.getTranRefNum()
					+ "\n---------------------- Wallet ID::"
					+ requestServiceResponse.getWallet_id());
	}

	public static void printLoginResponseDetails(
			AuthenticateResponse requestServiceResponse) {
		if (requestServiceResponse != null)
			logger.info("---------------------- p2PTransfer Response is not null \nMessage:: \nAccount number::"
					+ requestServiceResponse.getAccount_no()
					+ "\n---------------------- Account Name::"
					+ requestServiceResponse.getAccountName()
					+ "\n---------------------- Active Status::"
					+ requestServiceResponse.getActiveStatus()
					+ "\n---------------------- Agent ID::"
					+ requestServiceResponse.getAgent_id()
					+ "\n---------------------- Agent Password::"
					+ requestServiceResponse.getAgent_password()
					+ "\n---------------------- Amount::"
					+ requestServiceResponse.getAmount()
					+ "\n---------------------- Bank Code::"
					+ requestServiceResponse.getBank_code()
					+ "\n---------------------- Beneficiary MSISDN::"
					+ requestServiceResponse.getBen_msisdn()
					+ "\n---------------------- Channel ID::"
					+ requestServiceResponse.getChannel_id()
					+ "\n---------------------- Confirm Password::"
					+ requestServiceResponse.getConfirm_password()
					+ "\n---------------------- Customer ID::"
					+ requestServiceResponse.getCustomer_id()
					+ "\n---------------------- Customer MSISDN::"
					+ requestServiceResponse.getCustomer_msisdn()
					+ "\n---------------------- Customer name::"
					+ requestServiceResponse.getCustomerName()
					+ "\n---------------------- Customer reference number::"
					+ requestServiceResponse.getCustomerRefNum()
					+ "\n---------------------- Customer type::"
					+ requestServiceResponse.getCustomerType()
					+ "\n---------------------- Destination MSISDN::"
					+ requestServiceResponse.getDestination_msisdn()
					+ "\n---------------------- First name::"
					+ requestServiceResponse.getFirstname()
					+ "\n---------------------- ID::"
					+ requestServiceResponse.getId()
					+ "\n---------------------- Last name::"
					+ requestServiceResponse.getLastname()
					+ "\n---------------------- Merchant ID::"
					+ requestServiceResponse.getMerchant_id()
					+ "\n---------------------- Message::"
					+ requestServiceResponse.getMessage()
					+ "\n---------------------- Middle name::"
					+ requestServiceResponse.getMiddlename()
					+ "\n---------------------- MSISDN::"
					+ requestServiceResponse.getMsisdn()
					+ "\n---------------------- Narration::"
					+ requestServiceResponse.getNaration()
					+ "\n---------------------- New Password::"
					+ requestServiceResponse.getNew_password()
					+ "\n---------------------- Old Password::"
					+ requestServiceResponse.getOld_password()
					+ "\n---------------------- Password::"
					+ requestServiceResponse.getPassword()
					+ "\n---------------------- Product ID::"
					+ requestServiceResponse.getProduct_id()
					+ "\n---------------------- Recipient MSISDN::"
					+ requestServiceResponse.getRecipient_msisdn()
					+ "\n---------------------- Redeem Code::"
					+ requestServiceResponse.getRedeemCode()
					+ "\n---------------------- Response Code::"
					+ requestServiceResponse.getResponseCode()
					+ "\n---------------------- Transaction REference number::"
					+ requestServiceResponse.getTnxRefNo()
					+ "\n---------------------- Transaction reference::"
					+ requestServiceResponse.getTranRefNum()
					+ "\n---------------------- Wallet ID::"
					+ requestServiceResponse.getWallet_id()
					+ "\n---------------------- Wallet available balance::"
					+ requestServiceResponse.getWalletAvailableBalance()
					+ "\n---------------------- Wallet ledger balance::"
					+ requestServiceResponse.getWalletLedgerBalance()
					+ "\n---------------------- Wallet name:::"
					+ requestServiceResponse.getWalletName()
					+ "\n---------------------- Success::"
					+ requestServiceResponse.getSuccess()
					+ "\n---------------------- Transaction Reference Number::"
					+ requestServiceResponse.getTnxRefNo()
					+ "\n---------------------- Transfer Reference Number::"
					+ requestServiceResponse.getTranRefNum());
	}

	public static AuthenticateResponse doAuthentication(String username,
			String password) {
		logger.info("---------------------Inside doAuthentication");
		Authenticate authenticate = new Authenticate(), authenticateResponse = new Authenticate();
		AuthenticateResponse authResponse = new AuthenticateResponse();
		authResponse.setMsisdn(username);
		authResponse.setPassword(password);
		authenticate.setAuthenticate(authResponse);
		logger.info("----------------------After instantiation Authenticate");
		// authenticate.setArg0(username);
		// authenticate.setArg1(password);
		logger.info("----------------------After setting parameters");
		AuthenticateResponse response = new AuthenticateResponse();
		logger.info("----------------------before calling fets stub");
		try {
			fetsStub = new FetsServiceStub();
			logger.info("----------------------After calling fets stub");
			authenticateResponse = fetsStub.authenticate(authenticate);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			logger.info("----------------------Remote Exception after calling fets");
			e.printStackTrace();
		}
		if (authenticateResponse != null) {
			logger.info("----------------------authenticateResponse is not null");
			response = authenticateResponse.getAuthenticate();
			// response.s
			printLoginResponseDetails(response);

		} else {
			logger.info("----------------------authenticateResponse is null");
		}
		return response;
	}

	public static ServiceResponse doCashOut(MoneyTransfer moneyTransfer) {
		ServiceResponse serviceResponse = new ServiceResponse(), cashoutRequestResp = new ServiceResponse();
		logger.info("----------------------start of FETS cash out Type "
				+ moneyTransfer.getRecieverNumber() + " ::: "
				+ moneyTransfer.getTransactionPin());
		AuthenticateResponse authenticationResponse = doAuthentication(
				moneyTransfer.getRecieverNumber(),
				moneyTransfer.getTransactionPin());
		if (authenticationResponse != null) {
			Wallet[] walletArray = authenticationResponse.getWallets();
			logger.info("----------------------start of FETS cashout Type ");
			int count = 1;
			for (Wallet newWallet : walletArray) {
				logger.info("----------------------iterating through array of wallet : "
						+ count++);
				if (newWallet.getDefaultWallet()) {
					logger.info("----------------------Default wallet "
							+ newWallet.getId());
					moneyTransfer.setPayerWalletId(newWallet.getId());
					break;
				} else {
					logger.info("----------------------Wallet not default wallet");
				}
			}
			logger.info("----------------------Before setting the ServiceResponse parameters");

			logger.info("----------------------After setting acceptPayment attributes\n"
					+ moneyTransfer.toString());
			CashOutRequest cashOutRequest = new CashOutRequest(), cashOutRequestResponse = new CashOutRequest();
			// requestServiceResponse.setAccount_no(moneyTransfer.getPayerNumber());
			// cashoutRequestResp.setAccountName(param);
			// cashoutRequestResp.setAgent_id(Long.valueOf(moneyTransfer
			// .getTransactionPin()));
			// cashoutRequestResp.setAgent_password(param);
			cashoutRequestResp.setAmount(moneyTransfer.getAmount());
			// cashoutRequestResp.setBank_code(param);
			// cashoutRequestResp.setBen_msisdn(param);
			cashoutRequestResp.setChannel_id(moneyTransfer.getChannelId());
			// cashoutRequestResp.setConfirm_password(param);
			// cashoutRequestResp.setCustomer_id(param);
			cashoutRequestResp.setCustomer_msisdn(moneyTransfer
					.getPayerNumber());
			// cashoutRequestResp.setCustomerRefNum();
			// cashoutRequestResp
			// .setDestination_msisdn(moneyTransfer.getRecieverNumber());
			// cashoutRequestResp.setId(param);
			// cashoutRequestResp.setMerchant_id(param);
			// cashoutRequestResp.setMessage(param);
			cashoutRequestResp.setMsisdn(moneyTransfer.getRecieverNumber());
			cashoutRequestResp.setNaration(moneyTransfer.getRemarks());
			// cashoutRequestResp.setNew_password(param);
			// cashoutRequestResp.setOld_password(param);
			cashoutRequestResp.setPassword(moneyTransfer.getTransactionPin());
			// cashoutRequestResp.setProduct_id(param);
			// cashoutRequestResp.setRecipient_msisdn(param);
			// cashoutRequestResp.setRedeemCode(param);
			// cashoutRequestResp.setResponseCode(param);
			// cashoutRequestResp.setSuccess(param);
			// cashoutRequestResp.setTnxRefNo(param);
			// cashoutRequestResp.setTranRefNum(param);
			// cashoutRequestResp.setWallet_id(moneyTransfer.getPayerWalletId());

			cashOutRequest.setCashOutRequest(cashoutRequestResp);

			try {
				fetsStub = new FetsServiceStub();
				logger.info("----------------------After calling fets stub");
				cashOutRequestResponse = fetsStub
						.cashOutRequest(cashOutRequest);
				// fetsStub.redeemP2UnregisteredTransfer(redeemP2UnregisteredTransfer)
				// fetsStub.
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				logger.info("----------------------Remote Exception after calling fets");
				e.printStackTrace();
			}
			if (cashOutRequestResponse != null) {
				logger.info("----------------------cashOutRequestResponse is not null");

				serviceResponse = cashOutRequestResponse.getCashOutRequest();
				if (serviceResponse != null) {
					printResponseDetails(serviceResponse);
				} else {
					logger.info("----------------------serviceResponse is null");
				}
			} else {
				logger.info("----------------------cashOutRequestResponse is null");
			}
		} else {
			logger.info("----------------------Authentication response is null");
		}
		return serviceResponse;
	}

	public static ServiceResponse walletToBank(MoneyTransfer moneyTransfer) {
		ServiceResponse serviceResponse = new ServiceResponse(), w2bRequestResp = new ServiceResponse();
		logger.info("----------------------start of FETS cash out Type "
				+ moneyTransfer.getPayerNumber() + " ::: "
				+ moneyTransfer.getTransactionPin());
		AuthenticateResponse authenticationResponse = doAuthentication(
				moneyTransfer.getPayerNumber(),
				moneyTransfer.getTransactionPin());
		if (authenticationResponse != null) {
			Wallet[] walletArray = authenticationResponse.getWallets();
			logger.info("----------------------start of FETS cashout Type ");
			int count = 1;
			for (Wallet newWallet : walletArray) {
				logger.info("----------------------iterating through array of wallet : "
						+ count++);
				if (newWallet.getDefaultWallet()) {
					logger.info("----------------------Default wallet "
							+ newWallet.getId());
					moneyTransfer.setPayerWalletId(newWallet.getId());
					break;
				} else {
					logger.info("----------------------Wallet not default wallet");
				}
			}
			logger.info("----------------------Before setting the ServiceResponse parameters");

			logger.info("----------------------After setting acceptPayment attributes\n"
					+ moneyTransfer.toString());
			P2BankTransfer p2BankTransfer = new P2BankTransfer(), p2BankTransferResponse = new P2BankTransfer();

			w2bRequestResp.setAccount_no(moneyTransfer.getAccountNumber());
			w2bRequestResp.setAccountName(moneyTransfer.getAccountName());
			// w2bRequestResp.setAgent_id(Long.valueOf(moneyTransfer
			// .getTransactionPin()));
			// w2bRequestResp.setAgent_password(param);
			w2bRequestResp.setAmount(moneyTransfer.getAmount());
			w2bRequestResp.setBank_code(moneyTransfer.getBankCode());
			// w2bRequestResp.setBen_msisdn(param);
			w2bRequestResp.setChannel_id(moneyTransfer.getChannelId());
			// w2bRequestResp.setConfirm_password(param);
			// w2bRequestResp.setCustomer_id(param);
			// w2bRequestResp.setCustomer_msisdn(moneyTransfer.getRecieverNumber());
			// w2bRequestResp.setCustomerRefNum();
			// w2bRequestResp
			// .setDestination_msisdn(moneyTransfer.getRecieverNumber());
			// w2bRequestResp.setId(param);
			// w2bRequestResp.setMerchant_id(param);
			// w2bRequestResp.setMessage(param);
			w2bRequestResp.setMsisdn(moneyTransfer.getPayerNumber());
			w2bRequestResp.setNaration(moneyTransfer.getRemarks());
			// w2bRequestResp.setNew_password(param);
			// w2bRequestResp.setOld_password(param);
			w2bRequestResp.setPassword(moneyTransfer.getTransactionPin());
			// w2bRequestResp.setProduct_id(param);
			// w2bRequestResp.setRecipient_msisdn(param);
			// w2bRequestResp.setRedeemCode(param);
			// w2bRequestResp.setResponseCode(param);
			// w2bRequestResp.setSuccess(param);
			// w2bRequestResp.setTnxRefNo(param);
			// w2bRequestResp.setTranRefNum(param);
			w2bRequestResp.setWallet_id(moneyTransfer.getPayerWalletId());

			p2BankTransfer.setP2BankTransfer(w2bRequestResp);

			try {
				fetsStub = new FetsServiceStub();
				logger.info("----------------------After calling fets stub");
				p2BankTransferResponse = fetsStub
						.p2BankTransfer(p2BankTransfer);
				// fetsStub.redeemP2UnregisteredTransfer(redeemP2UnregisteredTransfer)
				// fetsStub.
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				logger.info("----------------------Remote Exception after calling fets");
				e.printStackTrace();
			}
			if (p2BankTransferResponse != null) {
				logger.info("----------------------p2BankTransferResponse is not null");

				serviceResponse = p2BankTransferResponse.getP2BankTransfer();
				if (serviceResponse != null) {
					printResponseDetails(serviceResponse);
				} else {
					logger.info("----------------------serviceResponse is null");
				}
			} else {
				logger.info("----------------------p2BankTransferResponse is null");
			}
		} else {
			logger.info("----------------------Authentication response is null");
		}
		return serviceResponse;
	}

	public static void main(String[] args) {

		MoneyTransfer moneyTransfer = new FetsPropertyValues()
				.getPropertyValues();
		doCashOut(moneyTransfer);// logger.info("THE FINAL BALANCE IS>>>>>" +
									// getBalance(moneyTransfer));

		// logger.info("----------------------------Balance retrieved:::"
		// + getBalance(moneyTransfer));
		// doCashIn(moneyTransfer);
		// walletToBank(moneyTransfer);
	}

}
