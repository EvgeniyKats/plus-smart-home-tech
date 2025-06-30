package ru.yandex.practicum.service.hub;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.config.KafkaTopicsNames;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioAddedEventAvro;
import ru.yandex.practicum.model.hub.BaseHubEvent;
import ru.yandex.practicum.model.hub.HubEventType;
import ru.yandex.practicum.model.hub.scenario.ScenarioAddedEvent;
import ru.yandex.practicum.service.kafka.KafkaEventProducer;

@Component
public class ScenarioAddedEventHandler extends BaseHubEventHandler<ScenarioAddedEventAvro> {
    public ScenarioAddedEventHandler(KafkaEventProducer kafkaEventProducer,
                                     KafkaTopicsNames topicsNames,
                                     HubEventMapper hubEventMapper) {
        super(kafkaEventProducer, topicsNames, hubEventMapper);
    }

    @Override
    protected HubEventAvro mapToHubEventAvro(BaseHubEvent event) {
        ScenarioAddedEventAvro avro = hubEventMapper.mapToScenarioAddedEventAvro((ScenarioAddedEvent) event);
        return buildHubEventAvro(event, avro);
    }

    @Override
    public HubEventType getHubEventType() {
        return HubEventType.SCENARIO_ADDED;
    }

    @Override
    public void handle(BaseHubEvent event) {
        validateEventType(event, ScenarioAddedEvent.class);
        super.handle(event);
    }
}
