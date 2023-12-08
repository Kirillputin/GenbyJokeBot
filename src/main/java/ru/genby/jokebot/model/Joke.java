package ru.genby.jokebot.model;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "jokes")
public class Joke {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "category")
    private String category;

    @Column(name = "joke")
    private String joke;

}
