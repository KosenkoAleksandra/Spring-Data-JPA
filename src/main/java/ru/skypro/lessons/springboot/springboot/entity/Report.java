package ru.skypro.lessons.springboot.springboot.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;


@ToString
@Getter
@Setter
@Entity
@Table(name = "report")
@AllArgsConstructor
@NoArgsConstructor
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Lob
    @Column(columnDefinition = "oid", nullable = false)
    private String report;

    @CreationTimestamp()
    @Column(updatable = false, name = "created_at", nullable = false)
    private Instant createdAt;
}
