package lk.cloud.noise.collect;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PeriodicSampleSubmitApplication {
    public static void main(String[] args) {
        SpringApplication.run(PeriodicSampleSubmitApplication.class, args);
    }
}
