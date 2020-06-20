package top.zbeboy.zone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;
import top.zbeboy.zone.config.ZoneProperties;
import top.zbeboy.zone.web.util.SpringBootUtil;

@SpringBootApplication
@EnableCaching
@Import(SpringBootUtil.class)
@EnableConfigurationProperties(ZoneProperties.class)
@EnableDiscoveryClient
@EnableFeignClients
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
