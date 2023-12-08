package ru.genby.jokebot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.genby.jokebot.model.Joke;
import java.util.List;
@Repository
public interface JokeRepository extends JpaRepository<Joke, Integer> {
    List<Joke> getJokeByIdNotIn(List<Long> exception);
    List<Joke> getJokesByCategoryAndIdNotIn(String category, List<Long> exception);
    List<Joke> getJokesByCategory(String category);
}
