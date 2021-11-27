package src.argon.argon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import src.argon.argon.entity.Worktime;

@Repository
public interface WorktimeRepository extends JpaRepository<Worktime, Long> {

}
