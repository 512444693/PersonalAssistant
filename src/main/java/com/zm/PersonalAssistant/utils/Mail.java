package com.zm.PersonalAssistant.utils;

import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPStore;
import org.apache.log4j.Logger;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

/**
 * Created by zhangmin on 2016/6/28.
 */
public class Mail {

    private static final Logger log = Logger.getLogger(Mail.class);

    private static final String MAIL_SERVER_HOST_SMTP = "smtp.163.com";

    private static final String MAIL_SERVER_HOST_IMAP = "imap.163.com";

    private static final String USER = "information_rec@163.com";

    private static final String PASSWORD = "zhangmin";

    private static final String MAIL_TO = "512444693@qq.com";

    private final Session session;

    private final Transport transport;

    private final IMAPStore store;

    public Mail() throws MessagingException {
        Properties prop = new Properties();

        //prop.setProperty("mail.debug", "true");

        //配置smtp
        prop.setProperty("mail.transport.protocol", "smtp");

        prop.setProperty("mail.smtp.host", MAIL_SERVER_HOST_SMTP);

        prop.setProperty("mail.smtp.auth", "true");

        //配置pop3
        prop.setProperty("mail.store.protocol", "imap");

        prop.setProperty("mail.imap.host", MAIL_SERVER_HOST_IMAP);

        // 1、创建session
        session = Session.getInstance(prop);

        //2、通过session得到transport对象
        try {
            transport = session.getTransport();
        } catch (NoSuchProviderException e) {
            log.error("Get transport error : ", e);
            throw e;
        }

        //3、通过session得到store对象
        try {
            store = (IMAPStore) session.getStore("imap");
        } catch (NoSuchProviderException e) {
            log.error("Get store error : ", e);
            throw e;
        }
        log.info("mail 对象创建成功");
    }

    public void sendTextToFixedClient(String subject, String text) {
        sendText(MAIL_TO, subject, text);
    }

    private void sendText(String mailTo, String subject, String text) {

        //1、创建邮件
        MimeMessage message = new MimeMessage(session);

        try {
            //2、邮件消息头
            //邮件发件人
            message.setFrom(new InternetAddress(USER));
            //邮件收件人
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(mailTo));

            //3、邮件主题
            message.setSubject(subject);

            //4、邮件消息体
            message.setText(text);

            message.saveChanges();

            //5、发送邮件
            send(message);
        } catch (MessagingException e) {
            log.error("构造邮件失败：" + subject, e);
        }
    }

    private void send(MimeMessage message) {
        String subject = "";
        try {
            subject = message.getSubject();

            //1、连上邮件服务器
            transport.connect(MAIL_SERVER_HOST_SMTP, USER, PASSWORD);


            //2、发送邮件
            transport.sendMessage(message, message.getAllRecipients());

            log.info("成功发送邮件 主题: " + subject);
        } catch (MessagingException e) {
            log.error("发送邮件失败 主题：" + subject, e);
        } finally {
            //3、关闭与邮件服务器连接
            if (transport != null) {
                try {
                    transport.close();
                } catch (MessagingException e) {
                    log.error("transport close exception", e);
                }
            }
        }
    }

    public void rec() {

        IMAPFolder folder = null;

        try {
            //1、连接服务器
            if(!store.isConnected()) {
                store.connect(MAIL_SERVER_HOST_IMAP, USER, PASSWORD);
            }

            //2、获得邮箱中的INBOX
            folder = (IMAPFolder) store.getFolder("INBOX");
            folder.open(Folder.READ_WRITE);

            //3、获得INBOX中的所有邮件
            if (folder.getUnreadMessageCount() > 0) {
                log.debug("收取到" + folder.getUnreadMessageCount() + "封未读邮件");
            }
            Message[] messages = folder.getMessages();
            for (Message message : messages) {
                String from = getFrom(message);
                //未读邮件且发件人固定
                if (!isSeen(message) && from.equals(MAIL_TO)) {
                    process(message);
                }
                //无论是否已读，都删除
                setSeenAndDelete(message);
                log.debug("删除邮件，发件人:" + from);
            }

        } catch (MessagingException e) {
            log.error("收取邮件异常", e);
        } finally {
            //4、关闭folder
            try {
                if (folder != null)
                    folder.close(true);
            } catch (MessagingException e) {
                log.error("folder or store close exception", e);
            }
        }
    }

    protected void process(Message message) {
        try {
            String from = getFrom(message);
            String subject = getSubject(message);
            String Content = getContent(message);
            log.debug("发件人:" + from + "\r\n" + "主题:" + subject + "\r\n" + "内容:" + Content);

            //自动回复
            MimeMessage replayMessage = (MimeMessage) message.reply(true);
            replayMessage.setFrom(new InternetAddress(USER));
            replayMessage.setRecipients(MimeMessage.RecipientType.TO, message.getFrom());
            replayMessage.setText("This is replay mail, good luck");
            replayMessage.saveChanges();
            send(replayMessage);
        } catch (MessagingException e) {
            log.error("处理：回复邮件失败", e);
        } catch (IOException e) {
            log.error("获取邮件信息失败", e);
        }
    }

    private String getFrom(Message msg) throws MessagingException {

        InternetAddress address[] = (InternetAddress[]) msg.getFrom();

        String from = address[0].getAddress();

        return from == null ? "" : from.trim();
    }

    private String getSubject(Message msg) throws MessagingException, UnsupportedEncodingException {
        String subject = msg.getSubject();
        return subject == null ? "" : subject.trim();
    }

    private String getContent(Message msg) throws IOException, MessagingException {
        return getContent((Part) msg);
    }

    //有待优化
    private String getContent(Part part) throws MessagingException, IOException {
         if(part.isMimeType("multipart/*")) {
            Multipart multipart = (Multipart) part.getContent();
            int counts = multipart.getCount();
            for (int i = 0; i < counts; i++) {
                if(multipart.getBodyPart(i).isMimeType("text/plain")) {
                    return multipart.getBodyPart(i).getContent().toString().trim();
                }
            }
         }else if(part.isMimeType("text/plain")) {
             return part.getContent().toString().trim();
         }
        return "";
    }
    
    //判断是否为已读
    private boolean isSeen(Message msg) throws MessagingException {
        Flags flags = msg.getFlags();
        if (flags.contains(Flags.Flag.SEEN)) {
            return true;
        }
        return false;
    }

    private void setSeenAndDelete(Message msg) throws MessagingException {
        msg.setFlag(Flags.Flag.SEEN, true);//标记为已读
        msg.setFlag(Flags.Flag.DELETED, true);//删除
    }

    public static void main(String[] args) throws Exception {
        final Mail mail = new Mail();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    mail.rec();
                }
            }
        }).start();
    }

}
