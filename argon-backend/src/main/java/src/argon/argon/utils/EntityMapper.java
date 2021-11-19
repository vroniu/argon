package src.argon.argon.utils;

import java.util.List;

public interface EntityMapper<Entity, DTO> {
    Entity toEntity(DTO dto);
    List<Entity> toEntity(List<DTO> dto);
    DTO toDTO(Entity entity);
    List<DTO> toDTO(List<Entity> entity);
}
