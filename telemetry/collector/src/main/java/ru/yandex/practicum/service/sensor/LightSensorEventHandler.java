package ru.yandex.practicum.service.sensor;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.config.KafkaTopicsNames;
import ru.yandex.practicum.kafka.telemetry.event.LightSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.model.sensor.BaseSensorEvent;
import ru.yandex.practicum.model.sensor.LightSensorEvent;
import ru.yandex.practicum.model.sensor.SensorEventType;
import ru.yandex.practicum.service.kafka.KafkaEventProducer;

@Component
public class LightSensorEventHandler extends BaseSensorEventHandler<LightSensorAvro> {
    public LightSensorEventHandler(KafkaEventProducer kafkaEventProducer,
                                   KafkaTopicsNames kafkaTopicsNames,
                                   SensorEventMapper sensorEventMapper) {
        super(kafkaEventProducer, kafkaTopicsNames, sensorEventMapper);
    }

    @Override
    public SensorEventType getSensorEventType() {
        return SensorEventType.LIGHT_SENSOR_EVENT;
    }

    @Override
    public void handle(BaseSensorEvent event) {
        if (isNotInstanceOf(event, LightSensorEvent.class)) {
            throw new IllegalArgumentException(event.getClass() + " is not instance of LightSensorEvent.class");
        }
        SensorEventAvro avro = mapToAvroSensorEvent(event);

        ProducerRecord<String, SpecificRecordBase> record = createRecord(event, avro);

        kafkaEventProducer.send(record);
    }

    @Override
    protected SensorEventAvro mapToAvroSensorEvent(BaseSensorEvent event) {
        LightSensorAvro avro = sensorEventMapper.mapToLightSensorAvro((LightSensorEvent) event);
        return buildSensorEventAvro(event, avro);
    }
}