package top.zbeboy.zone.service.internship;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.zbeboy.zone.config.CacheBook;
import top.zbeboy.zone.domain.tables.pojos.InternshipJournalContent;
import top.zbeboy.zone.domain.tables.records.InternshipJournalContentRecord;

import java.util.Optional;

import static top.zbeboy.zone.domain.Tables.INTERNSHIP_JOURNAL_CONTENT;

@Service("internshipJournalContentService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class InternshipJournalContentServiceImpl implements InternshipJournalContentService {

    private final DSLContext create;

    @Autowired
    InternshipJournalContentServiceImpl(DSLContext dslContext) {
        create = dslContext;
    }

    @Cacheable(cacheNames = CacheBook.INTERNSHIP_JOURNAL_CONTENT, key = "#internshipJournalId")
    @Override
    public Optional<InternshipJournalContentRecord> findByInternshipJournalId(String internshipJournalId) {
        return create.selectFrom(INTERNSHIP_JOURNAL_CONTENT)
                .where(INTERNSHIP_JOURNAL_CONTENT.INTERNSHIP_JOURNAL_ID.eq(internshipJournalId))
                .fetchOptional();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void save(InternshipJournalContent internshipJournalContent) {
        create.insertInto(INTERNSHIP_JOURNAL_CONTENT)
                .set(INTERNSHIP_JOURNAL_CONTENT.INTERNSHIP_JOURNAL_ID, internshipJournalContent.getInternshipJournalId())
                .set(INTERNSHIP_JOURNAL_CONTENT.INTERNSHIP_JOURNAL_CONTENT_, internshipJournalContent.getInternshipJournalContent())
                .set(INTERNSHIP_JOURNAL_CONTENT.INTERNSHIP_JOURNAL_HTML, internshipJournalContent.getInternshipJournalHtml())
                .set(INTERNSHIP_JOURNAL_CONTENT.INTERNSHIP_JOURNAL_DATE, internshipJournalContent.getInternshipJournalDate())
                .execute();
    }

    @CacheEvict(cacheNames = CacheBook.INTERNSHIP_JOURNAL_CONTENT, key = "#internshipJournalContent.internshipJournalId")
    @Override
    public void update(InternshipJournalContent internshipJournalContent) {
        create.update(INTERNSHIP_JOURNAL_CONTENT)
                .set(INTERNSHIP_JOURNAL_CONTENT.INTERNSHIP_JOURNAL_CONTENT_, internshipJournalContent.getInternshipJournalContent())
                .set(INTERNSHIP_JOURNAL_CONTENT.INTERNSHIP_JOURNAL_HTML, internshipJournalContent.getInternshipJournalHtml())
                .set(INTERNSHIP_JOURNAL_CONTENT.INTERNSHIP_JOURNAL_DATE, internshipJournalContent.getInternshipJournalDate())
                .where(INTERNSHIP_JOURNAL_CONTENT.INTERNSHIP_JOURNAL_ID.eq(internshipJournalContent.getInternshipJournalId()))
                .execute();
    }
}
