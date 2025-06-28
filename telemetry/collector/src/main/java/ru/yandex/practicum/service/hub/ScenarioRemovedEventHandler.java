package ru.yandex.practicum.service.hub;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.config.KafkaTopicsNames;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioRemovedEventAvro;
import ru.yandex.practicum.model.hub.BaseHubEvent;
import ru.yandex.practicum.model.hub.HubEventType;
import ru.yandex.practicum.model.hub.scenario.ScenarioRemovedEvent;
import ru.yandex.practicum.service.kafka.KafkaEventProducer;

@Component
public class ScenarioRemovedEventHandler extends BaseHubEventHandler<ScenarioRemovedEventAvro> {
    public ScenarioRemovedEventHandler(KafkaEventProducer kafkaEventProducer,
                                       KafkaTopicsNames topicsNames,
                                       HubEventMapper hubEventMapper) {
        super(kafkaEventProducer, topicsNames, hubEventMapper);
    }

    @Override
    protected HubEventAvro mapToAvroHubEvent(BaseHubEvent event) {
        ScenarioRemovedEventAvro avro = hubEventMapper.mapToScenarioRemovedEventAvro((ScenarioRemovedEvent) event);
        return buildHubEventAvro(event, avro);
    }

    @Override
    public HubEventType getHubEventType() {
        return HubEventType.SCENARIO_REMOVED;
    }

    @Override
    public void handle(BaseHubEvent event) {
        if (isNotInstanceOf(event, ScenarioRemovedEvent.class)) {
            throw new IllegalArgumentException(event.getClass() + " is not instance of ScenarioRemovedEvent.class");
        }

        HubEventAvro avro = mapToAvroHubEvent(event);

        ProducerRecord<String, SpecificRecordBase> record = createRecord(event, avro);

        kafkaEventProducer.send(record);
    }
}
