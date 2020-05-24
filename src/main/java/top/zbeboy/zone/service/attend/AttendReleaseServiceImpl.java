package top.zbeboy.zone.service.attend;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.jooq.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.zbeboy.zone.config.Workbook;
import top.zbeboy.zone.domain.tables.daos.AttendReleaseDao;
import top.zbeboy.zone.domain.tables.pojos.AttendRelease;
import top.zbeboy.zone.domain.tables.pojos.Users;
import top.zbeboy.zone.domain.tables.pojos.UsersType;
import top.zbeboy.zone.domain.tables.records.AttendReleaseRecord;
import top.zbeboy.zone.feign.platform.UsersTypeService;
import top.zbeboy.zone.service.data.StudentService;
import top.zbeboy.zone.service.platform.RoleService;
import top.zbeboy.zone.service.platform.UsersService;
import top.zbeboy.zone.service.plugin.PaginationPlugin;
import top.zbeboy.zone.service.util.SQLQueryUtil;
import top.zbeboy.zone.web.util.ByteUtil;
import top.zbeboy.zone.web.util.pagination.SimplePaginationUtil;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.Optional;

import static org.jooq.impl.DSL.now;
import static top.zbeboy.zone.domain.Tables.*;

@Service("attendReleaseService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class AttendReleaseServiceImpl implements AttendReleaseService, PaginationPlugin<SimplePaginationUtil> {

    private final DSLContext create;

    @Resource
    private AttendReleaseDao attendReleaseDao;

    @Resource
    private RoleService roleService;

    @Resource
    private UsersService usersService;

    @Resource
    private UsersTypeService usersTypeService;

    @Resource
    private StudentService studentService;

    @Autowired
    AttendReleaseServiceImpl(DSLContext dslContext) {
        create = dslContext;
    }

    @Override
    public AttendRelease findById(String id) {
        return attendReleaseDao.findById(id);
    }

    @Override
    public Result<AttendReleaseRecord> findIsAuto() {
        return create.selectFrom(ATTEND_RELEASE)
                .where(ATTEND_RELEASE.IS_AUTO.eq(ByteUtil.toByte(1))
                        .and(ATTEND_RELEASE.VALID_DATE.le(now()))
                        .and(ATTEND_RELEASE.EXPIRE_DATE.gt(now()))
                        .and(ATTEND_RELEASE.EXPIRE_DATE.gt(ATTEND_RELEASE.VALID_DATE)))
                .fetch();
    }

    @Override
    public Result<Record> findAllByPage(SimplePaginationUtil paginationUtil) {
        SelectOnConditionStep<Record> selectOnConditionStep = create.select()
                .from(ATTEND_RELEASE)
                .leftJoin(ORGANIZE)
                .on(ATTEND_RELEASE.ORGANIZE_ID.eq(ORGANIZE.ORGANIZE_ID))
                .join(GRADE)
                .on(ORGANIZE.GRADE_ID.eq(GRADE.GRADE_ID))
                .join(SCIENCE)
                .on(GRADE.SCIENCE_ID.eq(SCIENCE.SCIENCE_ID))
                .join(DEPARTMENT)
                .on(SCIENCE.DEPARTMENT_ID.eq(DEPARTMENT.DEPARTMENT_ID))
                .join(COLLEGE)
                .on(DEPARTMENT.COLLEGE_ID.eq(COLLEGE.COLLEGE_ID))
                .join(SCHOOL)
                .on(COLLEGE.SCHOOL_ID.eq(SCHOOL.SCHOOL_ID))
                .leftJoin(USERS)
                .on(ATTEND_RELEASE.USERNAME.eq(USERS.USERNAME));
        return queryAllByPage(selectOnConditionStep, paginationUtil, false);
    }

    @Override
    public int countAll(SimplePaginationUtil paginationUtil) {
        SelectOnConditionStep<Record1<Integer>> selectOnConditionStep = create.selectCount()
                .from(ATTEND_RELEASE)
                .leftJoin(ORGANIZE)
                .on(ATTEND_RELEASE.ORGANIZE_ID.eq(ORGANIZE.ORGANIZE_ID))
                .join(GRADE)
                .on(ORGANIZE.GRADE_ID.eq(GRADE.GRADE_ID))
                .join(SCIENCE)
                .on(GRADE.SCIENCE_ID.eq(SCIENCE.SCIENCE_ID))
                .join(DEPARTMENT)
                .on(SCIENCE.DEPARTMENT_ID.eq(DEPARTMENT.DEPARTMENT_ID))
                .join(COLLEGE)
                .on(DEPARTMENT.COLLEGE_ID.eq(COLLEGE.COLLEGE_ID))
                .join(SCHOOL)
                .on(COLLEGE.SCHOOL_ID.eq(SCHOOL.SCHOOL_ID))
                .leftJoin(USERS)
                .on(ATTEND_RELEASE.USERNAME.eq(USERS.USERNAME));
        return countAll(selectOnConditionStep, paginationUtil, false);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void save(AttendRelease attendRelease) {
        attendReleaseDao.insert(attendRelease);
    }

    @Override
    public void update(AttendRelease attendRelease) {
        attendReleaseDao.update(attendRelease);
    }

    @Override
    public Condition searchCondition(SimplePaginationUtil paginationUtil) {
        Condition a = null;
        JSONObject search = paginationUtil.getSearch();
        if (Objects.nonNull(search)) {
            String title = StringUtils.trim(search.getString("title"));
            if (StringUtils.isNotBlank(title)) {
                a = ATTEND_RELEASE.TITLE.like(SQLQueryUtil.likeAllParam(title));
            }
        }
        return a;
    }

    @Override
    public Condition extraCondition(SimplePaginationUtil paginationUtil) {
        Condition a = null;
        JSONObject search = paginationUtil.getSearch();

        Users users = usersService.getUserFromOauth(paginationUtil.getPrincipal());
        if (Objects.nonNull(search)) {
            String dataRange = StringUtils.trim(search.getString("dataRange"));
            if (StringUtils.isBlank(dataRange)) {
                dataRange = "1";// 默认个人
            }

            if (!roleService.isCurrentUserInRole(Workbook.authorities.ROLE_SYSTEM.name())) {
                UsersType usersType = usersTypeService.findById(users.getUsersTypeId());
                if (Objects.nonNull(usersType)) {
                    int dataRangeInt = NumberUtils.toInt(dataRange);
                    // 个人
                    if (dataRangeInt == 1) {
                        if (StringUtils.equals(Workbook.STUDENT_USERS_TYPE, usersType.getUsersTypeName())) {
                            // 学生查看可签到本班级数据
                            Optional<Record> record = studentService.findByUsernameRelation(users.getUsername());
                            if (record.isPresent()) {
                                int organizeId = record.get().get(ORGANIZE.ORGANIZE_ID);
                                a = ATTEND_RELEASE.ORGANIZE_ID.eq(organizeId)
                                        .and(ATTEND_RELEASE.ATTEND_END_TIME.gt(now()))
                                        .and(ATTEND_RELEASE.ATTEND_END_TIME.gt(ATTEND_RELEASE.ATTEND_START_TIME));
                            }
                        } else if (StringUtils.equals(Workbook.STAFF_USERS_TYPE, usersType.getUsersTypeName())) {
                            a = ATTEND_RELEASE.USERNAME.eq(users.getUsername());
                        }
                    }
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
            if (StringUtils.equals("releaseTime", orderColumnName)) {
                sortField = new SortField[1];
                if (isAsc) {
                    sortField[0] = ATTEND_RELEASE.RELEASE_TIME.asc();
                } else {
                    sortField[0] = ATTEND_RELEASE.RELEASE_TIME.desc();
                }
            }
        }
        sortToFinish(step, sortField);
    }
}
