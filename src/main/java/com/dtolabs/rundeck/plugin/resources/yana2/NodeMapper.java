package com.dtolabs.rundeck.plugin.resources.yana2;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import com.dtolabs.rundeck.core.common.INodeSet;
import com.dtolabs.rundeck.core.common.NodeFileParserException;
import com.dtolabs.rundeck.core.common.NodeSetImpl;
import com.dtolabs.rundeck.core.common.NodesXMLParser;

/**
 * Map node information from xml provided by Yana2 
 * @author sharad
 *
 */
public class NodeMapper {

    private String userName;
    private String password;
    private String url;
    private String nodeType;

    public void setUsername(String userName) {
        this.userName = userName;

    }

    public void setPassword(String password) {
        this.password = password;

    }

    public void setURL(String url) {
        this.url = url;

    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    /**
     * Perform the query and return the set of nodes
     * 
     */
    public INodeSet performQuery() {

        final NodeSetImpl nodeSet = new NodeSetImpl();

        InputStream in = null;
        try {
            in = new ByteArrayInputStream(new RundeckXMLTransform().parse(userName, password, url, nodeType).toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        }

        NodesXMLParser parser = new NodesXMLParser(in, nodeSet);
        try {
            parser.parse();
        } catch (NodeFileParserException e) {
            e.printStackTrace();
        }
        return nodeSet;
    }

    public static void main(String[] args) {
        NodeMapper mapper = new NodeMapper();
        mapper.setNodeType("VM");
        mapper.setUsername("admin");
        mapper.setPassword("admin");
        mapper.setURL("https://192.168.1.166:8443");
        System.out.println(mapper.performQuery());

    }

}
