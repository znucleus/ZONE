package top.zbeboy.zone.service.training;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.jooq.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.zbeboy.zone.domain.tables.daos.TrainingAttendDao;
import top.zbeboy.zone.domain.tables.pojos.TrainingAttend;
import top.zbeboy.zone.service.plugin.PaginationPlugin;
import top.zbeboy.zone.service.util.DateTimeUtil;
import top.zbeboy.zone.web.util.pagination.SimplePaginationUtil;

import javax.annotation.Resource;
import java.util.Objects;

import static top.zbeboy.zone.domain.Tables.*;

@Service("trainingAttendService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class TrainingAttendServiceImpl implements TrainingAttendService, PaginationPlugin<SimplePaginationUtil> {

    private final DSLContext create;

    @Resource
    private TrainingAttendDao trainingAttendDao;

    @Autowired
    TrainingAttendServiceImpl(DSLContext dslContext) {
        create = dslContext;
    }

    @Override
    public Result<Record> findAllByPage(SimplePaginationUtil paginationUtil) {
        SelectOnConditionStep<Record> selectOnConditionStep = create.select()
                .from(TRAINING_ATTEND)
                .leftJoin(SCHOOLROOM)
                .on(TRAINING_ATTEND.ATTEND_ROOM.eq(SCHOOLROOM.SCHOOLROOM_ID))
                .leftJoin(BUILDING)
                .on(SCHOOLROOM.BUILDING_ID.eq(BUILDING.BUILDING_ID));
        return queryAllByPage(selectOnConditionStep, paginationUtil, false);
    }

    @Override
    public int countAll(SimplePaginationUtil paginationUtil) {
        SelectOnConditionStep<Record1<Integer>> selectOnConditionStep = create.selectCount()
                .from(TRAINING_ATTEND)
                .leftJoin(SCHOOLROOM)
                .on(TRAINING_ATTEND.ATTEND_ROOM.eq(SCHOOLROOM.SCHOOLROOM_ID))
                .leftJoin(BUILDING)
                .on(SCHOOLROOM.BUILDING_ID.eq(BUILDING.BUILDING_ID));
        return countAll(selectOnConditionStep, paginationUtil, false);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void save(TrainingAttend trainingAttend) {
        trainingAttendDao.insert(trainingAttend);
    }

    @Override
    public Condition searchCondition(SimplePaginationUtil paginationUtil) {
        Condition a = null;
        JSONObject search = paginationUtil.getSearch();
        if (Objects.nonNull(search)) {
            String attendDate = StringUtils.trim(search.getString("attendDate"));
            if (StringUtils.isNotBlank(attendDate)) {
                java.sql.Date startDate;
                java.sql.Date endDate;
                if (attendDate.contains("至")) {
                    String[] arr = attendDate.split(" 至 ");
                    startDate = DateTimeUtil.defaultParseSqlDate(arr[0]);
                    endDate = DateTimeUtil.defaultParseSqlDate(arr[1]);
                } else {
                    startDate = DateTimeUtil.defaultParseSqlDate(attendDate);
                    endDate = DateTimeUtil.defaultParseSqlDate(attendDate);
                }

                a = TRAINING_ATTEND.ATTEND_DATE.ge(startDate).and(TRAINING_ATTEND.ATTEND_DATE.le(endDate));
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
                a = TRAINING_ATTEND.TRAINING_RELEASE_ID.eq(trainingReleaseId);
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
            if (StringUtils.equals("publishDate", orderColumnName)) {
                sortField = new SortField[1];
                if (isAsc) {
                    sortField[0] = TRAINING_ATTEND.PUBLISH_DATE.asc();
                } else {
                    sortField[0] = TRAINING_ATTEND.PUBLISH_DATE.desc();
                }
            }
        }
        sortToFinish(step, sortField);
    }
}
