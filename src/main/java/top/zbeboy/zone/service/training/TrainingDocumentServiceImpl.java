package top.zbeboy.zone.service.training;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.jooq.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.zbeboy.zone.service.plugin.PaginationPlugin;
import top.zbeboy.zone.service.util.SQLQueryUtil;
import top.zbeboy.zone.web.util.pagination.SimplePaginationUtil;

import java.util.Objects;

import static top.zbeboy.zone.domain.Tables.TRAINING_DOCUMENT;

@Service("trainingDocumentService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class TrainingDocumentServiceImpl implements TrainingDocumentService, PaginationPlugin<SimplePaginationUtil> {

    private final DSLContext create;

    @Autowired
    TrainingDocumentServiceImpl(DSLContext dslContext) {
        create = dslContext;
    }

    @Override
    public Result<Record> findAllByPage(SimplePaginationUtil paginationUtil) {
        return queryAllByPage(create, TRAINING_DOCUMENT, paginationUtil, false);
    }

    @Override
    public int countAll(SimplePaginationUtil paginationUtil) {
        return countAll(create, TRAINING_DOCUMENT, paginationUtil, false);
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
