package top.zbeboy.zone.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.ConfigureRedisAction;

/**
 * 用于解决加入spring-cloud-starter-sleuth后，连接redis失败
 * spring-session-redis需要使用键空间通知(keyspace notification)这个功能
 * redis.windows.conf 需要配置 notify-keyspace-events Ex
 * 同时这里需要设置ConfigureRedisAction.NO_OP
 */
@Configuration
public class RedisConfiguration {

    @Bean
    public static ConfigureRedisAction configureRedisAction() {
        return ConfigureRedisAction.NO_OP;
    }
}
