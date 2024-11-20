package repository.impl;

import domain.history.ExplosionRecord;
import repository.IHistoryRepository;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class InMemoryHistoryRepository implements IHistoryRepository {
    private final List<ExplosionRecord> records = new CopyOnWriteArrayList<>();

    @Override
    public void saveRecord(ExplosionRecord record) {
        Objects.requireNonNull(record, "Record cannot be null");
        records.add(record);
    }

    @Override
    public List<ExplosionRecord> getAllRecords() {
        return new ArrayList<>(records);
    }

    @Override
    public void clearHistory() {
        records.clear();
    }
}