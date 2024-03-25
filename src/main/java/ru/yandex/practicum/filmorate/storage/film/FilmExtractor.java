package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class FilmExtractor implements ResultSetExtractor<List<Film>> {
    @Override
    public List<Film> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<Integer, Film> films = new LinkedHashMap<>();

        while (rs.next()) {
            Integer filmId = rs.getInt("film_id");
            Film film = films.get(filmId);

            if (film == null) {
                film = new Film();
                films.put(filmId, film);
            }

            film.setId(rs.getInt("film_id"));
            film.setName(rs.getString("film_name"));
            film.setDescription(rs.getString("film_description"));
            film.setReleaseDate(rs.getTimestamp("film_releasedate")
                    .toLocalDateTime().toLocalDate());
            film.setDuration(rs.getInt("film_duration"));

            Integer ratingId = rs.getObject("rating_id", Integer.class);

            if (ratingId != null) {
                if (film.getMpa() == null) {
                    Mpa mpaRating = Mpa.builder().build();
                    mpaRating.setId(ratingId);
                    mpaRating.setName(rs.getString("rating_code"));
                    mpaRating.setDescription(rs.getString("rating_description"));

                    film.setMpa(mpaRating);
                }
            }

            Integer genreId = rs.getObject("genre_id", Integer.class);

            if (genreId != null) {
                LinkedHashSet<Genre> genres = film.getGenres();

                if (genres == null) {
                    genres = new LinkedHashSet<>();
                    film.setGenres(genres);
                }

                Genre genre = Genre.builder().build();
                genre.setId(genreId);
                genre.setName(rs.getString("genre_name"));

                genres.add(genre);
            }

            Integer userLike = rs.getObject("user_like", Integer.class);
            if (userLike != null) {
                Set<Integer> likes = film.getLikes();
                likes.add(userLike);
            }
        }

        return new ArrayList<>(films.values());
    }
}