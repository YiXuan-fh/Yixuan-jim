package org.jim.core.cache.redis;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import org.jim.core.JimVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * @author WChao
 * @date 2018年3月9日 上午1:06:33
 */
public class RedisConfigurationFactory {
	
    private static final Logger LOG = LoggerFactory.getLogger(RedisConfigurationFactory.class.getName());

    private static final String DEFAULT_CLASSPATH_CONFIGURATION_FILE = "jim.properties";
    
    /**
     * Constructor.
     */
    private RedisConfigurationFactory() {

    }

    /**
     * Configures a bean from an property file.
     */
    public static RedisConfiguration parseConfiguration(final File file) throws Exception {
        if (file == null) {
            throw new Exception("Attempt to configure redis from null file.");
        }
        LOG.debug("Configuring redis from file: {}", file);
        RedisConfiguration configuration = null;
        InputStream input = null;
        try {
            input = new BufferedInputStream(new FileInputStream(file));
            configuration = parseConfiguration(input);
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
        return configuration;
    }
    /**
     * Configures a bean from an property file available as an URL.
     */
    public static RedisConfiguration parseConfiguration(final URL url) throws Exception {
        LOG.debug("Configuring redis from URL: {}", url);
        RedisConfiguration configuration;
        InputStream input = null;
        try {
            input = url.openStream();
            configuration = parseConfiguration(input);
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
        return configuration;
    }
    /**
     * Configures a bean from an property file in the classpath.
     */
    public static RedisConfiguration parseConfiguration() throws Exception {
        ClassLoader standardClassloader = Thread.currentThread().getContextClassLoader();
        URL url = null;
        if (standardClassloader != null) {
            url = standardClassloader.getResource(DEFAULT_CLASSPATH_CONFIGURATION_FILE);
        }
        if (url == null) {
        	url = JimVersion.class.getResource(DEFAULT_CLASSPATH_CONFIGURATION_FILE);
        }
        if (url != null) {
            LOG.debug("Configuring redis from jim.properties found in the classpath: " + url);
        } else {
            LOG.warn("No configuration found. Configuring redis from jim.properties "
                    + " found in the classpath: {}", url);

        }
        RedisConfiguration configuration = parseConfiguration(url);
        return configuration;
    }
    
    /**
     * Configures a bean from an property input stream.
     */
    public static RedisConfiguration parseConfiguration(final InputStream inputStream) throws Exception {

        LOG.debug("Configuring redis from InputStream");

        RedisConfiguration configuration = null;
        try {
            Properties prop = new Properties();
            prop.load(inputStream);
            configuration = new RedisConfiguration(prop);
        } catch (Exception e) {
            throw new Exception("Error configuring from input stream. Initial cause was " + e.getMessage(), e);
        }
        return configuration;
    }
    
    public static void main(String[] args) throws Exception{
		RedisConfigurationFactory.parseConfiguration();
	}
}
