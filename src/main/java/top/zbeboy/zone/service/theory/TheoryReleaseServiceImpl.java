package top.zbeboy.zone.service.theory;

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
import top.zbeboy.zbase.bean.data.staff.StaffBean;
import top.zbeboy.zbase.bean.data.student.StudentBean;
import top.zbeboy.zbase.config.CacheBook;
import top.zbeboy.zbase.config.Workbook;
import top.zbeboy.zbase.domain.tables.daos.TheoryReleaseDao;
import top.zbeboy.zbase.domain.tables.pojos.TheoryRelease;
import top.zbeboy.zbase.domain.tables.pojos.Users;
import top.zbeboy.zbase.domain.tables.pojos.UsersType;
import top.zbeboy.zbase.feign.data.StaffService;
import top.zbeboy.zbase.feign.data.StudentService;
import top.zbeboy.zbase.feign.platform.RoleService;
import top.zbeboy.zbase.feign.platform.UsersTypeService;
import top.zbeboy.zbase.tools.service.util.SQLQueryUtil;
import top.zbeboy.zbase.tools.web.util.pagination.SimplePaginationUtil;
import top.zbeboy.zone.service.plugin.PaginationPlugin;
import top.zbeboy.zone.web.util.SessionUtil;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.Optional;

import static top.zbeboy.zbase.domain.Tables.*;

