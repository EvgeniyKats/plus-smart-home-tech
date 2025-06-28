package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.model.hub.BaseHubEvent;
import ru.yandex.practicum.model.sensor.BaseSensorEvent;

@Slf4j
@Validated
@RestController
@RequestMapping("/events")
public class EventController {
    @PostMapping("/sensors")
    public void collectSensorEvent(@Valid @RequestBody BaseSensorEvent sensorEvent) {

    }

    @PostMapping("/hubs")
    public void hubs(@Valid @RequestBody BaseHubEvent hubEvent) {

    }
}