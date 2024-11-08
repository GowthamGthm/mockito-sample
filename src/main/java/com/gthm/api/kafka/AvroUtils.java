package com.gthm.api.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.avro.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.allegro.schema.json2avro.converter.JsonAvroConverter;

public class AvroUtils {

    private static final Logger LOG = LoggerFactory.getLogger(AvroUtils.class);

    private final ObjectMapper mapper = new ObjectMapper();
    JsonAvroConverter jsonAvroConverter = new JsonAvroConverter();
    private final Schema avroSchema;


    public AvroUtils(Schema avroSchema) {
        this.avroSchema = avroSchema;
    }

//    private Object genericRecordToObject(String json, Class clazz) {
//        Object object = null;
//        try {
//            object = mapper.readValue(json, clazz);
//        } catch (JsonProcessingException e) {
//            LOG.error("Avro parsing exception", e);
//            throw new IllegalArgumentException();
//        }
//        return object;
//    }
//
//    public Object avroBytesToObj(SdpRecord sdpRecord, Class clazz) {
//        Object object = null;
//        try {
//
//            DatumReader<GenericRecord> datumReader = new SpecificDatumReader<>(avroSchema);
//            BinaryDecoder decoder = DecoderFactory.get()
//                                                  .binaryDecoder(sdpRecord.value, null);
//            GenericRecord genericRecord = datumReader.read(null, decoder);
//            object = genericRecordToObject(genericRecord.toString(), clazz);
//
//        } catch (IOException e) {
//            LOG.error("Exception while converting avro bytes to object", e);
//            throw new RuntimeException(e);
//        }
//        return object;
//    }


    public Object avroBytesToObject(SdpRecord record, Class clazz)  {

        Object obj = null;
        try {
            byte[] bytes = jsonAvroConverter.convertToJson(record.value, this.avroSchema);
            obj = mapper.readValue(bytes, clazz);
        } catch (Exception e) {
            LOG.error("Exception while converting avro bytes to object", e);
            throw new RuntimeException(e);
        }
        return obj;
    }

    public static byte[] jsonToAvroBytes(String schemaString, String jsonContent) {

        JsonAvroConverter converter = new JsonAvroConverter();
        byte[] bytes = null;
        try {
            bytes = converter.convertToAvro(jsonContent.getBytes(), schemaString);
        } catch (Exception e) {
            LOG.error("Exception while converting json to avro bytes", e);
            throw new RuntimeException(e);
        }
        return bytes;
    }


    public byte[] jsonToAvroBytes(String json) {

        JsonAvroConverter converter = new JsonAvroConverter();
        byte[] bytes = null;
        try {
            bytes = converter.convertToAvro(json.getBytes(), this.avroSchema);
        } catch (Exception e) {
            LOG.error("Exception while converting json to avro bytes", e);
            throw new RuntimeException(e);
        }
        return bytes;

    }


}