package src.argon.argon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import src.argon.argon.entity.Organization;

import java.util.List;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long> {
    List<Organization> findByOwnersId(Long ownerId);
    List<Organization> findByEmployeesId(Long employeeId);

    @Modifying
    @Query("UPDATE Organization o SET o.name = :name WHERE o.id = :id")
    Integer updateOrganizationFields(Long id, String name);
}
