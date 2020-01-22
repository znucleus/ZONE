package top.zbeboy.zone.service.data;

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
import top.zbeboy.zone.domain.tables.daos.DepartmentDao;
import top.zbeboy.zone.domain.tables.pojos.Department;
import top.zbeboy.zone.domain.tables.pojos.Users;
import top.zbeboy.zone.domain.tables.pojos.UsersType;
import top.zbeboy.zone.domain.tables.records.DepartmentRecord;
import top.zbeboy.zone.service.platform.RoleService;
import top.zbeboy.zone.service.platform.UsersService;
import top.zbeboy.zone.service.platform.UsersTypeService;
import top.zbeboy.zone.service.plugin.PaginationPlugin;
import top.zbeboy.zone.service.util.SQLQueryUtil;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static top.zbeboy.zone.domain.Tables.*;

@Service("departmentService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class DepartmentServiceImpl implements DepartmentService, PaginationPlugin<DataTablesUtil> {

    private final DSLContext create;

    @Resource
    private DepartmentDao departmentDao;

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
    DepartmentServiceImpl(DSLContext dslContext) {
        create = dslContext;
    }

    @Override
    public Department findById(int id) {
        return departmentDao.findById(id);
    }

    @Cacheable(cacheNames = CacheBook.DEPARTMENT, key = "#id")
    @Override
    public Optional<Record> findByIdRelation(int id) {
        return create.select()
                .from(DEPARTMENT)
                .join(COLLEGE)
                .on(DEPARTMENT.COLLEGE_ID.eq(COLLEGE.COLLEGE_ID))
                .join(SCHOOL)
                .on(COLLEGE.SCHOOL_ID.eq(SCHOOL.SCHOOL_ID))
                .where(DEPARTMENT.DEPARTMENT_ID.eq(id))
                .fetchOptional();
    }

    @Cacheable(cacheNames = CacheBook.DEPARTMENTS, key = "#collegeId + '_' + #departmentIsDel")
    @Override
    public Result<DepartmentRecord> findByCollegeIdAndDepartmentIsDel(int collegeId, Byte departmentIsDel) {
        return create.selectFrom(DEPARTMENT).where(DEPARTMENT.COLLEGE_ID.eq(collegeId)
                .and(DEPARTMENT.DEPARTMENT_IS_DEL.eq(departmentIsDel))).fetch();
    }

    @Override
    public Result<DepartmentRecord> findByDepartmentNameAndCollegeId(String departmentName, int collegeId) {
        return create.selectFrom(DEPARTMENT)
                .where(DEPARTMENT.DEPARTMENT_NAME.eq(departmentName).and(DEPARTMENT.COLLEGE_ID.eq(collegeId)))
                .fetch();
    }

    @Override
    public Result<DepartmentRecord> findByDepartmentNameAndCollegeIdNeDepartmentId(String departmentName, int collegeId, int departmentId) {
        return create.selectFrom(DEPARTMENT)
                .where(DEPARTMENT.DEPARTMENT_NAME.eq(departmentName).and(DEPARTMENT.DEPARTMENT_ID.ne(departmentId)).and(DEPARTMENT.COLLEGE_ID.eq(collegeId)))
                .fetch();
    }

    @Override
    public Result<Record> findAllByPage(DataTablesUtil dataTablesUtil) {
        SelectOnConditionStep<Record> selectOnConditionStep =
                create.select()
                        .from(DEPARTMENT)
                        .join(COLLEGE)
                        .on(DEPARTMENT.COLLEGE_ID.eq(COLLEGE.COLLEGE_ID))
                        .join(SCHOOL)
                        .on(COLLEGE.SCHOOL_ID.eq(SCHOOL.SCHOOL_ID));
        return queryAllByPage(selectOnConditionStep, dataTablesUtil, false);
    }

    @Override
    public int countAll(DataTablesUtil dataTablesUtil) {
        SelectOnConditionStep<Record1<Integer>> selectOnConditionStep = create.selectCount()
                .from(DEPARTMENT)
                .join(COLLEGE)
                .on(DEPARTMENT.COLLEGE_ID.eq(COLLEGE.COLLEGE_ID))
                .join(SCHOOL)
                .on(COLLEGE.SCHOOL_ID.eq(SCHOOL.SCHOOL_ID));
        return countAll(selectOnConditionStep, dataTablesUtil, true);
    }

    @Override
    public int countByCondition(DataTablesUtil dataTablesUtil) {
        SelectOnConditionStep<Record1<Integer>> selectOnConditionStep = create.selectCount()
                .from(DEPARTMENT)
                .join(COLLEGE)
                .on(DEPARTMENT.COLLEGE_ID.eq(COLLEGE.COLLEGE_ID))
                .join(SCHOOL)
                .on(COLLEGE.SCHOOL_ID.eq(SCHOOL.SCHOOL_ID));
        return countAll(selectOnConditionStep, dataTablesUtil, false);
    }

    @CacheEvict(cacheNames = CacheBook.DEPARTMENTS, allEntries = true)
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void save(Department department) {
        departmentDao.insert(department);
    }

    @CacheEvict(cacheNames = {CacheBook.DEPARTMENT, CacheBook.DEPARTMENTS}, allEntries = true)
    @Override
    public void update(Department department) {
        departmentDao.update(department);
    }

    @CacheEvict(cacheNames = {CacheBook.DEPARTMENT, CacheBook.DEPARTMENTS}, allEntries = true)
    @Override
    public void updateIsDel(List<Integer> ids, Byte isDel) {
        ids.forEach(id -> create.update(DEPARTMENT).set(DEPARTMENT.DEPARTMENT_IS_DEL, isDel).where(DEPARTMENT.DEPARTMENT_ID.eq(id)).execute());
    }

    @Override
    public Condition searchCondition(DataTablesUtil paginationUtil) {
        Condition a = null;
        JSONObject search = paginationUtil.getSearch();
        if (Objects.nonNull(search)) {
            String schoolName = StringUtils.trim(search.getString("schoolName"));
            String collegeName = StringUtils.trim(search.getString("collegeName"));
            String departmentName = StringUtils.trim(search.getString("departmentName"));
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
            if (StringUtils.equals("departmentId", orderColumnName)) {
                sortField = new SortField[1];
                if (isAsc) {
                    sortField[0] = DEPARTMENT.DEPARTMENT_ID.asc();
                } else {
                    sortField[0] = DEPARTMENT.DEPARTMENT_ID.desc();
                }
            }

            if (StringUtils.equals("schoolName", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = SCHOOL.SCHOOL_NAME.asc();
                    sortField[1] = DEPARTMENT.DEPARTMENT_ID.asc();
                } else {
                    sortField[0] = SCHOOL.SCHOOL_NAME.desc();
                    sortField[1] = DEPARTMENT.DEPARTMENT_ID.desc();
                }
            }

            if (StringUtils.equals("collegeName", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = COLLEGE.COLLEGE_NAME.asc();
                    sortField[1] = DEPARTMENT.DEPARTMENT_ID.asc();
                } else {
                    sortField[0] = COLLEGE.COLLEGE_NAME.desc();
                    sortField[1] = DEPARTMENT.DEPARTMENT_ID.desc();
                }
            }

            if (StringUtils.equals("departmentName", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = DEPARTMENT.DEPARTMENT_NAME.asc();
                    sortField[1] = DEPARTMENT.DEPARTMENT_ID.asc();
                } else {
                    sortField[0] = DEPARTMENT.DEPARTMENT_NAME.desc();
                    sortField[1] = DEPARTMENT.DEPARTMENT_ID.desc();
                }
            }

            if (StringUtils.equals("departmentIsDel", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = DEPARTMENT.DEPARTMENT_IS_DEL.asc();
                    sortField[1] = DEPARTMENT.DEPARTMENT_ID.asc();
                } else {
                    sortField[0] = DEPARTMENT.DEPARTMENT_IS_DEL.desc();
                    sortField[1] = DEPARTMENT.DEPARTMENT_ID.desc();
                }
            }
        }
        sortToFinish(step, sortField);
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
}
