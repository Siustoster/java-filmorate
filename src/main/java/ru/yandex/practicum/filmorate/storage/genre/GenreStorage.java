package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;
import java.util.List;

public interface GenreStorage {
    public Collection<Genre> findAllGenres();
    public List<Genre> findGenreById(int id);
}
