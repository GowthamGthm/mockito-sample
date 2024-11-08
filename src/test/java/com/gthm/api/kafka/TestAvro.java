package com.gthm.api.kafka;

import org.apache.avro.Schema;
import tech.allegro.schema.json2avro.converter.JsonAvroConverter;

import java.io.IOException;

public class TestAvro {

    public static byte[] jsonToAvroBytes(String avscFileName, String jsonFileName) throws IOException {

        String avsc = FileHelper.getFileContents(avscFileName);
        String json = FileHelper.getFileContents(jsonFileName);

        JsonAvroConverter converter = new JsonAvroConverter();
        byte[] bytes = converter.convertToAvro(json.getBytes(), avsc);
        return bytes;

    }


    public static byte[] jsonToAvroBytes(Schema schema, String jsonFileName) throws IOException {

        String json = FileHelper.getFileContents(jsonFileName);

        JsonAvroConverter converter = new JsonAvroConverter();
        byte[] bytes = converter.convertToAvro(json.getBytes(), schema);
        return bytes;

    }


}