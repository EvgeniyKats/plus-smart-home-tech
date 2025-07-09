package ru.yandex.practicum.snapshot.storage;

import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;
import ru.yandex.practicum.snapshot.model.SensorsSnapshotAvroAdapter;
import ru.yandex.practicum.snapshot.model.Snapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Класс-хранилище in memory для снапшотов типа SensorsSnapshotAvro
 */
public class SnapshotStorageAvroInMemory implements SnapshotStorage<SensorsSnapshotAvro, SensorEventAvro> {
    // Мапа снапшотов, key - SensorEventAvro.hubId, value - снапшот
    private final Map<String, Snapshot<SensorsSnapshotAvro, SensorEventAvro>> snapshots;

    public SnapshotStorageAvroInMemory() {
        snapshots = new HashMap<>();
    }

    @Override
    public Optional<SensorsSnapshotAvro> updateState(SensorEventAvro event) {
        // hubId - ключ к снапшоту
        String hubId = event.getHubId();
        Snapshot<SensorsSnapshotAvro, SensorEventAvro> snapshot = snapshots.get(hubId);

        // Если снапшота нет в мапе, создать его
        if (snapshot == null) {
            snapshot = new SensorsSnapshotAvroAdapter();
            snapshots.put(hubId, snapshot);
        }

        // Вызов обновления снапшота
        return snapshot.updateState(event);
    }
}
