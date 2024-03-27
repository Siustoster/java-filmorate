package ru.yandex.practicum.filmorate.service.user;

import lombok.RequiredArgsConstructor;
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
    private final UserStorage UserDbStorage;

    public void addFriend(int userId, int friendId) {
        User user = UserDbStorage.getUserById(userId);
        User friend = UserDbStorage.getUserById(friendId);
        UserDbStorage.addFriend(userId, friendId);
    }

    public void deleteFriend(int userId, int friendId) {
        User user = UserDbStorage.getUserById(userId);
        User friend = UserDbStorage.getUserById(friendId);
        UserDbStorage.deleteFriend(userId, friendId);
    }

    public List<User> getFriends(int userId) {
        User user = UserDbStorage.getUserById(userId);
        return UserDbStorage.getFriends(userId);
    }

    public List<User> getFriendsIntersection(int userId, int friendId) {
        List<User> intersections = new ArrayList<>();
        User user = UserDbStorage.getUserById(userId);
        User friend = UserDbStorage.getUserById(friendId);
        Set<Integer> friendList = new HashSet<>(user.getFriends());

        friendList.retainAll(friend.getFriends());

        for (int foundedId : friendList)
            intersections.add(UserDbStorage.getUserById(foundedId));
        return intersections;
    }

    public List<User> getAll() {
        return UserDbStorage.getAll();
    }

    public User getUserById(int id) {
        return UserDbStorage.getUserById(id);
    }

    public User createUser(User user) {
        return UserDbStorage.createUser(user);
    }

    public User updateUser(User user) {
        return UserDbStorage.updateUser(user);
    }
}