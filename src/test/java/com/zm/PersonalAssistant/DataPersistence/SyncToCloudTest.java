package com.zm.PersonalAssistant.DataPersistence;

import org.junit.*;

import java.io.File;
import java.io.IOException;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Created by zhangmin on 2016/6/16.
 */
public class SyncToCloudTest {
    public static final String FILE_READY_FOR_UPLOAD = "test.txt";

    public static final String DIRECTORY_LEVEL_1_NO_1 = "directory11";
    public static final String FILE_IN_DIRECTORY11_NO_1 = "file111";
    public static final String FILE_IN_DIRECTORY11_NO_2 = "file112";
    public static final String FILE_IN_DIRECTORY11_NO_3 = "file113";
    public static final String DIRECTORY_LEVEL_2_NO_1 = "directory21";
    public static final String DIRECTORY_LEVEL_2_NO_2_EMPTY = "directory22";
    public static final String FILE_IN_DIRECTORY21_NO_1 = "file211";
    public static final String FILE_IN_DIRECTORY21_NO_2 = "file212";

    @BeforeClass
    public static void prepare_files() throws IOException {
        File file = new File(FILE_READY_FOR_UPLOAD);
        file.createNewFile();

        File directory = new File(DIRECTORY_LEVEL_1_NO_1);
        directory.mkdir();
        file = new File(directory.getPath() + File.separator + FILE_IN_DIRECTORY11_NO_1);
        file.createNewFile();
        file = new File(directory.getPath() + File.separator + FILE_IN_DIRECTORY11_NO_2);
        file.createNewFile();
        file = new File(directory.getPath() + File.separator + FILE_IN_DIRECTORY11_NO_3);
        file.createNewFile();

        directory = new File(DIRECTORY_LEVEL_1_NO_1 + File.separator + DIRECTORY_LEVEL_2_NO_1);
        directory.mkdir();
        file = new File(directory.getPath() + File.separator + FILE_IN_DIRECTORY21_NO_1);
        file.createNewFile();
        file = new File(directory.getPath() + File.separator + FILE_IN_DIRECTORY21_NO_2);
        file.createNewFile();

        directory = new File(DIRECTORY_LEVEL_1_NO_1 + File.separator + DIRECTORY_LEVEL_2_NO_2_EMPTY);
        directory.mkdir();
    }

    @AfterClass
    public static void delete_files(){
        deleteForce(FILE_READY_FOR_UPLOAD);
        deleteForce(DIRECTORY_LEVEL_1_NO_1);
    }

    private static void deleteForce(String path){
        File file = new File(path);
        if(!file.exists())
            return;
        if(file.isFile()){
            file.delete();
        }
        else{
            File[] files = file.listFiles();
            for(File tmp : files){
                deleteForce(tmp.getPath());
            }
            file.delete();
        }
    }

    @Test
    public void sync_one_file_to_cloud(){
        //Arrange
        CloudPlatform mockDropBox = mock(DropBox.class);
        SyncToCloud syncToCloud = new SyncToCloud(mockDropBox);

        //Act
        syncToCloud.upload(FILE_READY_FOR_UPLOAD);

        //Assert
        verify(mockDropBox, times(1)).uploadFile(FILE_READY_FOR_UPLOAD);
        verify(mockDropBox, times(1)).uploadFile(anyString());
    }

    @Test
    public void sync_all_files_in_directory_to_cloud(){
        //Arrange
        CloudPlatform mockDropBox = mock(DropBox.class);
        SyncToCloud syncToCloud = new SyncToCloud(mockDropBox);

        //Act
        syncToCloud.upload(DIRECTORY_LEVEL_1_NO_1);

        //Assert
        verify(mockDropBox, times(1)).uploadFile(DIRECTORY_LEVEL_1_NO_1 + File.separator + FILE_IN_DIRECTORY11_NO_1);
        verify(mockDropBox, times(1)).uploadFile(DIRECTORY_LEVEL_1_NO_1 + File.separator + FILE_IN_DIRECTORY11_NO_2);
        verify(mockDropBox, times(1)).uploadFile(DIRECTORY_LEVEL_1_NO_1 + File.separator + FILE_IN_DIRECTORY11_NO_3);
        verify(mockDropBox, times(1)).uploadFile(DIRECTORY_LEVEL_1_NO_1 + File.separator + DIRECTORY_LEVEL_2_NO_1 + File.separator + FILE_IN_DIRECTORY21_NO_1);
        verify(mockDropBox, times(1)).uploadFile(DIRECTORY_LEVEL_1_NO_1 + File.separator + DIRECTORY_LEVEL_2_NO_1 + File.separator + FILE_IN_DIRECTORY21_NO_2);
        verify(mockDropBox, times(5)).uploadFile(anyString());
    }

