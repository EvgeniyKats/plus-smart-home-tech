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
 * Составной ключ для ScenarioAction
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
public class ScenarioActionId implements Serializable {
    private Long scenarioId;
    private String sensorId;
    private Long actionId;
}