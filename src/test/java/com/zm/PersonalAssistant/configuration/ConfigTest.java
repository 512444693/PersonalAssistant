package com.zm.PersonalAssistant.configuration;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by zhangmin on 2016/6/27.
 */
public class ConfigTest {

    private  ByteArrayInputStream byteArrayInputStream;

    class ConfigStub extends Config{

        public ConfigStub(String filePath) throws IOException {
            super(filePath);
        }

        @Override
        protected InputStream getInputStream(String filePath){
            return byteArrayInputStream;
        }
    }

    @Test
    public void test_get_use_dropBox_is_true() throws IOException {

        //Arrange
        byteArrayInputStream = new ByteArrayInputStream("useDropBox=true\r\ncheckInterval=10".getBytes());

        //Act
        ConfigStub conf = new ConfigStub("test.properties");

        //Assert
        assertTrue(conf.isUseDropBox());
    }

    @Test
    public void test_get_use_dropBox_is_false() throws IOException {

        //Arrange
        byteArrayInputStream = new ByteArrayInputStream("\r\nuseDropBox=false\r\ncheckInterval=10".getBytes());

        //Act
        ConfigStub conf = new ConfigStub("test.properties");

        //Assert
        assertFalse(conf.isUseDropBox());
    }

    @Test(expected = IllegalStateException.class)
    public void test_get_use_dropBox_fail() throws IOException {

        //Arrange
        byteArrayInputStream = new ByteArrayInputStream("useDropBox=111".getBytes());

        //Act
        ConfigStub conf = new ConfigStub("test.properties");
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_read_configuration_file_not_exist() throws IOException {

        //Arrange
        Config conf = new Config("conf.properties");//conf.properties is not exist
    }

    @Test
    public void test_get_check_interval_ok() throws IOException {

        //Arrange
        byteArrayInputStream = new ByteArrayInputStream("\r\nuseDropBox=false\r\ncheckInterval=10".getBytes());

        //Act
        ConfigStub conf = new ConfigStub("test.properties");

        //Assert
        assertEquals(10, conf.getCheckInterval());
    }

    @Test(expected = IllegalStateException.class)
    public void test_get_check_interval_fail() throws IOException {

        //Arrange
        byteArrayInputStream = new ByteArrayInputStream("\r\nuseDropBox=false\r\n".getBytes());

        //Act
        ConfigStub conf = new ConfigStub("test.properties");

        //Assert
        assertEquals(10, conf.getCheckInterval());
    }

    //TODO : 程序刚启动时：1.读取配置文件 2.从文件恢复数据 3.log4j配置 PropertyConfigurator.configure("conf/log4j.properties") 4.同步所有数据到云端 5.创建其它线程
    //                          任何步骤失败，程序退出, 若成功则循环等待或者join

    //TODO : 配置：2.client邮箱配置 server邮箱配置  邮箱其它配置
}
