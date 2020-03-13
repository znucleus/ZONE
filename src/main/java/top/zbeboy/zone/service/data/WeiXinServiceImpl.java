package top.zbeboy.zone.service.data;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.zbeboy.zone.domain.tables.daos.WeiXinDao;
import top.zbeboy.zone.domain.tables.pojos.WeiXin;
import top.zbeboy.zone.domain.tables.records.WeiXinRecord;

import javax.annotation.Resource;
import java.util.Optional;

import static top.zbeboy.zone.domain.Tables.*;

@Service("weiXinService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class WeiXinServiceImpl implements WeiXinService {

    private final DSLContext create;

    @Resource
    private WeiXinDao weiXinDao;

    @Autowired
    WeiXinServiceImpl(DSLContext dslContext) {
        create = dslContext;
    }

    @Override
    public Optional<WeiXinRecord> findByUsernameAndAppId(String username, String appId) {
        return create.selectFrom(WEI_XIN).where(WEI_XIN.USERNAME.eq(username).and(WEI_XIN.APP_ID.eq(appId))).fetchOptional();
    }

    @Override
    public Optional<Record> findByStudentIdAndAppId(int studentId, String appId) {
        return create.select()
                .from(WEI_XIN)
                .leftJoin(STUDENT)
                .on(WEI_XIN.USERNAME.eq(STUDENT.USERNAME))
                .leftJoin(USERS)
                .on(STUDENT.USERNAME.eq(USERS.USERNAME))
                .where(WEI_XIN.APP_ID.eq(appId).and(STUDENT.STUDENT_ID.eq(studentId)))
                .fetchOptional();
    }


    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void save(WeiXin weiXin) {
        weiXinDao.insert(weiXin);
    }

    @Override
    public void update(WeiXin weiXin) {
        weiXinDao.update(weiXin);
    }
}
