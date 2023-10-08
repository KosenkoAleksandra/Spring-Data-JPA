package ru.skypro.lessons.springboot.springboot.repository;

import org.springframework.data.repository.CrudRepository;
import ru.skypro.lessons.springboot.springboot.entity.Position;

public interface PositionRepository extends CrudRepository<Position, Integer> {
}
