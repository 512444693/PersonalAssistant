package com.zm.PersonalAssistant.thread;

import com.zm.PersonalAssistant.ThreadMsg.ThreadMsg;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import static com.zm.PersonalAssistant.utils.Log.log;

/**
 * Created by Administrator on 2016/7/2.
 */
public abstract class NoBlockingThread extends BasicThread {
    private BlockingQueue<ThreadMsg> msgQueue = new LinkedBlockingDeque();
    private int delayTime;//单位ms

    public NoBlockingThread(ThreadType threadType, int delayTime) {
        super(threadType);
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
                Thread.sleep(delayTime);
            } catch (InterruptedException e) {
                log.error("非阻塞线程" + getThreadType() + "睡眠被中断", e);
            }
        }
    }

    protected abstract void process(ThreadMsg msg);
    protected abstract void init();
    protected abstract void otherProcess();
}
