package top.zbeboy.zone.service.platform;

import org.jooq.DSLContext;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.zbeboy.zone.config.CacheBook;
import top.zbeboy.zone.domain.tables.daos.RoleApplicationDao;
import top.zbeboy.zone.domain.tables.pojos.RoleApplication;
import top.zbeboy.zone.domain.tables.records.RoleApplicationRecord;

import javax.annotation.Resource;
import java.util.List;

import static top.zbeboy.zone.domain.Tables.ROLE_APPLICATION;

@Service("roleApplicationService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class RoleApplicationServiceImpl implements RoleApplicationService {

    private final DSLContext create;

    @Resource
    private RoleApplicationDao roleApplicationDao;

    @Autowired
    RoleApplicationServiceImpl(DSLContext dslContext) {
        create = dslContext;
    }

    @Override
    public Result<RoleApplicationRecord> findByRoleId(String roleId) {
        return create.selectFrom(ROLE_APPLICATION)
                .where(ROLE_APPLICATION.ROLE_ID.eq(roleId))
                .fetch();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void save(RoleApplication roleApplication) {
        roleApplicationDao.insert(roleApplication);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void batchSave(List<RoleApplication> roleApplicationList) {
        roleApplicationDao.insert(roleApplicationList);
    }

    @CacheEvict(cacheNames = {CacheBook.MENU, CacheBook.ROLES_APPLICATION}, allEntries = true)
    @Override
    public void deleteByApplicationId(String applicationId) {
        create.deleteFrom(ROLE_APPLICATION)
                .where(ROLE_APPLICATION.APPLICATION_ID.eq(applicationId))
                .execute();
    }

    @CacheEvict(cacheNames = {CacheBook.MENU, CacheBook.ROLES_APPLICATION}, allEntries = true)
    @Override
    public void deleteByRoleId(String roleId) {
        create.deleteFrom(ROLE_APPLICATION)
                .where(ROLE_APPLICATION.ROLE_ID.eq(roleId))
                .execute();
    }
}
