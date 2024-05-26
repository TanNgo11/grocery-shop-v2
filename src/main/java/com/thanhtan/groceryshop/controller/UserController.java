package com.thanhtan.groceryshop.controller;

import com.thanhtan.groceryshop.dto.request.UpdateUserRequest;
import com.thanhtan.groceryshop.dto.request.UserRequest;
import com.thanhtan.groceryshop.dto.response.ApiResponse;
import com.thanhtan.groceryshop.dto.response.UserResponse;
import com.thanhtan.groceryshop.service.IUserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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
        return ApiResponse.<UserResponse>builder()
                .result(userService.createUser(request))
                .build();

    }

    @GetMapping("/myInfo")
    @PreAuthorize("hasRole('USER')||hasRole('ADMIN')")
    ApiResponse<UserResponse> getMyInfo() {

        return ApiResponse.<UserResponse>builder()
                .result(userService.getMyInfo())
                .build();
    }


    @PutMapping
    @PreAuthorize("hasRole('USER')||hasRole('ADMIN')")
    ApiResponse<UserResponse> updateUserProfile(@RequestBody @Valid UpdateUserRequest request) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.updateUserProfile(request))
                .build();
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
