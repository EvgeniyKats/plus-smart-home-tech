package ru.yandex.practicum.analyzer.service.hub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.analyzer.action.ActionRepository;
import ru.yandex.practicum.analyzer.action.model.Action;
import ru.yandex.practicum.analyzer.action.model.ActionType;
import ru.yandex.practicum.analyzer.condition.ConditionRepository;
import ru.yandex.practicum.analyzer.condition.model.Condition;
import ru.yandex.practicum.analyzer.condition.model.ConditionOperation;
import ru.yandex.practicum.analyzer.condition.model.ConditionType;
import ru.yandex.practicum.analyzer.exception.NotFoundException;
import ru.yandex.practicum.analyzer.scenario.ScenarioRepository;
import ru.yandex.practicum.analyzer.scenario.model.Scenario;
import ru.yandex.practicum.analyzer.scenario.model.ScenarioAction;
import ru.yandex.practicum.analyzer.scenario.model.ScenarioCondition;
import ru.yandex.practicum.analyzer.sensor.SensorRepository;
import ru.yandex.practicum.analyzer.sensor.model.Sensor;
import ru.yandex.practicum.analyzer.util.ValueToInteger;
import ru.yandex.practicum.kafka.telemetry.event.DeviceActionAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceRemovedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioConditionAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioRemovedEventAvro;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class HubServiceImpl implements HubService {
    private final SensorRepository sensorRepository;
    private final ScenarioRepository scenarioRepository;
    private final ConditionRepository conditionRepository;
    private final ActionRepository actionRepository;

    @Override
    public void addDevice(DeviceAddedEventAvro deviceAddedEventAvro, String hubId) {

        Sensor sensor = Sensor.builder()
                .hubId(hubId)
                .id(deviceAddedEventAvro.getId())
                .build();

        sensorRepository.save(sensor);
    }

    @Override
    public void removeDevice(DeviceRemovedEventAvro deviceRemovedEventAvro) {

        sensorRepository.deleteById(deviceRemovedEventAvro.getId());
    }

    @Transactional
    @Override
    public void addScenario(ScenarioAddedEventAvro scenarioAddedEventAvro, String hubId) {
        // Сценарий
        Scenario scenario = Scenario.builder()
                .hubId(hubId)
                .name(scenarioAddedEventAvro.getName())
                .build();
        scenarioRepository.save(scenario);

        // id сенсоров для последующей загрузки
        Set<String> sensorIds = new HashSet<>();
        scenarioAddedEventAvro.getConditions().forEach(conditionAvro -> sensorIds.add(conditionAvro.getSensorId()));
        scenarioAddedEventAvro.getActions().forEach(actionAvro -> sensorIds.add(actionAvro.getSensorId()));

        // загрузка сенсоров за 1 запрос
        Map<String, Sensor> sensorsMap = sensorRepository.findAllById(sensorIds).stream()
                .collect(Collectors.toMap(Sensor::getId, sensor -> sensor));

        // Проверям, все ли сенсоры найдены
        if (sensorsMap.size() != sensorIds.size()) {
            List<String> notFoundIds = new ArrayList<>();

            for (String sensorId : sensorIds) {
                if (!sensorsMap.containsKey(sensorId)) {
                    notFoundIds.add(sensorId);
                }
            }
            int diff = sensorIds.size() - sensorsMap.size();

            String msg = String.format("Не найдено %d сенсоров, ids=%s", diff, notFoundIds);
            log.warn(msg);
            throw new NotFoundException(msg);
        }

        // Условия
        for (ScenarioConditionAvro conditionAvro : scenarioAddedEventAvro.getConditions()) {
            Condition condition = Condition.builder()
                    .type(ConditionType.valueOf(conditionAvro.getType().name()))
                    .operation(ConditionOperation.valueOf(conditionAvro.getOperation().name()))
                    .value(ValueToInteger.convert(conditionAvro.getValue()))
                    .build();

            condition = conditionRepository.save(condition);

            // Получаем сенсор из мапы
            Sensor sensor = sensorsMap.get(conditionAvro.getSensorId());

            // Создаем ScenarioCondition
            ScenarioCondition scenarioCondition = new ScenarioCondition(scenario, sensor, condition);
            scenario.addScenarioCondition(scenarioCondition);
        }

        // Действия
        for (DeviceActionAvro actionAvro : scenarioAddedEventAvro.getActions()) {
            Action action = Action.builder()
                    .type(ActionType.valueOf(actionAvro.getType().name()))
                    .value(actionAvro.getValue())
                    .build();

            action = actionRepository.save(action);

            // Получаем сенсор из мапы
            Sensor sensor = sensorsMap.get(actionAvro.getSensorId());

            // Создаем ScenarioAction
            ScenarioAction scenarioAction = new ScenarioAction(scenario, sensor, action);
            scenario.addScenarioAction(scenarioAction);
        }
    }

    @Transactional
    @Override
    public void removeScenario(ScenarioRemovedEventAvro scenarioRemovedEventAvro, String hubId) {
        scenarioRepository.deleteByHubIdAndName(hubId, scenarioRemovedEventAvro.getName());
    }
}
