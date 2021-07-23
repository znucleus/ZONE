package top.zbeboy.zone.service.training;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.jooq.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.zbeboy.zbase.config.CacheBook;
import top.zbeboy.zbase.domain.tables.daos.TrainingDocumentDao;
import top.zbeboy.zbase.domain.tables.pojos.TrainingDocument;
import top.zbeboy.zbase.tools.service.util.SQLQueryUtil;
import top.zbeboy.zbase.tools.web.util.pagination.SimplePaginationUtil;
import top.zbeboy.zone.service.plugin.PaginationPlugin;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.Optional;

import static top.zbeboy.zbase.domain.Tables.*;

@Service("trainingDocumentService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class TrainingDocumentServiceImpl implements TrainingDocumentService, PaginationPlugin<SimplePaginationUtil> {

    private final DSLContext create;

    @Resource
    private TrainingDocumentDao trainingDocumentDao;

    @Autowired
    TrainingDocumentServiceImpl(DSLContext dslContext) {
        create = dslContext;
    }

    @Cacheable(cacheNames = CacheBook.TRAINING_DOCUMENT, key = "#id")
    @Override
    public TrainingDocument findById(String id) {
        return trainingDocumentDao.findById(id);
    }

    @Override
    public Optional<Record> findByIdRelation(String id) {
        return create.select()
                .from(TRAINING_DOCUMENT)
                .join(TRAINING_DOCUMENT_CONTENT)
                .on(TRAINING_DOCUMENT.TRAINING_DOCUMENT_ID.eq(TRAINING_DOCUMENT_CONTENT.TRAINING_DOCUMENT_ID))
                .where(TRAINING_DOCUMENT.TRAINING_DOCUMENT_ID.eq(id))
                .fetchOptional();
    }

    @Override
    public Result<Record> findAllByPage(SimplePaginationUtil paginationUtil) {
        SelectOnConditionStep<Record> selectOnConditionStep = create.select()
                .from(TRAINING_DOCUMENT)
                .leftJoin(TRAINING_RELEASE)
                .on(TRAINING_DOCUMENT.TRAINING_RELEASE_ID.eq(TRAINING_RELEASE.TRAINING_RELEASE_ID));
        return queryAllByPage(selectOnConditionStep, paginationUtil, false);
    }

    @Override
    public int countAll(SimplePaginationUtil paginationUtil) {
        SelectOnConditionStep<Record1<Integer>> selectOnConditionStep = create.selectCount()
                .from(TRAINING_DOCUMENT)
                .leftJoin(TRAINING_RELEASE)
                .on(TRAINING_DOCUMENT.TRAINING_RELEASE_ID.eq(TRAINING_RELEASE.TRAINING_RELEASE_ID));
        return countAll(selectOnConditionStep, paginationUtil, false);
    }

    @CacheEvict(cacheNames = CacheBook.TRAINING_DOCUMENT, key = "#trainingDocument.trainingDocumentId")
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void save(TrainingDocument trainingDocument) {
        trainingDocumentDao.insert(trainingDocument);
    }

    @CacheEvict(cacheNames = CacheBook.TRAINING_DOCUMENT, key = "#trainingDocument.trainingDocumentId")
    @Override
    public void update(TrainingDocument trainingDocument) {
        trainingDocumentDao.update(trainingDocument);
    }

    @CacheEvict(cacheNames = CacheBook.TRAINING_DOCUMENT, key = "#id")
    @Override
    public void updateReading(String id) {
        create.execute("UPDATE TRAINING_DOCUMENT SET READING = READING + 1 WHERE TRAINING_DOCUMENT_ID = ?", id);
    }

    @CacheEvict(cacheNames = CacheBook.TRAINING_DOCUMENT, key = "#id")
    @Override
    public void deleteById(String id) {
        trainingDocumentDao.deleteById(id);
    }

    @Override
    public Condition searchCondition(SimplePaginationUtil paginationUtil) {
        Condition a = null;
        JSONObject search = paginationUtil.getSearch();
        if (Objects.nonNull(search)) {
            String documentTitle = StringUtils.trim(search.getString("documentTitle"));
            if (StringUtils.isNotBlank(documentTitle)) {
                a = TRAINING_DOCUMENT.DOCUMENT_TITLE.like(SQLQueryUtil.likeAllParam(documentTitle));
            }
        }
        return a;
    }

    @Override
    public Condition extraCondition(SimplePaginationUtil paginationUtil) {
        Condition a = null;
        JSONObject search = paginationUtil.getSearch();

        if (Objects.nonNull(search)) {
            String trainingReleaseId = StringUtils.trim(search.getString("trainingReleaseId"));
            String courseId = StringUtils.trim(search.getString("courseId"));
            if (StringUtils.isNotBlank(trainingReleaseId)) {
                a = TRAINING_DOCUMENT.TRAINING_RELEASE_ID.eq(trainingReleaseId);
            }

            if (StringUtils.isNotBlank(courseId)) {
                int courseIdInt = NumberUtils.toInt(courseId);
                if (Objects.isNull(a)) {
                    a = TRAINING_DOCUMENT.COURSE_ID.eq(courseIdInt);
                } else {
                    a = a.and(TRAINING_DOCUMENT.COURSE_ID.eq(courseIdInt));
                }
            }
        }
        return a;
    }

    @Override
    public void sortCondition(SelectConnectByStep<Record> step, SimplePaginationUtil paginationUtil) {
        String orderColumnName = paginationUtil.getOrderColumnName();
        String orderDir = paginationUtil.getOrderDir();
        boolean isAsc = StringUtils.equalsIgnoreCase("asc", orderDir);
        SortField[] sortField = null;
        if (StringUtils.isNotBlank(orderColumnName)) {
            if (StringUtils.equals("createDate", orderColumnName)) {
                sortField = new SortField[1];
                if (isAsc) {
                    sortField[0] = TRAINING_DOCUMENT.CREATE_DATE.asc();
                } else {
                    sortField[0] = TRAINING_DOCUMENT.CREATE_DATE.desc();
                }
            }

            if (StringUtils.equals("reading", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = TRAINING_DOCUMENT.READING.asc();
                    sortField[1] = TRAINING_DOCUMENT.CREATE_DATE.asc();
                } else {
                    sortField[0] = TRAINING_DOCUMENT.READING.desc();
                    sortField[1] = TRAINING_DOCUMENT.CREATE_DATE.desc();
                }
            }
        }
        sortToFinish(step, sortField);
    }
}
