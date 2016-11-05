package com.zm.PersonalAssistant.thread;

import com.zm.PersonalAssistant.Reminder.AdvancedUnit;
import com.zm.PersonalAssistant.Reminder.Reminder;
import com.zm.PersonalAssistant.Reminder.ReminderMgr;
import com.zm.PersonalAssistant.Reminder.Repeat;
import com.zm.PersonalAssistant.Reminder.LunarCalendar;
import com.zm.PersonalAssistant.configuration.MyDef;
import com.zm.PersonalAssistant.server.PAServer;
import com.zm.frame.thread.msg.StringMsgBody;
import com.zm.frame.thread.msg.ThreadMsg;
import com.zm.frame.thread.thread.BlockingThread;
import com.zm.frame.utils.StringUtils;

import static com.zm.frame.log.Log.log;

/**
 * Created by Administrator on 2016/7/3.
 */
public class UserMsgProcessThreadIml extends BlockingThread {


    public UserMsgProcessThreadIml(int threadType, int threadId) {
        super(threadType, threadId);
    }

    @Override
    protected void processMsg(ThreadMsg msg) {
        String req = ((StringMsgBody) msg.msgBody).getMsgBody();
        log.debug("请求处理线程收到消息：" + msg.toString() + ", body :" + req);
        switch (msg.msgType) {
            case MyDef.MSG_TYPE_REQ:
                String replay = processUserReq(req);
                replayThreadMsg(msg, MyDef.MSG_TYPE_REPLY, new StringMsgBody(replay));
                break;
            default:
                log.error("请求处理线程收到不支持的消息类型 " + msg.msgType);
        }
    }

    private String processUserReq(String req) {
        this.setArgs(req.split("\\s+"));
        String commandStr = getNextArg();
        try {
            command = Integer.parseInt(commandStr);
            log.debug("收到命令为" + command + "的用户请求");
        } catch (Exception e) {
            log.error("命令错误: " + commandStr, e);
            return "命令错误：" + commandStr;
        }
        return  UserReqType.getUserReqTypeByOrdinal(command).process();
    }

    private void setArgs(String[] args) {
        this.args = args;
        this.argIndex = -1;
        StringBuffer sb = new StringBuffer("用户参数如下:\r\n");
        for(String str : args) {
            sb.append(str).append("-");
        }
        log.debug(sb);
    }

    private static String getNextArg() {
        while(argIndex < args.length) {
            argIndex++;
            if(!args[argIndex].equals("")) {
                return args[argIndex];
            }
        }
        return null;
    }

    @Override
    protected void init() {}

    private static ReminderMgr reminderMgr = PAServer.getInstance().getReminderMgr();
    private static String[] args;
    private static int argIndex = -1;
    private static int command = 0;

    enum UserReqType {
        GET_ALL_COMMANDS("得到所有命令") {
            public String process() {
                StringBuilder sb = new StringBuilder(GET_ALL_COMMANDS.getDescribe() + ": \r\n");
                UserReqType[] userReqTypes = UserReqType.values();
                //最后一个未知命令不加入处理
                for(int i = 0; i < (userReqTypes.length - 1); i++) {
                    UserReqType urt = userReqTypes[i];
                    sb.append(urt.ordinal()).append(". ").append(urt.getDescribe()).append("\r\n");
                }
                return sb.toString();
            }
        },
        GET_ALL_REMINDERS("得到所有提醒事项") {
            public String process() {
                return GET_ALL_REMINDERS.getDescribe() + "：\r\n" +reminderMgr.getAllReminderStr();
            }
        },
        REMOVE_REMINDER_ACCORDING_TO_NUM("根据编号删除提醒事项") {
            public String process() {
                StringBuffer ret = new StringBuffer(REMOVE_REMINDER_ACCORDING_TO_NUM.getDescribe() + ":\r\n");
                String reminderNumStr = getNextArg();
                if(reminderNumStr == null) {
                    ret.append("缺少编号参数");
                    return ret.toString();
                }
                int reminderNum = 0;
                try {
                    reminderNum = Integer.parseInt(reminderNumStr);
                    ret.append(reminderMgr.removeAccordingToNumber(reminderNum));
                } catch (NumberFormatException e) {
                    log.error("提醒事项编号错误: " + reminderNum, e);
                    return "提醒事项编号错误: " + reminderNumStr;
                } catch (IllegalArgumentException e) {
                    ret.append(e.getMessage());
                }
                return ret.toString();
            }
        },
        ADD_REMINDER("添加提醒事项") {
            public String process() {
                StringBuffer ret = new StringBuffer(ADD_REMINDER.getDescribe() + ":\r\n");
                Reminder reminder;
                try {
                    //是否按照阴历
                    Boolean ifLunar = parseToIfLunar(getNextArg());
                    log.debug("按照阴历: " + ifLunar);
                    //提醒时间
                    LunarCalendar reminderTime = parseToLunarCalendar(getNextArg(), ifLunar);
                    log.debug("提醒时间是: " + reminderTime);
                    //重复提醒
                    Repeat repeat = parseToRepeat(getNextArg());
                    log.debug("重复提醒: " + repeat);
                    //提前提醒
                    String advancedNotifyStr = parseToAdvancedNotifyStr(getNextArg());
                    log.debug("提前提醒: " + advancedNotifyStr);
                    //事件信息
                    String info = getNextArg();
                    log.debug("事件信息: " + info);
                    reminder = new Reminder(ifLunar, reminderTime, repeat, info, advancedNotifyStr);

                } catch (Exception e) {
                    log.error("添加提醒事项失败 :", e);
                    return ret.append("添加提醒事项失败 :" + e.getMessage()).toString();
                }
                reminderMgr.add(reminder);
                ret.append("添加提醒事项成功: " + reminder);
                return ret.toString();
            }
        },
        UN_KNOW_COMMAND("未知命令") {
            public String process() {
                return UN_KNOW_COMMAND.getDescribe() + ": " + command;
            }
        };

