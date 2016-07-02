package com.zm.PersonalAssistant.task;

import com.zm.PersonalAssistant.DataPersistence.Persistence;
import com.zm.PersonalAssistant.DataPersistence.SerializeObject;
import com.zm.PersonalAssistant.DataPersistence.SyncToCloud;
import com.zm.PersonalAssistant.ThreadMsg.ThreadMsg;
import com.zm.PersonalAssistant.configuration.MyConfig;
import com.zm.PersonalAssistant.server.Server;

import static com.zm.PersonalAssistant.utils.Log.log;

/**
 * Created by Administrator on 2016/7/2.
 */
public class DataPersistenceImlTask extends BasicTask {
    private SyncToCloud syncToCloud;
    private MyConfig config;
    //现在是一个对象，以后可以改成列表，持久化多个对象
    private Persistence persistence;

    public DataPersistenceImlTask(TaskType taskType) {
        super(taskType);
    }

    @Override
    public void putThreadMsg(ThreadMsg msg) {

    }

    @Override
    public void run() {
        init();

        while (true) {

            if(persistence.changed()) {
                SerializeObject.serialize(persistence);
                persistence.clearChanged();
                log.debug("序列化" + persistence.getClass().getName() + "成功");

                if(config.isUseDropBox()) {
                    syncToCloud.upload(Server.DATA_DIRECTORY_PATH + persistence.getClass().getName());
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
        config = Server.getInstance().getConfig();
        persistence = Server.getInstance().getReminderMgr();

        if(config.isUseDropBox()) {
            syncToCloud = Server.getInstance().getSyncToCloud();
            //同步所有文件，除了logs目录
            syncToCloud.uploadExcept(".", "logs");
        }
    }
}
