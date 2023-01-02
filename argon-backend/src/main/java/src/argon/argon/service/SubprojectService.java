package src.argon.argon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import src.argon.argon.dto.SubprojectDTO;
import src.argon.argon.entity.Subproject;
import src.argon.argon.mapper.SubprojectMapper;
import src.argon.argon.repository.ProjectRepository;
import src.argon.argon.repository.SubprojectRepository;

import javax.transaction.Transactional;

@Service
@Transactional
public class SubprojectService {

    @Autowired
    SubprojectMapper subprojectMapper;

    @Autowired
    SubprojectRepository subprojectRepository;

    @Autowired
    ProjectRepository projectRepository;

    public SubprojectDTO save(SubprojectDTO subprojectDTO) {
        Subproject subproject = subprojectMapper.toEntity(subprojectDTO);
        subproject.setProject(projectRepository.getById(subprojectDTO.getProjectId()));
        return subprojectMapper.toDTO(subprojectRepository.save(subproject));
    }

    public void delete(Long id) {
        subprojectRepository.deleteById(id);
    }

}
