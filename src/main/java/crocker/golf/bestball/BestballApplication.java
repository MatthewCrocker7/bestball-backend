package crocker.golf.bestball;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BestballApplication {

    public static void main(String[] args) {
        SpringApplication.run(BestballApplication.class, args);
    }

}
