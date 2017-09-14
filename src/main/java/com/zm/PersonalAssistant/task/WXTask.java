package com.zm.PersonalAssistant.task;

import com.qq.weixin.mp.aes.WXBizMsgCrypt;
import com.sun.net.httpserver.HttpExchange;
import com.zm.PersonalAssistant.configuration.MyDef;
import com.zm.PersonalAssistant.server.PAServer;
import com.zm.PersonalAssistant.thread.msg.WXMsgBody;
import com.zm.frame.thread.msg.StringMsgBody;
import com.zm.frame.thread.msg.ThreadMsg;
import com.zm.frame.thread.task.Task;
import com.zm.frame.thread.thread.BasicThread;
import com.zm.frame.utils.HttpExchangeUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.util.Map;

import static com.zm.frame.log.Log.log;

/**
 * Created by Administrator on 2016/11/5.
 */
public class WXTask extends Task {
    private WXBizMsgCrypt wxcpt = null;
    private HttpExchange httpExchange = null;
    private Map<String, String> parameters = null;
    private String sMsgSig = "";
    private String sTimeStamp = "";
    private String sNonce = "";
    private String sVerifyEchoStr = "";
    private RequestInfo requestInfo;
    private String user = PAServer.getInstance().getConfig().getUser();

    private final static String RESPONSEXML =
            "<xml>" +
                    "<ToUserName><![CDATA[%s]]></ToUserName>" +
                    "<FromUserName><![CDATA[%s]]></FromUserName>" +
                    "<CreateTime>%s</CreateTime>" +
                    "<MsgType><![CDATA[text]]>" +
                    "</MsgType><Content><![CDATA[%s]]>" +
                    "</Content>" +
                    "</xml>";

    public WXTask(int taskId, BasicThread thread, int time) {
        super(taskId, thread, time);
    }

    @Override
    public void processMsg(ThreadMsg msg) {
        switch (msg.msgType) {
            case MyDef.MSG_TYPE_WX:
                WXMsgBody body = (WXMsgBody) msg.msgBody;
                //初始化
                this.wxcpt = body.wxcpt;
                this.httpExchange = body.httpExchange;
                //解析参数
                parameters = HttpExchangeUtils.getParameters(httpExchange);
                sMsgSig = parameters.get("msg_signature");
                sTimeStamp = parameters.get("timestamp");
                sNonce = parameters.get("nonce");
                sVerifyEchoStr = parameters.get("echostr");
                //处理
                processReq();
                break;
            case MyDef.MSG_TYPE_REPLY:
                processReply(((StringMsgBody)msg.msgBody).getMsgBody());
                break;
            default:
                log.error("微信task收到不支持的线程消息类型 " + msg.msgType);
        }
    }

    private void processReq() {
        //// 参数个数为4，验证url有效性（开启回调模式）
        if (parameters.size() == 4) {
            String sEchoStr; //需要返回的明文
            try {
                sEchoStr = wxcpt.VerifyURL(sMsgSig, sTimeStamp,
                        sNonce, sVerifyEchoStr);
                //log.debug("verifyurl echostr: " + sEchoStr);
                // 验证URL成功，将sEchoStr返回
                HttpExchangeUtils.response(httpExchange, sEchoStr);
            } catch (Exception e) {
                log.error("验证URL失败，错误原因请查看异常");
                log.error(e);
            }
            remove();
            log.debug("WX task verify finished, delete itself");
        }
        //回调
        else if(parameters.size() == 3) {
            requestInfo = null;
            String sReqData = HttpExchangeUtils.getContent(httpExchange);
            try {
                String sMsg = wxcpt.DecryptMsg(sMsgSig, sTimeStamp, sNonce, sReqData);
                //log.debug("after decrypt msg: " + sMsg);
                requestInfo = RequestInfo.getFromXml(sMsg);
                log.debug("收到 " + requestInfo.getFromUserName() + " 发来的信息 : " + requestInfo.getContent());
                if (requestInfo.getFromUserName().equals(user)) {
                    sendThreadMsgTo(MyDef.MSG_TYPE_REQ, new StringMsgBody(requestInfo.getContent()),
                            MyDef.THREAD_TYPE_PROCESS);
                } else {
                    log.error("Wrong user : " + requestInfo.getFromUserName());
                }
            } catch (Exception e) {
                log.error("解密失败，失败原因请查看异常");
                log.error(e);
            }
        }
    }

    private void processReply(String replay) {
        String sRespData = String.format(RESPONSEXML, requestInfo.getFromUserName(),
                requestInfo.getToUserName(), System.currentTimeMillis() / 1000, replay);
        try{
            String sEncryptMsg = wxcpt.EncryptMsg(sRespData, sTimeStamp, sNonce);
            //log.debug("after encrypt sEncrytMsg: " + sEncryptMsg);
            // 加密成功
            HttpExchangeUtils.response(httpExchange, sEncryptMsg);
            log.debug("回包正确：" + replay);
        }
        catch(Exception e) {
            log.error("加密失败");
            log.error(e);
        }
        remove();
        log.debug("WX task reply finished, delete itself");
    }
}

class RequestInfo {
    private String toUserName;
    private String fromUserName;
    private String content;

    public static RequestInfo getFromXml(String sMsg) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        StringReader sr = new StringReader(sMsg);
        InputSource is = new InputSource(sr);
        Document document = db.parse(is);

        Element root = document.getDocumentElement();
        String ToUserName = root.getElementsByTagName("ToUserName").item(0).getTextContent();
        String FromUserName = root.getElementsByTagName("FromUserName").item(0).getTextContent();
        String Content = root.getElementsByTagName("Content").item(0).getTextContent();

        return new RequestInfo(ToUserName, FromUserName, Content);
    }

    private RequestInfo(String toUserName, String fromUserName, String content) {
        this.toUserName = toUserName;
        this.fromUserName = fromUserName;
        this.content = content;
    }

    public String getToUserName() {
        return toUserName;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
