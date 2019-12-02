package top.zbeboy.zone.service.data;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static top.zbeboy.zone.domain.Tables.COLLEGE_APPLICATION;

@Service("collegeApplicationService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class CollegeApplicationServiceImpl implements CollegeApplicationService{

    private final DSLContext create;

    @Autowired
    CollegeApplicationServiceImpl(DSLContext dslContext) {
        create = dslContext;
    }

    @Override
    public void deleteByApplicationId(String applicationId) {
        create.deleteFrom(COLLEGE_APPLICATION)
                .where(COLLEGE_APPLICATION.APPLICATION_ID.eq(applicationId))
                .execute();
    }
}
