package src.argon.argon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import src.argon.argon.dto.ProjectDTO;
import src.argon.argon.dto.SubprojectDTO;
import src.argon.argon.entity.Project;
import src.argon.argon.entity.Subproject;
import src.argon.argon.mapper.SubprojectMapper;
import src.argon.argon.repository.SubprojectRepository;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class SubprojectService {

    @Autowired
    SubprojectMapper subprojectMapper;
    @Autowired
    SubprojectRepository subprojectRepository;

    public SubprojectDTO save(SubprojectDTO subprojectDTO) {
        Subproject subproject = subprojectMapper.toEntity(subprojectDTO);
        return subprojectMapper.toDTO(subprojectRepository.save(subproject));
    }

    public void delete(Long id) {
        subprojectRepository.deleteById(id);
    }

}
