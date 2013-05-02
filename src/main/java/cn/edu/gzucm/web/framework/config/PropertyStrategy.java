package cn.edu.gzucm.web.framework.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.core.io.AbstractResource;

import cn.edu.gzucm.web.utils.MyProperties;

public class PropertyStrategy extends AbstractResource {

    private String propertyName;

    public PropertyStrategy(String propertyName) {

        this.propertyName = propertyName;
    }

    public PropertyStrategy() {

        //org.springframework.beans.factory.config.PropertyPlaceholderConfigurer a = new org.springframework.beans.factory.config.PropertyPlaceholderConfigurer();
        //a.setLocation(location)
    }

    @Override
    public String getDescription() {

        return null;
    }

    @Override
    public InputStream getInputStream() throws IOException {

        File propertyFile = new File(MyProperties.getConfigPath() + "/conf/" + propertyName);

        if (propertyFile.exists()) {

            return new FileInputStream(propertyFile);
        } else {

            return this.getClass().getClassLoader().getResourceAsStream(propertyName);
        }

    }
}
