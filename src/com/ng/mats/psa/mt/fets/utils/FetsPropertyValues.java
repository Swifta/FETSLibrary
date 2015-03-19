package com.ng.mats.psa.mt.fets.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

public class FetsPropertyValues {
	private static final Logger logger = Logger
			.getLogger(FetsPropertyValues.class.getName());

	public MoneyTransfer getPropertyValues() {
		// for cashout, payernumber = customer and reciever number = agent:::::
		// for cashing, payernumber = agent and reciever number = customer

		MoneyTransfer moneyTransfer = new MoneyTransfer();
		Properties prop = new Properties();
		String propFileName = "com/ng/mats/psa/mt/fets/utils/config.properties";

		InputStream inputStream = getClass().getClassLoader()
				.getResourceAsStream(propFileName);
		try {
			if (inputStream != null) {

				prop.load(inputStream);

			} else {
				throw new FileNotFoundException("property file '"
						+ propFileName + "' not found in the classpath");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// get the property value and print it out
		String parameterType = prop.getProperty("settings-type");
		logger.info("THE CONFIGURATION BEING USED AT THIS POINT IS ==========================="
				+ parameterType);

		moneyTransfer.setAmount(Double.valueOf(prop.getProperty("Amount_"
				+ parameterType)));
		moneyTransfer.setBillerMerchantId(Long.valueOf(prop
				.getProperty("BillerMerchantId_" + parameterType)));
		moneyTransfer.setBillerProductId(Long.valueOf(prop
				.getProperty("BillerProductId_" + parameterType)));
		moneyTransfer.setBillerTransactionRef(prop
				.getProperty("BillerTransactionRef_" + parameterType));
		moneyTransfer.setChannelId(Long.valueOf(prop.getProperty("ChannelId_"
				+ parameterType)));
		moneyTransfer.setCharge(Long.valueOf(prop.getProperty("ChannelId_"
				+ parameterType)));
		moneyTransfer.setPayerNumber(prop.getProperty("CustomerNumber_"
				+ parameterType));
		// moneyTransfer.setPayerNumber(prop.getProperty("AgentNumber_"+parameterType));
		// new agent detail 2347080306482
		// agent default 2348062239531
		// customer default 2348063005168
		// moneyTransfer.setPayerWalletId(3085);
		// moneyTransfer.setRecieverNumber(prop.getProperty("CustomerNumber_"+parameterType));
		moneyTransfer.setRecieverNumber(prop.getProperty("AgentNumber_"
				+ parameterType));
		moneyTransfer.setRemarks(prop.getProperty("Remarks_" + parameterType));
		moneyTransfer.setTransactionId(prop.getProperty("TransactionId_"
				+ parameterType));
		moneyTransfer.setReference(prop.getProperty("Reference_"
				+ parameterType));
		logger.info("=======THE PROPERTY OF URL FROM CONFIG IS========="
				+ prop.getProperty("Url_" + parameterType));
		moneyTransfer.setUrl(prop.getProperty("Url_" + parameterType));
		// old agent pin::::moneyTransfer.setTransactionPin("5678");
		moneyTransfer.setTransactionPin(prop.getProperty("TransactionPin_"
				+ parameterType));
		moneyTransfer.setAccountName(prop.getProperty("AccountName_"
				+ parameterType));
		moneyTransfer.setAccountNumber(prop.getProperty("AccountNumber_"
				+ parameterType));
		moneyTransfer
				.setBankCode(prop.getProperty("BankCode_" + parameterType));

		return moneyTransfer;
	}

}
