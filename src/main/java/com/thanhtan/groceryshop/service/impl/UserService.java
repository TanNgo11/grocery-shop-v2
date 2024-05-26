package com.thanhtan.groceryshop.service.impl;

import com.thanhtan.groceryshop.dto.request.UpdateUserRequest;
import com.thanhtan.groceryshop.dto.request.UserRequest;
import com.thanhtan.groceryshop.dto.response.UserResponse;
import com.thanhtan.groceryshop.entity.User;


import com.thanhtan.groceryshop.enums.Role;
import com.thanhtan.groceryshop.exception.AppException;
import com.thanhtan.groceryshop.exception.ErrorCode;
import com.thanhtan.groceryshop.mapper.UserMapper;
import com.thanhtan.groceryshop.repository.RoleRepository;
import com.thanhtan.groceryshop.repository.UserRepository;
import com.thanhtan.groceryshop.service.IUserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService implements IUserService {

    UserRepository userRepository;

    UserMapper userMapper;

    RoleRepository roleRepository;

    PasswordEncoder BCryptPasswordEncoder;

    @Override
    public UserResponse createUser(UserRequest request) {
        if (userRepository.existsByUsername(request.getUsername()))
            throw new AppException(ErrorCode.USER_EXISTED);

        User user = userMapper.toUser(request);
        user.setPassword(BCryptPasswordEncoder.encode(request.getPassword()));

        Set<com.thanhtan.groceryshop.entity.Role> roles = new HashSet<>();
        com.thanhtan.groceryshop.entity.Role userRole = roleRepository.findByName(Role.USER.name())
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));
        roles.add(userRole);
        user.setRoles(roles);

        return userMapper.toUserResponse(userRepository.save(user));
    }

    @Override
    public UserResponse getMyInfo() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user =   userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        UserResponse userResponse = userMapper.toUserResponse(user);
        Set<String> roles = new HashSet<>();
        user.getRoles().forEach(role -> roles.add(role.getName()));
        userResponse.setRoles(roles);
        return userResponse;
    }

    @Override
    public UserResponse updateUserProfile(UpdateUserRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        userMapper.updateUser(user, request);
        User updatedUser = userRepository.save(user);

        return userMapper.toUserResponse(updatedUser);
    }
}
