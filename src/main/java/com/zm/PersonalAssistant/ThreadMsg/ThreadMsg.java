package com.zm.PersonalAssistant.ThreadMsg;

import com.zm.PersonalAssistant.thread.ThreadType;

/**
 * Created by Administrator on 2016/7/2.
 */
public class ThreadMsg {
    public ThreadMsg(ThreadType srcThreadType, ThreadType desThreadType, ThreadMsgType msgType, ThreadMsgBody msgBody) {
        this.srcThreadType = srcThreadType;
        this.desThreadType = desThreadType;
        this.msgType = msgType;
        this.msgBody = msgBody;
    }

    public ThreadType getSrcThreadType() {
        return srcThreadType;
    }

    public void setSrcThreadType(ThreadType srcThreadType) {
        this.srcThreadType = srcThreadType;
    }

    public ThreadType getDesThreadType() {
        return desThreadType;
    }

    public void setDesThreadType(ThreadType desThreadType) {
        this.desThreadType = desThreadType;
    }

    public ThreadMsgType getMsgType() {
        return msgType;
    }

    public void setMsgType(ThreadMsgType msgType) {
        this.msgType = msgType;
    }

    public ThreadMsgBody getMsgBody() {
        return msgBody;
    }

    public void setMsgBody(ThreadMsgBody msgBody) {
        this.msgBody = msgBody;
    }

    private ThreadType srcThreadType;
    private ThreadType desThreadType;
    private ThreadMsgType msgType;
    private ThreadMsgBody msgBody;
}
