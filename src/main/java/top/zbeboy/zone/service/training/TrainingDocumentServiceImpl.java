package top.zbeboy.zone.service.training;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.zbeboy.zone.domain.tables.daos.TrainingDocumentDao;
import top.zbeboy.zone.domain.tables.pojos.TrainingDocument;
import top.zbeboy.zone.service.plugin.PaginationPlugin;
import top.zbeboy.zone.service.util.SQLQueryUtil;
import top.zbeboy.zone.web.util.pagination.SimplePaginationUtil;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.Optional;

import static org.jooq.impl.DSL.select;
import static top.zbeboy.zone.domain.Tables.TRAINING_DOCUMENT;
import static top.zbeboy.zone.domain.Tables.TRAINING_DOCUMENT_CONTENT;

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
        return queryAllByPage(create, TRAINING_DOCUMENT, paginationUtil, false);
    }

    @Override
    public int countAll(SimplePaginationUtil paginationUtil) {
        return countAll(create, TRAINING_DOCUMENT, paginationUtil, false);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void save(TrainingDocument trainingDocument) {
        trainingDocumentDao.insert(trainingDocument);
    }

    @Override
    public void update(TrainingDocument trainingDocument) {
        trainingDocumentDao.update(trainingDocument);
    }

    @Override
    public void updateReading(String id) {
        create.execute("UPDATE TRAINING_DOCUMENT SET READING = READING + 1 WHERE TRAINING_DOCUMENT_ID = ?", id);
    }

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
            if (StringUtils.isNotBlank(trainingReleaseId)) {
                a = TRAINING_DOCUMENT.TRAINING_RELEASE_ID.eq(trainingReleaseId);
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
        }
        sortToFinish(step, sortField);
    }
}
