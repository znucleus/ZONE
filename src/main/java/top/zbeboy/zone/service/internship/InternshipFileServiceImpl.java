package top.zbeboy.zone.service.internship;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.zbeboy.zbase.config.CacheBook;
import top.zbeboy.zbase.domain.tables.daos.InternshipFileDao;
import top.zbeboy.zbase.domain.tables.pojos.InternshipFile;

import javax.annotation.Resource;

import static top.zbeboy.zbase.domain.Tables.FILES;
import static top.zbeboy.zbase.domain.Tables.INTERNSHIP_FILE;

@Service("internshipFileService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class InternshipFileServiceImpl implements InternshipFileService {

    private final DSLContext create;

    @Resource
    private InternshipFileDao internshipFileDao;

    @Autowired
    InternshipFileServiceImpl(DSLContext dslContext) {
        create = dslContext;
    }

    @Cacheable(cacheNames = CacheBook.INTERNSHIP_FILES, key = "#internshipReleaseId")
    @Override
    public Result<Record> findByInternshipReleaseId(String internshipReleaseId) {
        return create.select()
                .from(INTERNSHIP_FILE)
                .join(FILES)
                .on(INTERNSHIP_FILE.FILE_ID.eq(FILES.FILE_ID))
                .where(INTERNSHIP_FILE.INTERNSHIP_RELEASE_ID.eq(internshipReleaseId))
                .fetch();
    }

    @CacheEvict(cacheNames = CacheBook.INTERNSHIP_FILES, key = "#internshipFile.internshipReleaseId")
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void save(InternshipFile internshipFile) {
        internshipFileDao.insert(internshipFile);
    }

    @CacheEvict(cacheNames = CacheBook.INTERNSHIP_FILES, key = "#internshipReleaseId")
    @Override
    public void deleteByInternshipReleaseId(String internshipReleaseId) {
        create.deleteFrom(INTERNSHIP_FILE).where(INTERNSHIP_FILE.INTERNSHIP_RELEASE_ID.eq(internshipReleaseId)).execute();
    }

    @CacheEvict(cacheNames = CacheBook.INTERNSHIP_FILES, key = "#internshipReleaseId")
    @Override
    public void deleteByFileIdAndInternshipReleaseId(String fileId, String internshipReleaseId) {
        create.deleteFrom(INTERNSHIP_FILE)
                .where(INTERNSHIP_FILE.INTERNSHIP_RELEASE_ID.eq(internshipReleaseId)
                        .and(INTERNSHIP_FILE.FILE_ID.eq(fileId))).execute();
    }
}
