package src.argon.argon.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    ProjectMapper projectMapper;


    public List<ProjectDTO> findAllProjects() {
        return projectMapper.toDTO(projectRepository.findAll());
    }

    public ProjectDTO findOne(Long id) {
        return projectMapper.toDTO(projectRepository.findById(id).orElse(null));
    }

    public ProjectDTO save(ProjectDTO project) {
        return projectMapper.toDTO(projectRepository.save(projectMapper.toEntity(project)));
    }

    public void delete(Long id) {
        projectRepository.deleteById(id);
    }
}
