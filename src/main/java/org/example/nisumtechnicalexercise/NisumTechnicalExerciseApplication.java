package org.example.nisumtechnicalexercise;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class NisumTechnicalExerciseApplication {

    public static void main(String[] args) {
        SpringApplication.run(NisumTechnicalExerciseApplication.class, args);
        log.info("Application runing OK...");
    }
}
