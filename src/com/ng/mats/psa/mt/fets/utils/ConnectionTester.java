package com.ng.mats.psa.mt.fets.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;

import javax.net.ssl.HttpsURLConnection;

public class ConnectionTester {
	private static final Logger logger = Logger
			.getLogger(ConnectionTester.class.getName());

	public static void main(String[] args) {
		String urlPage = "https://fetspay.fetswallet.com/rest/viewTransaction";
		String parameters = "wallet_id=779321&reference=402578V";
		// String webPage =
		// "http://testpay1.fetswallet.com/vidicon/rest/viewTransaction/-1149/310243V";
		MoneyTransfer moneyTransfer = new MoneyTransfer();
		moneyTransfer.setParameterType("production");
		moneyTransfer
				.setTrustStoreLocation("/Users/user/Documents/workspace/wso2esb-4.8.1/repository/resources/security/client-truststore.jks");
		moneyTransfer.setTrustStorePassword("wso2carbon");
		logger.info("------------------------"
				+ connectToURL(urlPage, parameters, moneyTransfer));

	}

	public static String connectToURL(String urlPage, String parameters,
			MoneyTransfer moneyTransfer) {
		logger.info("The web page>>>>>>>>>" + urlPage);
		String line = null;
		if (moneyTransfer.getParameterType().equalsIgnoreCase("test"))
			try {
				URL url = new URL(urlPage);

				HttpURLConnection connection = (HttpURLConnection) url
						.openConnection();
				connection.setRequestMethod("POST");
				connection.setDoOutput(true);
				OutputStreamWriter writer = new OutputStreamWriter(
						connection.getOutputStream());

				writer.write(parameters);
				writer.flush();

				BufferedReader reader = new BufferedReader(
						new InputStreamReader(connection.getInputStream()));

				// while ((line = in.readLine()) != null) {
				// System.out.println(line);
				// }
				line = reader.readLine();
				writer.close();
				reader.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
		else {
			try {
				URL url = new URL(urlPage);

				HttpsURLConnection connection = (HttpsURLConnection) url
						.openConnection();
				connection.setRequestMethod("POST");
				logger.info("-----------------------just before initiating socket factory connection");
				// connection.setSSLSocketFactory(getFactory(
				// moneyTransfer.getTrustStoreLocation(),
				// moneyTransfer.getTrustStorePassword()));
				connection.setDoOutput(true);
				OutputStreamWriter writer = new OutputStreamWriter(
						connection.getOutputStream());

				writer.write(parameters);
				writer.flush();

				BufferedReader reader = new BufferedReader(
						new InputStreamReader(connection.getInputStream()));

				// while ((line = in.readLine()) != null) {
				// System.out.println(line);
				// }
				line = reader.readLine();
				writer.close();
				reader.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return line;
	}
	/*
	 * private static SSLSocketFactory getFactory(String pKeyFilePath, String
	 * pKeyPassword) { SSLSocketFactory socketFactory = null;
	 * logger.info("------------------------Before creating the File"); try {
	 * File pKeyFile = new File(pKeyFilePath);
	 * logger.info("-------------------------After locating the jks");
	 * KeyManagerFactory keyManagerFactory = KeyManagerFactory
	 * .getInstance("SunX509"); KeyStore keyStore = KeyStore.getInstance("JKS");
	 * 
	 * InputStream keyInput = new FileInputStream(pKeyFile);
	 * keyStore.load(keyInput, pKeyPassword.toCharArray()); keyInput.close();
	 * 
	 * keyManagerFactory.init(keyStore, pKeyPassword.toCharArray());
	 * 
	 * SSLContext context = SSLContext.getInstance("SSL");
	 * context.init(keyManagerFactory.getKeyManagers(), null, new
	 * SecureRandom()); socketFactory = context.getSocketFactory(); ; } catch
	 * (FileNotFoundException e) { e.printStackTrace(); } catch
	 * (NoSuchAlgorithmException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } catch (KeyStoreException e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); } catch
	 * (CertificateException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } catch (IOException e) { // TODO Auto-generated
	 * catch block e.printStackTrace(); } catch (UnrecoverableKeyException e) {
	 * // TODO Auto-generated catch block e.printStackTrace(); } catch
	 * (KeyManagementException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } return socketFactory; }
	 */
}
