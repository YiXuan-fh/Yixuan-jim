package org.jim.server.protocol;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author WChao
 * @date 2018年3月9日 上午1:06:33
 */
public class ProtocolHandlerConfigurationFactory {
	
    private static final Logger LOG = LoggerFactory.getLogger(ProtocolHandlerConfigurationFactory.class.getName());

    private static final String DEFAULT_CLASSPATH_CONFIGURATION_FILE = "protocol_handler.properties";
    
    /**
     * Constructor.
     */
    private ProtocolHandlerConfigurationFactory() {

    }

    /**
     * Configures a bean from an property file.
     */
    public static List<ProtocolHandlerConfiguration> parseConfiguration(final File file) throws Exception {
        if (file == null) {
            throw new Exception("Attempt to configure server_handler from null file.");
        }
        LOG.debug("Configuring server_handler from file: {}", file);
        List<ProtocolHandlerConfiguration> configurations  = null;
        InputStream input = null;
        try {
            input = new BufferedInputStream(new FileInputStream(file));
            configurations = parseConfiguration(input);
        } catch (Exception e) {
            throw new Exception("Error configuring from " + file + ". Initial cause was " + e.getMessage(), e);
        } finally {
            try {
                if (input != null) {
                    input.close();
                }
            } catch (IOException e) {
                LOG.error("IOException while closing configuration input stream. Error was " + e.getMessage());
            }
        }
        return configurations;
    }
    /**
     * Configures a bean from an property file available as an URL.
     */
    public static List<ProtocolHandlerConfiguration> parseConfiguration(final URL url) throws Exception {
        LOG.debug("Configuring server_handler from URL: {}", url);
        List<ProtocolHandlerConfiguration> configurations;
        InputStream input = null;
        try {
            input = url.openStream();
            configurations = parseConfiguration(input);
        } catch (Exception e) {
            throw new Exception("Error configuring from " + url + ". Initial cause was " + e.getMessage(), e);
        } finally {
            try {
                if (input != null) {
                    input.close();
                }
            } catch (IOException e) {
                LOG.error("IOException while closing configuration input stream. Error was " + e.getMessage());
            }
        }
        return configurations;
    }
    /**
     * Configures a bean from an property file in the classpath.
     */
    public static List<ProtocolHandlerConfiguration> parseConfiguration() throws Exception {
        ClassLoader standardClassloader = Thread.currentThread().getContextClassLoader();
        URL url = null;
        if (standardClassloader != null) {
            url = standardClassloader.getResource(DEFAULT_CLASSPATH_CONFIGURATION_FILE);
        }
        if (url == null) {
        	url = ProtocolHandlerConfigurationFactory.class.getResource(DEFAULT_CLASSPATH_CONFIGURATION_FILE);
        }
        if (url != null) {
            LOG.debug("Configuring server_handler from server_handler.properties found in the classpath: " + url);
        } else {
            LOG.warn("No configuration found. Configuring server_handler from server_handler.properties "
                    + " found in the classpath: {}", url);

        }
        List<ProtocolHandlerConfiguration> configurations = parseConfiguration(url);
        return configurations;
    }
    
    /**
     * Configures a bean from an property input stream.
     */
    public static List<ProtocolHandlerConfiguration> parseConfiguration(final InputStream inputStream) throws Exception {

        LOG.debug("Configuring server_handler from InputStream");

        List<ProtocolHandlerConfiguration> configurations = new ArrayList<ProtocolHandlerConfiguration>();
        try {
            Properties props = new Properties();
            props.load(inputStream);
			for(String key : props.stringPropertyNames()){
    			configurations.add(new ProtocolHandlerConfiguration(key , props));
    		}
        } catch (Exception e) {
            throw new Exception("Error configuring from input stream. Initial cause was " + e.getMessage(), e);
        }
        return configurations;
    }
}
