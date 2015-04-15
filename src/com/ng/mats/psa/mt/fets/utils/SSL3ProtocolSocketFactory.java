package com.ng.mats.psa.mt.fets.utils;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;

import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;

import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.httpclient.protocol.SecureProtocolSocketFactory;

public class SSL3ProtocolSocketFactory implements SecureProtocolSocketFactory {

	private SSLContext sslcontext = null;

	public SSL3ProtocolSocketFactory() {
		super();

	}

	private static SSLContext createContext() {
		try {
			SSLContext context = SSLContext.getInstance("SSLv3");
			context.init(null, null, null);
			return context;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private SSLContext getSSLContext() {
		if (this.sslcontext == null) {
			this.sslcontext = createContext();
			return this.sslcontext;
		} else
			return this.sslcontext;
	}

	public Socket createSocket(String host, int port) throws IOException,
			UnknownHostException {
		SSLSocket socket = (SSLSocket) getSSLContext().getSocketFactory()
				.createSocket(host, port);
		socket.setEnabledProtocols(new String[] { "SSLv3" });
		return socket;
	}

	public Socket createSocket(String host, int port, InetAddress clientHost,
			int clientPort) throws IOException, UnknownHostException {
		SSLSocket socket = (SSLSocket) getSSLContext().getSocketFactory()
				.createSocket(host, port, clientHost, clientPort);
		socket.setEnabledProtocols(new String[] { "SSLv3" });
		return socket;
	}

	public Socket createSocket(Socket s, String host, int port,
			boolean autoClose) throws IOException, UnknownHostException {

		SSLSocket socket = (SSLSocket) getSSLContext().getSocketFactory()
				.createSocket(s, host, port, autoClose);

		socket.setEnabledProtocols(new String[] { "SSLv3" });
		return socket;
	}

	@SuppressWarnings({ "unused" })
	public Socket createSocket(String host, int port, InetAddress localAddress,
			int localPort, HttpConnectionParams params) throws IOException,
			UnknownHostException, ConnectTimeoutException {
		if (params == null) {
			throw new IllegalArgumentException("Parameters may not be null");
		}
		int timeout = params.getConnectionTimeout();
		SocketFactory socketfactory = getSSLContext().getSocketFactory();
		if (timeout == 0) {
			return socketfactory.createSocket(host, port, localAddress,
					localPort);
		} else {
			SSLSocket socket = (SSLSocket) getSSLContext().getSocketFactory()
					.createSocket(host, port, localAddress, localPort);
			SocketAddress localaddr = new InetSocketAddress(localAddress,
					localPort);
			SocketAddress remoteaddr = new InetSocketAddress(host, port);
			// uncommetnig those lines below makes the application throwing
			// exceptions
			// surprisingly, without invoking bind and connect everything seems
			// to work just fine
			// socket.bind(localaddr);
			// socket.connect(remoteaddr, timeout);
			socket.setEnabledProtocols(new String[] { "SSLv3" });
			return socket;
		}
	}

}
