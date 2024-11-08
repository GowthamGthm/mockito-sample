package com.gthm.api.util;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.FileNotFoundException;


public class FileUtilityTest {

    @Test
    void testLoadFile() throws Exception {

        Resource resource = new ClassPathResource("json/pokemon.json");

        String stringContent = FileUtility.loadFile(resource);

        Assertions.assertThat(stringContent).isNotNull().isNotEmpty();
        Assertions.assertThat(stringContent).startsWith("{");
        Assertions.assertThat(stringContent).endsWith("}");

    }


    @Test
    public void invalid_file_resource_type() {

        FileNotFoundException fileNotFoundException = org.junit.jupiter.api.Assertions.assertThrows(FileNotFoundException.class, () -> FileUtility.loadFile(new ByteArrayResource("content".getBytes())));
        fileNotFoundException.printStackTrace();

    }

}