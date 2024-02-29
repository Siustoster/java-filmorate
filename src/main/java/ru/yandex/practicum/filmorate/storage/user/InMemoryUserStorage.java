package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.Exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.Exceptions.ValidationExcepton;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

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

    @Override
    public List<User> getAll() {
        log.info("Запрос всех пользователей");
        return new ArrayList<>(users.values());
    }

    @Override
    public User createUser(User user) {
        validateUser(user);
        user.setId(generateId());
        user.setFriends(new HashSet<>());
        log.info("Создали пользователя {}", user);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        validateUser(user);
        if (user.getFriends() == null)
            user.setFriends(new HashSet<>());
        if (!users.containsKey(user.getId())) {
            log.warn("Пользователь с айди {} не найден", user.getId());
            throw new NotFoundException("Пользователь не найден");
        }
        log.info("Обновили пользователя {} на {}", users.get(user.getId()), user);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User getUserById(int id) {
        return Optional.ofNullable(users.get(id)).orElseThrow(()
                -> new NotFoundException("Пользователь с айди " + id + " не найден"));
    }
}
