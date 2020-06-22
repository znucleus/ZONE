package top.zbeboy.zone.hystrix.system;

import org.springframework.stereotype.Component;
import top.zbeboy.zbase.domain.tables.pojos.Files;
import top.zbeboy.zone.feign.system.FilesService;

@Component
public class FilesHystrixClientFallbackFactory implements FilesService {
    @Override
    public Files findById(String id) {
        return new Files();
    }

    @Override
    public void save(Files files) {

    }

    @Override
    public void update(Files files) {

    }

    @Override
    public void delete(Files files) {

    }

    @Override
    public void deleteById(String id) {

    }
}
