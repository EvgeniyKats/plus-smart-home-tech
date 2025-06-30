package ru.yandex.practicum.service.sensor;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.config.KafkaTopicsNames;
import ru.yandex.practicum.kafka.telemetry.event.ClimateSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.model.sensor.BaseSensorEvent;
import ru.yandex.practicum.model.sensor.ClimateSensorEvent;
import ru.yandex.practicum.model.sensor.SensorEventType;
import ru.yandex.practicum.service.kafka.KafkaEventProducer;

@Component
public class ClimateSensorEventHandler extends BaseSensorEventHandler<ClimateSensorAvro> {
    public ClimateSensorEventHandler(KafkaEventProducer kafkaEventProducer,
                                     KafkaTopicsNames kafkaTopicsNames,
                                     SensorEventMapper sensorEventMapper) {
        super(kafkaEventProducer, kafkaTopicsNames, sensorEventMapper);
    }

    @Override
    public SensorEventType getSensorEventType() {
        return SensorEventType.CLIMATE_SENSOR_EVENT;
    }

    @Override
    public void handle(BaseSensorEvent event) {
        validateEventType(event, ClimateSensorEvent.class);
        super.handle(event);
    }

    @Override
    protected SensorEventAvro mapToAvroSensorEvent(BaseSensorEvent event) {
        ClimateSensorAvro avro = sensorEventMapper.mapToClimateSensorAvro((ClimateSensorEvent) event);
        return buildSensorEventAvro(event, avro);
    }
}
