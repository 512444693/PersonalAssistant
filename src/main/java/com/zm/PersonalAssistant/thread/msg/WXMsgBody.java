package com.zm.PersonalAssistant.thread.msg;

import com.qq.weixin.mp.aes.WXBizMsgCrypt;
import com.sun.net.httpserver.HttpExchange;
import com.zm.frame.thread.msg.ThreadMsgBody;

/**
 * Created by Administrator on 2016/11/5.
 */
public class WXMsgBody extends ThreadMsgBody {
    public WXBizMsgCrypt wxcpt;
    public HttpExchange httpExchange;

    public WXMsgBody(WXBizMsgCrypt wxcpt, HttpExchange httpExchange) {
        this.wxcpt = wxcpt;
        this.httpExchange = httpExchange;
    }
}
