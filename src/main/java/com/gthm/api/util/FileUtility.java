package com.gthm.api.util;

import org.springframework.core.io.Resource;
import org.springframework.util.FileSystemUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileUtility {

    public static String loadFile(Resource resource) throws IOException {
        System.out.println("executing read file");
        String absolutePath = resource.getFile()
                                      .getAbsolutePath();

        return Files.readString(Path.of(absolutePath));

    }



}
