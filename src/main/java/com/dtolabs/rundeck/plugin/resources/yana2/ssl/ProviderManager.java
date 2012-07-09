package com.dtolabs.rundeck.plugin.resources.yana2.ssl;

import java.security.Security;

import javax.net.ssl.HttpsURLConnection;

/**
 * Install an all trusting naive provider implementation. If already installed ,
 * do nothing!
 * 
 * @author RamachandranS
 */
public final class ProviderManager {

    private static boolean isNaiveProviderInstalled = false;

    public static void installNaiveProvider() {
        if (!isNaiveProviderInstalled) {
            Security.addProvider(new NaiveProvider());
            Security.setProperty("ssl.TrustManagerFactory.algorithm", "TrustAllCertificates");
            // System.out.println("    Installing Naive Provider to trust all SSL Certificates");
            HttpsURLConnection.setDefaultHostnameVerifier(NaiveProvider.hv);
            isNaiveProviderInstalled = true;
        }
    }

}
