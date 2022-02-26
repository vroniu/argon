package src.argon.argon.service;

import org.hibernate.Session;
import org.hibernate.annotations.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import src.argon.argon.dto.WorktimeDTO;
import src.argon.argon.entity.Worktime;
import src.argon.argon.mapper.WorktimeMapper;
import src.argon.argon.repository.WorktimeRepository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

@Service
@Transactional
public class WorktimeService {

    @Autowired
    WorktimeRepository worktimeRepository;
    @Autowired
    WorktimeMapper worktimeMapper;

    public WorktimeDTO save(WorktimeDTO worktimeDto) {
        Worktime worktime = worktimeMapper.toEntity(worktimeDto);
        return worktimeMapper.toDTO(worktimeRepository.save(worktime));
    }

    public List<WorktimeDTO> getWorktimesAtDayForUser(LocalDate day, Long userEmployeeId, Long organizationId) {
        return worktimeMapper.toDTO(
                worktimeRepository.findByDayBetweenAndEmployeeIdAndSubprojectProjectOrganizationId(day, day,
                    userEmployeeId, organizationId)
        );
    }

    public List<WorktimeDTO> getWorktimesAtDateRangeForUser(LocalDate rangeStart, LocalDate rangeEnd, Long userEmployeeId, Long organizationId) {
        return worktimeMapper.toDTO(
                worktimeRepository.findByDayBetweenAndEmployeeIdAndSubprojectProjectOrganizationId(rangeStart, rangeEnd,
                        userEmployeeId, organizationId)
        );
    }

    public void deleteWorktime(Long id) {
        worktimeRepository.deleteById(id);
    }
}
