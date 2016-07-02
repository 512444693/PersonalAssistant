package com.zm.PersonalAssistant.DataPersistence;

import static com.zm.PersonalAssistant.utils.Log.log;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * 一个类只可以序列化一个实体
 * Created by zhangmin on 2016/7/1.
 */
public class SerializeObject {

    public static void serialize(Object object) {
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
    }


    public static Object deserialize(Class cls) {
        Object object = null;
        ObjectInputStream ois = null;
        try {
            File file = new File(cls.getName());
            if(file.exists() && file.isFile()) {
                ois = new ObjectInputStream(new FileInputStream(file));
                object = ois.readObject();
            }
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
