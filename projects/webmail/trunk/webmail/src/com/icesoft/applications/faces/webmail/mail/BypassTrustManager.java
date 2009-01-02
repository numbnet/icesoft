package com.icesoft.applications.faces.webmail.mail;

import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;

public class BypassTrustManager implements X509TrustManager {
	public void checkClientTrusted(X509Certificate[] cert, String prm) {
	}

	public void checkServerTrusted(X509Certificate[] cert, String prm) {
	}

	public X509Certificate[] getAcceptedIssuers() {
		return new X509Certificate[0];
	}
}
