package top.zbeboy.zone.service.theory;

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
import top.zbeboy.zbase.domain.tables.daos.TheoryAttendDao;
import top.zbeboy.zbase.domain.tables.pojos.TheoryAttend;
import top.zbeboy.zbase.domain.tables.records.TheoryAttendRecord;
import top.zbeboy.zbase.tools.service.util.DateTimeUtil;
import top.zbeboy.zbase.tools.web.util.pagination.SimplePaginationUtil;
import top.zbeboy.zone.service.plugin.PaginationPlugin;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.Optional;

import static top.zbeboy.zbase.domain.Tables.*;

@Service("theoryAttendService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class TheoryAttendServiceImpl implements TheoryAttendService, PaginationPlugin<SimplePaginationUtil> {

    private final DSLContext create;

    @Resource
    private TheoryAttendDao theoryAttendDao;

    @Autowired
    TheoryAttendServiceImpl(DSLContext dslContext) {
        create = dslContext;
    }

    @Override
    public TheoryAttend findById(String id) {
        return theoryAttendDao.findById(id);
    }

    @Override
    public TheoryAttendRecord findByTheoryReleaseIdWithRecentlyAttendDate(String theoryReleaseId) {
        return create.selectFrom(THEORY_ATTEND)
                .where(THEORY_ATTEND.THEORY_RELEASE_ID.eq(theoryReleaseId))
                .orderBy(THEORY_ATTEND.ATTEND_DATE.desc())
                .limit(1)
                .fetchOne();
    }

    @Cacheable(cacheNames = CacheBook.THEORY_ATTEND, key = "#id")
    @Override
    public Optional<Record> findByIdRelation(String id) {
        return create.select()
                .from(THEORY_ATTEND)
                .leftJoin(THEORY_RELEASE)
                .on(THEORY_ATTEND.THEORY_RELEASE_ID.eq(THEORY_RELEASE.THEORY_RELEASE_ID))
                .leftJoin(ORGANIZE)
                .on(THEORY_RELEASE.ORGANIZE_ID.eq(ORGANIZE.ORGANIZE_ID))
                .leftJoin(GRADE)
                .on(ORGANIZE.GRADE_ID.eq(GRADE.GRADE_ID))
                .leftJoin(SCIENCE)
                .on(GRADE.SCIENCE_ID.eq(SCIENCE.SCIENCE_ID))
                .leftJoin(DEPARTMENT)
                .on(SCIENCE.DEPARTMENT_ID.eq(DEPARTMENT.DEPARTMENT_ID))
                .leftJoin(COLLEGE)
                .on(DEPARTMENT.COLLEGE_ID.eq(COLLEGE.COLLEGE_ID))
                .leftJoin(SCHOOLROOM)
                .on(THEORY_ATTEND.ATTEND_ROOM.eq(SCHOOLROOM.SCHOOLROOM_ID))
                .leftJoin(BUILDING)
                .on(SCHOOLROOM.BUILDING_ID.eq(BUILDING.BUILDING_ID))
                .where(THEORY_ATTEND.THEORY_ATTEND_ID.eq(id))
                .fetchOptional();
    }

    @Override
    public Result<Record> findAllByPage(SimplePaginationUtil paginationUtil) {
        SelectOnConditionStep<Record> selectOnConditionStep = create.select()
                .from(THEORY_ATTEND)
                .leftJoin(SCHOOLROOM)
                .on(THEORY_ATTEND.ATTEND_ROOM.eq(SCHOOLROOM.SCHOOLROOM_ID))
                .leftJoin(BUILDING)
                .on(SCHOOLROOM.BUILDING_ID.eq(BUILDING.BUILDING_ID));
        return queryAllByPage(selectOnConditionStep, paginationUtil, false);
    }

    @Override
    public int countAll(SimplePaginationUtil paginationUtil) {
        SelectOnConditionStep<Record1<Integer>> selectOnConditionStep = create.selectCount()
                .from(THEORY_ATTEND)
                .leftJoin(SCHOOLROOM)
                .on(THEORY_ATTEND.ATTEND_ROOM.eq(SCHOOLROOM.SCHOOLROOM_ID))
                .leftJoin(BUILDING)
                .on(SCHOOLROOM.BUILDING_ID.eq(BUILDING.BUILDING_ID));
        return countAll(selectOnConditionStep, paginationUtil, false);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void save(TheoryAttend theoryAttend) {
        theoryAttendDao.insert(theoryAttend);
    }

    @CacheEvict(cacheNames = CacheBook.THEORY_ATTEND, key = "#theoryAttend.theoryAttendId")
    @Override
    public void update(TheoryAttend theoryAttend) {
        theoryAttendDao.update(theoryAttend);
    }

    @CacheEvict(cacheNames = CacheBook.THEORY_ATTEND, key = "#id")
    @Override
    public void deleteById(String id) {
        theoryAttendDao.deleteById(id);
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

                a = THEORY_ATTEND.ATTEND_DATE.ge(startDate).and(THEORY_ATTEND.ATTEND_DATE.le(endDate));
            }
        }
        return a;
    }

    @Override
    public Condition extraCondition(SimplePaginationUtil paginationUtil) {
        Condition a = null;
        JSONObject search = paginationUtil.getSearch();

        if (Objects.nonNull(search)) {
            String theoryReleaseId = StringUtils.trim(search.getString("theoryReleaseId"));
            if (StringUtils.isNotBlank(theoryReleaseId)) {
                a = THEORY_ATTEND.THEORY_RELEASE_ID.eq(theoryReleaseId);
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
                    sortField[0] = THEORY_ATTEND.ATTEND_DATE.asc();
                } else {
                    sortField[0] = THEORY_ATTEND.ATTEND_DATE.desc();
                }
            }
        }
        sortToFinish(step, sortField);
    }
}
