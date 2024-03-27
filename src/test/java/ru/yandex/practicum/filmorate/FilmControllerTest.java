package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;

@SpringBootTest
public class FilmControllerTest {
    private FilmController filmController;
    private Film film;
    private Film updatedFilm;

    @BeforeEach
    void createApp() {

    }
 /*
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

  */
}
