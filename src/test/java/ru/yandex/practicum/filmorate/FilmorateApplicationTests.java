package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.Exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.Exceptions.ValidationExcepton;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FilmorateApplicationTests {
    private UserController userController;
    private FilmController filmController;
    private User user;
    private Film film;
    private User updatedUser;
    private Film updatedFilm;

    @BeforeEach
    void createApp() {
        userController = new UserController();
        filmController = new FilmController();
        user = User.builder()
                .login("login")
                .email("email@yandex.ru")
                .name("name")
                .birthday(LocalDate.now())
                .build();
        updatedUser = User.builder()
                .id(1)
                .login("login2")
                .email("email2@yandex.ru")
                .name("name2")
                .birthday(LocalDate.of(1995, 1, 1))
                .build();

        film = Film.builder()
                .name("name")
                .description("film description 1")
                .duration(100)
                .releaseDate(LocalDate.of(1895, 12, 28))
                .build();
        updatedFilm = Film.builder()
                .id(1)
                .name("Film")
                .description("Film descr2")
                .duration(200)
                .releaseDate(LocalDate.of(2020, 5, 2))
                .build();
    }

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
    @DisplayName("Создать корректный фильм, дата релиза в нижней границе")
    void createCorrectFilm() {
        filmController.createFilm(film);
        List<Film> films = filmController.getFilms();
        Film createdFilm = films.get(0);

        assertNotNull(films);
        assertEquals(1, films.size());
        assertEquals(film, createdFilm);
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

    @Test
    @DisplayName("Добавления фильма, дата релиза ниже нижней границы")
    void createFilmBeforeLowerDateBorderShouldBeException() {
        film.setReleaseDate(LocalDate.of(1895, 12, 27));

        final ValidationExcepton exception = assertThrows(
                ValidationExcepton.class,
                () -> filmController.createFilm(film)
        );

        final List<Film> films = filmController.getFilms();

        assertNotNull(films);
        assertEquals(0, films.size());
        assertEquals("Дата релиза слишком ранняя", exception.getMessage());
    }

    @Test
    @DisplayName("Обновление фильма, все поля валидные")
    void correctUpdateFilm() {
        filmController.createFilm(film);
        filmController.updateFilm(updatedFilm);

        List<Film> films = filmController.getFilms();
        final Film newUpdatedFilm = films.get(0);

        assertNotNull(films);
        assertEquals(1, films.size());
        assertEquals(updatedFilm, newUpdatedFilm);
    }

    @Test
    @DisplayName("Обновление фильма, не правильный id")
    void updateIncorrectFilmIdShouldBeNFException() {
        filmController.createFilm(film);
        updatedFilm.setId(2);

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> filmController.updateFilm(updatedFilm)
        );

        assertEquals("Фильм не найден", exception.getMessage());
    }

    @Test
    @DisplayName("Создание фильма с длинным описанием в 201 символ")
    void createLongDescriptionShouldBeException() {
        film.setDescription("12345678901234567890123456789012345678901234567890123456789012345678901234567890"
                + "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890"
                + "12345678901234567890_");

        ValidationExcepton exception = assertThrows(
                ValidationExcepton.class,
                () -> filmController.createFilm(film)
        );

        assertEquals("Длина описания больше 200", exception.getMessage());
    }
}
