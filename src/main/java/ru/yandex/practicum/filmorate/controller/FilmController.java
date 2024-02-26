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

public class FilmController {




    @GetMapping
    public List<Film> getFilms() {

        return new ArrayList<>(films.values());
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        validateFilm(film);
        film.setId(generateId());

        films.put(film.getId(), film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        validateFilm(film);
        if (!films.containsKey(film.getId())) {

            throw new NotFoundException("Фильм не найден");
        }

        films.put(film.getId(), film);
        return film;
    }

    private void validateFilm(Film film) {
        if (film == null) {

            throw new ValidationExcepton("Нельзя присылать пустой запрос");
        }
        if (film.getDescription().length() > 200) {

            throw new ValidationExcepton("Длина описания больше 200");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, Month.DECEMBER, 28))) {

            throw new ValidationExcepton("Дата релиза слишком ранняя");
        }
    }
}
