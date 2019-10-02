package top.zbeboy.zone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import top.zbeboy.zone.config.ZoneProperties;

@SpringBootApplication
@EnableCaching
@EnableConfigurationProperties(ZoneProperties.class)
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
