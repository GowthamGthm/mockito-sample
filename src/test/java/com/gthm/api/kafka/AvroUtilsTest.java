package com.gthm.api.kafka;

import com.gthm.api.kafka.model.StructureRequest;
import com.gthm.api.models.Review;
import org.apache.avro.Schema;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

@ExtendWith(MockitoExtension.class)
class AvroUtilsTest {

    private final String AVSC_FILE_PATH = "avro/auto_message.avsc";
    private final String JSON_FILE_PATH = "json/pokemon.json";


    @Test
    public void test_success_parse() throws IOException {
        Schema schema = FileHelper.loadSchemaFile(new ClassPathResource(AVSC_FILE_PATH));
        AvroUtils avroUtils = new AvroUtils(schema);

        String json = FileHelper.getFileContents(JSON_FILE_PATH);
        byte[] bytes = avroUtils.jsonToAvroBytes(json);
        SdpRecord sdpRecord = new SdpRecord(bytes, "json");

        StructureRequest obj = (StructureRequest) avroUtils.avroBytesToObject(sdpRecord,
                StructureRequest.class);

        Assertions.assertNotNull(obj);
        Assertions.assertEquals(8301, obj.getId());
    }

    @Test
    public void test_failure_parse_with_string_byte() throws IOException {
        Schema schema = FileHelper.loadSchemaFile(new ClassPathResource(AVSC_FILE_PATH));
        AvroUtils avroUtils = new AvroUtils(schema);

        String json = FileHelper.getFileContents(JSON_FILE_PATH);
        SdpRecord sdpRecord = new SdpRecord(json.getBytes(), json);

        Assertions.assertThrows(RuntimeException.class,
                () -> avroUtils.avroBytesToObject(sdpRecord, StructureRequest.class));

    }

    @Test
    public void test_failure_for_null_bytes() throws IOException {
        Schema schema = FileHelper.loadSchemaFile(new ClassPathResource(AVSC_FILE_PATH));
        AvroUtils avroUtils = new AvroUtils(schema);

        SdpRecord sdpRecord = new SdpRecord(null, "");

        Assertions.assertThrows(RuntimeException.class,
                () -> avroUtils.avroBytesToObject(sdpRecord, StructureRequest.class));

    }

    @Test
    public void test_failure_for_wrong_target_class() throws IOException {

        Schema schema = FileHelper.loadSchemaFile(new ClassPathResource(AVSC_FILE_PATH));
        AvroUtils avroUtils = new AvroUtils(schema);

        String json = FileHelper.getFileContents(JSON_FILE_PATH);
        byte[] bytes = avroUtils.jsonToAvroBytes(json);
        SdpRecord sdpRecord = new SdpRecord(bytes, "json");

        Assertions.assertThrows(RuntimeException.class,
                () -> avroUtils.avroBytesToObject(sdpRecord, Review.class));

    }


//     newly added test cases

    @Test
    public void test_success_for_correct_json_to_avro_bytes() throws IOException {

        Schema schema = FileHelper.loadSchemaFile(new ClassPathResource(AVSC_FILE_PATH));
        AvroUtils avroUtils = new AvroUtils(schema);

        String json = FileHelper.getFileContents(JSON_FILE_PATH);
        byte[] bytes = avroUtils.jsonToAvroBytes(json);

        Assertions.assertNotNull(bytes);
    }

    @Test
    public void test_failure_for_incorrect_json_to_avro_bytes() throws IOException {

        Schema schema = FileHelper.loadSchemaFile(new ClassPathResource(AVSC_FILE_PATH));
        AvroUtils avroUtils = new AvroUtils(schema);

        String json = "{\"name\": \"TEST\" \"zip\": 77327}";
        Assertions.assertThrows(RuntimeException.class, () -> avroUtils.jsonToAvroBytes(json));
    }

    @Test
    public void test_failure_for_null_json_to_avro_bytes() throws IOException {

        Schema schema = FileHelper.loadSchemaFile(new ClassPathResource(AVSC_FILE_PATH));
        AvroUtils avroUtils = new AvroUtils(schema);

        String json = null;
        Assertions.assertThrows(RuntimeException.class, () -> avroUtils.jsonToAvroBytes(json));
    }


}