package ru.yandex.practicum.filmorate.service.film;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.Exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;


@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage FilmDbStorage;

    private final UserStorage UserDbStorage;

    public void setLike(int filmId, int userId) {
        if (UserDbStorage.getUserById(userId) == null)
            throw new NotFoundException("Пользователь с id = " + userId + " не найден");
        if (FilmDbStorage.getFilmById(filmId) == null)
            throw new NotFoundException("Фильм с id = " + filmId + " не найден");
        FilmDbStorage.setLike(filmId, userId);
    }

    public void deleteLike(int filmId, int userId) {
        if (UserDbStorage.getUserById(userId) == null)
            throw new NotFoundException("Пользователь с id = " + userId + " не найден");
        if (FilmDbStorage.getFilmById(filmId) == null)
            throw new NotFoundException("Фильм с id = " + filmId + " не найден");
        FilmDbStorage.unlike(filmId, userId);
    }

    public List<Film> getPopular(int count) {
        return FilmDbStorage.getPopular(count);
    }

    public Film getFilmById(int id) {
        return FilmDbStorage.getFilmById(id);
    }

    public List<Film> getFilms() {
        return FilmDbStorage.getFilms();
    }

    public Film createFilm(Film film) {
        return FilmDbStorage.createFilm(film);
    }

    public Film updateFilm(Film film) {
        return FilmDbStorage.updateFilm(film);
    }

}