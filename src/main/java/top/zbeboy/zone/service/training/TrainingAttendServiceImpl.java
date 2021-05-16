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
import top.zbeboy.zbase.domain.tables.daos.TrainingAttendDao;
import top.zbeboy.zbase.domain.tables.pojos.TrainingAttend;
import top.zbeboy.zbase.domain.tables.records.TrainingAttendRecord;
import top.zbeboy.zbase.tools.service.util.DateTimeUtil;
import top.zbeboy.zbase.tools.web.util.pagination.SimplePaginationUtil;
import top.zbeboy.zone.service.plugin.PaginationPlugin;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

import static top.zbeboy.zbase.domain.Tables.*;

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
    public TrainingAttend findById(String id) {
        return trainingAttendDao.findById(id);
    }

    @Override
    public TrainingAttendRecord findByTrainingReleaseIdWithRecentlyAttendDate(String trainingReleaseId) {
        return create.selectFrom(TRAINING_ATTEND)
                .where(TRAINING_ATTEND.TRAINING_RELEASE_ID.eq(trainingReleaseId))
                .orderBy(TRAINING_ATTEND.ATTEND_DATE.desc())
                .limit(1)
                .fetchOne();
    }

    @Cacheable(cacheNames = CacheBook.TRAINING_ATTEND, key = "#id")
    @Override
    public Optional<Record> findByIdRelation(String id) {
        return create.select()
                .from(TRAINING_ATTEND)
                .leftJoin(TRAINING_RELEASE)
                .on(TRAINING_ATTEND.TRAINING_RELEASE_ID.eq(TRAINING_RELEASE.TRAINING_RELEASE_ID))
                .leftJoin(ORGANIZE)
                .on(TRAINING_RELEASE.ORGANIZE_ID.eq(ORGANIZE.ORGANIZE_ID))
                .leftJoin(GRADE)
                .on(ORGANIZE.GRADE_ID.eq(GRADE.GRADE_ID))
                .leftJoin(SCIENCE)
                .on(GRADE.SCIENCE_ID.eq(SCIENCE.SCIENCE_ID))
                .leftJoin(DEPARTMENT)
                .on(SCIENCE.DEPARTMENT_ID.eq(DEPARTMENT.DEPARTMENT_ID))
                .leftJoin(COLLEGE)
                .on(DEPARTMENT.COLLEGE_ID.eq(COLLEGE.COLLEGE_ID))
                .leftJoin(SCHOOLROOM)
                .on(TRAINING_ATTEND.ATTEND_ROOM.eq(SCHOOLROOM.SCHOOLROOM_ID))
                .leftJoin(BUILDING)
                .on(SCHOOLROOM.BUILDING_ID.eq(BUILDING.BUILDING_ID))
                .where(TRAINING_ATTEND.TRAINING_ATTEND_ID.eq(id))
                .fetchOptional();

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

    @CacheEvict(cacheNames = CacheBook.TRAINING_ATTEND, key = "#trainingAttend.trainingAttendId")
    @Override
    public void update(TrainingAttend trainingAttend) {
        trainingAttendDao.update(trainingAttend);
    }

    @CacheEvict(cacheNames = CacheBook.TRAINING_ATTEND, key = "#id")
    @Override
    public void deleteById(String id) {
        trainingAttendDao.deleteById(id);
    }

    @Override
    public Condition searchCondition(SimplePaginationUtil paginationUtil) {
        Condition a = null;
        JSONObject search = paginationUtil.getSearch();
        if (Objects.nonNull(search)) {
            String attendDate = StringUtils.trim(search.getString("attendDate"));
            if (StringUtils.isNotBlank(attendDate)) {
                LocalDate startDate;
                LocalDate endDate;
                if (attendDate.contains("至")) {
                    String[] arr = attendDate.split(" 至 ");
                    startDate = DateTimeUtil.defaultParseLocalDate(arr[0]);
                    endDate = DateTimeUtil.defaultParseLocalDate(arr[1]);
                } else {
                    startDate = DateTimeUtil.defaultParseLocalDate(attendDate);
                    endDate = DateTimeUtil.defaultParseLocalDate(attendDate);
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
            if (StringUtils.equals("attendDate", orderColumnName)) {
                sortField = new SortField[1];
                if (isAsc) {
                    sortField[0] = TRAINING_ATTEND.ATTEND_DATE.asc();
                } else {
                    sortField[0] = TRAINING_ATTEND.ATTEND_DATE.desc();
                }
            }
        }
        sortToFinish(step, sortField);
    }
}
