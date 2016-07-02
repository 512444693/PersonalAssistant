package com.zm.PersonalAssistant.task;

import com.zm.PersonalAssistant.ThreadMsg.ThreadMsg;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import static com.zm.PersonalAssistant.utils.Log.log;

/**
 * Created by Administrator on 2016/7/2.
 */
public abstract class NoBlockingTask extends BasicTask {
    private BlockingQueue<ThreadMsg> msgQueue = new LinkedBlockingDeque();
    private int delayTime;

    public NoBlockingTask(TaskType taskType, int delayTime) {
        super(taskType);
        this.delayTime = delayTime;
    }

    @Override
    public void putThreadMsg(ThreadMsg msg) {
        msgQueue.offer(msg);
    }

    @Override
    public void run() {
        init();
        while (true) {
            ThreadMsg msg = msgQueue.poll();
            if(msg != null) {
                process(msg);
            }
            otherProcess();
            try {
                Thread.sleep(delayTime * 1000);
            } catch (InterruptedException e) {
                log.error("非阻塞线程睡眠被中断", e);
            }
        }
    }

    protected abstract void process(ThreadMsg msg);
    protected abstract void init();
    protected abstract void otherProcess();
}
