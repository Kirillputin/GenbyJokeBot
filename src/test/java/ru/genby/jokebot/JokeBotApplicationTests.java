package ru.genby.jokebot;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.genby.jokebot.model.Joke;
import ru.genby.jokebot.repository.JokeRepository;

import java.io.File;
import java.io.IOException;
import java.util.List;

@SpringBootTest
class JokeBotApplicationTests {
    @Autowired
    private JokeRepository jokeRepository;


    @Test
    void contextLoads() {

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            TypeFactory typeFactory = objectMapper.getTypeFactory();
            List<Joke> jokeList = objectMapper.readValue(new File("D:\\users\\KPutin\\Documents\\Java\\JokeBot\\src\\main\\resources\\database\\stupidstuff_rus.json"),
                    typeFactory.constructCollectionType(List.class, Joke.class));

//    JokeRepository jokeRepository = new JokeService

//    JpaRepository jokeService = new JpaRepository();

            jokeRepository.saveAll(jokeList);

        } catch (StreamReadException e) {
            throw new RuntimeException(e);
        } catch (DatabindException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

}
