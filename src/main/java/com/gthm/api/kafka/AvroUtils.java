package com.gthm.api.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.avro.AvroMapper;
import com.fasterxml.jackson.dataformat.avro.AvroSchema;
import org.apache.avro.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class AvroUtils {

    private static final Logger LOG = LoggerFactory.getLogger(AvroUtils.class);

    private static final ObjectMapper objMapper = new ObjectMapper();


    public static byte[] jsonToAvroBytes(String json, Schema schema, Class clazz) {
        AvroMapper avroMapper = new AvroMapper();
        try {
            AvroSchema avroSchema = avroMapper.schemaFrom(schema.toString());
            Object obj = objMapper.readValue(json, clazz);

            byte[] bytes = avroMapper.writer(avroSchema)
                                     .writeValueAsBytes(obj);
            return bytes;
        } catch (IOException e) {
            LOG.error("Exception while converting json to avro bytes", e);
            throw new RuntimeException(e);
        }
    }


    public static Object avroBytesToObject(SdpRecord record, Schema schema, Class clazz) {
        AvroMapper avroMapper = new AvroMapper();
        try {

            AvroSchema avroSchema = avroMapper.schemaFrom(schema.toString());

            Object obj = avroMapper.readerFor(clazz)
                                   .with(avroSchema)
                                   .readValue(record.value);
            return obj;
        } catch (IOException e) {
            LOG.error("Exception while converting avro bytes to object", e);
            throw new RuntimeException(e);
        }

    }

}