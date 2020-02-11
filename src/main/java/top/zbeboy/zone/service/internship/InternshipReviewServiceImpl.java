package top.zbeboy.zone.service.internship;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static top.zbeboy.zone.domain.Tables.INTERNSHIP_APPLY;

@Service("internshipReviewService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class InternshipReviewServiceImpl implements InternshipReviewService {

    private final DSLContext create;

    @Autowired
    InternshipReviewServiceImpl(DSLContext dslContext) {
        create = dslContext;
    }

    @Override
    public int countByInternshipReleaseIdAndInternshipApplyState(String internshipReleaseId, int internshipApplyState) {
        return create.selectCount()
                .from(INTERNSHIP_APPLY)
                .where(INTERNSHIP_APPLY.INTERNSHIP_RELEASE_ID.eq(internshipReleaseId).and(INTERNSHIP_APPLY.INTERNSHIP_APPLY_STATE.eq(internshipApplyState)))
                .fetchOne().value1();
    }
}
