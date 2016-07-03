package com.zm.PersonalAssistant.UI;

import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPStore;

import static com.zm.PersonalAssistant.utils.Log.log;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;
import java.util.Random;

/**
 * Created by zhangmin on 2016/6/28.
 */
public class Mail {
    private static final String MAIL_SERVER_HOST_SMTP = "smtp.163.com";

    private static final String MAIL_SERVER_HOST_IMAP = "imap.163.com";

    private static final String[] sourceText =  {"卫", "风", "氓", "周", "南", "关", "雎", "秦", "风", "蒹", "葭", "小", "雅", "采", "薇", "迢", "迢", "牵", "牛", "星", "行", "行", "重", "行", "行", "短", "歌", "行", "观", "沧", "海", "美", "女", "篇", "白", "马", "篇", "七", "步", "诗", "燕", "歌", "行", "归", "园", "田", "居", "饮", "酒", "送", "杜", "少", "府", "之", "任", "蜀", "州", "次", "北", "固", "山", "下", "使", "至", "塞", "上", "山", "居", "秋", "暝", "望", "岳", "春", "望", "茅", "屋", "为", "秋", "风", "所", "破", "歌", "兵", "车", "行", "闻", "官", "军", "收", "河", "南", "河", "北", "咏", "怀", "古", "迹", "?", "其", "三", "登", "高", "客", "至", "蜀", "相", "旅", "夜", "书", "怀", "闻", "王", "昌", "龄", "左", "迁", "龙", "标", "遥", "有", "此", "寄", "行", "路", "难", "蜀", "道", "难", "梦", "游", "天", "姥", "吟", "留", "别", "将", "进", "酒", "白", "雪", "歌", "送", "武", "判", "官", "归", "京", "早", "春", "呈", "水", "部", "张", "十", "八", "员", "外", "酬", "乐", "天", "扬", "州", "初", "逢", "席", "上", "见", "赠", "观", "刈", "麦", "钱", "塘", "湖", "春", "行", "琵", "琶", "行", "长", "相", "思", "离", "思", "?", "其", "四", "遣", "悲", "怀", "?", "其", "二", "雁", "门", "太", "守", "行", "李", "凭", "箜", "篌", "引", "山", "行", "江", "南", "春", "过", "华", "清", "宫", "秋", "夕", "赤", "壁", "泊", "秦", "淮", "赠", "别", "?", "其", "二", "锦", "瑟", "夜", "雨", "寄", "北", "无", "题", "商", "山", "早", "行", "菩", "萨", "蛮", "小", "山", "重", "叠", "金", "明", "灭", "虞", "美", "人", "春", "花", "秋", "月", "何", "时", "了", "相", "见", "欢", "无", "言", "独", "上", "西", "楼", "浪", "淘", "沙", "帘", "外", "雨", "潺", "潺", "雨", "霖", "铃", "蝶", "恋", "花", "伫", "倚", "危", "楼", "风", "细", "细", "渔", "家", "傲", "秋", "思", "浣", "溪", "沙", "", "", "一", "曲", "新", "词", "酒", "一", "杯", "桂", "枝", "香", "金", "陵", "怀", "古", "梅", "花", "登", "飞", "来", "峰", "念", "奴", "娇", "赤", "壁", "怀", "古", "江", "城", "子", "密", "州", "出", "猎", "江", "城", "子", "乙", "卯", "正", "月", "二", "十", "日", "夜", "记", "梦", "水", "调", "歌", "头", "明", "月", "几", "时", "有", "卜", "算", "子", "我", "住", "长", "江", "头", "鹊", "桥", "仙", "纤", "云", "弄", "巧", "春", "日", "青", "玉", "案", "凌", "波", "不", "过", "横", "塘", "路", "苏", "幕", "遮", "燎", "沉", "香", "一", "剪", "梅", "红", "藕", "香", "残", "玉", "簟", "秋", "如", "梦", "令", "常", "记", "溪", "亭", "日", "暮", "声", "声", "慢", "寻", "寻", "觅", "觅", "醉", "花", "阴", "薄", "雾", "浓", "云", "愁", "永", "昼", "武", "陵", "春", "风", "住", "尘", "香", "花", "已", "尽", "点", "绛", "唇", "寂", "寞", "深", "闺", "夏", "日", "绝", "句", "卜", "算", "子", "咏", "梅", "游", "山", "西", "村", "示", "儿", "十", "一", "月", "四", "日", "风", "雨", "大", "作", "沈", "园", "二", "首", "钗", "头", "凤", "红", "酥", "手", "永", "遇", "乐", "京", "口", "北", "固", "亭", "怀", "古", "破", "阵", "子", "醉", "里", "挑", "灯", "看", "剑", "水", "龙", "吟", "登", "建", "康", "赏", "心", "亭", "青", "玉", "案", "元", "夕", "摸", "鱼", "儿", "雁", "丘", "词", "过", "零", "丁", "洋", "天", "净", "沙", "秋", "思", "山", "坡", "羊", "潼", "关", "怀", "古", "山", "坡", "羊", "骊", "山", "怀", "古", "画", "堂", "春", "一", "生", "一", "代", "一", "双", "人", "木", "兰", "花", "令", "拟", "古", "决", "绝", "句", "长", "相", "思", "山", "一", "程", "己", "亥", "杂", "诗"};

