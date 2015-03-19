package com.ng.mats.psa.mt.fets.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class ConnectionTester {
	public static void main(String[] args) {
		String urlPage = "http://testpay1.fetswallet.com/vidicon/rest/viewTransaction";
		String parameters = "wallet_id=8&reference=123456789";
		// String webPage =
		// "http://testpay1.fetswallet.com/vidicon/rest/viewTransaction/-1149/310243V";
		System.out.println("------------------------"
				+ connectToURL(urlPage, parameters));

	}

	public static String connectToURL(String urlPage, String parameters) {
		System.out.println("The web page>>>>>>>>>" + urlPage);
		String line = null;
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

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));

			// while ((line = in.readLine()) != null) {
			// System.out.println(line);
			// }
			line = reader.readLine();
			writer.close();
			reader.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return line;
	}

}
