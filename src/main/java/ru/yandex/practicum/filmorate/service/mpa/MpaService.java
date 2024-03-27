package ru.yandex.practicum.filmorate.service.mpa;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.Exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MpaService {
    private final MpaStorage mpaStorage;

    public Collection<Mpa> findAllMpa() {
        return mpaStorage.findAllMpa();
    }

    public Mpa getMpaById(int id) {
        List<Mpa> listMpa = mpaStorage.findMpaById(id);
        if (listMpa.isEmpty())
            throw new NotFoundException("Рейтинг c запрашиваемым айди не найден");
        return listMpa.get(0);
    }
}
