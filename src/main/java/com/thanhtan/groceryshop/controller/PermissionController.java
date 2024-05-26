package com.thanhtan.groceryshop.controller;


import com.thanhtan.groceryshop.dto.request.PermissionRequest;
import com.thanhtan.groceryshop.dto.response.ApiResponse;
import com.thanhtan.groceryshop.dto.response.PermissionResponse;
import com.thanhtan.groceryshop.service.impl.PermissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.thanhtan.groceryshop.constant.PathConstant.API_V1_PERMISSION;

@RestController
@RequestMapping(API_V1_PERMISSION)
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionController {

    PermissionService permissionService;

    @GetMapping
    ApiResponse<List<PermissionResponse>> getPermissions() {
        return ApiResponse.<List<PermissionResponse>>builder()
                .result(permissionService.getAllPermission())
                .build();
    }

    @PostMapping
    ApiResponse<PermissionResponse> createPermission(@RequestBody PermissionRequest request
    ) {
        return ApiResponse.<PermissionResponse>builder()
                .result(permissionService.createPermission(request))
                .build();
    }

    @DeleteMapping("/{permissionId}")
    ApiResponse<Void> deletePermission(@PathVariable String permissionId) {
        permissionService.deletePermission(permissionId);
        return ApiResponse.<Void>builder().build();
    }
}
