package src.argon.argon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import src.argon.argon.entity.Organization;

import java.util.List;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long> {
    List<Organization> findByOwnersId(Long ownerId);
    List<Organization> findByEmployeesId(Long employeeId);
}
