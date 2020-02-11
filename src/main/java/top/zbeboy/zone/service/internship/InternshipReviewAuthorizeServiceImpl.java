package top.zbeboy.zone.service.internship;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static top.zbeboy.zone.domain.Tables.INTERNSHIP_REVIEW_AUTHORIZE;

@Service("internshipReviewAuthorizeService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class InternshipReviewAuthorizeServiceImpl implements InternshipReviewAuthorizeService {

    private final DSLContext create;

    @Autowired
    InternshipReviewAuthorizeServiceImpl(DSLContext dslContext) {
        create = dslContext;
    }

    @Override
    public Optional<Record> findByInternshipReleaseIdAndUsername(String internshipReleaseId, String username) {
        return create.select()
                .from(INTERNSHIP_REVIEW_AUTHORIZE)
                .where(INTERNSHIP_REVIEW_AUTHORIZE.INTERNSHIP_RELEASE_ID.eq(internshipReleaseId)
                        .and(INTERNSHIP_REVIEW_AUTHORIZE.USERNAME.eq(username)))
                .fetchOptional();
    }
}
