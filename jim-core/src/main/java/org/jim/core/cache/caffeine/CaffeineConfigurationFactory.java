package org.jim.core.cache.caffeine;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * @author WChao
 * @date 2018年3月9日 上午1:06:33
 */
public class CaffeineConfigurationFactory {
	
    private static final Logger LOG = LoggerFactory.getLogger(CaffeineConfigurationFactory.class.getName());

    private static final String DEFAULT_CLASSPATH_CONFIGURATION_FILE = "caffeine.properties";
    
    /**
     * Constructor.
     */
    private CaffeineConfigurationFactory() {

    }

    /**
     * Configures a bean from an property file.
     */
    public static List<CaffeineConfiguration> parseConfiguration(final File file) throws Exception {
        if (file == null) {
            throw new Exception("Attempt to configure caffeine from null file.");
        }
        LOG.debug("Configuring caffeine from file: {}", file);
        List<CaffeineConfiguration> configurations  = null;
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
    public static List<CaffeineConfiguration> parseConfiguration(final URL url) throws Exception {
        LOG.debug("Configuring caffeine from URL: {}", url);
        List<CaffeineConfiguration> configurations;
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
    public static List<CaffeineConfiguration> parseConfiguration() throws Exception {
        ClassLoader standardClassloader = Thread.currentThread().getContextClassLoader();
        URL url = null;
        if (standardClassloader != null) {
            url = standardClassloader.getResource(DEFAULT_CLASSPATH_CONFIGURATION_FILE);
        }
        if (url == null) {
        	url = CaffeineConfigurationFactory.class.getResource(DEFAULT_CLASSPATH_CONFIGURATION_FILE);
        }
        if (url != null) {
            LOG.debug("Configuring caffeine from caffeine.properties found in the classpath: " + url);
        } else {
            LOG.warn("No configuration found. Configuring caffeine from caffeine.properties "
                    + " found in the classpath: {}", url);

        }
        List<CaffeineConfiguration> configurations = parseConfiguration(url);
        return configurations;
    }
    
    /**
     * Configures a bean from an property input stream.
     */
    public static List<CaffeineConfiguration> parseConfiguration(final InputStream inputStream) throws Exception {

        LOG.debug("Configuring caffeine from InputStream");

        List<CaffeineConfiguration> configurations = new ArrayList<CaffeineConfiguration>();
        try {
            Properties props = new Properties();
            props.load(inputStream);
			for(String key : props.stringPropertyNames()){
    			configurations.add(new CaffeineConfiguration(key , props));
    		}
        } catch (Exception e) {
            throw new Exception("Error configuring from input stream. Initial cause was " + e.getMessage(), e);
        }
        return configurations;
    }
}
