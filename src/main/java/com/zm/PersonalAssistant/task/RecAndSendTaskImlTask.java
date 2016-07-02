package com.zm.PersonalAssistant.task;

import com.zm.PersonalAssistant.DataPersistence.SyncToCloud;
import com.zm.PersonalAssistant.ThreadMsg.StringMsgBody;
import com.zm.PersonalAssistant.ThreadMsg.ThreadMsg;
import com.zm.PersonalAssistant.UI.Mail;
import com.zm.PersonalAssistant.server.Server;

import static com.zm.PersonalAssistant.ThreadMsg.ThreadMsgType.USER_REQ_MSG;
import static com.zm.PersonalAssistant.task.TaskType.USER_MSG_PROCESS_TASK;
import static com.zm.PersonalAssistant.utils.Log.log;

/**
 * Created by Administrator on 2016/7/2.
 */
public class RecAndSendTaskImlTask extends NoBlockingTask {

    private Mail mail = Server.getInstance().getMail();

    public RecAndSendTaskImlTask(TaskType taskType, int delayTime) {
        super(taskType, delayTime);
    }

    //SEND_TO_ALL和USER_REPLAY_MSG现在处理方法一样
    @Override
    protected void process(ThreadMsg msg) {
        log.debug("RecAndSendThread process a msg " + msg.getMsgType());
        switch (msg.getMsgType()) {
            case SEND_TO_ALL:
                String notify = ((StringMsgBody)msg.getMsgBody()).getMsgBody();
                mail.send(notify);
                break;
            case USER_REPLAY_MSG:
                String reply = ((StringMsgBody)msg.getMsgBody()).getMsgBody();
                mail.send(reply);
                break;
            default:
                log.error("收到不支持的线程消息类型 " + msg.getMsgType());
        }
    }

    @Override
    protected void init() {

    }

    @Override
    protected void otherProcess() {
        String req = mail.rec();
        if(!req.equals("")) {
            sendThreadMsgTo(USER_MSG_PROCESS_TASK, USER_REQ_MSG, new StringMsgBody(req));
        }
    }
}
