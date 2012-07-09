package com.dtolabs.rundeck.plugin.resources.yana2.ssl;

import javax.net.ssl.SSLException;

import org.apache.http.conn.ssl.AbstractVerifier;
import org.apache.http.conn.ssl.X509HostnameVerifier;

/**
 * Purpose of this class is to accept all SSL connections. This should be configurable and turned off in production.
 * @author sharad
 *
 */
public class TolerantSSLVerifier extends AbstractVerifier {

    private final X509HostnameVerifier delegate;

    public TolerantSSLVerifier(final X509HostnameVerifier delegate) {
        this.delegate = delegate;
    }

    public void verify(String host, String[] cns, String[] subjectAlts) throws SSLException {
        /*
         * Tolerate everything! 
         * boolean ok = false; try { delegate.verify(host,
         * cns, subjectAlts); } catch (SSLException e) { for (String cn : cns) {
         * if (cn.startsWith("*.")) { try { delegate.verify(host, new String[] {
         * cn.substring(2) }, subjectAlts); ok = true; } catch (Exception e1) {
         * } } } if (!ok) throw e; }
         */
    }
}
