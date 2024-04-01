package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.List;

public interface UserStorage {
    List<User> getAll();

    User createUser(@Valid @RequestBody User user);

    User updateUser(@Valid @RequestBody User user);

    User getUserById(int id);

    void addFriend(int userId, int friendId);

    void deleteFriend(int userId, int friendId);

    List<User> getFriends(int userId);

}
