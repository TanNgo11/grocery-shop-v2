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
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

import static com.thanhtan.groceryshop.service.impl.CloudinaryService.getPublicIdFromUrl;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService implements IUserService {

    UserRepository userRepository;

    UserMapper userMapper;

    RoleRepository roleRepository;

    PasswordEncoder BCryptPasswordEncoder;

    CloudinaryService cloudinaryService;

    @Override
    public UserResponse createUser(UserRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) throw new AppException(ErrorCode.USER_EXISTED);

        User user = userMapper.toUser(request);
        user.setPassword(BCryptPasswordEncoder.encode(request.getPassword()));

        Set<com.thanhtan.groceryshop.entity.Role> roles = new HashSet<>();
        com.thanhtan.groceryshop.entity.Role userRole = roleRepository.findByName(Role.USER.name()).orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));
        roles.add(userRole);
        user.setRoles(roles);

        return userMapper.toUserResponse(userRepository.save(user));
    }

    @Override
    public UserResponse getMyInfo() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        UserResponse userResponse = userMapper.toUserResponse(user);
        Set<String> roles = new HashSet<>();
        user.getRoles().forEach(role -> roles.add(role.getName()));
        userResponse.setRoles(roles);
        return userResponse;
    }

    @Override
    public UserResponse updateUserProfileById(UpdateUserRequest request, MultipartFile file, Long userId) {
        User user = userRepository.findById(userId);
        if (user == null) throw new AppException(ErrorCode.USER_NOT_EXISTED);
        String oldImagePublicId = getPublicIdFromUrl(user.getAvatar() != null ? user.getAvatar() : "");
        userMapper.updateUser(user, request);
        if (file != null) {
            Map data = cloudinaryService.upload(file);
            if (data != null) {
                String newImageUrl = (String) data.get("url");
                if (!oldImagePublicId.isEmpty()) cloudinaryService.deleteImage(oldImagePublicId);
                user.setAvatar(newImageUrl);
            }
        }
        User updatedUser = userRepository.save(user);

        return userMapper.toUserResponse(updatedUser);
    }

    @Override
    public UserResponse updateUserProfile(UpdateUserRequest request, MultipartFile file) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        String oldImagePublicId = getPublicIdFromUrl(user.getAvatar() != null ? user.getAvatar() : "");
        userMapper.updateUser(user, request);
        if (file != null) {
            Map data = cloudinaryService.upload(file);
            if (data != null) {
                String newImageUrl = (String) data.get("url");
                if (!oldImagePublicId.isEmpty()) cloudinaryService.deleteImage(oldImagePublicId);
                user.setAvatar(newImageUrl);
            }
        }
        User updatedUser = userRepository.save(user);

        return userMapper.toUserResponse(updatedUser);
    }

    @Override
    public List<UserResponse> getUsers() {

        return userMapper.toUserResponseList(userRepository.findAllWithRoles());
    }

    @Override
    public UserResponse getUserById(Long userId) {
        User user = userRepository.findById(userId);
        UserResponse userResponse = userMapper.toUserResponse(user);
        Set<String> roles = new HashSet<>();
        user.getRoles().forEach(role -> roles.add(role.getName()));
        userResponse.setRoles(roles);
        return userResponse;
    }

    @Override
    public List<UserResponse> getAllAdmin() {
        List<User> adminUsers = userRepository.findAllWithRoles(Role.ADMIN.name());
        List<User> staffUsers = userRepository.findAllWithRoles(Role.STAFF.name());

        List<User> combinedUsers = new ArrayList<>();
        combinedUsers.addAll(adminUsers);
        combinedUsers.addAll(staffUsers);

        return userMapper.toUserResponseList(combinedUsers);
    }

    @Override
    public List<UserResponse> getAllCustomer() {
        return userMapper.toUserResponseList(userRepository.findAllWithRoles(Role.USER.name()));
    }


}
