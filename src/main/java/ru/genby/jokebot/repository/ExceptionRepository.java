package ru.genby.jokebot.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.genby.jokebot.model.ExceptionJoke;

import java.util.List;
@Repository
public interface ExceptionRepository extends JpaRepository<ExceptionJoke, Integer> {
    List<ExceptionJoke> getExceptionJokesByChatid(Long chatId);
}
