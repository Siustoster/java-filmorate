package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
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

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private Map<Integer, Film> films = new HashMap<>();
    public int createdIds = 0;

    public int generateId() {
        return ++createdIds;
    }
    @GetMapping
    public List<Film> getFilms() {
        log.info("Запрос всех фильмов");
        return new ArrayList<>(films.values());
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        validateFilm(film);
        film.setId(generateId());
        log.info("Добавлен новый фильм {}", film);
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
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
