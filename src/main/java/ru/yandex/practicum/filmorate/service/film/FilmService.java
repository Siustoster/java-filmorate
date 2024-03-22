package ru.yandex.practicum.filmorate.service.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.Exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    @Qualifier("FilmDbStorage")
    @Autowired
    FilmStorage filmStorage;
    @Qualifier("UserDbStorage")
    @Autowired
    UserStorage userStorage;

    public void setLike(int filmId, int userId) {
        if (userStorage.getUserById(userId) == null)
            throw new NotFoundException("Пользователь с id = " + userId + " не найден");
        filmStorage.getFilmById(filmId).getLikes().add(userId);
    }

    public void deleteLike(int filmId, int userId) {
        if (userStorage.getUserById(userId) == null)
            throw new NotFoundException("Пользователь с id = " + userId + " не найден");
        filmStorage.getFilmById(filmId).getLikes().remove(userId);
    }

    public List<Film> getPopular(int count) {
        return filmStorage.getFilms().stream()
                .sorted(Comparator.comparingInt(film -> -1 * film.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
    }

    public Film getFilmById(int id) {
        return filmStorage.getFilmById(id);
    }

    public List<Film> getFilms() {
        return filmStorage.getFilms();
    }

    public Film createFilm(Film film) {
        return filmStorage.createFilm(film);
    }

    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

}