package com.zm.PersonalAssistant.server;

import com.zm.PersonalAssistant.DataPersistence.DropBox;
import com.zm.PersonalAssistant.DataPersistence.SerializeObject;
import com.zm.PersonalAssistant.DataPersistence.SyncToCloud;
import com.zm.PersonalAssistant.Reminder.Reminder;
import com.zm.PersonalAssistant.Reminder.ReminderMgr;
import com.zm.PersonalAssistant.Reminder.Repeat;
import com.zm.PersonalAssistant.UI.Mail;
import com.zm.PersonalAssistant.configuration.MyConfig;
import com.zm.PersonalAssistant.task.*;
import com.zm.PersonalAssistant.ThreadMsg.ThreadMsg;
import com.zm.PersonalAssistant.utils.LunarCalendar;
import org.apache.log4j.PropertyConfigurator;

import javax.mail.MessagingException;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import static com.zm.PersonalAssistant.task.TaskType.NONE;
import static com.zm.PersonalAssistant.task.TaskType.REC_SEND_TASK;
import static com.zm.PersonalAssistant.task.TaskType.USER_MSG_PROCESS_TASK;
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

    private ConcurrentHashMap<TaskType, BasicTask> taskMap = new ConcurrentHashMap<>();
    private Thread persistenceThread;
    private Thread checkNotifyThread;
    private Thread recAndSendThread;
    private Thread userMsgProcessThread;

    private Server() {}

    public void addTask(TaskType taskType, BasicTask task) {
        taskMap.put(taskType, task);
    }

    public void sendThreadMsgTo(ThreadMsg msg) {
        BasicTask task = taskMap.get(msg.getDesTask());
        if(task != null) {
            task.putThreadMsg(msg);
        } else {
            log.error("发送线程消息失败, 找不到TaskType为" + msg.getDesTask() + "的线程（Task）");
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

        //3.创建线程
        persistenceThread = new Thread(new DataPersistenceImlTask(NONE));
        checkNotifyThread = new Thread(new CheckNotifyImlTask(NONE));
        recAndSendThread = new Thread(new RecAndSendTaskImlTask(REC_SEND_TASK,
                config.getRecAndSendInterval()));
        userMsgProcessThread = new Thread(new UserMsgProcessImlTask(USER_MSG_PROCESS_TASK));

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
        persistenceThread.start();
        checkNotifyThread.start();
        recAndSendThread.start();
        userMsgProcessThread.start();

        //test code
        try {
            Thread.sleep(10000);
            LunarCalendar timeToSchool = new LunarCalendar(false, 2016, 7, 2, 11, 55);
            Reminder reminder1 = new Reminder(false, timeToSchool.clone(), Repeat.DAY, "该上课了", "");
            reminderMgr.add(reminder1);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
