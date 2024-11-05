package com.gthm.api.kafka;

import org.apache.avro.Schema;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileHelper {

    public static Schema loadSchemaFile(Resource resource) throws IOException {

        System.out.println("executing read file");
        String absolutePath = resource.getFile()
                                      .getAbsolutePath();

        return Schema.parse(Files.readString(Path.of(absolutePath)));

    }


}