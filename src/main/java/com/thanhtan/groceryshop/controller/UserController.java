package com.thanhtan.groceryshop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.thanhtan.groceryshop.dto.request.UpdateUserRequest;
import com.thanhtan.groceryshop.dto.request.UserRequest;
import com.thanhtan.groceryshop.dto.response.ApiResponse;
import com.thanhtan.groceryshop.dto.response.UserResponse;
import com.thanhtan.groceryshop.service.IUserService;
import com.thanhtan.groceryshop.util.ValidationUtil;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.thanhtan.groceryshop.constant.PathConstant.API_V1_USERS;


@RestController
@RequestMapping(API_V1_USERS)
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {

    IUserService userService;

    @PostMapping
    ApiResponse<UserResponse> createUser(@RequestBody @Valid UserRequest request) {
        return ApiResponse.success(userService.createUser(request));

    }

    @GetMapping("/myInfo")
    @PreAuthorize("hasRole('USER')||hasRole('ADMIN')||hasRole('STAFF')")
    ApiResponse<UserResponse> getMyInfo() {
        return ApiResponse.success(userService.getMyInfo());
    }


    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('USER')||hasRole('ADMIN')")
    public ApiResponse<UserResponse> updateUserProfile(
            @RequestPart("user") String userStr,
            @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {
        UpdateUserRequest updateUserRequest = ValidationUtil.validateUserStr(userStr);

        return ApiResponse.success(userService.updateUserProfile(updateUserRequest, file));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')||hasRole('STAFF')")
    ApiResponse<List<UserResponse>> getAllUsers() {
        return ApiResponse.success(userService.getUsers());
    }

    @GetMapping("/customers")
    @PreAuthorize("hasRole('ADMIN')||hasRole('STAFF')")
    ApiResponse<List<UserResponse>> getAllCustomer() {
        return ApiResponse.success(userService.getAllCustomer());
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<List<UserResponse>> getAllAdmin() {
        return ApiResponse.success(userService.getAllAdmin());
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<UserResponse> getUserById(@PathVariable Long userId) {
        return ApiResponse.success(userService.getUserById(userId));
    }

    @PutMapping(value = "/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<UserResponse> updateUserProfileById(
            @PathVariable Long userId,
            @RequestPart("user") String userStr,
            @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {

        UpdateUserRequest updateUserRequest = ValidationUtil.validateUserStr(userStr);
        return ApiResponse.success(userService.updateUserProfileById(updateUserRequest, file, userId));
    }




//    @GetMapping
//    ApiResponse<List<UserResponse>> getUsers() {
//        var authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        log.info("Username {}", authentication.getName());
//        authentication.getAuthorities().forEach(grantedAuthority -> log.info("GrantedAuthority {}", grantedAuthority.getAuthority()));
//
//        return (ApiResponse.<List<UserResponse>>builder()
//                .result(userService.getUsers())
//                .build());
//
//    }
//
//    @GetMapping("/page")
//    public PaginatedApiResponse<UserResponse> getUsers(
//            @RequestParam(value = "page", defaultValue = "0") int page,
//            @RequestParam(value = "size", defaultValue = "10") int size) {
//        Pageable pageable = PageRequest.of(page, size);
//        PaginatedApiResponse<UserResponse> response = userService.getUsers(pageable);
//
//
//        return response;
//    }
//
//    @GetMapping("/{userId}")
//    ApiResponse<UserResponse> getUser(@PathVariable("userId") String userId) {
//        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
//        apiResponse.setCode(1000);
//        apiResponse.setResult(userService.getUserRespone(userId));
//        return apiResponse;
//    }
//
//    @PutMapping("/{userId}")
//    ApiResponse<UserResponse> updateUser(@PathVariable String userId, @RequestBody UserUpdateRequest request) {
//        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
//        apiResponse.setCode(1000);
//        apiResponse.setResult(userService.updateUser(userId, request));
//        return apiResponse;
//    }
//
//    @DeleteMapping("/{userId}")
//    String deleteUser(@PathVariable String userId) {
//        userService.deleteUser(userId);
//        return "User has been deleted";
//    }

}
