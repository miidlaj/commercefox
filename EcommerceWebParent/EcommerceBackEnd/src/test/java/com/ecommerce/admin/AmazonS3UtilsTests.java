package com.ecommerce.admin;

import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

public class AmazonS3UtilsTests {

    @Test
    public void testListFolder(){
        String folderName = "test-upload";
        List<String> listKeys = AmazonS3Util.listFolder(folderName);

        listKeys.forEach(System.out::println);
    }

    @Test
    public void testUploadFile() throws FileNotFoundException {
        String fileName = "token.txt";
        String folderName = "test-upload/one/two/three";
        String filePath = "C:\\Users\\LENOVO\\OneDrive\\Desktop\\" + fileName;

        InputStream inputStream = new FileInputStream(filePath);
        AmazonS3Util.uploadFile(folderName, fileName, inputStream);
    }

    @Test
    public void testDeleteFile(){
        String fileName = "test-upload/one/two/three/token.txt";
        AmazonS3Util.deleteFile(fileName);
    }

    @Test
    public void removeFolder(){
        String fileName = "test-upload";
        AmazonS3Util.removeFolder(fileName);
    }
}
