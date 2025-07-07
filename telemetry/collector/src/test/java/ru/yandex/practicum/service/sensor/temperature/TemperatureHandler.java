package ru.yandex.practicum.service.sensor.temperature;

import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.config.KafkaTopicsNames;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.grpc.telemetry.event.TemperatureSensorProto;
import ru.yandex.practicum.kafka.telemetry.event.TemperatureSensorAvro;
import ru.yandex.practicum.service.sensor.BaseSensorEventHandlerTest;
import ru.yandex.practicum.service.sensor.SensorEventAvroMapper;
import ru.yandex.practicum.service.sensor.SensorEventProtoMapper;
import ru.yandex.practicum.util.SensorEventHandleFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;

// Определяет проверку полей специфичного сенсора
abstract class TemperatureHandler extends BaseSensorEventHandlerTest {
    protected TemperatureSensorProto sourceProto;

    @Autowired
    public TemperatureHandler(SensorEventHandleFactory sensorEventHandleFactory, KafkaTopicsNames kafkaTopicsNames, SensorEventAvroMapper sensorEventAvroMapper, SensorEventProtoMapper sensorEventProtoMapper) {
        super(sensorEventHandleFactory,
                kafkaTopicsNames,
                sensorEventAvroMapper,
                sensorEventProtoMapper,
                sensorEventHandleFactory.getSensorEventHandlerByPayloadCase(SensorEventProto.PayloadCase.TEMPERATURE_SENSOR_EVENT));
    }

    @Override
    protected void checkSpecificAvroFields() {
        // Проверка полей специфичного сенсора
        TemperatureSensorAvro targetAvro = (TemperatureSensorAvro) targetBase.getPayload();
        assertEquals(sourceProto.getTemperatureC(), targetAvro.getTemperatureC());
        assertEquals(sourceProto.getTemperatureF(), targetAvro.getTemperatureF());
    }
}
