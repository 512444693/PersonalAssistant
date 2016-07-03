package com.zm.PersonalAssistant.thread;

import com.zm.PersonalAssistant.ThreadMsg.StringMsgBody;
import com.zm.PersonalAssistant.ThreadMsg.ThreadMsg;
import com.zm.PersonalAssistant.UI.Mail;
import com.zm.PersonalAssistant.server.Server;

import static com.zm.PersonalAssistant.ThreadMsg.ThreadMsgType.USER_REQ_MSG;
import static com.zm.PersonalAssistant.thread.ThreadType.USER_MSG_PROCESS_THREAD;
import static com.zm.PersonalAssistant.utils.Log.log;

/**
 * Created by Administrator on 2016/7/2.
 */
public class RecAndSendImlThread extends NoBlockingThread {

    private Mail mail = Server.getInstance().getMail();

    public RecAndSendImlThread(ThreadType threadType, int delayTime) {
        super(threadType, delayTime);
    }

    //SEND_TO_ALL和USER_REPLAY_MSG现在处理方法一样
    @Override
    protected void process(ThreadMsg msg) {
        log.debug("RecAndSendThread process a msg " + msg.getMsgType());
        switch (msg.getMsgType()) {
            case SEND_TO_ALL:
                String notify = ((StringMsgBody)msg.getMsgBody()).getMsgBody();
                //TODO : 设置发送消息超时时间
                //mail.send(notify);
                String tmp = "====================================\r\n"
                        + notify + "\r\n====================================";
                log.debug(tmp);
                break;
            case USER_REPLAY_MSG:
                String reply = ((StringMsgBody)msg.getMsgBody()).getMsgBody();
                //mail.send(reply);
                tmp = "====================================\r\n"
                        + reply + "\r\n====================================";
                log.debug(tmp);
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
            sendThreadMsgTo(USER_MSG_PROCESS_THREAD, USER_REQ_MSG, new StringMsgBody(req));
        }
    }
}
