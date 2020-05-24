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
import top.zbeboy.zone.config.CacheBook;
import top.zbeboy.zone.config.Workbook;
import top.zbeboy.zone.domain.tables.daos.TrainingReleaseDao;
import top.zbeboy.zone.domain.tables.pojos.TrainingRelease;
import top.zbeboy.zone.domain.tables.pojos.Users;
import top.zbeboy.zone.domain.tables.pojos.UsersType;
import top.zbeboy.zone.feign.platform.UsersTypeService;
import top.zbeboy.zone.service.data.StaffService;
import top.zbeboy.zone.service.data.StudentService;
import top.zbeboy.zone.service.platform.RoleService;
import top.zbeboy.zone.service.platform.UsersService;
import top.zbeboy.zone.service.plugin.PaginationPlugin;
import top.zbeboy.zone.service.util.SQLQueryUtil;
import top.zbeboy.zone.web.util.pagination.SimplePaginationUtil;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.Optional;

import static top.zbeboy.zone.domain.Tables.*;

@Service("trainingReleaseService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class TrainingReleaseServiceImpl implements TrainingReleaseService, PaginationPlugin<SimplePaginationUtil> {

    private final DSLContext create;

    @Resource
    private TrainingReleaseDao trainingReleaseDao;

    @Resource
    private RoleService roleService;

    @Resource
    private UsersService usersService;

    @Resource
    private UsersTypeService usersTypeService;

    @Resource
    private StudentService studentService;

    @Resource
    private StaffService staffService;

    @Autowired
    TrainingReleaseServiceImpl(DSLContext dslContext) {
        create = dslContext;
    }

    @Override
    public TrainingRelease findById(String id) {
        return trainingReleaseDao.findById(id);
    }

    @Cacheable(cacheNames = CacheBook.TRAINING_RELEASE, key = "#id")
    @Override
    public Optional<Record> findByIdRelation(String id) {
        return create.select()
                .from(TRAINING_RELEASE)
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
                .leftJoin(SCHOOL)
                .on(COLLEGE.SCHOOL_ID.eq(SCHOOL.SCHOOL_ID))
                .leftJoin(COURSE)
                .on(TRAINING_RELEASE.COURSE_ID.eq(COURSE.COURSE_ID))
                .where(TRAINING_RELEASE.TRAINING_RELEASE_ID.eq(id))
                .fetchOptional();
    }

    @Override
    public Result<Record> findAllByPage(SimplePaginationUtil paginationUtil) {
        SelectOnConditionStep<Record> selectOnConditionStep = create.select()
                .from(TRAINING_RELEASE)
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
                .leftJoin(SCHOOL)
                .on(COLLEGE.SCHOOL_ID.eq(SCHOOL.SCHOOL_ID))
                .leftJoin(COURSE)
                .on(TRAINING_RELEASE.COURSE_ID.eq(COURSE.COURSE_ID));
        return queryAllByPage(selectOnConditionStep, paginationUtil, false);
    }

    @Override
    public int countAll(SimplePaginationUtil paginationUtil) {
        SelectOnConditionStep<Record1<Integer>> selectOnConditionStep = create.selectCount()
                .from(TRAINING_RELEASE)
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
                .leftJoin(SCHOOL)
                .on(COLLEGE.SCHOOL_ID.eq(SCHOOL.SCHOOL_ID))
                .leftJoin(COURSE)
                .on(TRAINING_RELEASE.COURSE_ID.eq(COURSE.COURSE_ID));
        return countAll(selectOnConditionStep, paginationUtil, false);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void save(TrainingRelease trainingRelease) {
        trainingReleaseDao.insert(trainingRelease);
    }

    @CacheEvict(cacheNames = CacheBook.TRAINING_RELEASE, key = "#trainingRelease.trainingReleaseId")
    @Override
    public void update(TrainingRelease trainingRelease) {
        trainingReleaseDao.update(trainingRelease);
    }

    @CacheEvict(cacheNames = CacheBook.TRAINING_RELEASE, key = "#id")
    @Override
    public void deleteById(String id) {
        trainingReleaseDao.deleteById(id);
    }

    @Override
    public Condition searchCondition(SimplePaginationUtil paginationUtil) {
        Condition a = null;
        JSONObject search = paginationUtil.getSearch();
        if (Objects.nonNull(search)) {
            String title = StringUtils.trim(search.getString("title"));
            String publisher = StringUtils.trim(search.getString("publisher"));
            if (StringUtils.isNotBlank(title)) {
                a = TRAINING_RELEASE.TITLE.like(SQLQueryUtil.likeAllParam(title));
            }

            if (StringUtils.isNotBlank(publisher)) {
                if (Objects.isNull(a)) {
                    a = TRAINING_RELEASE.PUBLISHER.like(SQLQueryUtil.likeAllParam(publisher));
                } else {
                    a = a.and(TRAINING_RELEASE.PUBLISHER.like(SQLQueryUtil.likeAllParam(publisher)));
                }
            }
        }
        return a;
    }

    @Override
    public Condition extraCondition(SimplePaginationUtil paginationUtil) {
        Condition a = null;
        JSONObject search = paginationUtil.getSearch();

        Users users = usersService.getUserFromSession();
        if (Objects.nonNull(search)) {
            String dataRange = StringUtils.trim(search.getString("dataRange"));
            if (StringUtils.isBlank(dataRange)) {
                dataRange = "0";// 默认全部
            }

            int dataRangeInt = NumberUtils.toInt(dataRange);

            // 查看与本人相关
            if (dataRangeInt == 1) {
                a = TRAINING_RELEASE.USERNAME.eq(users.getUsername());
                UsersType usersType = usersTypeService.findById(users.getUsersTypeId());
                if (Objects.nonNull(usersType)) {
                    if (StringUtils.equals(Workbook.STAFF_USERS_TYPE, usersType.getUsersTypeName())) {
                        Optional<Record> record = staffService.findByUsernameRelation(users.getUsername());
                        if (record.isPresent()) {
                            int departmentId = record.get().get(DEPARTMENT.DEPARTMENT_ID);
                            a = a.or(DEPARTMENT.DEPARTMENT_ID.eq(departmentId));
                        }
                    } else if (StringUtils.equals(Workbook.STUDENT_USERS_TYPE, usersType.getUsersTypeName())) {
                        Optional<Record> record = studentService.findByUsernameRelation(users.getUsername());
                        if (record.isPresent()) {
                            int organizeId = record.get().get(ORGANIZE.ORGANIZE_ID);
                            a = a.or(ORGANIZE.ORGANIZE_ID.eq(organizeId));
                        }
                    }
                }
            } else {
                // 非系统，查看全院
                if (!roleService.isCurrentUserInRole(Workbook.authorities.ROLE_SYSTEM.name())) {
                    UsersType usersType = usersTypeService.findById(users.getUsersTypeId());
                    if (Objects.nonNull(usersType)) {
                        int collegeId = 0;
                        if (StringUtils.equals(Workbook.STAFF_USERS_TYPE, usersType.getUsersTypeName())) {
                            Optional<Record> record = staffService.findByUsernameRelation(users.getUsername());
                            if (record.isPresent()) {
                                collegeId = record.get().get(COLLEGE.COLLEGE_ID);
                            }
                        } else if (StringUtils.equals(Workbook.STUDENT_USERS_TYPE, usersType.getUsersTypeName())) {
                            Optional<Record> record = studentService.findByUsernameRelation(users.getUsername());
                            if (record.isPresent()) {
                                collegeId = record.get().get(COLLEGE.COLLEGE_ID);
                            }
                        }

                        a = COLLEGE.COLLEGE_ID.eq(collegeId);
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
                    sortField[0] = TRAINING_RELEASE.RELEASE_TIME.asc();
                } else {
                    sortField[0] = TRAINING_RELEASE.RELEASE_TIME.desc();
                }
            }
        }
        sortToFinish(step, sortField);
    }
}
