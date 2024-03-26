package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.Exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.Exceptions.ValidationExcepton;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.genre.GenreService;
import ru.yandex.practicum.filmorate.service.mpa.MpaService;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Set;

@Slf4j
@Component("FilmDbStorage")
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    @Autowired
    private GenreService genreService;
    @Autowired
    private MpaService mpaService;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Film> getFilms() {
        String sql = "SELECT f.id as film_id, " +
                "f.name as film_name," +
                "f.description as film_description," +
                "f.release_date as film_releasedate," +
                "f.duration as film_duration, " +
                "f.RATING_ID, " +
                "r.CODE as rating_code, " +
                "r.DESCRIPTION as rating_description, " +
                "g.ID as  genre_id, " +
                "g.NAME as genre_name," +
                "l.USER_ID AS user_like " +
                "FROM FILM as f " +
                "left join RATING as r on r.id = f.RATING_ID " +
                "left join FILMGENRE fg on fg.FILM_ID = f.ID " +
                "left join GENRE g on g.ID = fg.GENRE_ID " +
                "LEFT JOIN LIKES l ON l.FILM_ID  = f.ID " +
                "ORDER BY f.ID asc,FG.FILM_ID desc";
        return jdbcTemplate.query(sql, new FilmExtractor());
    }

    @Override
    public Film createFilm(Film film) {
        if (film.getDescription().length() > 100)
            throw new ValidationExcepton("Максимальная длина описания фильма 100 символов");
        if (film.getReleaseDate().isBefore(Date.valueOf("1890-03-26").toLocalDate()))
            throw new ValidationExcepton("Дата фильма должна быть позднее 25 марта 1890 г ");
        try {
            if (film.getMpa() != null)
                mpaService.getMpaById(film.getMpa().getId());
        } catch (NotFoundException e) {
            throw new ValidationExcepton("Проверьте корректность указанного рейтинга");
        }
        try {
            if (film.getGenres() != null) {
                for (Genre genre : film.getGenres())
                    genreService.getGenreById(genre.getId());
            }
        } catch (NotFoundException e) {
            throw new ValidationExcepton("Проверьте корректность указанных жанров");
        }
        String sql = "INSERT INTO FILM (NAME,DESCRIPTION,RELEASE_DATE,DURATION,RATING_ID) VALUES " +
                "(?,?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(4, film.getDuration());
            stmt.setInt(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);

        int filmId = keyHolder.getKey().intValue();
        updateGenre(film.getGenres(), filmId);

        return getFilmById(filmId);
    }

    @Override
    public Film updateFilm(Film film) {
        if (film.getReleaseDate().isBefore(Date.valueOf("1890-03-26").toLocalDate()))
            throw new ValidationExcepton("Дата фильма должна быть позднее 25 марта 1890 г ");
        String sql = "UPDATE FILM " +
                "set NAME=?, " +
                "DESCRIPTION=?, " +
                "RELEASE_DATE=?, " +
                "DURATION=?, " +
                "RATING_ID=? " +
                "WHERE FILM.ID=?";

        jdbcTemplate.update(sql, film.getName(),
                film.getDescription(),
                Date.valueOf(film.getReleaseDate()),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());

        updateGenre(film.getGenres(), film.getId());

        return getFilmById(film.getId());
    }

    @Override
    public Film getFilmById(int id) {
        String sql = "SELECT f.id as film_id, " +
                "f.name as film_name," +
                "f.description as film_description," +
                "f.release_date as film_releasedate," +
                "f.duration as film_duration, " +
                "f.RATING_ID, " +
                "r.CODE as rating_code, " +
                "r.DESCRIPTION as rating_description, " +
                "g.ID as  genre_id, " +
                "g.NAME as genre_name," +
                "l.USER_ID AS user_like " +
                "FROM FILM as f " +
                "left join RATING as r on r.id = f.RATING_ID " +
                "left join FILMGENRE fg on fg.FILM_ID = f.ID " +
                "left join GENRE g on g.ID = fg.GENRE_ID " +
                "LEFT JOIN LIKES l ON l.FILM_ID  = f.ID " +
                "where f.ID =? ";

        List<Film> films = jdbcTemplate.query(sql, new FilmExtractor(), id);
        if (films.isEmpty())
            throw new NotFoundException("Фильм с айди " + id + " не найден");
        return films.get(0);
    }

    @Override
    public void setLike(int filmId, int userId) {
        unlike(filmId, userId);
        String sqlInsert = "INSERT INTO LIKES(film_id, user_id) values ( ?,? )";
        jdbcTemplate.update(sqlInsert, filmId, userId);
    }

    @Override
    public void unlike(int filmId, int userId) {
        String sqlDelete = "DELETE FROM LIKES l WHERE l.FILM_ID =" + filmId + " and l.USER_ID =" + userId;
        jdbcTemplate.execute(sqlDelete);
    }

    private void updateGenre(Set<Genre> genreSet, int id) {
        jdbcTemplate.execute("DELETE FROM FILMGENRE WHERE FILM_ID=" + id);
        if (genreSet != null && !genreSet.isEmpty())
            for (Genre ge : genreSet) {
                jdbcTemplate.update("INSERT INTO FILMGENRE(FILM_ID,GENRE_ID) VALUES(?,?)", id, ge.getId());
            }
    }
}
