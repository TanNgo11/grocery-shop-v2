package com.thanhtan.groceryshop.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "coupons")
public class Coupon extends BaseEntity {
    @Column(unique = true, nullable = false)
    String code;
    @Column(nullable = false)
    double discount;
    @Column(nullable = false)
    Date expiryDate;

}
