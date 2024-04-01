package ru.yandex.practicum.filmorate.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@EqualsAndHashCode
public class Genre {
    private int id;
    private String name;
}
