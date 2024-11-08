package com.gthm.api.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.io.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class AvroUtils {

    private static final Logger LOG = LoggerFactory.getLogger(AvroUtils.class);

    private final ObjectMapper mapper = new ObjectMapper();
    private final Schema avroSchema;

    public AvroUtils(Schema avroSchema) {
        this.avroSchema = avroSchema;
    }

    public byte[] jsonToAvroBytes(String json) {

        DatumReader<Object> reader = new GenericDatumReader<>(this.avroSchema);
        GenericDatumWriter<Object> writer = new GenericDatumWriter<>(this.avroSchema);
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        try {
            Decoder decoder = DecoderFactory.get().jsonDecoder(this.avroSchema, json);
            Encoder encoder = EncoderFactory.get().binaryEncoder(output, null);
            Object datum = reader.read(null, decoder);
            writer.write(datum, encoder);
            encoder.flush();
            return output.toByteArray();
        } catch (IOException e) {
            LOG.error("Exception while converting json to avro bytes", e);
            throw new RuntimeException(e);
        }

    }


    public Object avroBytesToObject(SdpRecord record, Class clazz)  {

        GenericDatumReader<Object> reader = new GenericDatumReader<>(this.avroSchema);
        DatumWriter<Object> writer = new GenericDatumWriter<>(this.avroSchema);
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        try {
            JsonEncoder encoder = EncoderFactory.get().jsonEncoder(this.avroSchema, output, false);
            Decoder decoder = DecoderFactory.get().binaryDecoder(record.value, null);
            Object datum = reader.read(null, decoder);
            writer.write(datum, encoder);
            encoder.flush();
            output.flush();
            byte[] byteArray = output.toByteArray();

            Object obj = mapper.readValue(byteArray, clazz);
            return obj;
        } catch (IOException e) {
            LOG.error("Exception while converting avro bytes to object", e);
            throw new RuntimeException(e);
        }

    }

}