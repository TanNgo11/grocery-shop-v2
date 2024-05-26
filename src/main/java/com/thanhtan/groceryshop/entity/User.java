package com.thanhtan.groceryshop.entity;

import com.thanhtan.groceryshop.enums.Gender;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class User extends BaseEntity {

    String username;
    String password;
    String firstName;
    String lastName;
    LocalDate dateOfBirth;
    String phoneNumber;
    @Enumerated(EnumType.STRING)
    Gender gender;
    String address;

    @ManyToMany
    Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    List<Order> orders = new ArrayList<>();
}