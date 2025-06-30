package ru.yandex.practicum.service.sensor;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.config.KafkaTopicsNames;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.TemperatureSensorAvro;
import ru.yandex.practicum.model.sensor.BaseSensorEvent;
import ru.yandex.practicum.model.sensor.TemperatureSensorEvent;
import ru.yandex.practicum.model.sensor.SensorEventType;
import ru.yandex.practicum.service.kafka.KafkaEventProducer;

@Component
public class TemperatureSensorEventHandler extends BaseSensorEventHandler<TemperatureSensorAvro> {
    public TemperatureSensorEventHandler(KafkaEventProducer kafkaEventProducer,
                                         KafkaTopicsNames kafkaTopicsNames,
                                         SensorEventMapper sensorEventMapper) {
        super(kafkaEventProducer, kafkaTopicsNames, sensorEventMapper);
    }

    @Override
    public SensorEventType getSensorEventType() {
        return SensorEventType.TEMPERATURE_SENSOR_EVENT;
    }

    @Override
    public void handle(BaseSensorEvent event) {
        validateEventType(event, TemperatureSensorEvent.class);
        super.handle(event);
    }

    @Override
    protected SensorEventAvro mapToAvroSensorEvent(BaseSensorEvent event) {
        TemperatureSensorAvro avro = sensorEventMapper.mapToTemperatureSensorAvro((TemperatureSensorEvent) event);
        return buildSensorEventAvro(event, avro);
    }
}
