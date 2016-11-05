package com.zm.PersonalAssistant.thread;

import com.zm.PersonalAssistant.configuration.MyConfig;
import com.zm.PersonalAssistant.configuration.MyDef;
import com.zm.PersonalAssistant.server.PAServer;
import com.zm.PersonalAssistant.thread.msg.WXMsgBody;
import com.zm.PersonalAssistant.weixin.WXSend;
import com.zm.frame.thread.msg.StringMsgBody;
import com.zm.frame.thread.msg.ThreadMsg;
import com.zm.frame.thread.task.Task;
import com.zm.frame.thread.thread.BlockingThread;
import com.zm.frame.thread.thread.NoBlockingThread;

import javax.mail.MessagingException;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.*;

import static com.zm.frame.log.Log.log;

/**
 * Created by Administrator on 2016/7/2.
 */
public class RecAndSendThreadIml extends BlockingThread {

    private MyConfig config = PAServer.getInstance().getConfig();

    public RecAndSendThreadIml(int threadType, int threadId) {
        super(threadType, threadId);
    }

    @Override
    protected void processMsg(ThreadMsg msg) {
        log.debug("接收发送线程收到消息：" + msg.toString());
        //每次收到消息检查是否有任务超时
        checkTaskTimeout();
        switch (msg.msgType) {
            case MyDef.MSG_TYPE_WX:
                //10s超时
                Task task = addTask(MyDef.TASK_TYPE_WX, 10, null);
                task.processMsg(msg);
                break;
            case MyDef.MSG_TYPE_REPLY:
                //交给task处理
                super.processMsg(msg);
                break;
            case MyDef.MSG_TYPE_NOTIFY:
                String notify = ((StringMsgBody)msg.msgBody).getMsgBody();
                log.debug("向客户端通知：\r\n" + notify);
                WXSend.send(notify);
                break;
            default:
                log.error("接收发送线程收到不支持的线程消息类型 " + msg.msgType);
        }
    }

    @Override
    protected void init() {
        //sendThreadMsgTo(MyDef.MSG_TYPE_NOTIFY, new StringMsgBody("程序重启了"), MyDef.THREAD_TYPE_REC_AND_SEND);
    }
}
