package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;

@SpringBootTest
public class UserControllerTest {
    private UserController userController;
    private User user;
    private User updatedUser;

    @BeforeEach
    void createApp() {
    }

    /*
    @Test
    @DisplayName("Создать корректного пользователя, дата рождения в верхней границе")
    void createCorrectUser() {
        userController.createUser(user);
        List<User> users = userController.getAll();
        User createdUser = users.get(0);

        assertNotNull(users);
        assertEquals(1, users.size());
        assertEquals(user, createdUser);
    }

    @Test
    @DisplayName("Создать пользователя без имени")
    void createUserWithoutNameShouldBeLogin() {
        user.setName("");
        userController.createUser(user);
        String name = userController.getAll().get(0).getName();

        assertEquals(user.getLogin(), name);
    }

    @Test
    @DisplayName("Корректное обновление пользователя")
    void updateCorrectUser() {
        userController.createUser(user);
        userController.updateUser(updatedUser);

        List<User> users = userController.getAll();
        User newUpdatedUser = users.get(0);

        assertNotNull(users);
        assertEquals(1, users.size());
        assertEquals(updatedUser, newUpdatedUser);
    }

    @Test
    @DisplayName("Обновление пользователя с некорректным айди")
    void updateUserIncorrectShouldBeNFException() {
        userController.createUser(user);
        updatedUser.setId(2);
        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> userController.updateUser(updatedUser)
        );

        assertEquals("Пользователь не найден", exception.getMessage());
    }

     */
}
