package com.dtolabs.rundeck.plugin.resources.yana2;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.http.client.ClientProtocolException;

/**
 * Extract selective node information from XML format of nodes provided by Yana2
 * 
 * @author sharad
 * 
 */
public class RundeckXMLTransform {

    public ByteArrayOutputStream parse(String userName, String password, String url, String nodeType, String queryString) throws ClientProtocolException,
            IOException, TransformerException {

        InputStream in = new ByteArrayInputStream(new Yana2Nodes().getNodes(userName, password, url, queryString).getBytes());

        InputStream styleSheetStream = this.getClass().getClassLoader().getResourceAsStream("rundeck.xsl");

        TransformerFactory tFactory = TransformerFactory.newInstance();

        Transformer transformer = tFactory.newTransformer(new StreamSource(styleSheetStream));
        transformer.setParameter("nodeType", nodeType);      
        transformer.setParameter("url", url);

        StreamSource xmlSource = new StreamSource(in);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        transformer.transform(xmlSource, new StreamResult(baos));

        in.close();
        styleSheetStream.close();

        return baos;

    }

    public static void main(String[] args) throws Exception {

        RundeckXMLTransform parse = new RundeckXMLTransform();
        ByteArrayOutputStream baos = parse.parse("admin", "admin", "https://192.168.1.166:8443", "VM", "/node/list?format=xml");
        System.out.println(baos.toString());
    }

}
