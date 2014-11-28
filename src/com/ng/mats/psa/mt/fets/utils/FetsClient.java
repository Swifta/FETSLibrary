package com.ng.mats.psa.mt.fets.utils;

import java.rmi.RemoteException;
import java.util.logging.Logger;

import com.fets.mm.soap.services.FetsServicesServiceStub;
import com.fets.mm.soap.services.FetsServicesServiceStub.AcceptPayment;
import com.fets.mm.soap.services.FetsServicesServiceStub.AcceptPaymentResponse;
import com.fets.mm.soap.services.FetsServicesServiceStub.P2PTransfer;
import com.fets.mm.soap.services.FetsServicesServiceStub.P2PTransferResponse;
import com.fets.mm.soap.services.FetsServicesServiceStub.ServiceResponse;

public class FetsClient {
	private static FetsServicesServiceStub fetsStub;
	private static final Logger logger = Logger.getLogger(FetsClient.class
			.getName());

	public static ServiceResponse doCashOut(MoneyTransfer moneyTransfer) {
		ServiceResponse serviceResponse = new ServiceResponse();

		logger.info("----------------------start of FETS cashout Type ");
		P2PTransfer p2PTransfer = new P2PTransfer();
		p2PTransfer.setArg0(moneyTransfer.getPayerNumber());
		p2PTransfer.setArg1(moneyTransfer.getPayerWalletId());
		p2PTransfer.setArg2(moneyTransfer.getTransactionPin());
		p2PTransfer.setArg3(moneyTransfer.getChannelId());
		p2PTransfer.setArg4(moneyTransfer.getAmount());
		p2PTransfer.setArg5(String.valueOf(moneyTransfer.getCharge()));
		p2PTransfer.setArg6(moneyTransfer.getRemarks());

		logger.info("----------------------After setting p2ptransfer attributes\n"
				+ moneyTransfer.toString());
		P2PTransferResponse p2PTransferResponse = new P2PTransferResponse();
		try {
			fetsStub = new FetsServicesServiceStub();
			logger.info("----------------------After calling fets stub");
			p2PTransferResponse = fetsStub.p2PTransfer(p2PTransfer);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			logger.info("----------------------Remote Exception after calling fets");
			e.printStackTrace();
		}
		if (p2PTransferResponse != null) {
			logger.info("----------------------p2PTransferResponse is not null");

			serviceResponse = p2PTransferResponse.get_return();
			if (serviceResponse != null) {
				logger.info("---------------------- serviceResponse is not null \nMessage::"
						+ serviceResponse.getMessage()
						+ "\nResponse Code::"
						+ serviceResponse.getResponseCode()
						+ "\nSuccess / Failure::"
						+ serviceResponse.getSuccess());
			} else {
				logger.info("----------------------serviceResponse is null");
			}
		} else {
			logger.info("----------------------p2PTransferResponse is null");
		}
		return serviceResponse;
	}

	public static ServiceResponse doCashIn(MoneyTransfer moneyTransfer) {
		ServiceResponse serviceResponse = new ServiceResponse();

		logger.info("----------------------start of FETS cashin Type ");
		AcceptPayment acceptPayment = new AcceptPayment();
		acceptPayment.setArg0(moneyTransfer.getPayerNumber());
		acceptPayment.setArg1(moneyTransfer.getTransactionPin());
		acceptPayment.setArg2(moneyTransfer.getPayerWalletId());
		acceptPayment.setArg3(moneyTransfer.getChannelId());
		acceptPayment.setArg4(moneyTransfer.getAmount());
		acceptPayment.setArg5(moneyTransfer.getBillerMerchantId());
		acceptPayment.setArg6(moneyTransfer.getBillerProductId());
		acceptPayment.setArg7(moneyTransfer.getBillerTransactionRef());
		acceptPayment.setArg8(moneyTransfer.getRemarks());

		logger.info("----------------------After setting acceptPayment attributes\n"
				+ moneyTransfer.toString());
		AcceptPaymentResponse acceptPaymentResponse = new AcceptPaymentResponse();
		try {
			fetsStub = new FetsServicesServiceStub();
			logger.info("----------------------After calling fets stub");
			acceptPaymentResponse = fetsStub.acceptPayment(acceptPayment);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			logger.info("----------------------Remote Exception after calling fets");
			e.printStackTrace();
		}
		if (acceptPaymentResponse != null) {
			logger.info("----------------------acceptPaymentResponse is not null");

			serviceResponse = acceptPaymentResponse.get_return();
			if (serviceResponse != null) {
				logger.info("---------------------- serviceResponse is not null \nMessage::"
						+ serviceResponse.getMessage()
						+ "\nResponse Code::"
						+ serviceResponse.getResponseCode()
						+ "\nSuccess / Failure::"
						+ serviceResponse.getSuccess());
			} else {
				logger.info("----------------------serviceResponse is null");
			}
		} else {
			logger.info("----------------------acceptPaymentResponse is null");
		}
		return serviceResponse;
	}

	public static void main(String[] args) {
		MoneyTransfer moneyTransfer = new MoneyTransfer();
		moneyTransfer.setAmount(10);
		moneyTransfer.setBillerMerchantId(12345);
		moneyTransfer.setBillerProductId(1234);
		moneyTransfer.setBillerTransactionRef("0987654");
		moneyTransfer.setChannelId(1);
		moneyTransfer.setCharge(5);
		moneyTransfer.setPayerNumber("08063305711");
		moneyTransfer.setPayerWalletId(5544);
		moneyTransfer.setRecieverNumber("08092041723");
		moneyTransfer.setRemarks("Send to kachi");
		moneyTransfer.setTransactionId("982379479032847");
		moneyTransfer.setTransactionPin("0000");

		// doCashOut(moneyTransfer);
		doCashIn(moneyTransfer);
	}
}
