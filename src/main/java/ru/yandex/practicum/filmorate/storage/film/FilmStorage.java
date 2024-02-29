package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.Exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    List<Film> getFilms();
    Film createFilm(@Valid @RequestBody Film film);
    Film updateFilm(@Valid @RequestBody Film film);
    Film getFilmById(int id);
}
