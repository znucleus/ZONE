package top.zbeboy.zone.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * 数据库连接配置
 *
 * @author zbeboy
 * @version 1.0
 * @since 1.0
 */
@Configuration
public class DatasourceConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "datasource.mine")
    public DataSource dataSource() {
        return DataSourceBuilder.create()
                .type(HikariDataSource.class).build();
    }
}
