package ru.practicum.shareit.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    <T> List<T> findBy(Class<T> projectionClass);

    <T> Optional<T> findById(Long userId, Class<T> projectionClass);

}