    @Test
    public void sync_all_files_in_directory_to_cloud_except_someone_directory(){
        //Arrange
        CloudPlatform mockDropBox = mock(DropBox.class);
        SyncToCloud syncToCloud = new SyncToCloud(mockDropBox);

        //Act
        syncToCloud.uploadExcept(DIRECTORY_LEVEL_1_NO_1, DIRECTORY_LEVEL_1_NO_1 + File.separator + DIRECTORY_LEVEL_2_NO_1);

        //Assert
        verify(mockDropBox, times(1)).uploadFile(DIRECTORY_LEVEL_1_NO_1 + File.separator + FILE_IN_DIRECTORY11_NO_1);
        verify(mockDropBox, times(1)).uploadFile(DIRECTORY_LEVEL_1_NO_1 + File.separator + FILE_IN_DIRECTORY11_NO_2);
        verify(mockDropBox, times(1)).uploadFile(DIRECTORY_LEVEL_1_NO_1 + File.separator + FILE_IN_DIRECTORY11_NO_3);
        verify(mockDropBox, times(3)).uploadFile(anyString());
    }

    @Test
    public void sync_all_files_in_directory_to_cloud_except_someone_file(){
        //Arrange
        CloudPlatform mockDropBox = mock(DropBox.class);
        SyncToCloud syncToCloud = new SyncToCloud(mockDropBox);

        //Act
        syncToCloud.uploadExcept(DIRECTORY_LEVEL_1_NO_1, DIRECTORY_LEVEL_1_NO_1 + File.separator + DIRECTORY_LEVEL_2_NO_1 + File.separator + FILE_IN_DIRECTORY21_NO_1);

        //Assert
        verify(mockDropBox, times(1)).uploadFile(DIRECTORY_LEVEL_1_NO_1 + File.separator + FILE_IN_DIRECTORY11_NO_1);
        verify(mockDropBox, times(1)).uploadFile(DIRECTORY_LEVEL_1_NO_1 + File.separator + FILE_IN_DIRECTORY11_NO_2);
        verify(mockDropBox, times(1)).uploadFile(DIRECTORY_LEVEL_1_NO_1 + File.separator + FILE_IN_DIRECTORY11_NO_3);
        verify(mockDropBox, times(1)).uploadFile(DIRECTORY_LEVEL_1_NO_1 + File.separator + DIRECTORY_LEVEL_2_NO_1 + File.separator + FILE_IN_DIRECTORY21_NO_2);
        verify(mockDropBox, times(4)).uploadFile(anyString());
    }

    @Test
    public void no_file_upload_when_file_path_is_not_exists(){
        //Arrange
        CloudPlatform mockDropBox = mock(DropBox.class);
        SyncToCloud syncToCloud = new SyncToCloud(mockDropBox);

        //Act
        syncToCloud.upload("");
        syncToCloud.upload("notFile");

        //Assert
        verify(mockDropBox, never()).uploadFile(anyString());
    }

    @Test
    public void still_work_when_file_path_is_special(){
        //Arrange
        CloudPlatform mockDropBox = mock(DropBox.class);
        SyncToCloud syncToCloud = new SyncToCloud(mockDropBox);

        //Act
        syncToCloud.uploadExcept(".", "../PersonalAssistant/src/test");

        //Assert
        verify(mockDropBox, never()).uploadFile(contains("SynToCloudTest.java"));
    }
}
