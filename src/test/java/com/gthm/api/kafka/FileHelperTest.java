package com.gthm.api.kafka;

import org.apache.avro.Schema;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FileHelperTest {

    private String FILE_PATH = "avro/auto_message.avsc";

    @Test
    void testLoadSchemaFile() throws Exception {

        ClassPathResource resource = new ClassPathResource(FILE_PATH);
        final Schema result = FileHelper.loadSchemaFile(resource);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("StructureRequest");
        assertThat(result.getType()).isEqualTo(Schema.Type.RECORD);

    }

    @Test
    void testLoadSchemaFile_ThrowsIOException() {
        ClassPathResource resource = new ClassPathResource(FILE_PATH + "a");

        assertThatThrownBy(
                () -> FileHelper.loadSchemaFile(resource)).isInstanceOf(
                IOException.class);

    }


    //     newly added test cases

    @Test
    void testLoadFile_as_string() throws Exception {
        final String result = FileHelper.getFileContents(FILE_PATH);

        assertThat(result).isNotNull().isNotEmpty();
        assertThat(result).isInstanceOf(String.class);
    }

    @Test
    void testLoaFile_as_string_NullPointerException() {

        assertThatThrownBy(
                () -> FileHelper.getFileContents(FILE_PATH + "a")).isInstanceOf(
                NullPointerException.class);

    }

    @Test
    void testLoaFile_as_string_null_file_path() {

        assertThatThrownBy(
                () -> FileHelper.getFileContents(null)).isInstanceOf(
                NullPointerException.class);

    }

}