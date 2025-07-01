package ru.yandex.practicum.service.hub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import ru.yandex.practicum.config.KafkaTopicsNames;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.model.hub.BaseHubEvent;
import ru.yandex.practicum.service.HubEventHandler;
import ru.yandex.practicum.service.kafka.KafkaEventProducer;
import ru.yandex.practicum.service.kafka.ProducerSendParam;

@Slf4j
@RequiredArgsConstructor
public abstract class BaseHubEventHandler<T extends SpecificRecordBase> implements HubEventHandler {

    protected abstract HubEventAvro mapToHubEventAvro(BaseHubEvent event);

    protected final KafkaEventProducer kafkaEventProducer;
    protected final KafkaTopicsNames topicsNames;
    protected final HubEventMapper hubEventMapper;

    @Override
    public void handle(BaseHubEvent event) {
        log.trace("instance check confirm hubId={}", event.getHubId());
        HubEventAvro avro = mapToHubEventAvro(event);
        log.trace("map To avro confirm hubId={}", event.getHubId());
        ProducerSendParam param = createProducerSendParam(event, avro);
        log.trace("param created confirm hubId={}", event.getHubId());
        kafkaEventProducer.send(param);
        log.trace("record send confirm hubId={}", event.getHubId());
    }

    protected void validateEventType(BaseHubEvent event, Class<? extends BaseHubEvent> keyType) {
        log.trace("start validate event={} keyType={}", event.getClass(), keyType);
        if (!(keyType.isInstance(event))) {
            throw new IllegalArgumentException(event.getClass() + " is not instance of " + keyType);
        }
        log.trace("success validate event={} keyType={}", event.getClass(), keyType);
    }

    protected ProducerSendParam createProducerSendParam(BaseHubEvent event, HubEventAvro avro) {
        return ProducerSendParam.builder()
                .topic(topicsNames.getHubsTopic())
                .timestamp(event.getTimestamp().toEpochMilli())
                .key(event.getHubId())
                .value(avro)
                .build();
    }

    protected HubEventAvro buildHubEventAvro(BaseHubEvent event, T payloadAvro) {
        return HubEventAvro.newBuilder()
                .setTimestamp(event.getTimestamp())
                .setHubId(event.getHubId())
                .setPayload(payloadAvro)
                .build();
    }
}
