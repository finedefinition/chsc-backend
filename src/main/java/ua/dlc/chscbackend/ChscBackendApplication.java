package ua.dlc.chscbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class ChscBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChscBackendApplication.class, args);
    }

}