    private static final Random random = new Random(new Date().getTime());

    private String user;

    private String password;

    private String mailTo;

    private final Session session;

    private final Transport transport;

    private final IMAPStore store;

    public Mail(String user, String password, String mailTo) throws MessagingException {
        this.user = user;;
        this.password = password;
        this.mailTo = mailTo;

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

    public void send(String text) {
        sendTextToFixedClient(genRandomText(20), text + "\r\n\r\n================\r\n" + genRandomText(35));
    }

    private static String genRandomText(int num) {
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < num; i++) {
            sb.append(sourceText[random.nextInt(sourceText.length)]);
        }
        return sb.toString();
    }

    private void sendTextToFixedClient(String subject, String text) {
        sendText(mailTo, subject, text);
    }

    private void sendText(String mailTo, String subject, String text) {

        //1、创建邮件
        MimeMessage message = new MimeMessage(session);

        try {
            //2、邮件消息头
            //邮件发件人
            message.setFrom(new InternetAddress(user));
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
            transport.connect(MAIL_SERVER_HOST_SMTP, user, password);

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

    public String  rec() {
        String ret = "";

        IMAPFolder folder = null;

        try {
            //1、连接服务器
            if(!store.isConnected()) {
                store.connect(MAIL_SERVER_HOST_IMAP, user, password);
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
                if (!isSeen(message) && from.equals(mailTo)) {
                    //process(message);
                    String subject = getSubject(message);
                    String Content = getContent(message);
                    log.debug("发件人:" + from + "\r\n" + "主题:" + subject + "\r\n" + "内容:" + Content);
                    ret = Content;
                }
                //无论是否已读，都删除
                setSeenAndDelete(message);
                log.debug("删除邮件，发件人:" + from);
            }

        } catch (MessagingException e) {
            log.error("收取邮件异常", e);
        } catch (IOException e) {
            log.error("获取邮件信息失败", e);
        } finally {
            //4、关闭folder
            try {
                if (folder != null)
                    folder.close(true);
            } catch (MessagingException e) {
                log.error("folder or store close exception", e);
            }
        }
        return ret;
    }

/*    protected void process(Message message) {
        try {
            String from = getFrom(message);
            String subject = getSubject(message);
            String Content = getContent(message);
            log.debug("发件人:" + from + "\r\n" + "主题:" + subject + "\r\n" + "内容:" + Content);

            //自动回复
            MimeMessage replayMessage = (MimeMessage) message.reply(true);
            replayMessage.setFrom(new InternetAddress(user));
            replayMessage.setRecipients(MimeMessage.RecipientType.TO, message.getFrom());
            replayMessage.setText("This is replay mail, good luck");
            replayMessage.saveChanges();
            send(replayMessage);
        } catch (MessagingException e) {
            log.error("处理：回复邮件失败", e);
        } catch (IOException e) {
            log.error("获取邮件信息失败", e);
        }
    }*/

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
}
