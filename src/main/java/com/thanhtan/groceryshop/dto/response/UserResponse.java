package com.thanhtan.groceryshop.dto.response;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@SuperBuilder
public class UserResponse extends BaseDTO {

    String username;
    String firstName;
    String lastName;
    LocalDate dateOfBirth;
    String gender;
    String email;
    Set<String> roles;



}