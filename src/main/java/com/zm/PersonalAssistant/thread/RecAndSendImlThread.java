package com.zm.PersonalAssistant.thread;

import com.zm.PersonalAssistant.ThreadMsg.StringMsgBody;
import com.zm.PersonalAssistant.ThreadMsg.ThreadMsg;
import com.zm.PersonalAssistant.UI.Mail;
import com.zm.PersonalAssistant.configuration.MyConfig;
import com.zm.PersonalAssistant.server.Server;

import javax.mail.MessagingException;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.*;

import static com.zm.PersonalAssistant.ThreadMsg.ThreadMsgType.USER_REQ_MSG;
import static com.zm.PersonalAssistant.thread.ThreadType.USER_MSG_PROCESS_THREAD;
import static com.zm.PersonalAssistant.utils.Log.log;

/**
 * Created by Administrator on 2016/7/2.
 */
public class RecAndSendImlThread extends NoBlockingThread {

    private Mail mail = Server.getInstance().getMail();

    private MyConfig config = Server.getInstance().getConfig();

    private RecTask recTask = new RecTask();

    private long lastRecTime = new Date().getTime();

    private long recInterval = Server.getInstance().getConfig().getRecInterval() * 1000;

    //创建一个线程池用来发送消息
    private ExecutorService es = Executors.newCachedThreadPool();

    public RecAndSendImlThread(ThreadType threadType, int delayTime) {
        super(threadType, delayTime);
    }

    //SEND_TO_ALL和USER_REPLAY_MSG现在处理方法一样
    @Override
    protected void process(ThreadMsg msg) {
        log.debug("RecAndSendThread process a msg " + msg.getMsgType());
        try {
            switch (msg.getMsgType()) {
                case SEND_TO_ALL:
                    String notify = ((StringMsgBody)msg.getMsgBody()).getMsgBody();
                    //es.submit(new SendTask(notify));
                    es.invokeAny(Arrays.asList(new SendTask(notify)), 20, TimeUnit.SECONDS);
                    log.debug("向客户端发送如下内容：\r\n" + notify);
                    break;
                case USER_REPLAY_MSG:
                    String reply = ((StringMsgBody)msg.getMsgBody()).getMsgBody();
                    //es.submit(new SendTask(reply));
                    es.invokeAny(Arrays.asList(new SendTask(reply)), 20, TimeUnit.SECONDS);
                    log.debug("向客户端发送如下内容：\r\n" + reply);
                    break;
                default:
                    log.error("收到不支持的线程消息类型 " + msg.getMsgType());
            }
        } catch (Exception e) {
            log.error("Send mail time out");
            createNewMailAndSet();
        }
    }

    @Override
    protected void init() {}

    @Override
    protected void otherProcess() {
        long timeNow = new Date().getTime();
        if(timeNow - lastRecTime >=  recInterval) {
            try {
                //10秒超时
                es.invokeAny(Arrays.asList(recTask), 10, TimeUnit.SECONDS);
            } catch (Exception e) {
                log.error("Receive mail time out");
                createNewMailAndSet();
            }
            lastRecTime = timeNow;
        }
    }

    class SendTask implements Callable<Integer> {

        private final String notify;

        public SendTask(String notify) {
            this.notify = notify;
        }

        @Override
        public Integer call() throws Exception {
            mail.send(notify);
            return 0;
        }
    }

    class RecTask implements Callable<String> {

        @Override
        public String call() {
            log.debug("Receiving mail ...");
            String req = "";
            try {
                req = mail.rec();
            } catch (Exception e) {
                log.error("Mail rec error, and create new one");
                createNewMailAndSet();
            }
            if(!req.equals("")) {
                sendThreadMsgTo(USER_MSG_PROCESS_THREAD, USER_REQ_MSG, new StringMsgBody(req));
            }
            return req;
        }
    }

    private void createNewMailAndSet() {
        try {
            mail = new Mail(config.getMailSmtpHost(), config.getMailImapHost(),
                    config.getMailUser(), config.getMailPassword(), config.getMailTo(), config.isUseSmtpSSL());
        } catch (MessagingException e1) {
            log.error("Create new mail error, exit program", e1);
            System.exit(1);
        }
        Server.getInstance().setMail(mail);
    }
}
