package ru.yandex.practicum.filmorate.service.film;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.Exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;


@Service
@RequiredArgsConstructor
public class FilmService {

    @Qualifier("FilmDbStorage")
    private final FilmStorage filmDbStorage;

    @Qualifier("UserDbStorage")
    private final UserStorage userDbStorage;

    public void setLike(int filmId, int userId) {
        if (userDbStorage.getUserById(userId) == null)
            throw new NotFoundException("Пользователь с id = " + userId + " не найден");
        if (filmDbStorage.getFilmById(filmId) == null)
            throw new NotFoundException("Фильм с id = " + filmId + " не найден");
        filmDbStorage.setLike(filmId, userId);
    }

    public void deleteLike(int filmId, int userId) {
        if (userDbStorage.getUserById(userId) == null)
            throw new NotFoundException("Пользователь с id = " + userId + " не найден");
        if (filmDbStorage.getFilmById(filmId) == null)
            throw new NotFoundException("Фильм с id = " + filmId + " не найден");
        filmDbStorage.unlike(filmId, userId);
    }

    public List<Film> getPopular(int count) {
        return filmDbStorage.getPopular(count);
    }

    public Film getFilmById(int id) {
        return filmDbStorage.getFilmById(id);
    }

    public List<Film> getFilms() {
        return filmDbStorage.getFilms();
    }

    public Film createFilm(Film film) {
        return filmDbStorage.createFilm(film);
    }

    public Film updateFilm(Film film) {
        return filmDbStorage.updateFilm(film);
    }

}