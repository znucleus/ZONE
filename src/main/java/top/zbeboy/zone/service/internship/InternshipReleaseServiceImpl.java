package top.zbeboy.zone.service.internship;

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
import top.zbeboy.zone.domain.tables.daos.InternshipReleaseDao;
import top.zbeboy.zone.domain.tables.pojos.InternshipRelease;
import top.zbeboy.zone.domain.tables.pojos.Users;
import top.zbeboy.zone.domain.tables.pojos.UsersType;
import top.zbeboy.zone.feign.data.StaffService;
import top.zbeboy.zone.feign.platform.UsersTypeService;
import top.zbeboy.zone.service.data.StudentService;
import top.zbeboy.zone.service.platform.RoleService;
import top.zbeboy.zone.service.platform.UsersService;
import top.zbeboy.zone.service.plugin.PaginationPlugin;
import top.zbeboy.zone.service.util.SQLQueryUtil;
import top.zbeboy.zone.web.bean.data.staff.StaffBean;
import top.zbeboy.zone.web.util.pagination.SimplePaginationUtil;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.Optional;

import static top.zbeboy.zone.domain.Tables.*;

@Service("internshipReleaseService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class InternshipReleaseServiceImpl implements InternshipReleaseService, PaginationPlugin<SimplePaginationUtil> {

    private final DSLContext create;

    @Resource
    private InternshipReleaseDao internshipReleaseDao;

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
    InternshipReleaseServiceImpl(DSLContext dslContext) {
        create = dslContext;
    }

    @Override
    public InternshipRelease findById(String id) {
        return internshipReleaseDao.findById(id);
    }

    @Cacheable(cacheNames = CacheBook.INTERNSHIP_RELEASE, key = "#id")
    @Override
    public Optional<Record> findByIdRelation(String id) {
        return create.select()
                .from(INTERNSHIP_RELEASE)
                .leftJoin(SCIENCE)
                .on(INTERNSHIP_RELEASE.SCIENCE_ID.eq(SCIENCE.SCIENCE_ID))
                .leftJoin(INTERNSHIP_TYPE)
                .on(INTERNSHIP_TYPE.INTERNSHIP_TYPE_ID.eq(INTERNSHIP_RELEASE.INTERNSHIP_TYPE_ID))
                .leftJoin(DEPARTMENT)
                .on(SCIENCE.DEPARTMENT_ID.eq(DEPARTMENT.DEPARTMENT_ID))
                .leftJoin(COLLEGE)
                .on(DEPARTMENT.COLLEGE_ID.eq(COLLEGE.COLLEGE_ID))
                .leftJoin(SCHOOL)
                .on(COLLEGE.SCHOOL_ID.eq(SCHOOL.SCHOOL_ID))
                .where(INTERNSHIP_RELEASE.INTERNSHIP_RELEASE_ID.eq(id))
                .fetchOptional();
    }

    @Override
    public Result<Record> findAllByPage(SimplePaginationUtil paginationUtil) {
        SelectOnConditionStep<Record> selectOnConditionStep = create.select()
                .from(INTERNSHIP_RELEASE)
                .leftJoin(SCIENCE)
                .on(INTERNSHIP_RELEASE.SCIENCE_ID.eq(SCIENCE.SCIENCE_ID))
                .leftJoin(INTERNSHIP_TYPE)
                .on(INTERNSHIP_TYPE.INTERNSHIP_TYPE_ID.eq(INTERNSHIP_RELEASE.INTERNSHIP_TYPE_ID))
                .leftJoin(DEPARTMENT)
                .on(SCIENCE.DEPARTMENT_ID.eq(DEPARTMENT.DEPARTMENT_ID))
                .leftJoin(COLLEGE)
                .on(DEPARTMENT.COLLEGE_ID.eq(COLLEGE.COLLEGE_ID))
                .leftJoin(SCHOOL)
                .on(COLLEGE.SCHOOL_ID.eq(SCHOOL.SCHOOL_ID));
        return queryAllByPage(selectOnConditionStep, paginationUtil, false);
    }

    @Override
    public int countAll(SimplePaginationUtil paginationUtil) {
        SelectOnConditionStep<Record1<Integer>> selectOnConditionStep = create.selectCount()
                .from(INTERNSHIP_RELEASE)
                .leftJoin(SCIENCE)
                .on(INTERNSHIP_RELEASE.SCIENCE_ID.eq(SCIENCE.SCIENCE_ID))
                .leftJoin(INTERNSHIP_TYPE)
                .on(INTERNSHIP_TYPE.INTERNSHIP_TYPE_ID.eq(INTERNSHIP_RELEASE.INTERNSHIP_TYPE_ID))
                .leftJoin(DEPARTMENT)
                .on(SCIENCE.DEPARTMENT_ID.eq(DEPARTMENT.DEPARTMENT_ID))
                .leftJoin(COLLEGE)
                .on(DEPARTMENT.COLLEGE_ID.eq(COLLEGE.COLLEGE_ID))
                .leftJoin(SCHOOL)
                .on(COLLEGE.SCHOOL_ID.eq(SCHOOL.SCHOOL_ID));
        return countAll(selectOnConditionStep, paginationUtil, false);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void save(InternshipRelease internshipRelease) {
        internshipReleaseDao.insert(internshipRelease);
    }

    @CacheEvict(cacheNames = CacheBook.INTERNSHIP_RELEASE, key = "#internshipRelease.internshipReleaseId")
    @Override
    public void update(InternshipRelease internshipRelease) {
        internshipReleaseDao.update(internshipRelease);
    }

    @Override
    public Condition searchCondition(SimplePaginationUtil paginationUtil) {
        Condition a = null;
        JSONObject search = paginationUtil.getSearch();
        if (Objects.nonNull(search)) {
            String internshipTitle = StringUtils.trim(search.getString("internshipTitle"));
            String internshipReleaseIsDel = StringUtils.trim(search.getString("internshipReleaseIsDel"));
            if (StringUtils.isNotBlank(internshipTitle)) {
                a = INTERNSHIP_RELEASE.INTERNSHIP_TITLE.like(SQLQueryUtil.likeAllParam(internshipTitle));
            }

            if (StringUtils.isNotBlank(internshipReleaseIsDel)) {
                if (Objects.isNull(a)) {
                    a = INTERNSHIP_RELEASE.INTERNSHIP_RELEASE_IS_DEL.eq(NumberUtils.toByte(internshipReleaseIsDel));
                } else {
                    a = a.and(INTERNSHIP_RELEASE.INTERNSHIP_RELEASE_IS_DEL.eq(NumberUtils.toByte(internshipReleaseIsDel)));
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
                a = INTERNSHIP_RELEASE.USERNAME.eq(users.getUsername());
                UsersType usersType = usersTypeService.findById(users.getUsersTypeId());
                if (Objects.nonNull(usersType)) {
                    if (StringUtils.equals(Workbook.STAFF_USERS_TYPE, usersType.getUsersTypeName())) {
                        StaffBean bean = staffService.findByUsernameRelation(users.getUsername());
                        if (Objects.nonNull(bean) && bean.getStaffId() > 0) {
                            int departmentId = bean.getDepartmentId();
                            a = a.or(DEPARTMENT.DEPARTMENT_ID.eq(departmentId));
                        }
                    } else if (StringUtils.equals(Workbook.STUDENT_USERS_TYPE, usersType.getUsersTypeName())) {
                        Optional<Record> record = studentService.findByUsernameRelation(users.getUsername());
                        if (record.isPresent()) {
                            int scienceId = record.get().get(SCIENCE.SCIENCE_ID);
                            a = a.or(SCIENCE.SCIENCE_ID.eq(scienceId));
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
                            StaffBean bean = staffService.findByUsernameRelation(users.getUsername());
                            if (Objects.nonNull(bean) && bean.getStaffId() > 0) {
                                collegeId = bean.getCollegeId();
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
                    sortField[0] = INTERNSHIP_RELEASE.RELEASE_TIME.asc();
                } else {
                    sortField[0] = INTERNSHIP_RELEASE.RELEASE_TIME.desc();
                }
            }
        }
        sortToFinish(step, sortField);
    }
}
