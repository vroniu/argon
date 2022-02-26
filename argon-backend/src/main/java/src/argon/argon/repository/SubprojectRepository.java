package src.argon.argon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import src.argon.argon.entity.Subproject;

import java.util.List;

@Repository
public interface SubprojectRepository extends JpaRepository<Subproject, Long> {
    List<Subproject> findAllByProjectOrganizationId(Long projectOrganizationId);
}
