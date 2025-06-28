package ru.yandex.practicum.model.sensor;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type",
        defaultImpl = BaseSensorEvent.class
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = MotionBaseSensorEvent.class, name = "MOTION_SENSOR_EVENT"),
        @JsonSubTypes.Type(value = TemperatureBaseSensorEvent.class, name = "TEMPERATURE_SENSOR_EVENT"),
        @JsonSubTypes.Type(value = LightBaseSensorEvent.class, name = "LIGHT_SENSOR_EVENT"),
        @JsonSubTypes.Type(value = ClimateBaseSensorEvent.class, name = "CLIMATE_SENSOR_EVENT"),
        @JsonSubTypes.Type(value = SwitchBaseSensorEvent.class, name = "SWITCH_SENSOR_EVENT")
})
@Getter
@Setter
public abstract class BaseSensorEvent {
    @NotBlank
    String id;

    @NotBlank
    String hubId;

    Instant timestamp = Instant.now();

    public abstract SensorEventType getType();
}
