package com.gthm.api.dummy;

import com.fasterxml.jackson.dataformat.avro.AvroMapper;
import com.fasterxml.jackson.dataformat.avro.AvroSchema;
import com.gthm.api.kafka.AvroUtils;
import com.gthm.api.kafka.FileHelper;
import com.gthm.api.kafka.SdpRecord;
import com.gthm.api.kafka.model.StructureRequest;
import org.apache.avro.Schema;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

public class Test1 {

    static String AVSC_PATH = "avro/auto_message.avsc";
    static String JSON_PATH = "json/pokemon.json";


    public static void main(String[] args) throws IOException {

        String avscContent = FileHelper.getFileContents(AVSC_PATH);
        Schema schema = FileHelper.loadSchemaFile(new ClassPathResource(AVSC_PATH));

//        AvroUtils avroUtils = new AvroUtils(schema);
        byte[] bytes = AvroUtils.jsonToAvroBytes(FileHelper.getFileContents(JSON_PATH) , schema , StructureRequest.class);
        SdpRecord sdpRecord = new SdpRecord(bytes, "");


        AvroMapper mapper = AvroMapper.builder().build();
        AvroSchema avroSchema = mapper.schemaFrom(schema.toString());


        StructureRequest obj = mapper.readerFor(StructureRequest.class)
                                     .with(avroSchema)
                                     .readValue(sdpRecord.value);


        System.out.println("changes for the test : " + obj);


        byte[] avroBytes = mapper.writer(avroSchema)
                                 .writeValueAsBytes(obj);


        SdpRecord record = new SdpRecord(avroBytes, "");


        StructureRequest object1 = (StructureRequest) AvroUtils.avroBytesToObject(record, schema, StructureRequest.class);

        System.out.println(object1);
    }


}