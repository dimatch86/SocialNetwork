package ru.skillbox.zerone.backend.mapstruct;

import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import ru.skillbox.zerone.backend.model.dto.request.RegisterRequestDTO;
import ru.skillbox.zerone.backend.model.dto.response.UserDTO;
import ru.skillbox.zerone.backend.model.entity.User;

import java.util.List;

@Mapper
@DecoratedWith(UserMapperDecorator.class)
public interface UserMapper {
  UserDTO userToUserDTO(User user);

  User registerRequestDTOToUser(RegisterRequestDTO registerRequestDTO);

  List<UserDTO> usersToUserDTO(List<User> userList);
}
