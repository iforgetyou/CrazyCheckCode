package com.nothing;

import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import com.google.common.io.Files;
import org.junit.Test;

import java.io.File;
import java.util.HashMap;

import static org.junit.Assert.*;

public class FileMd5CheckTest {

    @Test
    public void testCheckFile() throws Exception {
        HashMap<String, File> filesMap = new HashMap<String, File>();

        File root = new File("D:/12306");
        File[] files = root.listFiles();
        for (File file : files) {
            HashCode hash = Files.hash(file, Hashing.sha1());
            if (filesMap.containsKey(hash)) {
                System.out.println("找到相同");
            } else {
//                System.out.println("没有找到,放入hashtable");
                filesMap.put(hash.toString(), file);
            }
            System.out.println(hash);
        }
    }
}