package ru.yandex.practicum.filmorate.service.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

@Service
public class FilmService {
    @Autowired
    FilmStorage filmStorage;


}
/*Создайте FilmService, который будет отвечать за операции с фильмами, — добавление и удаление лайка,
вывод 10 наиболее популярных фильмов по количеству лайков.
 Пусть пока каждый пользователь может поставить лайк фильму только один раз. */