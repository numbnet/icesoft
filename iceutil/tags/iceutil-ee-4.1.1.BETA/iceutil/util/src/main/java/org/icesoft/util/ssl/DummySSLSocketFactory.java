/*
 * Copyright 2004-2016 ICEsoft Technologies Canada Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.icesoft.util.ssl;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class DummySSLSocketFactory
extends SSLSocketFactory {
    private static final Logger LOGGER = Logger.getLogger(DummySSLSocketFactory.class.getName());

    private static final class Constant {
        private static final String PROTOCOL = "TLS";
    }

    private final SSLSocketFactory sslSocketFactory;

    public DummySSLSocketFactory() {
        try {
            SSLContext _sslContext = SSLContext.getInstance(Constant.PROTOCOL);
            _sslContext.init(
                null,
                new TrustManager[] {
                    new X509TrustManager() {
                        public void checkClientTrusted(final X509Certificate[] chain, final String authType)
                        throws CertificateException {
                            // Trust everything.
                        }

                        public void checkServerTrusted(final X509Certificate[] chain, final String authType)
                        throws CertificateException {
                            // Trust everything.
                        }

                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[0];
                        }
                    }
                },
                null
            );
            sslSocketFactory = _sslContext.getSocketFactory();
        } catch (final KeyManagementException exception) {
            throw new RuntimeException(exception);
        } catch (final NoSuchAlgorithmException exception) {
            throw new RuntimeException(exception);
        }
    }

    public static SocketFactory getDefault() {
        return new DummySSLSocketFactory();
    }

    public Socket createSocket()
    throws IOException {
        return getSSLSocketFactory().createSocket();
    }

    public Socket createSocket(
        final InetAddress host, final int port)
    throws IllegalArgumentException, IOException, NullPointerException, SecurityException {
        return getSSLSocketFactory().createSocket(host, port);
    }

    public Socket createSocket(
        final InetAddress address, final int port, final InetAddress localAddress, final int localPort)
    throws IllegalArgumentException, IOException, NullPointerException, SecurityException {
        return getSSLSocketFactory().createSocket(address, port, localAddress, localPort);
    }

    public Socket createSocket(
        final Socket socket, final String host, final int port, final boolean autoClose)
    throws IOException, NullPointerException {
        return getSSLSocketFactory().createSocket(socket, host, port, autoClose);
    }

    public Socket createSocket(
        final String host, final int port)
    throws IllegalArgumentException, IOException, UnknownHostException, SecurityException {
        return getSSLSocketFactory().createSocket(host, port);
    }

    public Socket createSocket(
        final String host, final int port, final InetAddress localHost, final int localPort)
    throws IllegalArgumentException, IOException, UnknownHostException, SecurityException {
        return getSSLSocketFactory().createSocket(host, port, localHost, localPort);
    }

    public String[] getDefaultCipherSuites() {
        return getSSLSocketFactory().getDefaultCipherSuites();
    }

    public String[] getSupportedCipherSuites() {
        return getSSLSocketFactory().getSupportedCipherSuites();
    }

    protected SSLSocketFactory getSSLSocketFactory() {
        return sslSocketFactory;
    }
}
