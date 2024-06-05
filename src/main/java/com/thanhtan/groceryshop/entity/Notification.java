package com.thanhtan.groceryshop.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Notification extends BaseEntity {

    String title;
    String content;

     boolean seen;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    User user;


}
