package top.zbeboy.zone.service.data;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.jooq.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.zbeboy.zone.config.CacheBook;
import top.zbeboy.zone.config.Workbook;
import top.zbeboy.zone.domain.tables.daos.OrganizeDao;
import top.zbeboy.zone.domain.tables.pojos.Organize;
import top.zbeboy.zone.domain.tables.pojos.Users;
import top.zbeboy.zone.domain.tables.pojos.UsersType;
import top.zbeboy.zone.domain.tables.records.OrganizeRecord;
import top.zbeboy.zone.service.platform.RoleService;
import top.zbeboy.zone.service.platform.UsersService;
import top.zbeboy.zone.service.platform.UsersTypeService;
import top.zbeboy.zone.service.plugin.PaginationPlugin;
import top.zbeboy.zone.service.util.SQLQueryUtil;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.Optional;

import static top.zbeboy.zone.domain.Tables.*;

@Service("organizeService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class OrganizeServiceImpl implements OrganizeService , PaginationPlugin<DataTablesUtil> {

    private final DSLContext create;

    @Resource
    private OrganizeDao organizeDao;

    @Resource
    private RoleService roleService;

    @Resource
    private UsersService usersService;

    @Resource
    private UsersTypeService usersTypeService;

    @Resource
    private StaffService staffService;

    @Resource
    private StudentService studentService;

    @Autowired
    OrganizeServiceImpl(DSLContext dslContext) {
        create = dslContext;
    }

    @Override
    public Organize findById(int id) {
        return organizeDao.findById(id);
    }

    @Cacheable(cacheNames = CacheBook.ORGANIZE, key = "#id")
    @Override
    public Optional<Record> findByIdRelation(int id) {
        return create.select()
                .from(ORGANIZE)
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
                .where(ORGANIZE.ORGANIZE_ID.eq(id))
                .fetchOptional();
    }

    @Cacheable(cacheNames = CacheBook.ORGANIZES, key = "#gradeId + '_' + #organizeIsDel")
    @Override
    public Result<OrganizeRecord> findByGradeIdAndOrganizeIsDel(int gradeId, Byte organizeIsDel) {
        return create.selectFrom(ORGANIZE).where(ORGANIZE.GRADE_ID.eq(gradeId)
                .and(ORGANIZE.ORGANIZE_IS_DEL.eq(organizeIsDel))).fetch();
    }

    @Override
    public Result<Record> findAllByPage(DataTablesUtil dataTablesUtil) {
        SelectOnConditionStep<Record> selectOnConditionStep =
                create.select()
                        .from(ORGANIZE)
                        .join(GRADE)
                        .on(ORGANIZE.GRADE_ID.eq(GRADE.GRADE_ID))
                        .join(SCIENCE)
                        .on(GRADE.SCIENCE_ID.eq(SCIENCE.SCIENCE_ID))
                        .join(DEPARTMENT)
                        .on(SCIENCE.DEPARTMENT_ID.eq(DEPARTMENT.DEPARTMENT_ID))
                        .join(COLLEGE)
                        .on(DEPARTMENT.COLLEGE_ID.eq(COLLEGE.COLLEGE_ID))
                        .join(SCHOOL)
                        .on(COLLEGE.SCHOOL_ID.eq(SCHOOL.SCHOOL_ID));
        return queryAllByPage(selectOnConditionStep, dataTablesUtil, false);
    }

    @Override
    public int countAll(DataTablesUtil dataTablesUtil) {
        SelectOnConditionStep<Record1<Integer>> selectOnConditionStep = create.selectCount()
                .from(ORGANIZE)
                .join(GRADE)
                .on(ORGANIZE.GRADE_ID.eq(GRADE.GRADE_ID))
                .join(SCIENCE)
                .on(GRADE.SCIENCE_ID.eq(SCIENCE.SCIENCE_ID))
                .join(DEPARTMENT)
                .on(SCIENCE.DEPARTMENT_ID.eq(DEPARTMENT.DEPARTMENT_ID))
                .join(COLLEGE)
                .on(DEPARTMENT.COLLEGE_ID.eq(COLLEGE.COLLEGE_ID))
                .join(SCHOOL)
                .on(COLLEGE.SCHOOL_ID.eq(SCHOOL.SCHOOL_ID));
        return countAll(selectOnConditionStep, dataTablesUtil, true);
    }

    @Override
    public int countByCondition(DataTablesUtil dataTablesUtil) {
        SelectOnConditionStep<Record1<Integer>> selectOnConditionStep = create.selectCount()
                .from(ORGANIZE)
                .join(GRADE)
                .on(ORGANIZE.GRADE_ID.eq(GRADE.GRADE_ID))
                .join(SCIENCE)
                .on(GRADE.SCIENCE_ID.eq(SCIENCE.SCIENCE_ID))
                .join(DEPARTMENT)
                .on(SCIENCE.DEPARTMENT_ID.eq(DEPARTMENT.DEPARTMENT_ID))
                .join(COLLEGE)
                .on(DEPARTMENT.COLLEGE_ID.eq(COLLEGE.COLLEGE_ID))
                .join(SCHOOL)
                .on(COLLEGE.SCHOOL_ID.eq(SCHOOL.SCHOOL_ID));
        return countAll(selectOnConditionStep, dataTablesUtil, false);
    }

    @Override
    public Condition searchCondition(DataTablesUtil paginationUtil) {
        Condition a = null;
        JSONObject search = paginationUtil.getSearch();
        if (Objects.nonNull(search)) {
            String schoolName = StringUtils.trim(search.getString("schoolName"));
            String collegeName = StringUtils.trim(search.getString("collegeName"));
            String departmentName = StringUtils.trim(search.getString("departmentName"));
            String scienceName = StringUtils.trim(search.getString("scienceName"));
            String grade = StringUtils.trim(search.getString("grade"));
            String organizeName = StringUtils.trim(search.getString("organizeName"));
            if (StringUtils.isNotBlank(schoolName)) {
                a = SCHOOL.SCHOOL_NAME.like(SQLQueryUtil.likeAllParam(schoolName));
            }

            if (StringUtils.isNotBlank(collegeName)) {
                if (Objects.isNull(a)) {
                    a = COLLEGE.COLLEGE_NAME.like(SQLQueryUtil.likeAllParam(collegeName));
                } else {
                    a = a.and(COLLEGE.COLLEGE_NAME.like(SQLQueryUtil.likeAllParam(collegeName)));
                }
            }

            if (StringUtils.isNotBlank(departmentName)) {
                if (Objects.isNull(a)) {
                    a = DEPARTMENT.DEPARTMENT_NAME.like(SQLQueryUtil.likeAllParam(departmentName));
                } else {
                    a = a.and(DEPARTMENT.DEPARTMENT_NAME.like(SQLQueryUtil.likeAllParam(departmentName)));
                }
            }

            if (StringUtils.isNotBlank(scienceName)) {
                if (Objects.isNull(a)) {
                    a = SCIENCE.SCIENCE_NAME.like(SQLQueryUtil.likeAllParam(scienceName));
                } else {
                    a = a.and(SCIENCE.SCIENCE_NAME.like(SQLQueryUtil.likeAllParam(scienceName)));
                }
            }

            if (StringUtils.isNotBlank(grade)) {
                if (Objects.isNull(a)) {
                    a = GRADE.GRADE_.like(SQLQueryUtil.likeAllParam(grade));
                } else {
                    a = a.and(GRADE.GRADE_.like(SQLQueryUtil.likeAllParam(grade)));
                }
            }

            if (StringUtils.isNotBlank(organizeName)) {
                if (Objects.isNull(a)) {
                    a = ORGANIZE.ORGANIZE_NAME.like(SQLQueryUtil.likeAllParam(organizeName));
                } else {
                    a = a.and(ORGANIZE.ORGANIZE_NAME.like(SQLQueryUtil.likeAllParam(organizeName)));
                }
            }
        }
        return a;
    }

    @Override
    public Condition extraCondition(DataTablesUtil paginationUtil) {
        Condition a = null;

        if (!roleService.isCurrentUserInRole(Workbook.authorities.ROLE_SYSTEM.name())) {
            Users users = usersService.getUserFromSession();
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
        return a;
    }

    @Override
    public void sortCondition(SelectConnectByStep<Record> step, DataTablesUtil paginationUtil) {
        String orderColumnName = paginationUtil.getOrderColumnName();
        String orderDir = paginationUtil.getOrderDir();
        boolean isAsc = StringUtils.equalsIgnoreCase("asc", orderDir);
        SortField[] sortField = null;
        if (StringUtils.isNotBlank(orderColumnName)) {
            if (StringUtils.equals("organizeId", orderColumnName)) {
                sortField = new SortField[1];
                if (isAsc) {
                    sortField[0] = ORGANIZE.ORGANIZE_ID.asc();
                } else {
                    sortField[0] = ORGANIZE.ORGANIZE_ID.desc();
                }
            }

            if (StringUtils.equals("schoolName", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = SCHOOL.SCHOOL_NAME.asc();
                    sortField[1] = ORGANIZE.ORGANIZE_ID.asc();
                } else {
                    sortField[0] = SCHOOL.SCHOOL_NAME.desc();
                    sortField[1] = ORGANIZE.ORGANIZE_ID.desc();
                }
            }

            if (StringUtils.equals("collegeName", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = COLLEGE.COLLEGE_NAME.asc();
                    sortField[1] = ORGANIZE.ORGANIZE_ID.asc();
                } else {
                    sortField[0] = COLLEGE.COLLEGE_NAME.desc();
                    sortField[1] = ORGANIZE.ORGANIZE_ID.desc();
                }
            }

            if (StringUtils.equals("departmentName", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = DEPARTMENT.DEPARTMENT_NAME.asc();
                    sortField[1] = ORGANIZE.ORGANIZE_ID.asc();
                } else {
                    sortField[0] = DEPARTMENT.DEPARTMENT_NAME.desc();
                    sortField[1] = ORGANIZE.ORGANIZE_ID.desc();
                }
            }

            if (StringUtils.equals("scienceName", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = SCIENCE.SCIENCE_NAME.asc();
                    sortField[1] = ORGANIZE.ORGANIZE_ID.asc();
                } else {
                    sortField[0] = SCIENCE.SCIENCE_NAME.desc();
                    sortField[1] = ORGANIZE.ORGANIZE_ID.desc();
                }
            }

            if (StringUtils.equals("grade", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = GRADE.GRADE_.asc();
                    sortField[1] = ORGANIZE.ORGANIZE_ID.asc();
                } else {
                    sortField[0] = GRADE.GRADE_.desc();
                    sortField[1] = ORGANIZE.ORGANIZE_ID.desc();
                }
            }

            if (StringUtils.equals("organizeName", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = ORGANIZE.ORGANIZE_NAME.asc();
                    sortField[1] = ORGANIZE.ORGANIZE_ID.asc();
                } else {
                    sortField[0] = ORGANIZE.ORGANIZE_NAME.desc();
                    sortField[1] = ORGANIZE.ORGANIZE_ID.desc();
                }
            }

            if (StringUtils.equals("organizeIsDel", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = ORGANIZE.ORGANIZE_IS_DEL.asc();
                    sortField[1] = ORGANIZE.ORGANIZE_ID.asc();
                } else {
                    sortField[0] = ORGANIZE.ORGANIZE_IS_DEL.desc();
                    sortField[1] = ORGANIZE.ORGANIZE_ID.desc();
                }
            }
        }
        sortToFinish(step, sortField);
    }
}
