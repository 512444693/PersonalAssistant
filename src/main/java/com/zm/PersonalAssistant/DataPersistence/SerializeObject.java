package com.zm.PersonalAssistant.DataPersistence;

import com.zm.PersonalAssistant.server.Server;
import static com.zm.PersonalAssistant.utils.Log.log;
import java.io.*;

/**
 * Created by zhangmin on 2016/7/1.
 */
public class SerializeObject {
    public static final String DIRECTORY_PATH = Server.DATA_DIRECTORY_PATH;
    public static final String TMP_FILE_SUFFIX = ".TMP";

    //创建目录
    static {
        File directory = new File(DIRECTORY_PATH);
        if(!directory.exists() || !directory.isDirectory()) {
            directory.mkdir();
        }
    }

    public static void serialize(Object object) {
        ObjectOutputStream oos = null;
        //先写在临时文件
        File fileTmp = new File(DIRECTORY_PATH + object.getClass().getName() + TMP_FILE_SUFFIX);
        //正式文件
        File file = new File(DIRECTORY_PATH + object.getClass().getName());

        boolean writeDone = false;
        try {
            oos = new ObjectOutputStream(new FileOutputStream(fileTmp));
            oos.writeObject(object);
            writeDone = true;
        } catch (NotSerializableException e) {
            log.error("Not Serializable Exception :", e);
        } catch (IOException e) {
            log.error("IO Exception :", e);
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                    log.error("IO Exception :", e);
                }
            }
            if(writeDone) {
                //临时文件写成功后重命名
                file.delete();
                fileTmp.renameTo(file);
            }
        }
    }


    public static Object deserialize(Class cls) {
        Object object = null;
        ObjectInputStream ois = null;
        //正式文件
        File file = new File(DIRECTORY_PATH + cls.getName());
        try {
            if (file.exists() && file.isFile()) {
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
            if (ois != null) {
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
