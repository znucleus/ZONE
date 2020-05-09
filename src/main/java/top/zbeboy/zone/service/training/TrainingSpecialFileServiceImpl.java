package top.zbeboy.zone.service.training;

import org.apache.commons.lang3.StringUtils;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.zbeboy.zone.config.CacheBook;
import top.zbeboy.zone.domain.tables.daos.TrainingSpecialFileDao;
import top.zbeboy.zone.domain.tables.pojos.TrainingSpecialFile;
import top.zbeboy.zone.service.util.SQLQueryUtil;
import top.zbeboy.zone.web.vo.training.special.TrainingSpecialFileSearchVo;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static top.zbeboy.zone.domain.Tables.FILES;
import static top.zbeboy.zone.domain.Tables.TRAINING_SPECIAL_FILE;

@Service("trainingSpecialFileService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class TrainingSpecialFileServiceImpl implements TrainingSpecialFileService {

    private final DSLContext create;

    @Resource
    private TrainingSpecialFileDao trainingSpecialFileDao;

    @Autowired
    TrainingSpecialFileServiceImpl(DSLContext dslContext) {
        create = dslContext;
    }

    @Override
    public TrainingSpecialFile findById(String id) {
        return trainingSpecialFileDao.findById(id);
    }

    @Cacheable(cacheNames = CacheBook.TRAINING_SPECIAL_FILE, key = "#id")
    @Override
    public Optional<Record> findByIdRelation(String id) {
        return create.select()
                .from(TRAINING_SPECIAL_FILE)
                .leftJoin(FILES)
                .on(TRAINING_SPECIAL_FILE.FILE_ID.eq(FILES.FILE_ID))
                .where(TRAINING_SPECIAL_FILE.TRAINING_SPECIAL_FILE_ID.eq(id))
                .fetchOptional();
    }

    @Override
    public List<TrainingSpecialFile> findByFileId(String fileId) {
        return trainingSpecialFileDao.fetchByFileId(fileId);
    }

    @Override
    public Result<Record> findAllByCondition(TrainingSpecialFileSearchVo trainingSpecialFileSearchVo) {
        Condition a = null;
        if (StringUtils.isNotBlank(trainingSpecialFileSearchVo.getFileTypeId())) {
            a = TRAINING_SPECIAL_FILE.FILE_TYPE_ID.eq(trainingSpecialFileSearchVo.getFileTypeId());
        }

        if (StringUtils.isNotBlank(trainingSpecialFileSearchVo.getOriginalFileName())) {
            if (Objects.isNull(a)) {
                a = FILES.ORIGINAL_FILE_NAME.like(SQLQueryUtil.likeAllParam(trainingSpecialFileSearchVo.getOriginalFileName()));
            } else {
                a = a.and(FILES.ORIGINAL_FILE_NAME.like(SQLQueryUtil.likeAllParam(trainingSpecialFileSearchVo.getOriginalFileName())));
            }
        }

        if (Objects.nonNull(trainingSpecialFileSearchVo.getMapping())) {
            if (Objects.isNull(a)) {
                a = TRAINING_SPECIAL_FILE.MAPPING.eq(trainingSpecialFileSearchVo.getMapping());
            } else {
                a = a.and(TRAINING_SPECIAL_FILE.MAPPING.eq(trainingSpecialFileSearchVo.getMapping()));
            }
        }

        Result<Record> records;
        if (Objects.isNull(a)) {
            records = create.select()
                    .from(TRAINING_SPECIAL_FILE)
                    .leftJoin(FILES)
                    .on(TRAINING_SPECIAL_FILE.FILE_ID.eq(FILES.FILE_ID))
                    .fetch();
        } else {
            records = create.select()
                    .from(TRAINING_SPECIAL_FILE)
                    .leftJoin(FILES)
                    .on(TRAINING_SPECIAL_FILE.FILE_ID.eq(FILES.FILE_ID))
                    .where(a)
                    .fetch();
        }
        return records;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void save(TrainingSpecialFile trainingSpecialFile) {
        trainingSpecialFileDao.insert(trainingSpecialFile);
    }

    @CacheEvict(cacheNames = CacheBook.TRAINING_SPECIAL_FILE, key = "#trainingSpecialFile.trainingSpecialFileId")
    @Override
    public void update(TrainingSpecialFile trainingSpecialFile) {
        trainingSpecialFileDao.update(trainingSpecialFile);
    }

    @CacheEvict(cacheNames = CacheBook.TRAINING_SPECIAL_FILE, key = "#id")
    @Override
    public void updateDownloads(String id) {
        create.execute("UPDATE TRAINING_SPECIAL_FILE SET DOWNLOADS = DOWNLOADS + 1 WHERE TRAINING_SPECIAL_FILE_ID = ?", id);
    }

    @CacheEvict(cacheNames = CacheBook.TRAINING_SPECIAL_FILE, key = "#id")
    @Override
    public void deleteById(String id) {
        trainingSpecialFileDao.deleteById(id);
    }
}
