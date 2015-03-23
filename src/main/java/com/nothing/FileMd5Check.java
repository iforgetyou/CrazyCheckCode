package com.nothing;


import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by zyan.zhang on 2015/3/23.
 */
public class FileMd5Check {

    public String checkFile(String filePath) throws IOException {
        HashCode hash = Files.hash(new File(filePath), Hashing.sha1());
        return hash.toString();

    }
}
