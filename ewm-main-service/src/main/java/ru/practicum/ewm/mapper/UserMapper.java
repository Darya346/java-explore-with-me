package ru.practicum.ewm.mapper;

import ru.practicum.ewm.dto.response.UserDto;
import ru.practicum.ewm.dto.request.NewUserRequest;
import ru.practicum.ewm.model.User;

public class UserMapper {

    public static UserDto toDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setName(user.getName());
        return dto;
    }

    public static User toUser(NewUserRequest request) {
        User user = new User();
        user.setEmail(request.getEmail());
        user.setName(request.getName());
        return user;
    }
}