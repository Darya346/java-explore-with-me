package ru.practicum.ewm.service;

import ru.practicum.ewm.dto.request.NewUserRequest;
import ru.practicum.ewm.dto.response.UserDto;

import java.util.List;

public interface UserService {

    UserDto createUser(NewUserRequest newUserRequest);

    List<UserDto> getUsers(List<Long> ids, int from, int size);

    void deleteUser(Long userId);
}