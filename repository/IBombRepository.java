// repository/IBombRepository.java
package repository;

import domain.bomb.Bomb;
import java.util.List;
import java.util.Optional;

public interface IBombRepository {
    void save(Bomb bomb);
    void delete(String bombId);
    Optional<Bomb> findById(String id);
    List<Bomb> findAll();
}
