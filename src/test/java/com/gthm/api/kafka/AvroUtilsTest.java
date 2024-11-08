package com.gthm.api.kafka;

import com.gthm.api.kafka.model.StructureRequest;
import com.gthm.api.models.Pokemon;
import com.gthm.api.models.Review;
import org.apache.avro.AvroRuntimeException;
import org.apache.avro.Schema;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@ExtendWith(MockitoExtension.class)
class AvroUtilsTest {

    private String AVSC_FILE_PATH = "avro/auto_message.avsc";
    private String JSON_FILE_PATH = "json/pokemon.json";


    @Test
    public void test_success_parse() throws IOException {
        Schema schema = FileHelper.loadSchemaFile(new ClassPathResource(AVSC_FILE_PATH));
        AvroUtils avroUtils = new AvroUtils(schema);

        byte[] bytes = TestAvro.jsonToAvroBytes(AVSC_FILE_PATH, JSON_FILE_PATH);
        SdpRecord sdpRecord = new SdpRecord(bytes, "json");

        StructureRequest obj = (StructureRequest) avroUtils.avroBytesToObj(sdpRecord, StructureRequest.class);

        Assertions.assertNotNull(obj);
        Assertions.assertEquals(8301, obj.getId());
    }

    @Test
    public void test_failure_parse_with_string_byte() throws IOException {
        Schema schema = FileHelper.loadSchemaFile(new ClassPathResource(AVSC_FILE_PATH));
        AvroUtils avroUtils = new AvroUtils(schema);

        ClassPathResource jsonClassPath = new ClassPathResource(JSON_FILE_PATH);
        String json = Files.readString(Path.of(jsonClassPath.getFile()
                                                            .getAbsolutePath()));

        SdpRecord sdpRecord = new SdpRecord(json.getBytes(), json);

        Assertions.assertThrows(AvroRuntimeException.class, () -> avroUtils.avroBytesToObj(sdpRecord,
                StructureRequest.class));

    }

    @Test
    public void test_failure_for_null_bytes() throws IOException {
        Schema schema = FileHelper.loadSchemaFile(new ClassPathResource(AVSC_FILE_PATH));
        AvroUtils avroUtils = new AvroUtils(schema);

        SdpRecord sdpRecord = new SdpRecord(null, "");

        Assertions.assertThrows(NullPointerException.class, () -> avroUtils.avroBytesToObj(sdpRecord,
                StructureRequest.class));

    }

    @Test
    public void test_failure_for_wrong_target_class() throws IOException {

        Schema schema = FileHelper.loadSchemaFile(new ClassPathResource(AVSC_FILE_PATH));
        AvroUtils avroUtils = new AvroUtils(schema);

        byte[] bytes = TestAvro.jsonToAvroBytes(schema , JSON_FILE_PATH);
        SdpRecord sdpRecord = new SdpRecord(bytes, "json");

        Assertions.assertThrows(IllegalArgumentException.class, () -> avroUtils.avroBytesToObj(sdpRecord, Review.class));

    }


}