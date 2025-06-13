package online_shop.mapper;

import online_shop.dto.UserDto;
import online_shop.entity.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(UserDto userDto);
    List<User> toEntity(List<UserDto> userDtoList);

    UserDto toDto(User user);
    List<UserDto> toDto(List<User> userList);
}
