package com.zm.PersonalAssistant.task;

import com.zm.PersonalAssistant.ThreadMsg.ThreadMsg;
import com.zm.PersonalAssistant.ThreadMsg.ThreadMsgBody;
import com.zm.PersonalAssistant.ThreadMsg.ThreadMsgType;
import com.zm.PersonalAssistant.server.Server;

/**
 * Created by Administrator on 2016/7/2.
 */
public abstract class BasicTask implements Runnable {

    private Server server = Server.getInstance();
    private TaskType taskType;

    public BasicTask(TaskType taskType) {
        super();
        this.taskType = taskType;
        server.addTask(taskType, this);
    }

    //供Server用
    public abstract void putThreadMsg(ThreadMsg msg);

    //自己发送线程消息用
    protected void sendThreadMsgTo(TaskType desTaskType, ThreadMsgType msgType, ThreadMsgBody msgBody) {
        ThreadMsg msg = new ThreadMsg(this.taskType, desTaskType, msgType, msgBody);
        server.sendThreadMsgTo(msg);
    }

    protected void replayThreadMsg(ThreadMsg recMsg, ThreadMsgType msgType, ThreadMsgBody msgBody) {
        ThreadMsg msg = new ThreadMsg(this.taskType, recMsg.getSrcTask(), msgType, msgBody);
        server.sendThreadMsgTo(msg);
    }
}
