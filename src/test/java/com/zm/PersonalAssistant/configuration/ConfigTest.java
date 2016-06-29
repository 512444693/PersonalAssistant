package com.zm.PersonalAssistant.configuration;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by zhangmin on 2016/6/29.
 */
public class ConfigTest {
    private ByteArrayInputStream byteArrayInputStream;

    class ConfigStub extends Config {

        public ConfigStub(String filePath) throws IOException {
            super(filePath);
        }

        @Override
        protected InputStream getInputStream(String filePath){
            return byteArrayInputStream;
        }
    }

    @Test
    public void testGetBooleanRight() throws IOException {
        //Arrange
        byteArrayInputStream = new ByteArrayInputStream("useDropBox=true".getBytes());

        //Act
        ConfigStub conf = new ConfigStub("whatever name");

        //Assert
        assertTrue(conf.getBoolean("useDropBox"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetBooleanFailValueNotRight() throws IOException {
        //Arrange
        byteArrayInputStream = new ByteArrayInputStream("useDropBox=tru".getBytes());

        //Act
        ConfigStub conf = new ConfigStub("whatever name");

        //Assert
        conf.getBoolean("useDropBox");
    }

    @Test(expected = IllegalStateException.class)
    public void testGetBooleanFailNotExist() throws IOException {
        //Arrange
        byteArrayInputStream = new ByteArrayInputStream("hah=true".getBytes());

        //Act
        ConfigStub conf = new ConfigStub("whatever name");

        //Assert
        conf.getBoolean("useDropBox");
    }

    @Test
    public void testGetIntRight() throws IOException {
        //Arrange
        byteArrayInputStream = new ByteArrayInputStream("interval=0".getBytes());

        //Act
        ConfigStub conf = new ConfigStub("whatever name");

        //Assert
        assertEquals(0, conf.getInt("interval"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetIntFailValueNotRight() throws IOException {
        //Arrange
        byteArrayInputStream = new ByteArrayInputStream("interval=abc".getBytes());

        //Act
        ConfigStub conf = new ConfigStub("whatever name");

        //Assert
        conf.getInt("interval");
    }

    @Test(expected = IllegalStateException.class)
    public void testGetIntFailNotExist() throws IOException {
        //Arrange
        byteArrayInputStream = new ByteArrayInputStream("hah=true".getBytes());

        //Act
        ConfigStub conf = new ConfigStub("whatever name");

        //Assert
        conf.getInt("interval");
    }

    @Test
    public void testGetStringRight() throws IOException {
        //Arrange
        byteArrayInputStream = new ByteArrayInputStream("email=information@163.com".getBytes());

        //Act
        ConfigStub conf = new ConfigStub("whatever name");

        //Assert
        assertEquals("information@163.com", conf.getString("email"));
    }

    @Test
    public void testGetIntValueEmpty() throws IOException {
        //Arrange
        byteArrayInputStream = new ByteArrayInputStream("email=".getBytes());

        //Act
        ConfigStub conf = new ConfigStub("whatever name");

        //Assert
        assertEquals("", conf.getString("email"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetStringFailNotExist() throws IOException {
        //Arrange
        byteArrayInputStream = new ByteArrayInputStream("hah=true".getBytes());

        //Act
        ConfigStub conf = new ConfigStub("whatever name");

        //Assert
        conf.getString("interval");
    }
}
