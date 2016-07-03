package com.zm.PersonalAssistant.thread;

import com.zm.PersonalAssistant.ThreadMsg.ThreadMsg;
import com.zm.PersonalAssistant.ThreadMsg.ThreadMsgBody;
import com.zm.PersonalAssistant.ThreadMsg.ThreadMsgType;
import com.zm.PersonalAssistant.server.Server;

/**
 * Created by Administrator on 2016/7/2.
 */
public abstract class BasicThread extends Thread {

    private Server server = Server.getInstance();
    private ThreadType threadType;

    public BasicThread(ThreadType threadType) {
        super();
        this.threadType = threadType;
        server.addThread(this);
    }

    public ThreadType getThreadType() {
        return threadType;
    }

    //供Server用
    public abstract void putThreadMsg(ThreadMsg msg);

    //自己发送线程消息用
    protected void sendThreadMsgTo(ThreadType desThreadType, ThreadMsgType msgType, ThreadMsgBody msgBody) {
        ThreadMsg msg = new ThreadMsg(this.threadType, desThreadType, msgType, msgBody);
        server.sendThreadMsgTo(msg);
    }

    protected void replayThreadMsg(ThreadMsg recMsg, ThreadMsgType msgType, ThreadMsgBody msgBody) {
        ThreadMsg msg = new ThreadMsg(this.threadType, recMsg.getSrcThread(), msgType, msgBody);
        server.sendThreadMsgTo(msg);
    }
}
