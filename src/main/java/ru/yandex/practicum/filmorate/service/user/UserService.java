package ru.yandex.practicum.filmorate.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {
    @Qualifier("UserDbStorage")
    private final UserStorage userDbStorage;

    public void addFriend(int userId, int friendId) {
        User user = userDbStorage.getUserById(userId);
        User friend = userDbStorage.getUserById(friendId);
        userDbStorage.addFriend(userId, friendId);
    }

    public void deleteFriend(int userId, int friendId) {
        User user = userDbStorage.getUserById(userId);
        User friend = userDbStorage.getUserById(friendId);
        userDbStorage.deleteFriend(userId, friendId);
    }

    public List<User> getFriends(int userId) {
        User user = userDbStorage.getUserById(userId);
        return userDbStorage.getFriends(userId);
    }

    public List<User> getFriendsIntersection(int userId, int friendId) {
        List<User> intersections = new ArrayList<>();
        User user = userDbStorage.getUserById(userId);
        User friend = userDbStorage.getUserById(friendId);
        Set<Integer> friendList = new HashSet<>(user.getFriends());

        friendList.retainAll(friend.getFriends());

        for (int foundedId : friendList)
            intersections.add(userDbStorage.getUserById(foundedId));
        return intersections;
    }

    public List<User> getAll() {
        return userDbStorage.getAll();
    }

    public User getUserById(int id) {
        return userDbStorage.getUserById(id);
    }

    public User createUser(User user) {
        return userDbStorage.createUser(user);
    }

    public User updateUser(User user) {
        return userDbStorage.updateUser(user);
    }
}