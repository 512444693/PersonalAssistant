package com.zm.PersonalAssistant.server;

import com.zm.PersonalAssistant.DataPersistence.DropBox;
import com.zm.PersonalAssistant.DataPersistence.SerializeObject;
import com.zm.PersonalAssistant.DataPersistence.SyncToCloud;
import com.zm.PersonalAssistant.Reminder.ReminderMgr;
import com.zm.PersonalAssistant.configuration.MyConfig;
import com.zm.PersonalAssistant.configuration.MyDef;
import com.zm.frame.conf.Definition;
import com.zm.frame.thread.server.ThreadServer;
import com.zm.frame.thread.thread.MyThreadGroup;

import java.io.IOException;

import static com.zm.frame.log.Log.log;

/**
 * Created by Administrator on 2016/11/5.
 */
public class PAServer {

    private static final PAServer instance = new PAServer();

    private MyConfig config;
    private ReminderMgr reminderMgr;
    private SyncToCloud syncToCloud;

    private PAServer() {}

    public static PAServer getInstance() {
        return instance;
    }

    public MyConfig getConfig() {
        return config;
    }

    public ReminderMgr getReminderMgr() {
        return reminderMgr;
    }

    public SyncToCloud getSyncToCloud() {
        return syncToCloud;
    }

    public void init() throws IOException {

        //1.读取配置文件
        config = new MyConfig(Definition.CONFIGURATION_DIRECTORY_PATH +  "conf.properties");

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
            DropBox dropBox = new DropBox(Definition.CONFIGURATION_DIRECTORY_PATH + "DropBox.auth");
            syncToCloud = new SyncToCloud(dropBox);
        }

        new PAClassFactory();

        //3.创建线程, 注意，一种类型的线程只能创建一个
        new MyThreadGroup(MyDef.THREAD_TYPE_PERSISTENCE, 1, null);
        new MyThreadGroup(MyDef.THREAD_TYPE_CHECK_NOTIFY, 1, null);
        new MyThreadGroup(MyDef.THREAD_TYPE_PROCESS, 1, null);
        new MyThreadGroup(MyDef.THREAD_TYPE_REC_AND_SEND, 1, null);
        new MyThreadGroup(MyDef.THREAD_TYPE_HTTP_LISTEN, 1, new String[] {config.getPort()});

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
        ThreadServer.getInstance().startThreads();
    }
}
