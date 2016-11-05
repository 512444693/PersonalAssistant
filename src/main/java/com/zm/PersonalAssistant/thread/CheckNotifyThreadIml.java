package com.zm.PersonalAssistant.thread;

import com.zm.PersonalAssistant.Reminder.ReminderMgr;
import com.zm.PersonalAssistant.configuration.MyConfig;
import com.zm.PersonalAssistant.Reminder.LunarCalendar;
import com.zm.PersonalAssistant.configuration.MyDef;
import com.zm.PersonalAssistant.server.PAServer;
import com.zm.frame.thread.msg.StringMsgBody;
import com.zm.frame.thread.msg.ThreadMsgBody;
import com.zm.frame.thread.thread.BasicThread;

import static com.zm.frame.log.Log.log;

/**
 * Created by Administrator on 2016/7/2.
 * 检查事件，并通知发送线程
 */
public class CheckNotifyThreadIml extends BasicThread {
    ReminderMgr reminderMgr = PAServer.getInstance().getReminderMgr();
    MyConfig config = PAServer.getInstance().getConfig();

    public CheckNotifyThreadIml(int threadType, int threadId) {
        super(threadType, threadId);
    }

    @Override
    protected void init() {}

    @Override
    protected void process() {
        while (true) {
            LunarCalendar timeNow = new LunarCalendar();
            //log.debug("Now is " + timeNow + ", check notify ...");
            String notify = reminderMgr.getNotify(timeNow);
            if(!notify.equals("")) {
                log.debug("检查到通知：\r\n" + notify);
                ThreadMsgBody msgBody = new StringMsgBody(notify);
                sendThreadMsgTo(MyDef.MSG_TYPE_NOTIFY, msgBody, MyDef.THREAD_TYPE_REC_AND_SEND);
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
