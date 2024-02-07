package ua.dlc.chscbackend;

import jakarta.annotation.Resources;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("file:${user.dir}/.env")
public class ChscBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChscBackendApplication.class, args);
    }

}
