package com.zm.PersonalAssistant.DataPersistence;

import com.zm.PersonalAssistant.Reminder.ReminderMgr;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * 一个类只可以序列化一个实体
 * Created by zhangmin on 2016/7/1.
 */
public class SerializeObject {
    private static final Logger log = Logger.getLogger(SerializeObject.class);
    private static final Map<Class, Object> classMap = new HashMap<>();

    public static void serialize(Object object) {

        if(classMap.containsKey(object.getClass()) && classMap.get(object.getClass()) != object) {

            log.error("Serialization fail :" + object.getClass() + " is already exists");

        } else {
            ObjectOutputStream oos = null;
            try {
                oos =  new ObjectOutputStream(new FileOutputStream(object.getClass().getName()));
                oos.writeObject(object);
            } catch (NotSerializableException e) {
                log.error("Not Serializable Exception :", e);
            } catch (IOException e) {
                log.error("IO Exception :", e);
            } finally {
                if(oos != null) {
                    try {
                        oos.close();
                    } catch (IOException e) {
                        log.error("IO Exception :", e);
                    }
                }
            }

            if(!classMap.containsKey(object.getClass())) {
                classMap.put(object.getClass(), object);
            }
        }
    }

    /*public static Object deserialize(Class cls) {
        if(!classMap.containsKey(cls)) {
            log.error("Deserialization fail :" + cls + "is not exists");
            return null;
        } else {
            Object object = classMap.get(cls);
            ObjectInputStream ois = null;
            try {
                ois = new ObjectInputStream(new FileInputStream(cls.getName()));
                object = ois.readObject();
            } catch (ClassNotFoundException e) {
                log.error("Class Not Found Exception :", e);
            } catch (NotSerializableException e) {
                log.error("Not Serializable Exception :", e);
            } catch (IOException e) {
                log.error("IO Exception :", e);
            } finally {
                if(ois != null) {
                    try {
                        ois.close();
                    } catch (IOException e) {
                        log.error("IO Exception :", e);
                    }
                }
            }
            return object;
        }
    }*/

    public static Object deserialize(Class cls) {
        Object object = null;
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new FileInputStream(cls.getName()));
            object = ois.readObject();
        } catch (ClassNotFoundException e) {
            log.error("Class Not Found Exception :", e);
        } catch (NotSerializableException e) {
            log.error("Not Serializable Exception :", e);
        } catch (IOException e) {
            log.error("IO Exception :", e);
        } finally {
            if(ois != null) {
                try {
                    ois.close();
                } catch (IOException e) {
                    log.error("IO Exception :", e);
                }
            }
        }
        return object;
    }
}
