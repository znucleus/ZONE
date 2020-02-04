package top.zbeboy.zone.service.system;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.zbeboy.zone.domain.tables.daos.FilesDao;
import top.zbeboy.zone.domain.tables.pojos.Files;

import javax.annotation.Resource;

@Service("filesService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class FilesServiceImpl implements FilesService {

    @Resource
    private FilesDao filesDao;

    @Override
    public Files findById(String id) {
        return filesDao.findById(id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void save(Files files) {
        filesDao.insert(files);
    }

    @Override
    public void delete(Files files) {
        filesDao.delete(files);
    }

    @Override
    public void deleteById(String id) {
        filesDao.deleteById(id);
    }
}
