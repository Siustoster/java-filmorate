package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import ru.yandex.practicum.filmorate.Exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserDbStorageTest {
    private final JdbcTemplate jdbcTemplate;
    private UserDbStorage userStorage;


    @BeforeEach
    public void init() {
        userStorage = new UserDbStorage(jdbcTemplate);
    }


    @Test
    public void deleteFriendTest() {
        User newUser1 = User.builder()
                .id(1)
                .email("user@mail.ru")
                .login("vanya123")
                .name("Ivan")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();
        int user1Id = userStorage.createUser(newUser1).getId();
        newUser1.setId(user1Id);

        User newUser2 = User.builder()
                .id(1)
                .email("bas@mail.ru")
                .login("igor")
                .name("ss")
                .birthday(LocalDate.of(2020, 1, 1))
                .build();
        int user2Id = userStorage.createUser(newUser2).getId();
        newUser2.setId(user2Id);

        userStorage.addFriend(user1Id, user2Id);

        List<User> user1Friends = List.of(newUser2);

        assertThat(userStorage.getFriends(user1Id))
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(user1Friends);

        assertThat(userStorage.getFriends(user2Id))
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(new ArrayList<User>());

        userStorage.deleteFriend(user1Id, user2Id);

        assertThat(userStorage.getFriends(user1Id))
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(new ArrayList<User>());

        assertThat(userStorage.getFriends(user2Id))
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(new ArrayList<User>());
    }

    @Test
    public void addFriendTest() {
        User newUser1 = User.builder()
                .id(1)
                .email("user@mail.ru")
                .login("vanya123")
                .name("Ivan")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();
        int user1Id = userStorage.createUser(newUser1).getId();
        newUser1.setId(user1Id);

        User newUser2 = User.builder()
                .id(1)
                .email("bas@mail.ru")
                .login("igor")
                .name("ss")
                .birthday(LocalDate.of(2020, 1, 1))
                .build();
        int user2Id = userStorage.createUser(newUser2).getId();
        newUser2.setId(user2Id);

        userStorage.addFriend(user1Id, user2Id);

        List<User> user1Friends = List.of(newUser2);

        assertThat(userStorage.getFriends(user1Id))
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(user1Friends);

        assertThat(userStorage.getFriends(user2Id))
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(new ArrayList<User>());
    }

    @Test
    public void updateUserTest() {
        User newUser = User.builder()
                .id(1)
                .email("user@mail.ru")
                .login("vanya123")
                .name("Ivan")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();
        int userId = userStorage.createUser(newUser).getId();
        newUser.setId(userId);

        User updatedUser = User.builder()
                .id(1)
                .email("bas@mail.ru")
                .login("igor")
                .name("ss")
                .birthday(LocalDate.of(2020, 1, 1))
                .build();
        updatedUser.setId(userId);

        userStorage.updateUser(updatedUser);
        assertThat(updatedUser)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(userStorage.getUserById(userId));
    }

    @Test
    public void createUserTest() {
        User newUser = User.builder()
                .id(1)
                .email("user@email.ru")
                .login("vanya123")
                .name("Ivan")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();
        int userId = userStorage.createUser(newUser).getId();
        newUser.setId(userId);
        User savedUser = userStorage.getUserById(userId);

        assertThat(savedUser)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(newUser);
    }

    @Test
    public void findUserByIdTest() {
        // Подготавливаем данные для теста
        User newUser = User.builder()
                .id(1)
                .email("user@email.ru")
                .login("vanya123")
                .name("Ivan")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();
        User newUser2 = User.builder()
                .id(1)
                .email("user@email.ru")
                .login("vanya123")
                .name("Ivan")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();
        User newUser3 = User.builder()
                .id(1)
                .email("user@email.ru")
                .login("vanya123")
                .name("Ivan")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();
        userStorage.createUser(newUser);
        int userId = userStorage.createUser(newUser2).getId();
        newUser2.setId(userId);
        userStorage.createUser(newUser3);
        User savedUser = userStorage.getUserById(userId);

        assertThat(savedUser)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(newUser2);
    }

    @Test
    public void findUserByIdTestFail() {
        final NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> userStorage.getUserById(99999)
        );

        assertEquals("Пользователь с айди 99999 не найден", exception.getMessage());
    }

    @Test
    public void findAllUsersTest() {
        // Подготавливаем данные для теста
        User newUser1 = User.builder()
                .id(1)
                .email("user@email.ru")
                .login("vanya123")
                .name("Ivan")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();
        int userId = userStorage.createUser(newUser1).getId();
        newUser1.setId(userId);

        User newUser2 = User.builder()
                .id(1)
                .email("user2@email.ru")
                .login("vanya1234")
                .name("Ivan2")
                .birthday(LocalDate.of(2010, 1, 1))
                .build();
        userId = userStorage.createUser(newUser2).getId();
        newUser2.setId(userId);
        List<User> savedUsers = List.of(newUser1, newUser2);

        assertThat(userStorage.getAll())
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(savedUsers);
    }
} 