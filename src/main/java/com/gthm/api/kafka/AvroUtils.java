package com.gthm.api.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.*;
import org.apache.avro.specific.SpecificDatumReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AvroUtils {

    private static final Logger LOG = LoggerFactory.getLogger(AvroUtils.class);

    private final ObjectMapper mapper = new ObjectMapper();
    private final Schema avroSchema;

    public AvroUtils(Schema avroSchema) {
        this.avroSchema = avroSchema;
    }

    private Object genericRecordToObject(String json, Class clazz) {
        Object object = null;
        try {
            object = mapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            LOG.error("Avro parsing exception", e);
            throw new IllegalArgumentException();
        }
        return object;
    }

    public Object avroBytesToObj(SdpRecord sdpRecord, Class clazz) {
        Object object = null;
        try {

            DatumReader<GenericRecord> datumReader = new SpecificDatumReader<>(avroSchema);
            BinaryDecoder decoder = DecoderFactory.get()
                                                  .binaryDecoder(sdpRecord.value, null);
            GenericRecord genericRecord = datumReader.read(null, decoder);
            object = genericRecordToObject(genericRecord.toString(), clazz);

        } catch (IOException e) {
            LOG.error("Exception while converting avro bytes to object", e);
            throw new RuntimeException(e);
        }
        return object;
    }

}