@Service("theoryReleaseService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class TheoryReleaseServiceImpl implements TheoryReleaseService, PaginationPlugin<SimplePaginationUtil> {

    private final DSLContext create;

    @Resource
    private TheoryReleaseDao theoryReleaseDao;

    @Resource
    private UsersTypeService usersTypeService;

    @Resource
    private StudentService studentService;

    @Resource
    private StaffService staffService;

    @Resource
    private RoleService roleService;

    @Autowired
    TheoryReleaseServiceImpl(DSLContext dslContext) {
        create = dslContext;
    }

    @Cacheable(cacheNames = CacheBook.THEORY_RELEASE, key = "#id")
    @Override
    public TheoryRelease findById(String id) {
        return theoryReleaseDao.findById(id);
    }

    @Override
    public Optional<Record> findByIdRelation(String id) {
        return create.select()
                .from(THEORY_RELEASE)
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
                .leftJoin(SCHOOL)
                .on(COLLEGE.SCHOOL_ID.eq(SCHOOL.SCHOOL_ID))
                .leftJoin(COURSE)
                .on(THEORY_RELEASE.COURSE_ID.eq(COURSE.COURSE_ID))
                .where(THEORY_RELEASE.THEORY_RELEASE_ID.eq(id))
                .fetchOptional();
    }

    @Override
    public Result<Record> findAllByPage(SimplePaginationUtil paginationUtil) {
        SelectOnConditionStep<Record> selectOnConditionStep = create.select()
                .from(THEORY_RELEASE)
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
                .leftJoin(SCHOOL)
                .on(COLLEGE.SCHOOL_ID.eq(SCHOOL.SCHOOL_ID))
                .leftJoin(COURSE)
                .on(THEORY_RELEASE.COURSE_ID.eq(COURSE.COURSE_ID));
        return queryAllByPage(selectOnConditionStep, paginationUtil, false);
    }

    @Override
    public int countAll(SimplePaginationUtil paginationUtil) {
        SelectOnConditionStep<Record1<Integer>> selectOnConditionStep = create.selectCount()
                .from(THEORY_RELEASE)
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
                .leftJoin(SCHOOL)
                .on(COLLEGE.SCHOOL_ID.eq(SCHOOL.SCHOOL_ID))
                .leftJoin(COURSE)
                .on(THEORY_RELEASE.COURSE_ID.eq(COURSE.COURSE_ID));
        return countAll(selectOnConditionStep, paginationUtil, false);
    }

    @CacheEvict(cacheNames = CacheBook.THEORY_RELEASE, key = "#theoryRelease.theoryReleaseId")
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void save(TheoryRelease theoryRelease) {
        theoryReleaseDao.insert(theoryRelease);
    }

    @CacheEvict(cacheNames = CacheBook.THEORY_RELEASE, key = "#theoryRelease.theoryReleaseId")
    @Override
    public void update(TheoryRelease theoryRelease) {
        theoryReleaseDao.update(theoryRelease);
    }

    @CacheEvict(cacheNames = CacheBook.THEORY_RELEASE, key = "#id")
    @Override
    public void deleteById(String id) {
        theoryReleaseDao.deleteById(id);
    }

    @Override
    public Condition searchCondition(SimplePaginationUtil paginationUtil) {
        Condition a = null;
        JSONObject search = paginationUtil.getSearch();
        if (Objects.nonNull(search)) {
            String title = StringUtils.trim(search.getString("title"));
            String publisher = StringUtils.trim(search.getString("publisher"));
            if (StringUtils.isNotBlank(title)) {
                a = THEORY_RELEASE.TITLE.like(SQLQueryUtil.likeAllParam(title));
            }

            if (StringUtils.isNotBlank(publisher)) {
                if (Objects.isNull(a)) {
                    a = THEORY_RELEASE.PUBLISHER.like(SQLQueryUtil.likeAllParam(publisher));
                } else {
                    a = a.and(THEORY_RELEASE.PUBLISHER.like(SQLQueryUtil.likeAllParam(publisher)));
                }
            }
        }
        return a;
    }

    @Override
    public Condition extraCondition(SimplePaginationUtil paginationUtil) {
        Condition a = null;
        JSONObject search = paginationUtil.getSearch();

        Users users = SessionUtil.getUserFromSession();
        if (Objects.nonNull(search)) {
            String dataRange = StringUtils.trim(search.getString("dataRange"));
            if (StringUtils.isBlank(dataRange)) {
                dataRange = "0";// 默认全部
            }

            int dataRangeInt = NumberUtils.toInt(dataRange);

            // 查看与本人相关
            if (dataRangeInt == 1) {
                a = THEORY_RELEASE.USERNAME.eq(users.getUsername());
                Optional<UsersType> optionalUsersType = usersTypeService.findById(users.getUsersTypeId());
                if (optionalUsersType.isPresent()) {
                    UsersType usersType = optionalUsersType.get();
                    if (StringUtils.equals(Workbook.STAFF_USERS_TYPE, usersType.getUsersTypeName())) {
                        Optional<StaffBean> optionalStaffBean = staffService.findByUsernameRelation(users.getUsername());
                        if (optionalStaffBean.isPresent()) {
                            int departmentId = optionalStaffBean.get().getDepartmentId();
                            a = a.or(DEPARTMENT.DEPARTMENT_ID.eq(departmentId));
                        }
                    } else if (StringUtils.equals(Workbook.STUDENT_USERS_TYPE, usersType.getUsersTypeName())) {
                        Optional<StudentBean> optionalStudentBean = studentService.findByUsernameRelation(users.getUsername());
                        if (optionalStudentBean.isPresent()) {
                            int organizeId = optionalStudentBean.get().getOrganizeId();
                            a = a.or(ORGANIZE.ORGANIZE_ID.eq(organizeId));
                        }
                    }
                }
            } else {
                // 非系统，查看全院
                if (!roleService.isCurrentUserInRole(users.getUsername(), Workbook.authorities.ROLE_SYSTEM.name())) {
                    Optional<UsersType> optionalUsersType = usersTypeService.findById(users.getUsersTypeId());
                    if (optionalUsersType.isPresent()) {
                        UsersType usersType = optionalUsersType.get();
                        int collegeId = 0;
                        if (StringUtils.equals(Workbook.STAFF_USERS_TYPE, usersType.getUsersTypeName())) {
                            Optional<StaffBean> optionalStaffBean = staffService.findByUsernameRelation(users.getUsername());
                            if (optionalStaffBean.isPresent()) {
                                collegeId = optionalStaffBean.get().getCollegeId();
                            }
                        } else if (StringUtils.equals(Workbook.STUDENT_USERS_TYPE, usersType.getUsersTypeName())) {
                            Optional<StudentBean> optionalStudentBean = studentService.findByUsernameRelation(users.getUsername());
                            if (optionalStudentBean.isPresent()) {
                                collegeId = optionalStudentBean.get().getCollegeId();
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
                    sortField[0] = THEORY_RELEASE.RELEASE_TIME.asc();
                } else {
                    sortField[0] = THEORY_RELEASE.RELEASE_TIME.desc();
                }
            }
        }
        sortToFinish(step, sortField);
    }
}
