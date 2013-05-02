package cn.edu.gzucm.web.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

public class MyProperties {

    private static Logger _logger = Logger.getLogger(MyProperties.class);

    private static ResourceBundle resourceBundle;
    private static ResourceBundle pageBundle;
    private static String configPath = null;
    private static String serverConfigFile = "server.properties";
    private static String applicationConfigFile = "application.properties";
    private static String configFileForWrite = "write.properties";
    private static File propertyFileForWrite;
    private static Properties appProperties;
    private static Properties writeProperties;

    static {
        initialize();
    }

    public static void initialize() {

        if (System.getProperty("os.name").startsWith("Windows")) {
            configPath = "C:/xuanker";
        } else {
            configPath = "/xuanker";
        }
        final File propertyFile = new File(configPath + "/conf/" + serverConfigFile);
        final File propertyFile2 = new File(configPath + "/conf/" + applicationConfigFile);
        propertyFileForWrite = new File(configPath + "/conf/" + configFileForWrite);
        try {
            if (!propertyFile.exists()) {
                appProperties = getPropertiesFromClasspath();
            } else {
                appProperties = new Properties();
                appProperties.load(new FileInputStream(propertyFile));
                appProperties.load(new FileInputStream(propertyFile2));
            }

            writeProperties = new Properties();
            if (!propertyFileForWrite.exists()) {
                //propertyFileForWrite.createNewFile();
            } else {
                writeProperties.load(new FileInputStream(propertyFileForWrite));
            }
        } catch (final IOException e) {
            _logger.error("", e);
            //LoggerUtil.error(_logger, e);
        }

    }

    private static Properties getPropertiesFromClasspath() throws IOException {

        // loading xmlProfileGen.properties from the classpath
        final Properties props = new Properties();
        final InputStream inputStream = XKUtils.class.getClassLoader().getResourceAsStream(serverConfigFile);
        final InputStream inputStream2 = XKUtils.class.getClassLoader().getResourceAsStream(applicationConfigFile);

        if (inputStream == null) {
            throw new FileNotFoundException("property file '" + serverConfigFile + "' not found in the classpath");
        }

        props.load(inputStream2);
        props.load(inputStream);

        return props;
    }

    public static Properties loadProperties(String filename) {

        final File propertyFile = new File(configPath + "/conf/" + filename);
        Properties newProperties = new Properties();
        try {
            if (!propertyFile.exists()) {
                final InputStream inputStream = XKUtils.class.getClassLoader().getResourceAsStream(filename);
                newProperties.load(inputStream);
            } else {
                newProperties.load(new FileInputStream(propertyFile));
            }
        } catch (Exception e) {

        }
        return newProperties;
    }

    public static String getProperty(final String key) {

        return appProperties.getProperty(key);
    }

    public static String getProperty(final String key, final String defaultValue) {

        return appProperties.getProperty(key, defaultValue);
    }

    public static int getIntProperty(final String name, final int defaultValue) {

        final String value = appProperties.getProperty(name, String.valueOf(defaultValue));
        if (value.equals("")) {
            return defaultValue;
        }
        return Integer.parseInt(value);
    }

    public static boolean getBooleanProperty(final String name) {

        String value = appProperties.getProperty(name, "false");
        if (value.equals("")) {
            value = "false";
        }
        return Boolean.parseBoolean(value);
    }

    /**
     * get from code.properties
     * @param key
     * @return
     */
    public static String getMessage(final String key) {

        return resourceBundle.getString(key);
    }

    /**
     * get from messages.properties
     * @param key
     * @return
     */
    public static String getPageMessage(final String key) {

        return pageBundle.getString(key);
    }

    public static String getMessage(final String key, final Object... params) {

        try {
            return MessageFormat.format(resourceBundle.getString(key), params);
        } catch (final MissingResourceException e) {
            return '!' + key + '!';
        }
    }

    public static ResourceBundle getResourceBundle() {

        return resourceBundle;
    }

    public static String getConfigPath() {

        return configPath;
    }

    private static Properties loadProperty(final String name) {

        final Properties result = new Properties();

        final File file = new File(configPath + "/conf/" + name + ".properties");
        try {
            if (!file.exists()) {
                final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                final InputStream inputStream = classLoader.getResourceAsStream(name + ".properties");

                //InputStream inputStream = XKUtils.class.getClassLoader().getResourceAsStream(name + ".properties");
                result.load(inputStream);
            } else {
                result.load(new FileInputStream(file));
            }
        } catch (final Exception e) {

        }
        return result;
    }

    public static Properties getAppProperties() {

        return appProperties;
    }

    public static String getWriteProperty(final String name) {

        return writeProperties.getProperty(name);

    }

    public static void saveProperty(final String name, final String value) {

        writeProperties.setProperty(name, value);
        try {
            writeProperties.store(new FileOutputStream(propertyFileForWrite), null);
        } catch (final FileNotFoundException e) {
            _logger.error("file not found", e);
        } catch (final IOException e) {
            _logger.error("write properties IO exception", e);
        }

    }

    /**
     * 设置代理服务器
     */
    public static void changeProxyServer(final String host, final String port) {

        //设置是否使用http代理访问网络
        System.getProperties().setProperty("http.proxySet", "true");
        //设置http访问要使用的代理服务器的地址
        System.getProperties().setProperty("http.proxyHost", host);
        //设置http访问要使用的代理服务器的端口
        System.getProperties().setProperty("http.proxyPort", port);
        //设置不需要通过代理服务器访问的主机，可以使用*通配符，多个地址用分隔符分开（可以将公司内部地址也加进去）
        System.getProperties().setProperty("http.nonProxyHosts", "localhost|192.168.*|127.0.0.*");
    }

    public static void disableProxyServer() {

        System.getProperties().setProperty("http.proxySet", "false");
    }

    public static Properties getWriteProperties() {

        return writeProperties;
    }

    public static void setAppProperties(final Properties appProperties) {

        MyProperties.appProperties = appProperties;
    }

    public static void addProperties(final Properties appProperties) {

        MyProperties.appProperties.putAll(appProperties);
    }

    public static void changeProxyServer() {

        if (getBooleanProperty("system.useProxy")) {
            changeProxyServer("192.168.0.5", "8010");
        }
    }
}
