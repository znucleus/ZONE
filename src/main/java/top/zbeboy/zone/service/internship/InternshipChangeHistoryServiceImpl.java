package top.zbeboy.zone.service.internship;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.zbeboy.zone.domain.tables.daos.InternshipChangeHistoryDao;
import top.zbeboy.zone.domain.tables.pojos.InternshipChangeHistory;

import javax.annotation.Resource;

import static top.zbeboy.zone.domain.Tables.INTERNSHIP_CHANGE_HISTORY;

@Service("internshipChangeHistoryService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class InternshipChangeHistoryServiceImpl implements InternshipChangeHistoryService {

    private final DSLContext create;

    @Resource
    private InternshipChangeHistoryDao internshipChangeHistoryDao;

    @Autowired
    InternshipChangeHistoryServiceImpl(DSLContext dslContext) {
        create = dslContext;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void save(InternshipChangeHistory internshipChangeHistory) {
        internshipChangeHistoryDao.insert(internshipChangeHistory);
    }

    @Override
    public void deleteByInternshipReleaseIdAndStudentId(String internshipReleaseId, int studentId) {
        create.deleteFrom(INTERNSHIP_CHANGE_HISTORY).where(INTERNSHIP_CHANGE_HISTORY.INTERNSHIP_RELEASE_ID.eq(internshipReleaseId)
                .and(INTERNSHIP_CHANGE_HISTORY.STUDENT_ID.eq(studentId))).execute();
    }
}
