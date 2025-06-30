package ru.yandex.practicum.service.hub;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.config.KafkaTopicsNames;
import ru.yandex.practicum.kafka.telemetry.event.DeviceRemovedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.model.hub.BaseHubEvent;
import ru.yandex.practicum.model.hub.HubEventType;
import ru.yandex.practicum.model.hub.device.DeviceRemovedEvent;
import ru.yandex.practicum.service.kafka.KafkaEventProducer;

@Component
public class DeviceRemovedEventHandler extends BaseHubEventHandler<DeviceRemovedEventAvro> {
    public DeviceRemovedEventHandler(KafkaEventProducer kafkaEventProducer,
                                     KafkaTopicsNames topicsNames,
                                     HubEventMapper hubEventMapper) {
        super(kafkaEventProducer, topicsNames, hubEventMapper);
    }

    @Override
    protected HubEventAvro mapToHubEventAvro(BaseHubEvent event) {
        DeviceRemovedEventAvro avro = hubEventMapper.mapToDeviceRemovedEventAvro((DeviceRemovedEvent) event);
        return buildHubEventAvro(event, avro);
    }

    @Override
    public HubEventType getHubEventType() {
        return HubEventType.DEVICE_REMOVED;
    }

    @Override
    public void handle(BaseHubEvent event) {
        validateEventType(event, DeviceRemovedEvent.class);
        super.handle(event);
    }
}
