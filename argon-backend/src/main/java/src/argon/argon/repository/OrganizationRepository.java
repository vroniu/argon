package src.argon.argon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import src.argon.argon.entity.Organization;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long> {

}
