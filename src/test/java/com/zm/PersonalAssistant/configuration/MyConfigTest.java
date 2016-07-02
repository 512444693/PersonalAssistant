package com.zm.PersonalAssistant.configuration;

import com.zm.PersonalAssistant.server.Server;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.io.IOException;

/**
 * Created by Administrator on 2016/7/2.
 */
public class MyConfigTest {
    @Test
    public void testWork() throws IOException {
        MyConfig config = new MyConfig(Server.CONFIGURATION_DIRECTORY_PATH +  "conf.properties");

        assertEquals(10, config.getCheckInterval());
    }
}
