package com.zm.PersonalAssistant.thread;

import com.zm.PersonalAssistant.Reminder.ReminderMgr;
import com.zm.PersonalAssistant.ThreadMsg.StringMsgBody;
import com.zm.PersonalAssistant.ThreadMsg.ThreadMsg;
import com.zm.PersonalAssistant.ThreadMsg.ThreadMsgBody;
import com.zm.PersonalAssistant.ThreadMsg.ThreadMsgType;
import com.zm.PersonalAssistant.configuration.MyConfig;
import com.zm.PersonalAssistant.server.Server;
import com.zm.PersonalAssistant.utils.LunarCalendar;
import static com.zm.PersonalAssistant.utils.Log.log;

/**
 * Created by Administrator on 2016/7/2.
 */
public class CheckNotifyImlThread extends BasicThread {
    ReminderMgr reminderMgr = Server.getInstance().getReminderMgr();
    MyConfig config = Server.getInstance().getConfig();
    public CheckNotifyImlThread(ThreadType threadType) {
        super(threadType);
    }

    @Override
    public void putThreadMsg(ThreadMsg msg) {

    }

    @Override
    public void run() {
        while (true) {
            LunarCalendar timeNow = new LunarCalendar();
            //log.debug("Now is " + timeNow + ", check notify ...");
            String notify = reminderMgr.getNotify(timeNow);
            if(!notify.equals("")) {
                log.debug("检查到通知：\r\n" + notify);
                ThreadMsgBody msgBody = new StringMsgBody(notify);
                sendThreadMsgTo(ThreadType.REC_SEND_THREAD, ThreadMsgType.SEND_TO_ALL, msgBody);
                //log.debug("通知收发线程发送数据");
            }
            try {
                Thread.sleep(config.getCheckInterval() * 1000);
            } catch (InterruptedException e) {
                log.error("检查事件线程中断异常, ", e);
            }
        }
    }
}