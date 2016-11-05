package com.zm.PersonalAssistant.thread;

import com.zm.PersonalAssistant.DataPersistence.Persistence;
import com.zm.PersonalAssistant.DataPersistence.SerializeObject;
import com.zm.PersonalAssistant.DataPersistence.SyncToCloud;
import com.zm.PersonalAssistant.configuration.MyConfig;
import com.zm.PersonalAssistant.configuration.MyDef;
import com.zm.PersonalAssistant.server.PAServer;
import com.zm.frame.thread.thread.BasicThread;

import static com.zm.frame.log.Log.log;

/**
 * Created by Administrator on 2016/7/2.
 * 持久化，云同步
 */
public class DataPersistenceThreadIml extends BasicThread {
    private SyncToCloud syncToCloud;
    private MyConfig config;
    //现在是一个对象，以后可以改成列表，持久化多个对象
    private Persistence persistence;

    public DataPersistenceThreadIml(int threadType, int threadId) {
        super(threadType, threadId);
    }

    @Override
    public void process() {

        while (true) {
            if(persistence.changed()) {
                SerializeObject.serialize(persistence);
                persistence.clearChanged();
                log.debug("序列化" + persistence.getClass().getName() + "成功");

                if(config.isUseDropBox()) {
                    syncToCloud.upload(MyDef.DATA_DIRECTORY_PATH + persistence.getClass().getName());
                }
            }
            try {
                Thread.sleep(config.getPersistenceInterval() * 1000);
            } catch (InterruptedException e) {
                log.error("中断异常", e);
            }

        }
    }

    public void init() {

        //得到对象
        config = PAServer.getInstance().getConfig();
        persistence = PAServer.getInstance().getReminderMgr();

        //线程启动时云同步所有文件
        if(config.isUseDropBox()) {
            syncToCloud = PAServer.getInstance().getSyncToCloud();
            //同步所有文件，除了logs目录
            syncToCloud.uploadExcept(".", "logs");
        }
    }
}
