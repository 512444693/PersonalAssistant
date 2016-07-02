package com.zm.PersonalAssistant.ThreadMsg;

/**
 * Created by Administrator on 2016/7/2.
 */
public class StringMsgBody extends ThreadMsgBody {
    private String msgBody;

    public String getMsgBody() {
        return msgBody;
    }

    public StringMsgBody(String msgBody) {
        this.msgBody = msgBody;
    }
}
