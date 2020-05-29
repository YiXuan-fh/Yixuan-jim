package org.jim.core.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Properties;
/**
 * @author WChao
 * @date 2019-06-15
 * 加载属性配置类
 */
public class Prop {

    private Logger logger = LoggerFactory.getLogger(Prop.class);
    private Properties properties;

    public Prop(String fileName) {
        this(fileName, "UTF-8");
    }

    public Prop(String fileName, String encoding) {
        this.properties = null;
        InputStream inputStream = null;

        try {
            inputStream = this.getClassLoader().getResourceAsStream(fileName);
            if (inputStream == null) {
                throw new IllegalArgumentException("Properties file not found in classpath: " + fileName);
            }

            this.properties = new Properties();
            this.properties.load(new InputStreamReader(inputStream, encoding));
        } catch (IOException var12) {
            throw new RuntimeException("Error loading properties file.", var12);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException var11) {
                    logger.error(var11.getMessage(), var11);
                }
            }

        }

    }

    private ClassLoader getClassLoader() {
        ClassLoader ret = Thread.currentThread().getContextClassLoader();
        return ret != null ? ret : this.getClass().getClassLoader();
    }

    public Prop(File file) {
        this(file, "UTF-8");
    }

    public Prop(File file, String encoding) {
        this.properties = null;
        if (file == null) {
            throw new IllegalArgumentException("File can not be null.");
        } else if (!file.isFile()) {
            throw new IllegalArgumentException("File not found : " + file.getName());
        } else {
            FileInputStream inputStream = null;

            try {
                inputStream = new FileInputStream(file);
                this.properties = new Properties();
                this.properties.load(new InputStreamReader(inputStream, encoding));
            } catch (IOException var12) {
                throw new RuntimeException("Error loading properties file.", var12);
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException var11) {
                        logger.error(var11.getMessage(), var11);
                    }
                }

            }

        }
    }

    public String get(String key) {
        return this.properties.getProperty(key);
    }

    public String get(String key, String defaultValue) {
        return this.properties.getProperty(key, defaultValue);
    }

    public Integer getInt(String key) {
        return this.getInt(key, (Integer) null);
    }

    public Integer getInt(String key, Integer defaultValue) {
        String value = this.properties.getProperty(key);
        return value != null ? Integer.parseInt(value.trim()) : defaultValue;
    }

    public Long getLong(String key) {
        return this.getLong(key, (Long) null);
    }

    public Long getLong(String key, Long defaultValue) {
        String value = this.properties.getProperty(key);
        return value != null ? Long.parseLong(value.trim()) : defaultValue;
    }

    public Boolean getBoolean(String key) {
        return this.getBoolean(key, (Boolean) null);
    }

    public Boolean getBoolean(String key, Boolean defaultValue) {
        String value = this.properties.getProperty(key);
        if (value != null) {
            value = value.toLowerCase().trim();
            if ("true".equals(value)) {
                return true;
            } else if ("false".equals(value)) {
                return false;
            } else {
                throw new RuntimeException("The value can not parse to Boolean : " + value);
            }
        } else {
            return defaultValue;
        }
    }

    public boolean containsKey(String key) {
        return this.properties.containsKey(key);
    }

    public Properties getProperties() {
        return this.properties;
    }
}