package top.zbeboy.zone.service.internship;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static top.zbeboy.zone.domain.Tables.INTERNSHIP_APPLY;

@Service("internshipApplyService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class InternshipApplyServiceImpl implements InternshipApplyService {

    private final DSLContext create;

    @Autowired
    InternshipApplyServiceImpl(DSLContext dslContext) {
        create = dslContext;
    }

    @Override
    public Optional<Record> findByInternshipReleaseIdAndStudentId(String internshipReleaseId, int studentId) {
        return create.select()
                .from(INTERNSHIP_APPLY)
                .where(INTERNSHIP_APPLY.INTERNSHIP_RELEASE_ID.eq(internshipReleaseId).and(INTERNSHIP_APPLY.STUDENT_ID.eq(studentId)))
                .fetchOptional();
    }
}
