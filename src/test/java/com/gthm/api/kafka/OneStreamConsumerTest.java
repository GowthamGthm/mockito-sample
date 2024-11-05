package com.gthm.api.kafka;


import org.apache.avro.Schema;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OneStreamConsumerTest {

    String featureToggleName = "ONESTREAM_ENABLED";
    String oneStreamTopicName = "STRUCTURE_TOPIC_V3";

    @Spy
    @InjectMocks
    private OneStreamConsumer oneStreamConsumer;

    @Mock
    private EventNotificationTransformer eventTransformer;

    @Mock
    private FeatureToggleService featureToggleService;

    @Mock
    private Acknowledgment acknowledgment;

    @BeforeEach
    void setUp() throws IOException {

        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(oneStreamConsumer, "featureToggleName", featureToggleName);
        ReflectionTestUtils.setField(oneStreamConsumer, "oneStreamTopicName", oneStreamTopicName);

        Schema schema = FileHelper.loadSchemaFile(new ClassPathResource("avro/auto_message.avsc"));
        ReflectionTestUtils.setField(oneStreamConsumer, "avroSchema", schema);

    }

    @Test
    public void testListen_withFeatureToggleEnabled() throws Exception {
        SdpRecord mockSdpRecord = mock(SdpRecord.class);
        StructureRequest mockStructureRequest = mock(StructureRequest.class);
        AvroUtils mockAvroUtils = mock(AvroUtils.class);

        when(featureToggleService.isFeatureToggleEnabled(anyString())).thenReturn(true);
        when(oneStreamConsumer.newAvroUtilsObject(any())).thenReturn(mockAvroUtils);
        when(mockAvroUtils.avroBytesToObj(mockSdpRecord, StructureRequest.class)).thenReturn(mockStructureRequest);

        oneStreamConsumer.listen(mockSdpRecord, acknowledgment);

        verify(featureToggleService).isFeatureToggleEnabled(anyString());
        verify(oneStreamConsumer).newAvroUtilsObject(any());
        verify(eventTransformer).transformDealerNavExceptions(mockStructureRequest);
        verify(acknowledgment).acknowledge();
    }

    @Test
    public void testListen_withFeatureToggleDisabled() throws Exception {
        SdpRecord mockSdpRecord = mock(SdpRecord.class);
        when(featureToggleService.isFeatureToggleEnabled(anyString())).thenReturn(false);

        oneStreamConsumer.listen(mockSdpRecord, acknowledgment);

        verify(featureToggleService).isFeatureToggleEnabled(anyString());
        verify(acknowledgment).acknowledge();
        verify(eventTransformer, never()).transformDealerNavExceptions(any());
    }

    @Test
    public void testListen_exceptionDuringProcessing() throws Exception {
        SdpRecord mockSdpRecord = mock(SdpRecord.class);
        when(featureToggleService.isFeatureToggleEnabled(anyString())).thenReturn(true);
        when(oneStreamConsumer.newAvroUtilsObject(any())).thenThrow(new RuntimeException("Mocked exception"));

        oneStreamConsumer.listen(mockSdpRecord, acknowledgment);

        verify(featureToggleService).isFeatureToggleEnabled(anyString());
        verify(acknowledgment).acknowledge();
    }

    @Test
    public void testListen_avroConversionFailure() throws Exception {
        SdpRecord mockSdpRecord = mock(SdpRecord.class);
        AvroUtils mockAvroUtils = mock(AvroUtils.class);

        when(featureToggleService.isFeatureToggleEnabled(anyString())).thenReturn(true);
        when(oneStreamConsumer.newAvroUtilsObject(any())).thenReturn(mockAvroUtils);
        when(mockAvroUtils.avroBytesToObj(mockSdpRecord, StructureRequest.class)).thenThrow(new RuntimeException("Avro conversion failed"));

        oneStreamConsumer.listen(mockSdpRecord, acknowledgment);

        verify(featureToggleService).isFeatureToggleEnabled(anyString());
        verify(acknowledgment).acknowledge();
        verify(eventTransformer, never()).transformDealerNavExceptions(any());
    }

    @Test
    public void testListen_exceptionDuringTransformation() throws Exception {
        SdpRecord mockSdpRecord = mock(SdpRecord.class);
        StructureRequest mockStructureRequest = mock(StructureRequest.class);
        AvroUtils mockAvroUtils = mock(AvroUtils.class);

        when(featureToggleService.isFeatureToggleEnabled(anyString())).thenReturn(true);
        when(oneStreamConsumer.newAvroUtilsObject(any())).thenReturn(mockAvroUtils);
        when(mockAvroUtils.avroBytesToObj(mockSdpRecord, StructureRequest.class)).thenReturn(mockStructureRequest);
        doThrow(new RuntimeException("Transformation error")).when(eventTransformer)
                                                             .transformDealerNavExceptions(mockStructureRequest);

        oneStreamConsumer.listen(mockSdpRecord, acknowledgment);

        verify(featureToggleService).isFeatureToggleEnabled(anyString());
        verify(eventTransformer).transformDealerNavExceptions(mockStructureRequest);
        verify(acknowledgment).acknowledge();
    }

    // Additional tests can be added to cover other scenarios


    @Test
    public void testListen_emptyTopicName() throws Exception {
        SdpRecord mockSdpRecord = mock(SdpRecord.class);
        ReflectionTestUtils.setField(oneStreamConsumer, "oneStreamTopicName", oneStreamTopicName);

        oneStreamConsumer.listen(mockSdpRecord, acknowledgment);

        verify(featureToggleService).isFeatureToggleEnabled(anyString());
        verify(acknowledgment).acknowledge();
        verify(eventTransformer, never()).transformDealerNavExceptions(any());
    }

    @Test
    public void testListen_newAvroUtilsObject_calledOnce() throws Exception {
        SdpRecord mockSdpRecord = mock(SdpRecord.class);
        StructureRequest mockStructureRequest = mock(StructureRequest.class);
        AvroUtils mockAvroUtils = mock(AvroUtils.class);

        when(featureToggleService.isFeatureToggleEnabled(anyString())).thenReturn(true);
        when(oneStreamConsumer.newAvroUtilsObject(any())).thenReturn(mockAvroUtils);
        when(mockAvroUtils.avroBytesToObj(mockSdpRecord, StructureRequest.class)).thenReturn(mockStructureRequest);

        oneStreamConsumer.listen(mockSdpRecord, acknowledgment);

        verify(oneStreamConsumer, times(1)).newAvroUtilsObject(any());
    }

    @Test
    public void testListen_multipleInvocations() throws Exception {
        SdpRecord mockSdpRecord1 = mock(SdpRecord.class);
        SdpRecord mockSdpRecord2 = mock(SdpRecord.class);
        StructureRequest mockStructureRequest = mock(StructureRequest.class);
        AvroUtils mockAvroUtils = mock(AvroUtils.class);

        when(featureToggleService.isFeatureToggleEnabled(anyString())).thenReturn(true);
        when(oneStreamConsumer.newAvroUtilsObject(any())).thenReturn(mockAvroUtils);
        when(mockAvroUtils.avroBytesToObj(mockSdpRecord1, StructureRequest.class)).thenReturn(mockStructureRequest);
        when(mockAvroUtils.avroBytesToObj(mockSdpRecord2, StructureRequest.class)).thenReturn(mockStructureRequest);

        oneStreamConsumer.listen(mockSdpRecord1, acknowledgment);
        oneStreamConsumer.listen(mockSdpRecord2, acknowledgment);

        verify(eventTransformer, times(2)).transformDealerNavExceptions(mockStructureRequest);
        verify(acknowledgment, times(2)).acknowledge();
    }
}