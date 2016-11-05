package com.zm.PersonalAssistant.thread;

import com.qq.weixin.mp.aes.AesException;
import com.qq.weixin.mp.aes.WXBizMsgCrypt;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.zm.PersonalAssistant.configuration.MyConfig;
import com.zm.PersonalAssistant.configuration.MyDef;
import com.zm.PersonalAssistant.server.PAServer;
import com.zm.PersonalAssistant.thread.msg.WXMsgBody;
import com.zm.frame.thread.thread.BasicThread;

import java.io.IOException;
import java.net.InetSocketAddress;

import static com.zm.frame.log.Log.log;

/**
 * Created by Administrator on 2016/11/5.
 */
public class HttpListenThreadIml extends BasicThread {

    private final int port;
    private WXBizMsgCrypt wxcpt = null;
    private MyConfig config;

    public HttpListenThreadIml(int threadType, int threadId, String port) {
        super(threadType, threadId);
        this.port = Integer.parseInt(port);
    }

    @Override
    protected void init() {
        config = PAServer.getInstance().getConfig();
        try {
            wxcpt = new WXBizMsgCrypt(config.getToken(), config.getEncodingAESKey(), config.getCorpid());
        } catch (AesException e) {
            log.error("微信对象初始化失败");
            log.error(e);
            System.exit(1);
        }
    }

    @Override
    protected void process() {
        try {
            HttpServer httpServer = HttpServer.create(new InetSocketAddress(port), 0);
            httpServer.createContext("/", new WXHandler());
            httpServer.setExecutor(null); // creates a default executor
            httpServer.start();
        } catch (IOException e) {
            log.error(e);
            System.exit(1);
        }
    }

    class WXHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            sendThreadMsgTo(MyDef.MSG_TYPE_WX, new WXMsgBody(wxcpt, httpExchange), MyDef.THREAD_TYPE_REC_AND_SEND);
        }
    }
}
