package com.jakan.auth_service.service.facade;

import com.jakan.auth_service.dto.UserDto;

public interface UserService extends AbstractService<UserDto, Long> {
    UserDto findByUsername(String username);
}
