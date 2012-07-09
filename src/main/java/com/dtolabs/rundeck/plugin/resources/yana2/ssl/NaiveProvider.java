package com.dtolabs.rundeck.plugin.resources.yana2.ssl;

import java.net.URL;
import java.net.URLConnection;
import java.security.KeyStore;
import java.security.Provider;
import java.security.Security;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.ManagerFactoryParameters;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactorySpi;
import javax.net.ssl.X509TrustManager;

/* The following code disables certificate checking.
 * Use the Security.addProvider and Security.setProperty
 * calls to enable it */
public class NaiveProvider extends Provider {
    /**
     * 
     */
    private static final long serialVersionUID = -8901812301408063754L;

    public NaiveProvider() {
        super("NaiveProvider", 1.0, "Trust certificates");
        put("TrustManagerFactory.TrustAllCertificates", NaiveTrustManagerFactory.class.getName());
    }

    protected static class NaiveTrustManagerFactory extends TrustManagerFactorySpi {
        public NaiveTrustManagerFactory() {
        }

        @Override
        protected void engineInit(KeyStore keystore) {
        }

        @Override
        protected void engineInit(ManagerFactoryParameters mgrparams) {
        }

        @Override
        protected TrustManager[] engineGetTrustManagers() {
            return new TrustManager[] { new NaiveX509TrustManager() };
        }
    }

    protected static class NaiveX509TrustManager implements X509TrustManager {
        public void checkClientTrusted(X509Certificate[] chain, String authType) {
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType) {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    }

    protected static HostnameVerifier hv = new HostnameVerifier() {
        public boolean verify(String urlHostName, SSLSession session) {
            System.out.println("Warning: URL Host: " + urlHostName + " vs. " + session.getPeerHost());
            return true;
        }
    };

    public static void main(String[] args) throws Exception {
        // Install the all-trusting trust manager
        Security.addProvider(new NaiveProvider());
        Security.setProperty("ssl.TrustManagerFactory.algorithm", "TrustAllCertificates");

        URL url = new URL("https://192.168.1.166:8443");
        HttpsURLConnection.setDefaultHostnameVerifier(hv);
        URLConnection con = url.openConnection();
        con.getInputStream();

    }
}