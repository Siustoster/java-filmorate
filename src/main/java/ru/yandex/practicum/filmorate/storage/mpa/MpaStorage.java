package ru.yandex.practicum.filmorate.storage.mpa;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;
import java.util.List;

public interface MpaStorage {
    public Collection<Mpa> findAllMpa();

    public List<Mpa> findMpaById(int id);
}
