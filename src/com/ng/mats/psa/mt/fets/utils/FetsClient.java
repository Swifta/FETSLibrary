package com.ng.mats.psa.mt.fets.utils;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.util.logging.Logger;

import org.apache.axis2.AxisFault;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.commons.httpclient.protocol.Protocol;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.fets.mm.soap.services.FetsServiceStub;
import com.fets.mm.soap.services.FetsServiceStub.Authenticate;
import com.fets.mm.soap.services.FetsServiceStub.AuthenticateResponse;
import com.fets.mm.soap.services.FetsServiceStub.CashOutRequest;
import com.fets.mm.soap.services.FetsServiceStub.P2BankTransfer;
import com.fets.mm.soap.services.FetsServiceStub.P2PTransfer;
import com.fets.mm.soap.services.FetsServiceStub.RedeemP2UnregisteredTransfer;
import com.fets.mm.soap.services.FetsServiceStub.ServiceResponse;
import com.fets.mm.soap.services.FetsServiceStub.Wallet;

/*import com.fets.mm.soap.services.test.FetsServiceStub;
 import com.fets.mm.soap.services.test.FetsServiceStub.Authenticate;
 import com.fets.mm.soap.services.test.FetsServiceStub.AuthenticateResponse;
 import com.fets.mm.soap.services.test.FetsServiceStub.CashOutRequest;
 import com.fets.mm.soap.services.test.FetsServiceStub.P2BankTransfer;
 import com.fets.mm.soap.services.test.FetsServiceStub.P2PTransfer;
 import com.fets.mm.soap.services.test.FetsServiceStub.RedeemP2UnregisteredTransfer;
 import com.fets.mm.soap.services.test.FetsServiceStub.ServiceResponse;
 import com.fets.mm.soap.services.test.FetsServiceStub.Wallet;
 */

public class FetsClient {
	private static FetsServiceStub fetsStub;
	private static final Logger logger = Logger.getLogger(FetsClient.class
			.getName());
	private String wso2appserverHome = "";

