package top.zbeboy.zone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import top.zbeboy.zbase.config.ZoneProperties;
import top.zbeboy.zone.web.util.SpringBootUtil;

@ComponentScan(basePackages = {"top.zbeboy.zone", "top.zbeboy.zbase"})
@SpringBootApplication
@EnableCaching
@Import(SpringBootUtil.class)
@EnableConfigurationProperties(ZoneProperties.class)
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"top.zbeboy.zone", "top.zbeboy.zbase"})
@EnableRedisHttpSession
public class Application {

    public static void main(String[] args) {
        System.setProperty("net.fortuna.ical4j.timezone.cache.impl", "net.fortuna.ical4j.util.MapTimeZoneCache");
        SpringApplication.run(Application.class, args);
    }
}
