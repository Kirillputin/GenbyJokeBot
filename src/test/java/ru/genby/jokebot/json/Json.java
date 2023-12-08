package ru.genby.jokebot.json;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureJdbc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import ru.genby.jokebot.model.Joke;
import ru.genby.jokebot.repository.JokeRepository;

import java.io.File;
import java.io.IOException;
import java.util.List;

@SpringBootTest
@AutoConfigureJdbc
@Configuration
//@SpringBootConfiguration
public class Json {
    @Autowired
//    private final JokeRepository jokeRepository;
    private JokeRepository jokeRepository;

//    public Json(JokeRepository jokeRepository) {
//        this.jokeRepository = jokeRepository;
//    }

    @Test
    public void loadJson(){

        try {
    ObjectMapper objectMapper = new ObjectMapper();
    TypeFactory typeFactory = objectMapper.getTypeFactory();
    List<Joke> jokeList = objectMapper.readValue(new File("D:\\users\\KPutin\\Documents\\Java\\JokeBot\\src\\main\\resources\\database\\stupidstuff_rus.json"),
            typeFactory.constructCollectionType(List.class,Joke.class));

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
