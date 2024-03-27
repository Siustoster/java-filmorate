package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.Exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.Exceptions.ValidationExcepton;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreDaoImpl;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaDaoImpl;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmDbStorageTest {
    private final JdbcTemplate jdbcTemplate;
    private FilmStorage filmStorage;
    private GenreStorage genreStorage;
    private MpaStorage mpaStorage;
    private UserStorage userStorage;

    @BeforeEach
    void init() {
        genreStorage = new GenreDaoImpl(jdbcTemplate);
        mpaStorage = new MpaDaoImpl(jdbcTemplate);
        filmStorage = new FilmDbStorage(jdbcTemplate, genreStorage, mpaStorage);
        userStorage = new UserDbStorage(jdbcTemplate);
    }

    @Test
    public void filmCreateTest() {
        Film newFilm = Film.builder()
                .name("Пори Гаттер")
                .description("Мальчик всю жизнь живёт под лестницей")
                .duration(155)
                .releaseDate(LocalDate.of(1990, 1, 1))
                .mpa(mpaStorage.findMpaById(1).get(0))
                .build();

        Film savedFilm = filmStorage.createFilm(newFilm);
        newFilm.setId(savedFilm.getId());
        assertThat(savedFilm)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(newFilm);
    }

    @Test
    public void filmCreateTestFailDatePast() {
        Film newFilm = Film.builder()
                .id(1)
                .name("Пори Гаттер")
                .description("Мальчик всю жизнь живёт под лестницей")
                .duration(155)
                .releaseDate(LocalDate.of(1800, 1, 1))
                .mpa(mpaStorage.findMpaById(1).get(0))
                .build();
        final ValidationExcepton exception = assertThrows(
                ValidationExcepton.class,
                () -> filmStorage.createFilm(newFilm)
        );

        assertEquals("Дата фильма должна быть позднее 25 марта 1890 г ", exception.getMessage());
    }

    @Test
    public void filmGetFilmsTest() {
        Film newFilm1 = Film.builder()
                .id(2)
                .name("Пори Гаттер")
                .description("Мальчик всю жизнь живёт под лестницей")
                .duration(155)
                .releaseDate(LocalDate.of(1999, 1, 1))
                .mpa(mpaStorage.findMpaById(1).get(0))
                .build();
        Film newFilm2 = Film.builder()
                .id(3)
                .name("Пори Гаттер 2")
                .description("Всё еще под лестницей")
                .duration(166)
                .releaseDate(LocalDate.of(2010, 1, 1))
                .mpa(mpaStorage.findMpaById(2).get(0))
                .build();

        newFilm1.setId(filmStorage.createFilm(newFilm1).getId());
        newFilm2.setId(filmStorage.createFilm(newFilm2).getId());

        List<Film> createdFilms = filmStorage.getFilms();

        assertThat(createdFilms.get(0))
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(newFilm1);

        assertThat(createdFilms.get(1))
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(newFilm2);

        assertEquals(createdFilms.size(), 2);
    }

    @Test
    public void updateFilmTest() {
        Film newFilm1 = Film.builder()
                .id(1)
                .name("Пори Гаттер 3")
                .description("Похоже, все еще под лестницей")
                .duration(155)
                .releaseDate(LocalDate.of(2015, 1, 1))
                .mpa(mpaStorage.findMpaById(3).get(0))
                .build();
        Film newFilm2 = Film.builder()
                .id(1)
                .name("Пори Гаттер 4")
                .description("Под ней и умрёт")
                .duration(166)
                .releaseDate(LocalDate.of(2020, 1, 1))
                .mpa(mpaStorage.findMpaById(4).get(0))
                .build();

        newFilm2.setId(filmStorage.createFilm(newFilm1).getId());
        filmStorage.updateFilm(newFilm2);

        assertThat(newFilm2)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(filmStorage.getFilmById(newFilm2.getId()));
    }

    @Test
    public void getFilmByIdTest() {
        Film newFilm1 = Film.builder()
                .id(1)
                .name("Пори Гаттер 3")
                .description("Похоже, все еще под лестницей")
                .duration(155)
                .releaseDate(LocalDate.of(2015, 1, 1))
                .mpa(mpaStorage.findMpaById(3).get(0))
                .build();

        int id = filmStorage.createFilm(newFilm1).getId();
        newFilm1.setId(id);

        assertThat(newFilm1)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(filmStorage.getFilmById(newFilm1.getId()));
    }

    @Test
    public void getFilmByIdFailTest() {
        final NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> filmStorage.getFilmById(999999)
        );

        assertEquals("Фильм с айди 999999 не найден", exception.getMessage());
    }

    @Test
    public void setLikeTest() {
        User newUser = User.builder()
                .id(1)
                .name("Рон Уизли")
                .email("poorweasley@wizard.uk")
                .birthday(LocalDate.of(1990, 1, 1))
                .login("smartweasley")
                .build();

        int userId = userStorage.createUser(newUser).getId();
        newUser.setId(userId);

        Film newFilm1 = Film.builder()
                .id(1)
                .name("Пори Гаттер 3")
                .description("Похоже, все еще под лестницей")
                .duration(155)
                .releaseDate(LocalDate.of(2015, 1, 1))
                .mpa(mpaStorage.findMpaById(3).get(0))
                .build();


        int filmId = filmStorage.createFilm(newFilm1).getId();
        newFilm1.setId(filmId);

        filmStorage.setLike(filmId, userId);
        newFilm1.setLikes(Set.of(userId));

        assertThat(filmStorage.getFilmById(filmId))
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(newFilm1);
    }

    @Test
    public void unlikeTest() {
        User newUser = User.builder()
                .id(1)
                .name("Рон Уизли")
                .email("poorweasley@wizard.uk")
                .birthday(LocalDate.of(1990, 1, 1))
                .login("smartweasley")
                .build();

        int userId = userStorage.createUser(newUser).getId();
        newUser.setId(userId);

        Film newFilm1 = Film.builder()
                .id(1)
                .name("Пори Гаттер 3")
                .description("Похоже, все еще под лестницей")
                .duration(155)
                .releaseDate(LocalDate.of(2015, 1, 1))
                .mpa(mpaStorage.findMpaById(3).get(0))
                .build();


        int filmId = filmStorage.createFilm(newFilm1).getId();
        newFilm1.setId(filmId);

        filmStorage.setLike(filmId, userId);
        newFilm1.setLikes(Set.of(userId));

        assertThat(filmStorage.getFilmById(filmId))
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(newFilm1);

        filmStorage.unlike(filmId, userId);
        newFilm1.setLikes(new HashSet<>());

        assertThat(filmStorage.getFilmById(filmId))
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(newFilm1);
    }

    @Test
    public void mpaGetByIdTest() {
        List<Mpa> mpaList = mpaStorage.findMpaById(1);
        assertNotNull(mpaList);
        assertEquals(mpaList.get(0).getId(), 1);
        assertEquals(mpaList.get(0).getName(), "G");
        assertEquals(mpaList.get(0).getDescription(), "Фильм демонстрируется без ограничений");

        mpaList = mpaStorage.findMpaById(5);
        assertNotNull(mpaList);
        assertEquals(mpaList.get(0).getId(), 5);
        assertEquals(mpaList.get(0).getName(), "NC-17");
        assertEquals(mpaList.get(0).getDescription(), "Лица 17-летнего возраста и младше на фильм не допускаются");
    }

    @Test
    public void mpaGetAllTest() {
        List<Mpa> mpaList = (List<Mpa>) mpaStorage.findAllMpa();
        assertNotNull(mpaList);
        assertEquals(5, mpaList.size());
    }

    @Test
    public void genreGetAllTest() {
        List<Genre> genreList = (List<Genre>) genreStorage.findAllGenres();
        assertNotNull(genreList);
        assertEquals(6, genreList.size());
    }

    @Test
    public void genreGetByIdTest() {
        List<Genre> genreList = genreStorage.findGenreById(1);
        assertNotNull(genreList);
        assertEquals(genreList.get(0).getId(), 1);
        assertEquals(genreList.get(0).getName(), "Комедия");
    }
}
