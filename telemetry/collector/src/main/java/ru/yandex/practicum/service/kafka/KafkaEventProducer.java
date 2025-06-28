package ru.yandex.practicum.service.kafka;

import jakarta.annotation.PreDestroy;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.serializer.GeneralAvroSerializer;

import java.util.Properties;

@Component
public class KafkaEventProducer implements AutoCloseable {
    private final Producer<String, SpecificRecordBase> producer;

    private KafkaEventProducer() {
        Properties config = new Properties();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, GeneralAvroSerializer.class);
        this.producer = new KafkaProducer<>(config);
    }

    public void send(ProducerRecord<String, SpecificRecordBase> record) {
        producer.send(record);
    }

    @PreDestroy
    @Override
    public void close() throws Exception {
        if (producer != null) {
            producer.flush();
            producer.close();
        }
    }
}
