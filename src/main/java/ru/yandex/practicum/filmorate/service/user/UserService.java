package ru.yandex.practicum.filmorate.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {
    @Qualifier("UserDbStorage")
    @Autowired
    UserStorage userStorage;

    public void addFriend(int userId, int friendId) {
        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);
        userStorage.addFriend(userId, friendId);
    }

    public void deleteFriend(int userId, int friendId) {
        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);
        userStorage.deleteFriend(userId, friendId);
    }

    public List<User> getFriends(int userId) {
        User user = userStorage.getUserById(userId);
        return userStorage.getFriends(userId);
    }

    public List<User> getFriendsIntersection(int userId, int friendId) {
        List<User> intersections = new ArrayList<>();
        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);
        Set<Integer> friendList = new HashSet<>(user.getFriends());

        friendList.retainAll(friend.getFriends());

        for (int foundedId : friendList)
            intersections.add(userStorage.getUserById(foundedId));
        return intersections;
    }

    public List<User> getAll() {
        return userStorage.getAll();
    }

    public User getUserById(int id) {
        return userStorage.getUserById(id);
    }

    public User createUser(User user) {
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }
}