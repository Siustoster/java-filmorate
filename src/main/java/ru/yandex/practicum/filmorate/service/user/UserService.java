package ru.yandex.practicum.filmorate.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {
    @Autowired
    UserStorage userStorage;

    public void addFriend(int userId, int friendId) {
        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);
        Set<Integer> userFriends = user.getFriends();
        Set<Integer> friendFriends = friend.getFriends();
        if (userFriends == null)
            userFriends = new HashSet<>();
        userFriends.add(friendId);

        if (friendFriends == null)
            friendFriends = new HashSet<>();
        friendFriends.add(userId);

        user.setFriends(userFriends);
        friend.setFriends(friendFriends);
    }

    public void deleteFriend(int userId, int friendId) {
        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);
        Set<Integer> userFriends = user.getFriends();
        Set<Integer> friendFriends = friend.getFriends();
        userFriends.remove(friendId);
        friendFriends.remove(userId);

        user.setFriends(userFriends);
        friend.setFriends(friendFriends);
    }

    public List<User> getFriends(int userId) {
        List<User> friends = new ArrayList<>();
        User user = userStorage.getUserById(userId);
        for (Integer foundedUserId : user.getFriends())
            friends.add(userStorage.getUserById(foundedUserId));
        return friends;
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