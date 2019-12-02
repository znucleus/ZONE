package top.zbeboy.zone.service.platform;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.zbeboy.zone.domain.tables.daos.RoleApplicationDao;
import top.zbeboy.zone.domain.tables.pojos.RoleApplication;

import javax.annotation.Resource;

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

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void save(RoleApplication roleApplication) {
        roleApplicationDao.insert(roleApplication);
    }

    @Override
    public void deleteByApplicationId(String applicationId) {
        create.deleteFrom(ROLE_APPLICATION)
                .where(ROLE_APPLICATION.APPLICATION_ID.eq(applicationId))
                .execute();
    }
}
