package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Film {

    private int id;
    @NotNull
    @NotBlank
    private String name;
    private String description;
    private LocalDate releaseDate;
    @Builder.Default
    private Set<Genre> genres = new LinkedHashSet<>();
    @Builder.Default
    private Set<Integer> likes = new HashSet<>();
    private Mpa mpa;
    @Positive
    private int duration;
}
