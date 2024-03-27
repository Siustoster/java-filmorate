package ru.yandex.practicum.filmorate.storage.mpa;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;
import java.util.List;

public interface MpaStorage {
    Collection<Mpa> findAllMpa();

    List<Mpa> findMpaById(int id);
}
