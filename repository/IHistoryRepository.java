package repository;

import domain.history.ExplosionRecord;
import java.util.List;

public interface IHistoryRepository {
    void saveRecord(ExplosionRecord record);
    List<ExplosionRecord> getAllRecords();
}