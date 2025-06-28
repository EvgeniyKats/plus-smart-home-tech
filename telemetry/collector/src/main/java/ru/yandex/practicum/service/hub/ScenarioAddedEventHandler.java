package ru.yandex.practicum.service.hub;

import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.config.KafkaTopicsNames;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioAddedEventAvro;
import ru.yandex.practicum.model.hub.BaseHubEvent;
import ru.yandex.practicum.model.hub.HubEventType;
import ru.yandex.practicum.model.hub.scenario.ScenarioAddedEvent;
import ru.yandex.practicum.service.kafka.KafkaEventProducer;

@Slf4j
@Component
public class ScenarioAddedEventHandler extends BaseHubEventHandler<ScenarioAddedEventAvro> {
    public ScenarioAddedEventHandler(KafkaEventProducer kafkaEventProducer,
                                     KafkaTopicsNames topicsNames,
                                     HubEventMapper hubEventMapper) {
        super(kafkaEventProducer, topicsNames, hubEventMapper);
    }

    @Override
    protected HubEventAvro mapToAvroHubEvent(BaseHubEvent event) {
        ScenarioAddedEventAvro avro = hubEventMapper.mapToScenarioAddedEventAvro((ScenarioAddedEvent) event);
        return buildHubEventAvro(event, avro);
    }

    @Override
    public HubEventType getHubEventType() {
        return HubEventType.SCENARIO_ADDED;
    }

    @Override
    public void handle(BaseHubEvent event) {
        if (isNotInstanceOf(event, ScenarioAddedEvent.class)) {
            throw new IllegalArgumentException(event.getClass() + " is not instance of ScenarioAddedEvent.class");
        }
        log.trace("instance check confirm hubId={}", event.getHubId());
        HubEventAvro avro = mapToAvroHubEvent(event);
        log.trace("map To avro confirm hubId={}", event.getHubId());;
        ProducerRecord<String, SpecificRecordBase> record = createRecord(event, avro);
        log.trace("record created confirm hubId={}", event.getHubId());
        kafkaEventProducer.send(record);
        log.trace("record send confirm hubId={}", event.getHubId());
    }
}
