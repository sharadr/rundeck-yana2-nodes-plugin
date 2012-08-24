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
        String url = "https://centos62-rundeck-tomcat:8443/yana2";

        String xml = conn.getNodes("admin", "admin", url, "/api/node/list/xml?project=demo");
        System.out.println(xml);
    }

    public String getNodes(String username, String password, String url, String queryString) throws ClientProtocolException, IOException {
        // TODO: Make this configurable.
        ProviderManager.installNaiveProvider();

        DefaultHttpClient httpClient = getTolerantClient();
        httpClient.getParams().setBooleanParameter(ClientPNames.HANDLE_REDIRECTS, true);

        try {
            /* Request the base URL from Yana2 to determine which authentication check has been configured: */
            HttpGet httpGet = new HttpGet(url);
            ResponseHandler<String> basicResponseHandler = new BasicResponseHandler();
            String responseBody = httpClient.execute(httpGet, basicResponseHandler);

            /* Check for the Tomcat container security check and post credentials: */
            String securityCheck = "";

            if (responseBody.contains("j_security_check"))
              securityCheck = "/j_security_check";
            else
              securityCheck = "/j_spring_security_check";

            HttpPost httpPost = new HttpPost(url + securityCheck);

            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(new BasicNameValuePair("j_username", username));
            nvps.add(new BasicNameValuePair("j_password", password));

            httpPost.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));

            HttpResponse httpResponse = httpClient.execute(httpPost);

            HttpEntity entity = httpResponse.getEntity();
            EntityUtils.consume(entity);

            /* Re-request the base URL from Yana2 to complete the required redirect: */
            httpGet = new HttpGet(url);
            ResponseHandler<String> reBasicResponseHandler = new BasicResponseHandler();
            String reResponseBody = httpClient.execute(httpGet, reBasicResponseHandler);

            /* Finally query the node list: */
            httpGet = new HttpGet(url + queryString);

            // Create a response handler
            ResponseHandler<String> queryBasicResponseHandler = new BasicResponseHandler();
            String queryResponseBody = httpClient.execute(httpGet, queryBasicResponseHandler);

            if (queryResponseBody.contains("<html>")) {
                throw new IOException("Could not get node information from Yana. Check your URL and/or credentials");
            }

            return queryResponseBody;

        } finally {
            // When HttpClient instance is no longer needed,
            // shut down the connection manager to ensure
            // immediate deallocation of all system resources
            httpClient.getConnectionManager().shutdown();
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
