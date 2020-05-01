package fii.practic.health;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class FiipracticApplication {

    public static void main(String[] args) {
        SpringApplication.run(FiipracticApplication.class, args);
    }
}
