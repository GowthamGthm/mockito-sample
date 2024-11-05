package com.gthm.api.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gthm.api.kafka.model.StructureRequest;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AvroUtilsTest {

    @Mock
    private Schema mockAvroSchema;

    @Mock
    private ObjectMapper mockMapper;

    @Mock
    private GenericDatumReader<GenericRecord> mockAvroReader;

    @Mock
    private GenericDatumWriter<GenericRecord> mockAvroWriter;

    private AvroUtils avroUtilsUnderTest;

    @BeforeEach
    void setUp() throws IOException {
        Schema schema = FileHelper.loadSchemaFile(new ClassPathResource("avro/auto_message.avsc"));
        avroUtilsUnderTest = new AvroUtils(schema);
        // TODO: Set the following fields: mapper, avroReader, avroWriter.
    }

    @Test
    void testAvroBytesToObj() throws Exception {

        StructureRequest request = Instancio.create(StructureRequest.class);
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(request);

        // Setup
        final SdpRecord sdpRecord = SdpRecord.builder()
                                             .value(json.getBytes())
                                             .build();

        when(mockMapper.readValue(anyString(), eq(StructureRequest.class))).thenReturn(request);

        // Run the test
        StructureRequest result = (StructureRequest)
                avroUtilsUnderTest.avroBytesToObj(sdpRecord, StructureRequest.class);

        // Verify the results
        assertThat(result.getId()).isEqualTo(request.getId());
    }

    @Test
    void testAvroBytesToObj_ObjectMapperThrowsJsonProcessingException() throws Exception {
        // Setup
        final SdpRecord sdpRecord = SdpRecord.builder()
                                             .value("content".getBytes())
                                             .build();
        when(mockMapper.readValue("json", Object.class)).thenThrow(JsonProcessingException.class);

        // Run the test
        assertThatThrownBy(() -> avroUtilsUnderTest.avroBytesToObj(sdpRecord, Object.class)).isInstanceOf(
                IllegalArgumentException.class);
    }

    @Test
    void testAvroBytesToObj_ObjectMapperThrowsJsonMappingException() throws Exception {
        // Setup
        final SdpRecord sdpRecord = SdpRecord.builder()
                                             .value("content".getBytes())
                                             .build();
        when(mockMapper.readValue("json", Object.class)).thenThrow(JsonMappingException.class);

        // Run the test
        assertThatThrownBy(() -> avroUtilsUnderTest.avroBytesToObj(sdpRecord, Object.class)).isInstanceOf(IllegalArgumentException.class);
    }
}
