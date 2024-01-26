package ru.practicum.shareit.user.dao;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.user.model.User;

import javax.validation.ValidationException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Repository
public class InMemoryUserDao implements UserDao {

    private final Map<Long, User> userMap = new HashMap<>();
    private Long id = 0L;


    @Override
    public Collection<User> getAllUsers() {
        return userMap.values();
    }

    @Override
    public User getUserById(Long userId) {
        User user = userMap.get(userId);
        if (null == user) {
            throw new NotFoundException("There's no user with id " + userId);
        }
        return user;
    }

    @Override
    public User postUser(User user) {
        emailDuplicationCheck(user);
        user.setId(generateUserId());
        userMap.put(user.getId(), user);
        return user;
    }

    @Override
    public User patchUserById(Long userId, User user) {
        User updatingUser = getUserById(userId);
        if (null != user.getEmail()
                && !user.getEmail().equals(updatingUser.getEmail())
                && !user.getEmail().isBlank()) {
            emailDuplicationCheck(user);
            updatingUser.setEmail(user.getEmail());
        }
        if (null != user.getName() && !user.getName().isBlank()) {
            updatingUser.setName(user.getName());
        }
        return updatingUser;
    }

    @Override
    public void deleteUserById(Long userId) {
        getUserById(userId);
        userMap.remove(userId);
    }

    private void emailDuplicationCheck(User user) {
        if (getAllUsers().stream()
                .map(User::getEmail)
                .anyMatch(str -> str.equals(user.getEmail()))) {
            throw new ValidationException("User email already exists!");
        }
    }

    private Long generateUserId() {
        return ++id;
    }

}
