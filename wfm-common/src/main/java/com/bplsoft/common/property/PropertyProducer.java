package com.bplsoft.common.property;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Properties;

public class PropertyProducer {

    private static final Logger logger = LoggerFactory.getLogger(PropertyProducer.class);

    public static final String INTERNAL_APPLICATION_PROPERTIES = "/application.properties";
    public static final String APP_PROPERTIES = "APP_PROPERTIES";

    private static Properties properties;


    @Property
    @Produces
    public String produceString(final InjectionPoint ip) {
        return properties.getProperty(getKey(ip));
    }

    @Property
    @Produces
    public int produceInt(final InjectionPoint ip) {
        return Integer.valueOf(properties.getProperty(getKey(ip)));
    }

    @Property
    @Produces
    public boolean produceBoolean(final InjectionPoint ip) {
        return Boolean.valueOf(properties.getProperty(getKey(ip)));
    }

    private String getKey(final InjectionPoint ip) {
        if (ip.getAnnotated().isAnnotationPresent(Property.class)
            && !ip.getAnnotated().getAnnotation(Property.class).value().isEmpty()) {
            return ip.getAnnotated().getAnnotation(Property.class).value();
        } else {
            return ip.getMember().getName();
        }
    }


    public static String getValue(String key) {
        return properties.getProperty(key);
    }

    public static void init() throws Exception {
        if (properties == null) {
            try {
                // create properties
                properties = new Properties();

                // try to read first given properties file
                InputStream inputStream;
                final String wssdProperty = System.getProperty(APP_PROPERTIES);
                if (wssdProperty != null) {
                    final Path wssdPropertiesFilePath = Paths.get(wssdProperty);
                    inputStream = Files.newInputStream(wssdPropertiesFilePath, StandardOpenOption.READ);
                } else {
                    // otherwise read internal app properties file
                    logger.info("no system property defined with key '" + APP_PROPERTIES + "' will use internal " + INTERNAL_APPLICATION_PROPERTIES);
                    inputStream = PropertyProducer.class.getResourceAsStream(INTERNAL_APPLICATION_PROPERTIES);
                    if (inputStream == null) {
                        throw new Exception("No properties file found");
                    }
                }
                properties.load(inputStream);
            } catch (final Exception e) {
                throw new Exception("Configuration file could not be loaded, " +
                    "either supply properties file with APP_PROPERTIES environment key or make " +
                    "application.properties available in classpath.", e);
            }
        }
    }

}
