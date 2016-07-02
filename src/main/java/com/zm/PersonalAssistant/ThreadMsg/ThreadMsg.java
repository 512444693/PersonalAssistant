package com.zm.PersonalAssistant.ThreadMsg;

import com.zm.PersonalAssistant.task.TaskType;

/**
 * Created by Administrator on 2016/7/2.
 */
public class ThreadMsg {
    public ThreadMsg(TaskType srcTask, TaskType desTask, ThreadMsgType msgType, ThreadMsgBody msgBody) {
        this.srcTask = srcTask;
        this.desTask = desTask;
        this.msgType = msgType;
        this.msgBody = msgBody;
    }

    public TaskType getSrcTask() {
        return srcTask;
    }

    public void setSrcTask(TaskType srcTask) {
        this.srcTask = srcTask;
    }

    public TaskType getDesTask() {
        return desTask;
    }

    public void setDesTask(TaskType desTask) {
        this.desTask = desTask;
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

    private TaskType srcTask;
    private TaskType desTask;
    private ThreadMsgType msgType;
    private ThreadMsgBody msgBody;
}