        private static String parseToAdvancedNotifyStr(String nextArg) {
            if(nextArg.indexOf("不") != -1) {
                return "";
            }
            return nextArg;
        }

        private static LunarCalendar parseToLunarCalendar(String nextArg, Boolean ifLunar) {
            LunarCalendar ret;
            if(nextArg.indexOf("后") != -1) {//按照多少时间后提醒来处理
                ret = new LunarCalendar();

                AdvancedUnit timeTypeLater = null;
                AdvancedUnit[] aus = AdvancedUnit.values();
                for(AdvancedUnit au : aus) {
                    if(nextArg.indexOf(au.getDescribe()) != -1) {
                        timeTypeLater = au;
                    }
                }
                if(timeTypeLater == null) {
                    throw new IllegalArgumentException("时间不正确：" + nextArg);
                }

                int num = StringUtils.getStartInt(nextArg);
                switch (timeTypeLater) {
                    case MINUTE:    ret.addMinute(num);     break;
                    case HOUR:  ret.addHour(num);   break;
                    case DAY:   ret.addDate(num);   break;
                    case WEEK:  ret.addWeek(num);   break;
                    case MONTH: ret.addMonth(num);  break;
                }
                return ret;
            } else {//按照绝对时间处理
                String[] times = nextArg.split("/");
                if(times.length != 5)
                    throw new IllegalArgumentException("时间不正确：" + nextArg);
                int year = Integer.parseInt(times[0]);
                int month = Integer.parseInt(times[1]);
                int date = Integer.parseInt(times[2]);
                int hour = Integer.parseInt(times[3]);
                int minute = Integer.parseInt(times[4]);
                ret = new LunarCalendar(ifLunar, year, month, date, hour, minute);
                if(ret.compareTo(new LunarCalendar()) < 0) {
                    throw new IllegalArgumentException("提醒时间小于现在时间");
                }
                return ret;
            }
        }

        private static Boolean parseToIfLunar(String nextArg) {
            if(nextArg.indexOf("阳") != -1) {
                return false;
            }
            return true;
        }

        private static Repeat parseToRepeat(String nextArg) {
            Repeat[] repeats = Repeat.values();
            for(Repeat tmp : repeats) {
                if(nextArg.indexOf(tmp.getDescribe()) != -1) {
                    return tmp;
                }
            }
            throw new IllegalArgumentException("重复参数不正确：" + nextArg);
        }

        public static UserReqType getUserReqTypeByOrdinal(int ordinal) {
            UserReqType[] userReqTypes = UserReqType.values();
            for(UserReqType urt : userReqTypes) {
                if(urt.ordinal() == ordinal)
                    return urt;
            }
            return UN_KNOW_COMMAND;
        }

        private String describe;
        UserReqType(String describe) { this.describe = describe; }
        public String getDescribe() { return this.describe; }
        public String process() {return "";}
    }
}

//[]方括号为必须
//[3]   按[阳/阴农]历    [10][分钟/小时/天/星期/月][后]   重复[从不/每天/每周/每月/每年]    提前[1/minute;2/hour;3/day;3/week;5/month]/[不]    [做什么]
//[3]   按[阳/阴农]历    [2016/7/3/21/14]                 重复[从不/每天/每周/每月/每年]    提前[1/minute;2/hour;3/day;3/week;5/month]/[不]    [做什么]