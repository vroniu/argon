package src.argon.argon.mapper;

import org.mapstruct.Mapper;
import src.argon.argon.dto.UserDTO;
import src.argon.argon.security.models.User;
import src.argon.argon.utils.EntityMapper;

@Mapper(componentModel = "spring", uses = {EmployeeMapper.class})
public interface UserMapper extends EntityMapper<User, UserDTO> {

    default User fromId(Long id) {
        if (id == null)
            return null;
        User user = new User();
        user.setId(id);
        return user;
    }
}
