package com.zm.PersonalAssistant.ThreadMsg;

import com.zm.PersonalAssistant.thread.ThreadType;

/**
 * Created by Administrator on 2016/7/2.
 */
public class ThreadMsg {
    public ThreadMsg(ThreadType srcThread, ThreadType desThread, ThreadMsgType msgType, ThreadMsgBody msgBody) {
        this.srcThread = srcThread;
        this.desThread = desThread;
        this.msgType = msgType;
        this.msgBody = msgBody;
    }

    public ThreadType getSrcThread() {
        return srcThread;
    }

    public void setSrcThread(ThreadType srcThread) {
        this.srcThread = srcThread;
    }

    public ThreadType getDesThread() {
        return desThread;
    }

    public void setDesThread(ThreadType desThread) {
        this.desThread = desThread;
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

    private ThreadType srcThread;
    private ThreadType desThread;
    private ThreadMsgType msgType;
    private ThreadMsgBody msgBody;
}
