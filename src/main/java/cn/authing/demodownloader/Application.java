package cn.authing.demodownloader;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        File file = new File("cache");
        if (!file.exists()) {
            file.mkdir();

            file = new File("cache/java-spring-boot");
            file.mkdir();
        }
        SpringApplication.run(Application.class, args);
    }
}
