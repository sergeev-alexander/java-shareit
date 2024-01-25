package ru.practicum.shareit.user.dao;

import ru.practicum.shareit.user.model.User;

import java.util.Collection;

public interface UserDao {

    Collection<User> getAllUsers();

    User getUserById(Long userId);

    User postUser(User user);

    User patchUserById(Long userId, User user);

    void deleteUserById(Long userId);

}
