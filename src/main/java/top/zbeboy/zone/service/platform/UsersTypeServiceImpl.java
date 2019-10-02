package top.zbeboy.zone.service.platform;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.zbeboy.zone.config.CacheBook;
import top.zbeboy.zone.domain.tables.daos.UsersTypeDao;
import top.zbeboy.zone.domain.tables.pojos.UsersType;

import javax.annotation.Resource;

@Service("usersTypeService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class UsersTypeServiceImpl implements UsersTypeService {

    @Resource
    private UsersTypeDao usersTypeDao;

    @Cacheable(cacheNames = CacheBook.USERS_TYPE_BY_NAME, key = "#usersTypeName")
    @Override
    public UsersType findByUsersTypeName(String usersTypeName) {
        return usersTypeDao.fetchOneByUsersTypeName(usersTypeName);
    }

    @Cacheable(cacheNames = CacheBook.USERS_TYPE_BY_ID, key = "#usersTypeId")
    @Override
    public UsersType findById(int usersTypeId) {
        return usersTypeDao.findById(usersTypeId);
    }
}
