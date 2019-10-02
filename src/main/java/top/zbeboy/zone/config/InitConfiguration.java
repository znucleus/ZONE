package top.zbeboy.zone.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

/**
 * 初始化配置.
 *
 * @author zbeboy
 * @version 1.0
 * @since 1.0
 */
@Component
public class InitConfiguration implements CommandLineRunner {

    private final Logger log = LoggerFactory.getLogger(InitConfiguration.class);

    private final CacheManager cacheManager;

    @Autowired
    public InitConfiguration(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Override
    public void run(String... strings) throws Exception {
        log.info("\n\n" + "=========================================================\n"
                + "Using cache manager: " + this.cacheManager.getClass().getName() + "\n"
                + "=========================================================\n\n");
    }
}
