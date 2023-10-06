package ru.skypro.lessons.springboot.springboot.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

@ToString
@Getter
@Setter
@Entity
@Table(name = "employee")
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 16)
    private String name;

    private Integer salary;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "position_id")
    private Position position;
}
