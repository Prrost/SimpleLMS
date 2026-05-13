package kz.diploma.rprettser.simplelms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@SpringBootApplication
public class SimpleLmsApplication {

    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone(System.getenv().getOrDefault("APP_TIMEZONE", "Asia/Almaty")));
        SpringApplication.run(SimpleLmsApplication.class, args);
    }

}
