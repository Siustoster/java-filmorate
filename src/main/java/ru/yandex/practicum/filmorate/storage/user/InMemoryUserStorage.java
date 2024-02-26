package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.Exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.Exceptions.ValidationExcepton;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private Map<Integer, User> users = new HashMap<>();
    private int createdIds = 0;

    private int generateId() {
        return ++createdIds;
    }

    private void validateUser(User user) {
        if (user == null) {
            log.warn("Прислали пустой запрос к users");
            throw new ValidationExcepton("Нельзя присылать пустой запрос");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            log.info("Пришло пустое имя");
            user.setName(user.getLogin());
        }
    }
    public List<User> getAll() {
        log.info("Запрос всех пользователей");
        return new ArrayList<>(users.values());
    }

    public User createUser(User user) {
        validateUser(user);
        user.setId(generateId());
        log.info("Создали пользователя {}", user);
        users.put(user.getId(), user);
        return user;
    }

    public User updateUser(User user) {
        validateUser(user);
        if (!users.containsKey(user.getId())) {
            log.warn("Пользователь с айди {} не найден", user.getId());
            throw new NotFoundException("Пользователь не найден");
        }
        log.info("Обновили пользователя {} на {}", users.get(user.getId()), user);
        users.put(user.getId(), user);
        return user;
    }
}
