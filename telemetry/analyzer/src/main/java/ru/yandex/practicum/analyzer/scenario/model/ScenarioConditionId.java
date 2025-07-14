package ru.yandex.practicum.analyzer.scenario.model;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

/**
 * Составной ключ для ScenarioCondition
 */

// Lombok annotations
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PRIVATE)
// JPA annotations
@Embeddable
public class ScenarioConditionId implements Serializable {
    Long scenarioId;
    String sensorId;
    Long conditionId;
}
