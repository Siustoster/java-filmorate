package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.Exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.Exceptions.ValidationExcepton;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private Map<Integer, User> users = new HashMap<>();
    public  int createdIds = 0;

    public int generateId() {
        return ++createdIds;
    }
    @GetMapping
    public List<User> getAll() {
        log.info("Запрос всех пользователей");
        return new ArrayList<>(users.values());
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            log.info("Пришло пустое имя");
            user.setName(user.getLogin());
        }
        user.setId(generateId());
        log.info("Создали пользователя {}", user);
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        if (user.getName().isBlank()) {
            log.info("Пришло пустое имя");
            user.setName(user.getLogin());
        }
        if (!users.containsKey(user.getId())) {
            log.warn("Пользователь с айди {} не найден", user.getId());
            throw new NotFoundException("Пользователь не найден");
        }
        log.info("Обновили пользователя {} на {}", users.get(user.getId()), user);
        users.put(user.getId(), user);
        return user;
    }
}
