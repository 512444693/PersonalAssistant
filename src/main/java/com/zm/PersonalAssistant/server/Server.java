package com.zm.PersonalAssistant.server;

import com.zm.PersonalAssistant.DataPersistence.DropBox;
import com.zm.PersonalAssistant.DataPersistence.SerializeObject;
import com.zm.PersonalAssistant.DataPersistence.SyncToCloud;
import com.zm.PersonalAssistant.Reminder.Reminder;
import com.zm.PersonalAssistant.Reminder.ReminderMgr;
import com.zm.PersonalAssistant.Reminder.Repeat;
import com.zm.PersonalAssistant.UI.Mail;
import com.zm.PersonalAssistant.configuration.MyConfig;
import com.zm.PersonalAssistant.thread.*;
import com.zm.PersonalAssistant.ThreadMsg.ThreadMsg;
import com.zm.PersonalAssistant.utils.LunarCalendar;

import javax.mail.MessagingException;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.zm.PersonalAssistant.thread.ThreadType.*;
import static com.zm.PersonalAssistant.utils.Log.log;

/**
 * Created by Administrator on 2016/7/2.
 */
public class Server {

    public static final String CONFIGURATION_DIRECTORY_PATH = "conf" + File.separator;
    public static final String DATA_DIRECTORY_PATH = "data" + File.separator;

    private ReminderMgr reminderMgr;
    private MyConfig config;
    private SyncToCloud syncToCloud;
    private Mail mail;

    private static final Server instance = new Server();

    private ConcurrentHashMap<ThreadType, BasicThread> threadMap = new ConcurrentHashMap<>();

    private Server() {}

    public void addThread(BasicThread thread) {
        threadMap.put(thread.getThreadType(), thread);
    }

    public void sendThreadMsgTo(ThreadMsg msg) {
        BasicThread thread = threadMap.get(msg.getDesThreadType());
        if(thread != null) {
            thread.putThreadMsg(msg);
        } else {
            log.error("发送线程消息失败, 找不到ThreadType为" + msg.getDesThreadType() + "的线程");
        }
    }

    public static Server getInstance() {
        return instance;
    }

    public ReminderMgr getReminderMgr() {
        return reminderMgr;
    }

    public MyConfig getConfig() {
        return config;
    }

    public SyncToCloud getSyncToCloud() {
        return syncToCloud;
    }

    public Mail getMail() {
        return mail;
    }

    public void init() throws IOException, MessagingException {

        //1.读取配置文件
        config = new MyConfig(CONFIGURATION_DIRECTORY_PATH +  "conf.properties");

        //2.从文件恢复数据,或创建新数据
        Object object = SerializeObject.deserialize(ReminderMgr.class);
        if(object != null) {
            reminderMgr = (ReminderMgr) object;
            log.debug("从文件中恢复 ReminderMgr");
            reminderMgr.getAllReminderStr();
        } else {
            reminderMgr = ReminderMgr.getInstance();
            log.debug("创建一个新的 ReminderMgr");
        }

        if(config.isUseDropBox()) {
            DropBox dropBox = new DropBox(CONFIGURATION_DIRECTORY_PATH + "DropBox.auth");
            syncToCloud = new SyncToCloud(dropBox);
        }

        mail = new Mail(config.getMailUser(), config.getMailPassword(), config.getMailTo());

        //3.创建线程, 注意，一种类型的线程只能创建一个
        new DataPersistenceImlThread(PERSISTENCE_THREAD);
        new CheckNotifyImlThread(CHECK_NOTIFY_THREAD);
        new RecAndSendImlThread(REC_SEND_THREAD, config.getRecAndSendInterval());
        new UserMsgProcessImlThread(USER_MSG_PROCESS_THREAD);

        //任何步骤失败，程序退出, 若成功则循环等待或者join
    }

    public void start() {
        try {
            init();
        } catch (Exception e) {
            log.error("初始化失败 :", e);
            System.err.println("初始化失败 :");
            e.printStackTrace();
            return;
        }
        startThread();

        //test code
        /*try {
            Thread.sleep(10000);
            LunarCalendar timeToSchool = new LunarCalendar();
            timeToSchool.addMinute(1);
            Reminder reminder1 = new Reminder(false, timeToSchool.clone(), Repeat.DAY, "该上课了", "");
            reminderMgr.add(reminder1);
            timeToSchool.addMinute(1);
            reminder1 = new Reminder(false, timeToSchool.clone(), Repeat.DAY, "该睡觉了", "");
            reminderMgr.add(reminder1);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

    }

    private void startThread() {
        Iterator<Map.Entry<ThreadType, BasicThread>> iterator = threadMap.entrySet().iterator();
        Map.Entry<ThreadType, BasicThread> tmp;
        while (iterator.hasNext()) {
            tmp = iterator.next();
            tmp.getValue().start();
        }
    }
}
