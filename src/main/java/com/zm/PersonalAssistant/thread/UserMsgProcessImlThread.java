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
                String replay = processUserReq(req);
                replayThreadMsg(msg, ThreadMsgType.USER_REPLAY_MSG, new StringMsgBody(replay));
                break;
            default:
                log.error("收到不支持的线程消息类型 " + msg.getMsgType());
        }
    }

    private String processUserReq(String req) {

        return "";
    }

    @Override
    protected void init() {

    }
    //TODO: (多少分钟/小时/天/星期/月/年)后  按（阳历/农历） 重复（从不/每天/每周/每月/每年） 并且提前(几分钟/小时/天/周/月/从不)  提醒我(做什么)
    //TODO : (几年几月几号几时几分)          按（阳历/农历） 重复（从不/每天/每周/每月/每年） 并且提前(几分钟/小时/天/周/月/从不)  提醒我(做什么)
    //TODO : 提醒的时间如果比现在的时间小，则不允许添加reminder
}
