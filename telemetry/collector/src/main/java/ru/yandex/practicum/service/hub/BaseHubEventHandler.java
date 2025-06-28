package ru.yandex.practicum.service.hub;

import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.ProducerRecord;
import ru.yandex.practicum.config.KafkaTopicsNames;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.model.hub.BaseHubEvent;
import ru.yandex.practicum.service.HubEventHandler;
import ru.yandex.practicum.service.kafka.KafkaEventProducer;

@RequiredArgsConstructor
public abstract class BaseHubEventHandler<T extends SpecificRecordBase> implements HubEventHandler {
    protected final KafkaEventProducer kafkaEventProducer;
    protected final KafkaTopicsNames topicsNames;
    protected final HubEventMapper hubEventMapper;

    protected boolean isNotInstanceOf(Object object, Class<?> keyType) {
        return !(keyType.isInstance(object));
    }

    protected ProducerRecord<String, SpecificRecordBase> createRecord(BaseHubEvent event, HubEventAvro avro) {
        return new ProducerRecord<>(
                topicsNames.getHubsTopic(),
                null,
                event.getTimestamp().toEpochMilli(),
                event.getHubId(),
                avro);
    }

    protected abstract HubEventAvro mapToAvroHubEvent(BaseHubEvent event);

    protected HubEventAvro buildHubEventAvro(BaseHubEvent event, T payloadAvro) {
        return HubEventAvro.newBuilder()
                .setTimestamp(event.getTimestamp())
                .setHubId(event.getHubId())
                .setPayload(payloadAvro)
                .build();
    }
}
