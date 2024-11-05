package com.gthm.api.kafka;

import com.gthm.api.kafka.model.StructureRequest;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.apache.avro.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;


import java.io.IOException;

@Component
public class OneStreamConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(OneStreamConsumer.class);

    @Autowired
    EventNotificationTransformer eventTransformer;

    @Autowired
    FeatureToggleService featureToggleService;

    Schema avroSchema;

    @Value("classpath:avro/auto_message.avsc")
    private Resource schemaPath;

    @Value("onestream.featureToggleName")
    private String featureToggleName;

    @Getter
    @Value("${onestream.topicName}")
    private String oneStreamTopicName;

    @PostConstruct
    public void init() throws IOException {
        avroSchema = FileHelper.loadSchemaFile(schemaPath);
    }

    @KafkaListener(topics = "#{'${_listener.getAutoExceptionNotificationTopic()'}", containerFactory = "concurrentKafkaListenerContainerFactory")
    public void listen(SdpRecord sdpRecord, Acknowledgment acknowledgment) throws IOException {
        LOGGER.info("Received onestream message for the topic: {}", oneStreamTopicName);
        StructureRequest structureRequest = null;

        try {
            boolean featureToggleEnabled = featureToggleService.isFeatureToggleEnabled(featureToggleName);
            LOGGER.info("featureToggleEnabled : {}", featureToggleEnabled);
            if (featureToggleEnabled) {
                LOGGER.info("FeatureToggle enabled started processing the message for the topic: {}",
                        oneStreamTopicName);
                AvroUtils avroUtils = newAvroUtilsObject(avroSchema);
                structureRequest = (StructureRequest) avroUtils.avroBytesToObj(sdpRecord,
                        StructureRequest.class);
                eventTransformer.transformDealerNavExceptions(structureRequest);
            }
            acknowledgment.acknowledge();
        } catch (Exception e) {
            LOGGER.error("ApplicationDecisionedEvent: Listening to topic: {} and failed updating to elastic search", oneStreamTopicName, e);
            acknowledgment.acknowledge();
        }
    }

    protected AvroUtils newAvroUtilsObject (Schema avroSchema){
        return new AvroUtils(avroSchema);
    }

}