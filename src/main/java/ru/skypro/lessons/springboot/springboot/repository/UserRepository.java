package ru.skypro.lessons.springboot.springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.lessons.springboot.springboot.entity.AuthUser;

public interface UserRepository extends JpaRepository<AuthUser, Long> {
    AuthUser findByUsername(String username);
}
