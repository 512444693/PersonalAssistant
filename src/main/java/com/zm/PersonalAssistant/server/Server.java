package com.zm.PersonalAssistant.server;

import com.zm.PersonalAssistant.DataPersistence.DropBox;
import com.zm.PersonalAssistant.DataPersistence.SerializeObject;
import com.zm.PersonalAssistant.DataPersistence.SyncToCloud;
import com.zm.PersonalAssistant.Reminder.Reminder;
import com.zm.PersonalAssistant.Reminder.ReminderMgr;
import com.zm.PersonalAssistant.Reminder.Repeat;
import com.zm.PersonalAssistant.configuration.MyConfig;
import com.zm.PersonalAssistant.task.DataPersistenceTask;
import com.zm.PersonalAssistant.utils.LunarCalendar;
import org.apache.log4j.PropertyConfigurator;

import java.io.File;
import java.io.IOException;
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

    private static final Server instance = new Server();

    private Thread persistenceThread;

    private Server() {}

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

    public void init() throws IOException {
        //0.log4j配置
        PropertyConfigurator.configure(Server.CONFIGURATION_DIRECTORY_PATH +  "log4j.properties");

        //1.读取配置文件
        config = new MyConfig(CONFIGURATION_DIRECTORY_PATH +  "conf.properties");

        //2.从文件恢复数据,或创建新数据
        Object object = SerializeObject.deserialize(ReminderMgr.class);
        if(object != null) {
            reminderMgr = (ReminderMgr) object;
        } else {
            reminderMgr = ReminderMgr.getInstance();
        }

        if(config.isUseDropBox()) {
            DropBox dropBox = new DropBox(CONFIGURATION_DIRECTORY_PATH + "DropBox.auth");
            syncToCloud = new SyncToCloud(dropBox);
        }

        //3.创建线程
        persistenceThread = new Thread(new DataPersistenceTask());

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

        //test code
        try {
            Thread.sleep(60000);
            LunarCalendar timeToSchool = new LunarCalendar(false, 2016, 6, 26, 0, 0);
            Reminder reminder1 = new Reminder(false, timeToSchool.clone(), Repeat.DAY, "该上课了", "1/hour");
            reminderMgr.add(reminder1);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    //TODO : 配置：2.client邮箱配置 server邮箱配置  邮箱其它配置 3.收取邮件时间间隔
}
