package src.argon.argon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import src.argon.argon.dto.ProjectDTO;
import src.argon.argon.entity.Project;
import src.argon.argon.mapper.ProjectMapper;
import src.argon.argon.repository.ProjectRepository;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class ProjectService {

    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    ProjectMapper projectMapper;

    public List<ProjectDTO> getProjectsForOrganization(Long organizationId) {
        return projectMapper.toDTO(projectRepository.findAllByOrganizationId(organizationId));
    }

    public ProjectDTO save(ProjectDTO projectDTO) {
        Project project = projectMapper.toEntity(projectDTO);
        return projectMapper.toDTO(projectRepository.save(project));
    }

    public void delete(Long id) {
        projectRepository.deleteById(id);
    }
}
