package com.zm.PersonalAssistant.weixin;

import com.google.gson.Gson;
import com.zm.PersonalAssistant.configuration.MyConfig;
import com.zm.PersonalAssistant.server.PAServer;
import com.zm.frame.utils.HttpsClientUtils;
import static com.zm.frame.log.Log.log;

/**
 * Created by Administrator on 2016/11/5.
 */
public class WXSend {
    private static MyConfig config = PAServer.getInstance().getConfig();
    private static final String user = config.getUser();
    private static final String CORPID = config.getCorpid();
    private static final String CORPSECRET = config.getCorpsecret();
    private static final String GET_TOKEN_URL = String.format(
            "https://qyapi.weixin.qq.com/cgi-bin/gettoken?CORPID=%s&CORPSECRET=%s",
            CORPID, CORPSECRET);

    private static final String SEND_INFO_URL_FORMAT =
            "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=%s";

    private static final String SEND_INFO_JSON_FORMAT = "{\"touser\": \"" + user + "\"," +
            "\"msgtype\": \"text\",\"agentid\": 0,\"text\": {\"content\": \"%s\"},\"safe\":0}";

    public static void send(String info) {
        String acccessTokenJson = HttpsClientUtils.get(GET_TOKEN_URL);
        AccessTokenJson accessTokenJson = AccessTokenJson.GetFromJson(acccessTokenJson);
        ErrorJson errorJson = ErrorJson.GetFromJson(acccessTokenJson);

        if(!(accessTokenJson.getAccess_token() == null)) {//获取access token成功
            String sendInfoUrl = String.format(SEND_INFO_URL_FORMAT, accessTokenJson.getAccess_token());
            String content = String.format(SEND_INFO_JSON_FORMAT, info);

            String sendRetMsg = HttpsClientUtils.post(sendInfoUrl, content);
            errorJson = ErrorJson.GetFromJson(sendRetMsg);
            if(errorJson.getErrcode() == 0) {
                log.debug("消息发送成功 : " + info);
            } else {
                log.error("消息发送失败:" + errorJson.getErrcode() + ":" + errorJson.getErrmsg());
            }
        } else {
            log.error("获取access token失败");
            log.error(errorJson.getErrcode() + ":" + errorJson.getErrmsg());
        }
    }
}

class AccessTokenJson {

    public static AccessTokenJson GetFromJson (String json) {
        return new Gson().fromJson(json, AccessTokenJson.class);
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }

    private String access_token;
    private int expires_in;
}

class ErrorJson {

    public static ErrorJson GetFromJson (String json) {
        return new Gson().fromJson(json, ErrorJson.class);
    }

    public int getErrcode() {
        return errcode;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    private int errcode;
    private String errmsg;
}