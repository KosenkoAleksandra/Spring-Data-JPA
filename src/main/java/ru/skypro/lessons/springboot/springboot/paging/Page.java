package ru.skypro.lessons.springboot.springboot.paging;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import ru.skypro.lessons.springboot.springboot.entity.Employee;

public abstract class Page<T> implements PagingAndSortingRepository<Employee, Integer> {
    PageRequest pageRequest = new PageRequest(0, 10);
    @Override
    public org.springframework.data.domain.Page<Employee> findAll(Pageable pageable) {
        return null;
    }
}
