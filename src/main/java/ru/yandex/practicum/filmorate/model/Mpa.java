package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

@Data

public class Mpa {
    private int id;
    private String name;
    private String description;
    public Mpa(int id, String code, String description) {
        this.id = id;
        this.name = code;
        this.description = description;
    }
}
