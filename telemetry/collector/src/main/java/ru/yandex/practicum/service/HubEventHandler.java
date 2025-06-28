package ru.yandex.practicum.service;

import ru.yandex.practicum.model.hub.BaseHubEvent;
import ru.yandex.practicum.model.hub.HubEventType;

public interface HubEventHandler {
    HubEventType getHubEventType();

    void handle(BaseHubEvent hubEvent);
}
