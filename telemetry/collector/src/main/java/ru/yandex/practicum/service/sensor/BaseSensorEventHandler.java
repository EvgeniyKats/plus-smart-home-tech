package ru.yandex.practicum.service.sensor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import ru.yandex.practicum.config.KafkaTopicsNames;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.model.sensor.BaseSensorEvent;
import ru.yandex.practicum.service.kafka.KafkaEventProducer;
import ru.yandex.practicum.service.SensorEventHandler;
import ru.yandex.practicum.service.kafka.ProducerSendParam;

@Slf4j
@RequiredArgsConstructor
public abstract class BaseSensorEventHandler<T extends SpecificRecordBase> implements SensorEventHandler {

    protected abstract SensorEventAvro mapToAvroSensorEvent(BaseSensorEvent event);

    protected final KafkaEventProducer kafkaEventProducer;
    protected final KafkaTopicsNames topicsNames;
    protected final SensorEventMapper sensorEventMapper;

    @Override
    public void handle(BaseSensorEvent event) {
        log.trace("instance check confirm hubId={}", event.getHubId());
        SensorEventAvro avro = mapToAvroSensorEvent(event);
        log.trace("map To avro confirm hubId={}", event.getHubId());
        ProducerSendParam param = createProducerSendParam(event, avro);
        log.trace("param created confirm hubId={}", event.getHubId());
        kafkaEventProducer.send(param);
        log.trace("record send confirm hubId={}", event.getHubId());
    }

    protected void validateEventType(BaseSensorEvent event, Class<? extends BaseSensorEvent> keyType) {
        log.trace("start validate event={} keyType={}", event.getClass(), keyType);
        if (!(keyType.isInstance(event))) {
            throw new IllegalArgumentException(event.getClass() + " is not instance of " + keyType);
        }
        log.trace("success validate event={} keyType={}", event.getClass(), keyType);
    }

    protected ProducerSendParam createProducerSendParam(BaseSensorEvent event, SensorEventAvro avro) {
        return ProducerSendParam.builder()
                .topic(topicsNames.getSensorsTopic())
                .timestamp(event.getTimestamp().toEpochMilli())
                .key(event.getHubId())
                .value(avro)
                .build();
    }

    protected SensorEventAvro buildSensorEventAvro(BaseSensorEvent event, T payloadAvro) {
        return SensorEventAvro.newBuilder()
                .setId(event.getId())
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .setPayload(payloadAvro)
                .build();
    }
}
