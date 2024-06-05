package com.thanhtan.groceryshop.service;

import com.thanhtan.groceryshop.dto.request.UpdateUserRequest;
import com.thanhtan.groceryshop.dto.request.UserRequest;
import com.thanhtan.groceryshop.dto.response.UserResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IUserService {
    UserResponse createUser(UserRequest request);

    UserResponse getMyInfo();

    UserResponse updateUserProfile(UpdateUserRequest request, MultipartFile file);

    UserResponse updateUserProfileById(UpdateUserRequest request, MultipartFile file, Long userId);

    List<UserResponse> getUsers();

    List<UserResponse> getAllCustomer();

    UserResponse getUserById(Long userId);

    List<UserResponse> getAllAdmin();
}
