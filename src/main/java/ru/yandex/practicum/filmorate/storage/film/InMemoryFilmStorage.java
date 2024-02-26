package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.Exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.Exceptions.ValidationExcepton;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private Map<Integer, Film> films = new HashMap<>();
    private int createdIds = 0;

    private int generateId() {
        return ++createdIds;
    }

    public List<Film> getFilms() {
        log.info("Запрос всех фильмов");
        return new ArrayList<>(films.values());
    }

    @PostMapping
    public Film createFilm(Film film) {
        validateFilm(film);
        film.setId(generateId());
        log.info("Добавлен новый фильм {}", film);
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping
    public Film updateFilm(Film film) {
        validateFilm(film);
        if (!films.containsKey(film.getId())) {
            log.warn("Фильм с айди {} не найден", film.getId());
            throw new NotFoundException("Фильм не найден");
        }
        log.info("Обновили фильм {} на {}", films.get(film.getId()), film);
        films.put(film.getId(), film);
        return film;
    }

    private void validateFilm(Film film) {
        if (film == null) {
            log.warn("Прислали пустой запрос к films");
            throw new ValidationExcepton("Нельзя присылать пустой запрос");
        }
        if (film.getDescription().length() > 200) {
            log.warn("Длина описания больше 200");
            throw new ValidationExcepton("Длина описания больше 200");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, Month.DECEMBER, 28))) {
            log.warn("Дата релиза слишком ранняя - {}", film.getReleaseDate());
            throw new ValidationExcepton("Дата релиза слишком ранняя");
        }
    }
}