	public FetsClient(String parameterType) throws AxisFault {
		fetsStub = new FetsServiceStub();
		fetsStub._getServiceClient().getOptions().setManageSession(true);
		long timeOutInMilliSeconds = (5 * 36 * 1000);
		fetsStub._getServiceClient().getOptions()
				.setTimeOutInMilliSeconds(timeOutInMilliSeconds);
		if (System.getProperty("os.name").equals("Mac OS X")) {
			wso2appserverHome = "/Users/user/Documents/workspace/wso2esb-4.8.1";
		} else {
			wso2appserverHome = "/opt/mats/wso2esb-4.8.1";
		}
		if (parameterType.equalsIgnoreCase("production"))
			try {
				configureSecurity();
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	public static ServiceResponse doCashIn(MoneyTransfer moneyTransfer) {
		ServiceResponse serviceResponse = new ServiceResponse(), requestServiceResponse = new ServiceResponse();

		logger.info("----------------------start of FETS cash in Type "
				+ moneyTransfer.getRecieverNumber() + " ::: "
				+ moneyTransfer.getTransactionPin());
		AuthenticateResponse authenticationResponse = doAuthentication(
				moneyTransfer.getRecieverNumber(),
				moneyTransfer.getTransactionPin());
		if (authenticationResponse != null) {
			moneyTransfer = retrieveWallet(authenticationResponse,
					moneyTransfer);
			logger.info("----------------------Before setting the ServiceResponse parameters");
			P2PTransfer p2PTransfer = new P2PTransfer();
			requestServiceResponse.setAmount(moneyTransfer.getAmount());
			requestServiceResponse.setChannel_id(moneyTransfer.getChannelId());
			requestServiceResponse.setDestination_msisdn(moneyTransfer
					.getRecieverNumber());
			requestServiceResponse.setMsisdn(moneyTransfer.getPayerNumber());
			requestServiceResponse.setNaration(moneyTransfer.getRemarks());
			requestServiceResponse.setPassword(moneyTransfer
					.getTransactionPin());
			requestServiceResponse.setTnxRefNo(moneyTransfer
					.getBillerTransactionRef());
			requestServiceResponse.setTranRefNum(moneyTransfer
					.getBillerTransactionRef());
			requestServiceResponse.setWallet_id(moneyTransfer
					.getPayerWalletId());

			p2PTransfer.setP2PTransfer(requestServiceResponse);
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
			moneyTransfer = retrieveWallet(authenticationResponse,
					moneyTransfer);

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
			moneyTransfer = retrieveWallet(authenticationResponse,
					moneyTransfer);
			logger.info("----------------------Before setting the ServiceResponse parameters");

			logger.info("----------------------After setting acceptPayment attributes\n"
					+ moneyTransfer.toString());
			CashOutRequest cashOutRequest = new CashOutRequest(), cashOutRequestResponse = new CashOutRequest();
			cashoutRequestResp.setAmount(moneyTransfer.getAmount());
			cashoutRequestResp.setChannel_id(moneyTransfer.getChannelId());
			cashoutRequestResp.setCustomer_msisdn(moneyTransfer
					.getPayerNumber());
			cashoutRequestResp.setMsisdn(moneyTransfer.getRecieverNumber());
			cashoutRequestResp.setNaration(moneyTransfer.getRemarks());
			cashoutRequestResp.setPassword(moneyTransfer.getTransactionPin());

			cashOutRequest.setCashOutRequest(cashoutRequestResp);

			try {
				fetsStub = new FetsServiceStub();
				logger.info("----------------------After calling fets stub");
				cashOutRequestResponse = fetsStub
						.cashOutRequest(cashOutRequest);
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

	public static ServiceResponse doCashOutUnregistered(
			MoneyTransfer moneyTransfer) {
		ServiceResponse serviceResponse = new ServiceResponse(), redeemRequestResp = new ServiceResponse();
		logger.info("----------------------start of FETS cash out unregistered Type "
				+ moneyTransfer.getRecieverNumber()
				+ " ::: "
				+ moneyTransfer.getTransactionPin());
		AuthenticateResponse authenticationResponse = doAuthentication(
				moneyTransfer.getRecieverNumber(),
				moneyTransfer.getTransactionPin());
		if (authenticationResponse != null) {
			moneyTransfer = retrieveWallet(authenticationResponse,
					moneyTransfer);
			moneyTransfer.setAgentId(authenticationResponse.getCustomer_id());
			logger.info("----------------------Before setting the ServiceResponse parameters cash out unregistered");

			logger.info("----------------------After setting acceptPayment attributes\n"
					+ moneyTransfer.toString());

			// redeemRequestResp.setAmount(moneyTransfer.getAmount());
			redeemRequestResp.setChannel_id(moneyTransfer.getChannelId());
			redeemRequestResp.setRecipient_msisdn(moneyTransfer
					.getRecipientMsisdn());

			redeemRequestResp.setRedeemCode(moneyTransfer.getRedeemCode());
			redeemRequestResp.setTnxRefNo(moneyTransfer.getTxnRefNo());
			redeemRequestResp.setAgent_id(moneyTransfer.getAgentId());
			redeemRequestResp.setWallet_id(moneyTransfer.getPayerWalletId());
			redeemRequestResp.setAgent_password(moneyTransfer
					.getTransactionPin());

			RedeemP2UnregisteredTransfer redeemP2UnregisteredTransfer = new RedeemP2UnregisteredTransfer();
			redeemP2UnregisteredTransfer
					.setRedeemP2UnregisteredTransfer(redeemRequestResp);
			RedeemP2UnregisteredTransfer redeemP2UnregisteredTransferResponse = new RedeemP2UnregisteredTransfer();
			try {
				fetsStub = new FetsServiceStub();
				logger.info("----------------------After calling fets stub cash out unregistered");
				redeemP2UnregisteredTransferResponse = fetsStub
						.redeemP2UnregisteredTransfer(redeemP2UnregisteredTransfer);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				logger.info("----------------------Remote Exception after calling fets cash out unregistered");
				e.printStackTrace();
			}
			if (redeemP2UnregisteredTransferResponse != null) {
				logger.info("----------------------redeemP2UnregisteredTransferResponse is not null cash out unregistered");

				serviceResponse = redeemP2UnregisteredTransferResponse
						.getRedeemP2UnregisteredTransfer();
				if (serviceResponse != null) {
					printResponseDetails(serviceResponse);
				} else {
					logger.info("----------------------serviceResponse is null cash out unregistered");
				}
			} else {
				logger.info("----------------------redeemP2UnregisteredTransferResponse is null cash out unregistered");
			}
		} else {
			logger.info("----------------------Authentication response is null cash out unregistered");
		}
		return serviceResponse;
	}

	public static MoneyTransfer retrieveWallet(
			AuthenticateResponse authenticationResponse,
			MoneyTransfer moneyTransfer) {
		Wallet[] walletArray = authenticationResponse.getWallets();
		logger.info("----------------------start of FETS wallet iteration");
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
		return moneyTransfer;
	}

	public static ServiceResponse walletToBank(MoneyTransfer moneyTransfer) {
		ServiceResponse serviceResponse = new ServiceResponse(), w2bRequestResp = new ServiceResponse();
		logger.info("----------------------start of FETS cash out Type "
				+ moneyTransfer.getPayerNumber() + " ::: "
				+ moneyTransfer.getTransactionPin());
		AuthenticateResponse authenticationResponse = doAuthentication(
				moneyTransfer.getRecieverNumber(),
				moneyTransfer.getTransactionPin());
		if (authenticationResponse != null) {
			moneyTransfer = retrieveWallet(authenticationResponse,
					moneyTransfer);
			logger.info("----------------------Before setting the ServiceResponse parameters");

			logger.info("----------------------After setting acceptPayment attributes\n"
					+ moneyTransfer.toString());
			P2BankTransfer p2BankTransfer = new P2BankTransfer(), p2BankTransferResponse = new P2BankTransfer();

			w2bRequestResp.setAccount_no(moneyTransfer.getAccountNumber());
			w2bRequestResp.setAccountName(moneyTransfer.getAccountName());
			w2bRequestResp.setAmount(moneyTransfer.getAmount());
			w2bRequestResp.setBank_code(moneyTransfer.getBankCode());
			w2bRequestResp.setChannel_id(moneyTransfer.getChannelId());
			w2bRequestResp.setMsisdn(moneyTransfer.getPayerNumber());
			w2bRequestResp.setNaration(moneyTransfer.getRemarks());
			w2bRequestResp.setPassword(moneyTransfer.getTransactionPin());
			w2bRequestResp.setWallet_id(moneyTransfer.getPayerWalletId());

			p2BankTransfer.setP2BankTransfer(w2bRequestResp);

			try {
				fetsStub = new FetsServiceStub();
				logger.info("----------------------After calling fets stub");
				p2BankTransferResponse = fetsStub
						.p2BankTransfer(p2BankTransfer);
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

	public static Boolean verifyCashout(MoneyTransfer moneyTransfer) {
		logger.info("----------------------inside the verify cashout");
		Boolean success = null;
		AuthenticateResponse authenticationResponse = doAuthentication(
				moneyTransfer.getRecieverNumber(),
				moneyTransfer.getTransactionPin());
		logger.info("----------------------after AuthenticationResponse >>>>>");
		if (authenticationResponse != null) {
			logger.info("----------------------after AuthenticationResponse >>>>> NOT NULL");
			moneyTransfer = retrieveWallet(authenticationResponse,
					moneyTransfer);

			String parameters = "wallet_id=" + moneyTransfer.getPayerWalletId()
					+ "&reference=" + moneyTransfer.getReference();

			logger.info("----------------------Before setting the ServiceResponse parameters and the URL is::: "
					+ parameters);

			ConnectionTester conTester = new ConnectionTester();
			String responseFormat = conTester.connectToURL(
					moneyTransfer.getUrl(), parameters);
			JSONObject json = null;
			try {
				logger.info("----------------------converting response to json object>>"
						+ responseFormat);
				json = new JSONObject(responseFormat);
				// "redeemCode":null,"wallet_id":0,"channel_id":0,"merchant_id":0,"product_id":0,"agent_id":0,"amount":0.0}
				int responseCode = json.getInt("responseCode");
				success = json.getBoolean("success");
				String message = json.getString("message");
				String bankCode = json.getString("bank_code");
				String accountNumber = json.getString("account_no");
				String beneficiaryMSISDN = json.getString("ben_msisdn");
				String senderMSISDN = json.getString("msisdn");
				String password = json.getString("password");
				// String narration = json.getString("narration");
				String oldPassword = json.getString("old_password");
				String newPassword = json.getString("new_password");
				String confirmPassword = json.getString("confirm_password");
				String customerId = json.getString("customer_id");
				String accountName = json.getString("accountName");
				String customerMSISDN = json.getString("customer_msisdn");
				String destinationMSISDN = json.getString("destination_msisdn");
				String customerRefNum = json.getString("customerRefNum");
				String tranRefNum = json.getString("tranRefNum");
				String agentPassword = json.getString("agent_password");
				String recipientMSISDN = json.getString("recipient_msisdn");
				String tnxRefNo = json.getString("tnxRefNo");
				String redeemCode = json.getString("redeemCode");
				int walletId = json.getInt("wallet_id");
				int channelId = json.getInt("channel_id");
				int merchantId = json.getInt("merchant_id");
				int productId = json.getInt("product_id");
				int agentId = json.getInt("agent_id");
				String amount = json.getString("amount");

			} catch (JSONException e) {
				logger.info("------------------------There was a json exception. The response is not a valid JSON");
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			logger.info("----------------------after AuthenticationResponse >>>>> is NULL");
		}
		return success;
	}

	@SuppressWarnings("deprecation")
	public void configureSecurity() throws UnknownHostException, IOException {
		String clientSSLStore = wso2appserverHome + File.separator
				+ "repository" + File.separator + "resources" + File.separator
				+ "security" + File.separator + "client-truststore.jks";

		// wso2carbon.jks client-truststore.jks

		System.getProperties().remove("javax.net.ssl.trustStore");
		System.getProperties().remove("javax.net.ssl.trustStoreType");
		System.getProperties().remove("javax.net.ssl.trustStorePassword");

		System.setProperty("javax.net.ssl.trustStore", clientSSLStore);
		System.setProperty("javax.net.ssl.trustStoreType", "JKS");
		System.setProperty("javax.net.ssl.trustStorePassword", "wso2carbon");
		System.setProperty("jsse.enableSNIExtension", "false");
		System.setProperty("javax.net.debug", "ssl");
		System.setProperty("https.protocols", "SSLv3");
		System.setProperty("https.protocols", "TLSV");
		// java.lang.System.setProperty("jdk.tls.client.protocols",
		// "TLSv1,TLSv1.1,TLSv1.2");
		Protocol myProtocolHandler = new Protocol("https",
				new SSL3ProtocolSocketFactory(), 443);

		fetsStub._getServiceClient()
				.getOptions()
				.setProperty(HTTPConstants.CUSTOM_PROTOCOL_HANDLER,
						myProtocolHandler);

		// fundgateStub._getServiceClient().getOptions()
		// .setProperty(HTTPConstants.CHUNKED, "false");

	}

	public static void main(String[] args) throws AxisFault {

		MoneyTransfer moneyTransfer = new FetsPropertyValues()
				.getPropertyValues();
		// new FetsClient(moneyTransfer.getParameterType())
		// .verifyCashout(moneyTransfer);
		// new FetsClient(moneyTransfer.getParameterType())
		// .doCashOut(moneyTransfer);
		logger.info("THE FINAL BALANCE IS>>>>>"
				+ new FetsClient(moneyTransfer.getParameterType())
						.getBalance(moneyTransfer));
		// new FetsClient(moneyTransfer.getParameterType())
		// .doCashOutUnregistered(moneyTransfer);

		// logger.info("----------------------------Balance retrieved:::"
		// + getBalance(moneyTransfer));
		// new FetsClient(moneyTransfer.getParameterType())
		// .doCashIn(moneyTransfer);
		// new FetsClient(moneyTransfer.getParameterType())
		// .walletToBank(moneyTransfer);
	}
}
