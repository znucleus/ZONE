package top.zbeboy.zone.service.training;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.jooq.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.zbeboy.zbase.config.CacheBook;
import top.zbeboy.zbase.domain.tables.daos.TrainingSpecialDocumentDao;
import top.zbeboy.zbase.domain.tables.pojos.TrainingSpecialDocument;
import top.zbeboy.zbase.tools.service.util.SQLQueryUtil;
import top.zbeboy.zbase.tools.web.util.pagination.SimplePaginationUtil;
import top.zbeboy.zone.service.plugin.PaginationPlugin;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.Optional;

import static top.zbeboy.zbase.domain.Tables.TRAINING_SPECIAL_DOCUMENT;
import static top.zbeboy.zbase.domain.Tables.TRAINING_SPECIAL_DOCUMENT_CONTENT;

@Service("trainingSpecialDocumentService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class TrainingSpecialDocumentServiceImpl implements TrainingSpecialDocumentService, PaginationPlugin<SimplePaginationUtil> {

    private final DSLContext create;

    @Resource
    private TrainingSpecialDocumentDao trainingSpecialDocumentDao;

    @Autowired
    TrainingSpecialDocumentServiceImpl(DSLContext dslContext) {
        create = dslContext;
    }

    @Cacheable(cacheNames = CacheBook.TRAINING_SPECIAL_DOCUMENT, key = "#id")
    @Override
    public TrainingSpecialDocument findById(String id) {
        return trainingSpecialDocumentDao.findById(id);
    }

    @Override
    public Optional<Record> findByIdRelation(String id) {
        return create.select()
                .from(TRAINING_SPECIAL_DOCUMENT)
                .join(TRAINING_SPECIAL_DOCUMENT_CONTENT)
                .on(TRAINING_SPECIAL_DOCUMENT.TRAINING_SPECIAL_DOCUMENT_ID.eq(TRAINING_SPECIAL_DOCUMENT_CONTENT.TRAINING_SPECIAL_DOCUMENT_ID))
                .where(TRAINING_SPECIAL_DOCUMENT.TRAINING_SPECIAL_DOCUMENT_ID.eq(id))
                .fetchOptional();
    }

    @Override
    public Result<Record> findAllByPage(SimplePaginationUtil paginationUtil) {
        return queryAllByPage(create, TRAINING_SPECIAL_DOCUMENT, paginationUtil, false);
    }

    @Override
    public int countAll(SimplePaginationUtil paginationUtil) {
        return countAll(create, TRAINING_SPECIAL_DOCUMENT, paginationUtil, false);
    }

    @CacheEvict(cacheNames = CacheBook.TRAINING_SPECIAL_DOCUMENT, key = "#trainingSpecialDocument.trainingSpecialDocumentId")
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void save(TrainingSpecialDocument trainingSpecialDocument) {
        trainingSpecialDocumentDao.insert(trainingSpecialDocument);
    }

    @CacheEvict(cacheNames = CacheBook.TRAINING_SPECIAL_DOCUMENT, key = "#trainingSpecialDocument.trainingSpecialDocumentId")
    @Override
    public void update(TrainingSpecialDocument trainingSpecialDocument) {
        trainingSpecialDocumentDao.update(trainingSpecialDocument);
    }

    @CacheEvict(cacheNames = CacheBook.TRAINING_SPECIAL_DOCUMENT, key = "#id")
    @Override
    public void updateReading(String id) {
        create.execute("UPDATE TRAINING_SPECIAL_DOCUMENT SET READING = READING + 1 WHERE TRAINING_SPECIAL_DOCUMENT_ID = ?", id);
    }

    @CacheEvict(cacheNames = CacheBook.TRAINING_SPECIAL_DOCUMENT, key = "#id")
    @Override
    public void deleteById(String id) {
        trainingSpecialDocumentDao.deleteById(id);
    }

    @Override
    public Condition searchCondition(SimplePaginationUtil paginationUtil) {
        Condition a = null;
        JSONObject search = paginationUtil.getSearch();
        if (Objects.nonNull(search)) {
            String title = StringUtils.trim(search.getString("title"));
            if (StringUtils.isNotBlank(title)) {
                a = TRAINING_SPECIAL_DOCUMENT.TITLE.like(SQLQueryUtil.likeAllParam(title));
            }
        }
        return a;
    }

    @Override
    public Condition extraCondition(SimplePaginationUtil paginationUtil) {
        Condition a = null;
        JSONObject search = paginationUtil.getSearch();

        if (Objects.nonNull(search)) {
            String trainingSpecialId = StringUtils.trim(search.getString("trainingSpecialId"));
            if (StringUtils.isNotBlank(trainingSpecialId)) {
                a = TRAINING_SPECIAL_DOCUMENT.TRAINING_SPECIAL_ID.eq(trainingSpecialId);
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
                    sortField[0] = TRAINING_SPECIAL_DOCUMENT.CREATE_DATE.asc();
                } else {
                    sortField[0] = TRAINING_SPECIAL_DOCUMENT.CREATE_DATE.desc();
                }
            }
        }
        sortToFinish(step, sortField);
    }
}
