package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.Exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.Exceptions.ValidationExcepton;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    UserStorage userStorage;
    @Autowired
    UserService userService;

    @GetMapping
    public List<User> getAll() {

        return userStorage.getAll();
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        return userStorage.createUser(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        return userStorage.updateUser(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable int id,@PathVariable int friendId) {
        userService.addFriend(id,friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable int id,@PathVariable int friendId) {
        userService.deleteFriend(id,friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getUserFriendsList(@PathVariable int id) {
        return userService.getFriends(id);
    }
    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getUsersIntersection(@PathVariable int id,@PathVariable int otherId) {
        return userService.getFriendsIntersection(id,otherId);
    }
    @ExceptionHandler
    public Map<String, String> handleException(final RuntimeException e) {
        return Map.of(
                "error", "Ошибка при выполнении запроса",
                "errorMessage", e.getMessage()
        );
    }



}
