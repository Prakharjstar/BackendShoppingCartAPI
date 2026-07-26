package com.dailycodework.demo.service.User;

import com.dailycodework.demo.dto.UserDto;
import com.dailycodework.demo.model.User;
import com.dailycodework.demo.request.CreateUserRequest;
import com.dailycodework.demo.request.UserUpdateRequest;

public interface UserService {
    User getUserById(Long userId);
    User createUser(CreateUserRequest request);
    User updateUser(UserUpdateRequest request , Long userId);
    void deleteUser(Long userId);

    UserDto convertUserToDto(User user);

    User getAuthenticatedUser();
}
