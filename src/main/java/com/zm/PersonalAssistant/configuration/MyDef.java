package com.zm.PersonalAssistant.configuration;

import java.io.File;

/**
 * Created by Administrator on 2016/11/5.
 */
public class MyDef {

    public static final String DATA_DIRECTORY_PATH = "data" + File.separator;

    //线程类型
    public static final int THREAD_TYPE_PERSISTENCE = 1001;
    public static final int THREAD_TYPE_CHECK_NOTIFY = 1002;
    public static final int THREAD_TYPE_PROCESS = 1003;
    public static final int THREAD_TYPE_HTTP_LISTEN = 1004;
    public static final int THREAD_TYPE_REC_AND_SEND = 1005;

    //消息类型
    public static final int MSG_TYPE_NOTIFY = 2001;
    public static final int MSG_TYPE_REQ = 2002;
    public static final int MSG_TYPE_REPLY = 2003;
    public static final int MSG_TYPE_WX = 2004;

    //task类型
     public static final int TASK_TYPE_WX = 3001;
}
