package com.thanhtan.groceryshop.mapper;


import com.thanhtan.groceryshop.dto.request.UpdateUserRequest;
import com.thanhtan.groceryshop.dto.request.UserRequest;
import com.thanhtan.groceryshop.dto.response.UserResponse;
import com.thanhtan.groceryshop.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.lang.annotation.Target;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toUser(UserRequest request);

    @Mapping(target = "roles", ignore = true)
    UserResponse toUserResponse(User user);


    @Mapping(target = "username", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "orders", ignore = true)
    void updateUser(@MappingTarget User user, UpdateUserRequest request);
}
