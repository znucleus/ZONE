package top.zbeboy.zone.service.data;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.zbeboy.zone.domain.tables.daos.CollegeApplicationDao;
import top.zbeboy.zone.domain.tables.pojos.Application;
import top.zbeboy.zone.domain.tables.pojos.CollegeApplication;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static top.zbeboy.zone.domain.Tables.APPLICATION;
import static top.zbeboy.zone.domain.Tables.COLLEGE_APPLICATION;

@Service("collegeApplicationService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class CollegeApplicationServiceImpl implements CollegeApplicationService {

    private final DSLContext create;

    @Resource
    private CollegeApplicationDao collegeApplicationDao;

    @Autowired
    CollegeApplicationServiceImpl(DSLContext dslContext) {
        create = dslContext;
    }

    @Override
    public List<Application> findByPidAndCollegeId(String pid, int collegeId) {
        List<Application> applications = new ArrayList<>();
        Result<Record> records = create.select()
                .from(COLLEGE_APPLICATION)
                .join(APPLICATION)
                .on(APPLICATION.APPLICATION_ID.eq(COLLEGE_APPLICATION.APPLICATION_ID))
                .where(APPLICATION.APPLICATION_PID.eq(pid).and(COLLEGE_APPLICATION.COLLEGE_ID.eq(collegeId)))
                .orderBy(APPLICATION.APPLICATION_SORT.asc())
                .fetch();
        if (records.isNotEmpty()) {
            applications = records.into(Application.class);
        }
        return applications;
    }

    @Override
    public List<CollegeApplication> findByCollegeId(int collegeId) {
        return collegeApplicationDao.fetchByCollegeId(collegeId);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void batchSave(List<CollegeApplication> collegeApplication) {
        collegeApplicationDao.insert(collegeApplication);
    }

    @Override
    public void deleteByCollegeId(int collegeId) {
        create.deleteFrom(COLLEGE_APPLICATION)
                .where(COLLEGE_APPLICATION.COLLEGE_ID.eq(collegeId))
                .execute();
    }

    @Override
    public void deleteByApplicationId(String applicationId) {
        create.deleteFrom(COLLEGE_APPLICATION)
                .where(COLLEGE_APPLICATION.APPLICATION_ID.eq(applicationId))
                .execute();
    }
}
