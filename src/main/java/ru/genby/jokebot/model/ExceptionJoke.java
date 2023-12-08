package ru.genby.jokebot.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "exception_joke")
public class ExceptionJoke {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "chatid")
    private Long chatid;

    @Column(name = "number_joke")
    private  Long  numberJoke;

    public ExceptionJoke(Long chatId, Long numberJoke ) {
       this.chatid = chatId;
       this.numberJoke = numberJoke;
    }

    public ExceptionJoke() {

    }
}
