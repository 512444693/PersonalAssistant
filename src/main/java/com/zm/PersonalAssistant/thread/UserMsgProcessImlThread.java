package com.zm.PersonalAssistant.thread;

import com.zm.PersonalAssistant.ThreadMsg.StringMsgBody;
import com.zm.PersonalAssistant.ThreadMsg.ThreadMsg;
import com.zm.PersonalAssistant.ThreadMsg.ThreadMsgType;

import static com.zm.PersonalAssistant.utils.Log.log;

/**
 * Created by Administrator on 2016/7/3.
 */
public class UserMsgProcessImlThread extends BlockingThread {

    public UserMsgProcessImlThread(ThreadType threadType) {
        super(threadType);
    }

    @Override
    protected void process(ThreadMsg msg) {
        log.debug("UserMsgProcessThread process a msg " + msg.getMsgType());
        switch (msg.getMsgType()) {
            case USER_REQ_MSG:
                String req = ((StringMsgBody)msg.getMsgBody()).getMsgBody();
                //TODO : 用户包处理
                replayThreadMsg(msg, ThreadMsgType.USER_REPLAY_MSG, msg.getMsgBody());
                break;
            default:
                log.error("收到不支持的线程消息类型 " + msg.getMsgType());
        }
    }

    @Override
    protected void init() {

    }
}
