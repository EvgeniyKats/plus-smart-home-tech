package ru.yandex.practicum.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/events")
public class EventController {
    @PostMapping
    public void sensors() {

    }

    @PostMapping
    public void hubs() {

    }
}
