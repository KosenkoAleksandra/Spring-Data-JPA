package ru.skypro.lessons.springboot.springboot.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "authority")
public class Authority {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false, unique = true)
    private String role;
}
