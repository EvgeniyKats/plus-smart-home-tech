package ru.yandex.practicum.kafka.serializer;

import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;
import ru.yandex.practicum.kafka.telemetry.event.LightSensorAvro;

public class LightAvroDeserializer implements Deserializer<LightSensorAvro> {
    private final DecoderFactory decoderFactory = DecoderFactory.get();
    private final DatumReader<LightSensorAvro> reader = new SpecificDatumReader<>(LightSensorAvro.getClassSchema());


    @Override
    public LightSensorAvro deserialize(String topic, byte[] data) {
        try {
            if (data != null) {
                BinaryDecoder decoder = decoderFactory.binaryDecoder(data, null);
                return this.reader.read(null, decoder);
            }
            return null;
        } catch (Exception e) {
            throw new SerializationException("Ошибка десереализации данных из топика [" + topic + "]", e);
        }

    }
}
