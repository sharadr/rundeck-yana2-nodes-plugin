package com.dtolabs.rundeck.plugin.resources.yana2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.dtolabs.rundeck.core.common.Framework;
import com.dtolabs.rundeck.core.plugins.Plugin;
import com.dtolabs.rundeck.core.plugins.configuration.ConfigurationException;
import com.dtolabs.rundeck.core.plugins.configuration.Describable;
import com.dtolabs.rundeck.core.plugins.configuration.Description;
import com.dtolabs.rundeck.core.plugins.configuration.Property;
import com.dtolabs.rundeck.core.plugins.configuration.PropertyUtil;
import com.dtolabs.rundeck.core.resources.ResourceModelSource;
import com.dtolabs.rundeck.core.resources.ResourceModelSourceFactory;

@Plugin(name = "yana2", service = "ResourceModelSource")
public class Yana2ResourceModelSourceFactory implements ResourceModelSourceFactory, Describable {

    public static final String PROVIDER_NAME = "yana2";

    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String URL = "url";
    public static final String NODE_TYPE = "nodeType";
    public static final String QUERY_STRING = "queryString";

    @SuppressWarnings("unused")
    private Framework framework;

    private static List<Property> descriptionProperties = new ArrayList<Property>();

    static {
        descriptionProperties.add(PropertyUtil.string(USERNAME, "Username", "Yana Username", true, "admin"));
        descriptionProperties.add(PropertyUtil.string(PASSWORD, "Password", "Yana Password", true, "admin"));
        descriptionProperties.add(PropertyUtil.string(URL, "URL", "Yana URL", true, null));
        descriptionProperties.add(PropertyUtil.string(NODE_TYPE, "Node Type", "Node Type", true, null));
        descriptionProperties.add(PropertyUtil.string(QUERY_STRING, "Query String", "Query String", true, "/node/list?format=xml"));

    }

    public Yana2ResourceModelSourceFactory(final Framework framework) {
        this.framework = framework;
    }

    static Description DESC = new Description() {
        public String getName() {
            return PROVIDER_NAME;
        }

        public String getTitle() {
            return "Yana2 Resources";
        }

        public String getDescription() {
            return "Produces node information from Yana2";
        }

        public List<Property> getProperties() {
            return descriptionProperties;
        }

        public Map<String, String> getPropertiesMapping() {
            return null;
        }
    };

    public Description getDescription() {
        return DESC;
    }

    public ResourceModelSource createResourceModelSource(Properties properties) throws ConfigurationException {
        final Yana2ResourceModelSource yana2ResourceModelSource = new Yana2ResourceModelSource(properties);
        yana2ResourceModelSource.validate();
        return yana2ResourceModelSource;
    }

}
