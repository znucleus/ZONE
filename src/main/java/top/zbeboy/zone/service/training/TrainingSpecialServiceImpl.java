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

import static top.zbeboy.zone.domain.Tables.FILES;
import static top.zbeboy.zone.domain.Tables.TRAINING_SPECIAL;

@Service("trainingSpecialService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class TrainingSpecialServiceImpl implements TrainingSpecialService, PaginationPlugin<SimplePaginationUtil> {

    private final DSLContext create;

    @Autowired
    TrainingSpecialServiceImpl(DSLContext dslContext) {
        create = dslContext;
    }

    @Override
    public Result<Record> findAllByPage(SimplePaginationUtil paginationUtil) {
        SelectOnConditionStep<Record> selectOnConditionStep = create.select()
                .from(TRAINING_SPECIAL)
                .leftJoin(FILES)
                .on(TRAINING_SPECIAL.COVER.eq(FILES.FILE_ID));
        return queryAllByPage(selectOnConditionStep, paginationUtil, false);
    }

    @Override
    public int countAll(SimplePaginationUtil paginationUtil) {
        return countAll(create, TRAINING_SPECIAL, paginationUtil, false);
    }

    @Override
    public Condition searchCondition(SimplePaginationUtil paginationUtil) {
        Condition a = null;
        JSONObject search = paginationUtil.getSearch();
        if (Objects.nonNull(search)) {
            String title = StringUtils.trim(search.getString("title"));
            if (StringUtils.isNotBlank(title)) {
                a = TRAINING_SPECIAL.TITLE.like(SQLQueryUtil.likeAllParam(title));
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
            if (StringUtils.equals("releaseTime", orderColumnName)) {
                sortField = new SortField[1];
                if (isAsc) {
                    sortField[0] = TRAINING_SPECIAL.RELEASE_TIME.asc();
                } else {
                    sortField[0] = TRAINING_SPECIAL.RELEASE_TIME.desc();
                }
            }
        }
        sortToFinish(step, sortField);
    }
}
