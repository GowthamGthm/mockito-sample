package com.gthm.api.dummy;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.avro.AvroMapper;
import com.fasterxml.jackson.dataformat.avro.AvroSchema;
import com.gthm.api.kafka.FileHelper;
import com.gthm.api.kafka.SdpRecord;
import com.gthm.api.kafka.model.StructureRequest;
import org.apache.avro.Schema;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

public class Test {

    private static String AVRO_FILE_PATH = "avro/auto_message.avsc";
    private static String JSON_FILE_PATH = "json/pokemon.json";

    public static void main(String[] args) throws IOException {

        Schema schema = FileHelper.loadSchemaFile(new ClassPathResource(AVRO_FILE_PATH));
        String json = FileHelper.getFileContents(JSON_FILE_PATH);

        byte[] bytes = jsonToAvroBytes(json, schema, StructureRequest.class);
        SdpRecord record = new SdpRecord(bytes, "");

        avroBytesToObject(record, schema, StructureRequest.class);




    }

    private static byte[] jsonToAvroBytes(String json, Schema schema , Class clazz) throws IOException {

        AvroMapper avroMapper = new AvroMapper();
        AvroSchema avroSchema = avroMapper.schemaFrom(schema.toString());

        ObjectMapper objMapper = new ObjectMapper();
        Object obj = objMapper.readValue(json, clazz);

        byte[] bytes = avroMapper.writer(avroSchema)
                             .writeValueAsBytes(obj);
        return bytes;

    }

    private static void avroBytesToObject(SdpRecord record, Schema schema, Class clazz) throws IOException {

        AvroMapper mapper = new AvroMapper();
        AvroSchema avroSchema = mapper.schemaFrom(schema.toString());

        StructureRequest obj = mapper.readerFor(clazz)
                                     .with(avroSchema).readValue(record.value);

        System.out.println(obj.getId());
        System.out.println(obj.getName());
        System.out.println(obj.getReviews());

    }

}