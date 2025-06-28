package ru.yandex.practicum.kafka.serializer;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.VoidDeserializer;
import ru.yandex.practicum.kafka.telemetry.event.LightSensorAvro;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

public class Main {
    public static void main(String[] args) {
//        LightSensorAvro lightSensorAvro = LightSensorAvro.newBuilder()
//                .setLuminosity(1)
//                .setLinkQuality(2)
//                .build();
//        System.exit(0);
//
//        GeneralAvroSerializer generalAvroSerializer = new GeneralAvroSerializer();
//        System.out.println("generalAvroSerializer.serialize(\"\",null) = " + Arrays.toString(generalAvroSerializer.serialize("", null)));
//        System.exit(0);
        Properties config = new Properties();
        // эти настройки нужны, чтобы консьюмер всегда читал сообщения с самого начала топика (то есть все сообщения)
        config.put(ConsumerConfig.GROUP_ID_CONFIG, UUID.randomUUID().toString());
//        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        // обязательные настройки
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, LightAvroDeserializer.class);

        String topic = "telemetry.sensors.v1";

        try (Consumer<String, LightSensorAvro> consumer = new KafkaConsumer<>(config)) {
            consumer.subscribe(List.of(topic));

            while (true) {
                ConsumerRecords<String, LightSensorAvro> records =
                        consumer.poll(Duration.ofMillis(500));
                for (ConsumerRecord<String, LightSensorAvro> record : records) {
                    System.out.println("record.value() = " + record.value());
                }
            }
        }




//
//        GeneralAvroSerializer generalAvroSerializer = new GeneralAvroSerializer();
//        byte[] bytes = generalAvroSerializer.serialize("topicName", lightSensorAvro);
//
//        LightAvroDeserializer lightAvroDeserializer = new LightAvroDeserializer();
//
//        LightSensorAvro afterDeserialize = lightAvroDeserializer.deserialize("topicName", bytes);
//
//        System.out.println(afterDeserialize.getLuminosity());
//        System.out.println(afterDeserialize.getLinkQuality());


    }
}
