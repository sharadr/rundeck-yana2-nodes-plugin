package com.dtolabs.rundeck.plugin.resources.yana2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.dtolabs.rundeck.plugin.resources.yana2.ssl.ProviderManager;
import com.dtolabs.rundeck.plugin.resources.yana2.ssl.TolerantSSLVerifier;

public class Yana2Nodes {

    public static void main(String[] args) throws Exception {
        Yana2Nodes conn = new Yana2Nodes();
        String url = "https://192.168.1.166:8443/";

        String xml = conn.getNodes("admin", "admin", url, "/node/list?format=xml");
        System.out.println(xml);
    }

    public String getNodes(String username, String password, String url, String queryString) throws ClientProtocolException, IOException {

        // TODO: Make this configurable.
        ProviderManager.installNaiveProvider();

        DefaultHttpClient httpclient = getTolerantClient();
        httpclient.getParams().setBooleanParameter(ClientPNames.HANDLE_REDIRECTS, true);

        try {

            HttpPost httpost = new HttpPost(url + "/j_spring_security_check");

            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(new BasicNameValuePair("j_username", username));
            nvps.add(new BasicNameValuePair("j_password", password));

            httpost.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));

            HttpResponse response = httpclient.execute(httpost);

            HttpEntity entity = response.getEntity();
            EntityUtils.consume(entity);

            HttpGet httpget = new HttpGet(url + queryString);

            // Create a response handler
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String responseBody = httpclient.execute(httpget, responseHandler);

            entity = response.getEntity();
            EntityUtils.consume(entity);

            if (responseBody.contains("DOCTYPE html")) {
                throw new IOException("Could not get node information from Yana. Check your URL and/or credentials");
            }

            return responseBody;

        } finally {
            // When HttpClient instance is no longer needed,
            // shut down the connection manager to ensure
            // immediate deallocation of all system resources
            httpclient.getConnectionManager().shutdown();
        }
    }

    // TODO : Make this configurable.
    @SuppressWarnings("deprecation")
    private DefaultHttpClient getTolerantClient() {
        DefaultHttpClient client = new DefaultHttpClient();
        SSLSocketFactory sslSocketFactory = (SSLSocketFactory) client.getConnectionManager().getSchemeRegistry().getScheme("https").getSchemeSocketFactory();
        final X509HostnameVerifier delegate = sslSocketFactory.getHostnameVerifier();
        if (!(delegate instanceof TolerantSSLVerifier)) {
            sslSocketFactory.setHostnameVerifier(new TolerantSSLVerifier(delegate));
        }
        return client;
    }

}
