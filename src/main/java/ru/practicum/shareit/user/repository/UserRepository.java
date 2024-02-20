package ru.practicum.shareit.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    <T> List<T> findBy(Class<T> projectionClass);

    <T> Optional<T> findById(Long userId, Class<T> projectionClass);

    default void checkUserById(Long userId) {
        if (!existsById(userId)) {
            throw new NotFoundException("There's no user with id " + userId);
        }
    }

    default User getUserById(Long userId) {
        return findById(userId).orElseThrow(() -> new NotFoundException("There's no user with id " + userId));
    }

}
