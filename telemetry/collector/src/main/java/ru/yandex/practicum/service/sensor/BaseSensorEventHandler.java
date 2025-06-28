package ru.yandex.practicum.service.sensor;

import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.ProducerRecord;
import ru.yandex.practicum.config.KafkaTopicsNames;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.model.sensor.BaseSensorEvent;
import ru.yandex.practicum.service.kafka.KafkaEventProducer;
import ru.yandex.practicum.service.SensorEventHandler;

@RequiredArgsConstructor
public abstract class BaseSensorEventHandler<T extends SpecificRecordBase> implements SensorEventHandler {
    protected final KafkaEventProducer kafkaEventProducer;
    protected final KafkaTopicsNames topicsNames;
    protected final SensorEventMapper sensorEventMapper;

    protected boolean isNotInstanceOf(Object object, Class<?> keyType) {
        return !(keyType.isInstance(object));
    }

    protected ProducerRecord<String, SpecificRecordBase> createRecord(BaseSensorEvent event, SensorEventAvro avro) {
        return new ProducerRecord<>(
                topicsNames.getSensorsTopic(),
                null,
                event.getTimestamp().toEpochMilli(),
                event.getId(),
                avro);
    }

    protected abstract SensorEventAvro mapToAvroSensorEvent(BaseSensorEvent event);

    protected SensorEventAvro buildSensorEventAvro(BaseSensorEvent event, T payloadAvro) {
        return SensorEventAvro.newBuilder()
                .setId(event.getId())
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .setPayload(payloadAvro)
                .build();
    }
}
