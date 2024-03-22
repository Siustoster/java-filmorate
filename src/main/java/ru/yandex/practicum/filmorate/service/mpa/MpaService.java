package ru.yandex.practicum.filmorate.service.mpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.Exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.util.Collection;
import java.util.List;

@Service
public class MpaService {
    @Autowired
    private MpaStorage mpaStorage;

    public Collection<Mpa> findAllMpa() {
        return mpaStorage.findAllMpa();
    }

    public Mpa getMpaById(int id) {
        List<Mpa> listMpa = mpaStorage.findMpaById(id);
        if (!listMpa.isEmpty())
            return listMpa.get(0);
        else throw new NotFoundException("Рейтинг c запрашиваемым айди не найден");
    }
}
