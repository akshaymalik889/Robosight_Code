package com.example.TestingJWT.entities;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")  //admin table have field called user_id, and this will be foreign key for User entity
    private User user;    //make one to one mapping with USER Entity
}
