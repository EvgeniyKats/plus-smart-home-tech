package ru.yandex.practicum.model.hub.scenario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.model.hub.HubEventType;

import java.time.Instant;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ScenarioRemovedEvent {
    @NotBlank
    String hubId;

    Instant timestamp = Instant.now();

    @NotBlank
    String name;

    @NotNull
    HubEventType type;
}
