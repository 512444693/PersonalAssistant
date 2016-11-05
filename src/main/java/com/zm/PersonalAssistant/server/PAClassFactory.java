package com.zm.PersonalAssistant.server;

import com.zm.PersonalAssistant.configuration.MyDef;
import com.zm.PersonalAssistant.task.WXTask;
import com.zm.PersonalAssistant.thread.*;
import com.zm.frame.thread.server.ClassFactory;
import com.zm.frame.thread.task.Task;
import com.zm.frame.thread.thread.BasicThread;

/**
 * Created by Administrator on 2016/11/5.
 */
public class PAClassFactory extends ClassFactory {

    @Override
    public BasicThread genThread(int threadType, int threadId, String[] args) {
        BasicThread ret = null;
        switch (threadType) {
            case MyDef.THREAD_TYPE_PERSISTENCE :
                ret =  new DataPersistenceThreadIml(threadType, threadId);
                break;
            case MyDef.THREAD_TYPE_CHECK_NOTIFY :
                ret =  new CheckNotifyThreadIml(threadType, threadId);
                break;
            case MyDef.THREAD_TYPE_PROCESS :
                ret =  new UserMsgProcessThreadIml(threadType, threadId);
                break;
            case MyDef.THREAD_TYPE_REC_AND_SEND :
                ret =  new RecAndSendThreadIml(threadType, threadId);
                break;
            case MyDef.THREAD_TYPE_HTTP_LISTEN :
                ret =  new HttpListenThreadIml(threadType, threadId, args[0]);
                break;
        }
        return ret;
    }

    @Override
    public Task genTask(int taskType, int taskId, BasicThread thread, int time, String[] args) {
        Task ret = null;
        switch (taskType) {
            case MyDef.TASK_TYPE_WX :
                ret = new WXTask(taskId, thread, time);
                break;
        }
        return ret;
    }

}
