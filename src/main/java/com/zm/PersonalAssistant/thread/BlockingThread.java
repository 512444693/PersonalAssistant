package com.zm.PersonalAssistant.thread;

import com.zm.PersonalAssistant.ThreadMsg.ThreadMsg;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import static com.zm.PersonalAssistant.utils.Log.log;

/**
 * Created by Administrator on 2016/7/2.
 */
public abstract class BlockingThread extends BasicThread {

    private BlockingQueue<ThreadMsg> msgQueue = new LinkedBlockingDeque();

    public BlockingThread(ThreadType threadType) {
        super(threadType);
    }

    @Override
    public void putThreadMsg(ThreadMsg msg) {
        try {
            msgQueue.put(msg);
        } catch (InterruptedException e) {
            log.error("塞消息中断异常", e);
        }
    }


    @Override
    public void run() {
        init();
        while(true) {
            try {
                ThreadMsg msg= msgQueue.take();
                process(msg);
            } catch (InterruptedException e) {
                log.error("取消息中断异常", e);
            }
        }
    }

    protected abstract void process(ThreadMsg msg);
    protected abstract void init();
}
