package ru.yandex.practicum.util;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.exception.NotFoundException;
import ru.yandex.practicum.model.hub.HubEventType;
import ru.yandex.practicum.service.HubEventHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Component
public class HubEventHandleFactory {
    private final Map<HubEventType, HubEventHandler> hubEvent;

    public HubEventHandleFactory(Set<HubEventHandler> handlers) {
        hubEvent = new HashMap<>();
        handlers.forEach(handler -> hubEvent.put(handler.getHubEventType(), handler));
    }

    public HubEventHandler getHubEventHandlerByType(HubEventType hubEventType) {
        return Optional.ofNullable(hubEvent.get(hubEventType))
                .orElseThrow(() -> new NotFoundException(hubEventType + " not exist in map"));
    }
}
