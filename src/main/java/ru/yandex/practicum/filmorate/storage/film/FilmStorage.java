package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.List;

public interface FilmStorage {
    List<Film> getFilms();
    Film createFilm(@Valid @RequestBody Film film);
    Film updateFilm(@Valid @RequestBody Film film);
    Film getFilmById(int id);
}
