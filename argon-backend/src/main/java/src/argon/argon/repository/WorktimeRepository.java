package src.argon.argon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import src.argon.argon.entity.Worktime;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface WorktimeRepository extends JpaRepository<Worktime, Long> {
    List<Worktime> findByDayBetweenAndEmployeeIdAndSubprojectProjectOrganizationId(
            LocalDate startDate, LocalDate endDate,
            Long employeeId, Long subprojectProjectOrganizationId
    );
}
