package ru.yandex.practicum.filmorate.service.genre;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.Exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.Collection;
import java.util.List;

@Service
public class GenreService {
    @Autowired
    private GenreStorage genreStorage;

    public Collection<Genre> findAllGenres() {
        return genreStorage.findAllGenres();
    }

    public Genre getGenreById(int id) {
        List<Genre> listGenre = genreStorage.findGenreById(id);
        if (!listGenre.isEmpty())
            return listGenre.get(0);
        else throw new NotFoundException("Жанр c запрашиваемым айди не найден");
    }
}
