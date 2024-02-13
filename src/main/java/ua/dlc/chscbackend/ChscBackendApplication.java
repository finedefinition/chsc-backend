package ua.dlc.chscbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
@PropertySource("file:${user.dir}/.env")
public class ChscBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChscBackendApplication.class, args);
    }

}
