package ru.yandex.practicum.analyzer.service.snapshot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.analyzer.condition.model.Condition;
import ru.yandex.practicum.analyzer.condition.model.ConditionOperation;
import ru.yandex.practicum.analyzer.condition.model.ConditionType;
import ru.yandex.practicum.analyzer.scenario.ScenarioRepository;
import ru.yandex.practicum.analyzer.scenario.model.Scenario;
import ru.yandex.practicum.analyzer.scenario.model.ScenarioCondition;
import ru.yandex.practicum.analyzer.service.snapshot.condition.ConditionValueHandler;
import ru.yandex.practicum.analyzer.util.ConditionValueHandlerFactory;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Service
public class SnapshotServiceImpl implements SnapshotService {

    private final ScenarioRepository scenarioRepository;
    private final ConditionValueHandlerFactory conditionValueHandlerFactory;

    @Transactional(readOnly = true) // Только чтение данных из БД
    public void handleSnapshot(SensorsSnapshotAvro snapshotAvro) {
        String hubId = snapshotAvro.getHubId();

        // Сценарии, связанные с хабом
        List<Scenario> scenarios = scenarioRepository.findByHubId(hubId);

        // мапа key - id сенсора, value - состояние
        Map<String, SensorStateAvro> sensorStateMap = snapshotAvro.getSensorsState();

        // обход сценариев с проверкой условий
        for (Scenario scenario : scenarios) {
            boolean allConditionsMet = checkScenarioConditions(scenario.getScenarioConditions(), sensorStateMap);

            if (allConditionsMet) {
                // TODO вызов метода сервиса hub router
            }
        }
    }

    /**
     * Проверяет, выполнены ли все условия сценария
     */
    private boolean checkScenarioConditions(Set<ScenarioCondition> scenarioConditions,
                                            Map<String, SensorStateAvro> sensorStateMap) {
        for (ScenarioCondition scenarioCondition : scenarioConditions) {

            String sensorId = scenarioCondition.getSensor().getId();

            Condition condition = scenarioCondition.getCondition(); // условие (из БД)
            SensorStateAvro sensorState = sensorStateMap.get(sensorId); // состояние (из снапшота)

            // если не выполняется хотя бы одно условие, произойдёт выход из цикла
            if (!isConditionMet(condition, sensorState)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Проверяет выполнение отдельного условия
     *
     * @param condition   - условие (из БД)
     * @param sensorState - состояние (из снапшота)
     * @return true, если условие выполняется, иначе false
     */
    private boolean isConditionMet(Condition condition, SensorStateAvro sensorState) {
        // Если состояние не найдено в снапшоте, условие не выполняется
        if (sensorState == null) {
            return false;
        }

        // Тип условия
        ConditionType type = condition.getType();

        // Получаем обработчика значения
        ConditionValueHandler handler = conditionValueHandlerFactory.getHandlerByConditionType(type);

        // Получаем конкретное значение из состояния снапшота
        Integer sensorValue = handler.handleValue(sensorState.getData());

        // Оператор сравнения и сравниваемое значение
        ConditionOperation operation = condition.getOperation();
        Integer compareValue = condition.getValue();

        return switch (operation) {
            case EQUALS -> sensorValue.compareTo(compareValue) == 0;
            case GREATER_THAN -> sensorValue.compareTo(compareValue) > 0;
            case LOWER_THAN -> sensorValue.compareTo(compareValue) < 0;
        };
    }
}
