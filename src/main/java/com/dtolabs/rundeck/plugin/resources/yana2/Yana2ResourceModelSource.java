package com.dtolabs.rundeck.plugin.resources.yana2;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.dtolabs.rundeck.core.common.INodeSet;
import com.dtolabs.rundeck.core.plugins.configuration.ConfigurationException;
import com.dtolabs.rundeck.core.resources.ResourceModelSource;
import com.dtolabs.rundeck.core.resources.ResourceModelSourceException;

/**
 * Yana2ResourceModelSource produces nodes by querying the Yana2 nodes to list instances.
 * 
 * @author sharad
 * 
 */
public class Yana2ResourceModelSource implements ResourceModelSource {

    static Logger logger = Logger.getLogger(Yana2ResourceModelSource.class);
    private String userName;
    private String password;
    private String url;
    private String nodeType;

    INodeSet iNodeSet;
    NodeMapper mapper;

    public Yana2ResourceModelSource(final Properties configuration) {
        this.userName = configuration.getProperty(Yana2ResourceModelSourceFactory.USERNAME);
        this.password = configuration.getProperty(Yana2ResourceModelSourceFactory.PASSWORD);
        this.url = configuration.getProperty(Yana2ResourceModelSourceFactory.URL);
        this.nodeType = configuration.getProperty(Yana2ResourceModelSourceFactory.NODE_TYPE);
        initialize();

    }

    public INodeSet getNodes() throws ResourceModelSourceException {
        iNodeSet = mapper.performQuery();
        return iNodeSet;
    }

    private void initialize() {
        mapper = new NodeMapper();
        mapper.setUsername(userName);
        mapper.setPassword(password);
        mapper.setURL(url);
        mapper.setNodeType(nodeType);
    }

    public void validate() throws ConfigurationException {
        if (null == userName) {
            throw new ConfigurationException("userName is required");
        }
        if (null == password) {
            throw new ConfigurationException("password is required");
        }

        if (null == url) {
            throw new ConfigurationException("url is required");
        }

        if (null == nodeType) {
            throw new ConfigurationException("nodeType is required");
        }

    }

}
