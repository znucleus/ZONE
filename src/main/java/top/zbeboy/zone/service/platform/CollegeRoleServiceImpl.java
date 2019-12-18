package top.zbeboy.zone.service.platform;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.jooq.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.zbeboy.zone.config.Workbook;
import top.zbeboy.zone.domain.tables.daos.CollegeRoleDao;
import top.zbeboy.zone.domain.tables.pojos.CollegeRole;
import top.zbeboy.zone.domain.tables.pojos.Users;
import top.zbeboy.zone.domain.tables.pojos.UsersType;
import top.zbeboy.zone.service.data.StaffService;
import top.zbeboy.zone.service.data.StudentService;
import top.zbeboy.zone.service.plugin.PaginationPlugin;
import top.zbeboy.zone.service.util.SQLQueryUtil;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.Optional;

import static top.zbeboy.zone.domain.Tables.*;

@Service("collegeRoleService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class CollegeRoleServiceImpl implements CollegeRoleService, PaginationPlugin<DataTablesUtil> {

    private final DSLContext create;

    @Resource
    private CollegeRoleDao collegeRoleDao;

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
    CollegeRoleServiceImpl(DSLContext dslContext) {
        create = dslContext;
    }

    @Override
    public Optional<Record> findByRoleIdRelation(String roleId) {
        return create.select()
                .from(COLLEGE_ROLE)
                .leftJoin(ROLE)
                .on(ROLE.ROLE_ID.eq(COLLEGE_ROLE.ROLE_ID))
                .where(ROLE.ROLE_ID.eq(roleId))
                .fetchOptional();
    }

    @Override
    public Result<Record> findByRoleNameAndCollegeId(String roleName, int collegeId) {
        return create.select()
                .from(COLLEGE_ROLE)
                .leftJoin(ROLE)
                .on(ROLE.ROLE_ID.eq(COLLEGE_ROLE.ROLE_ID))
                .where(ROLE.ROLE_NAME.eq(roleName).and(COLLEGE_ROLE.COLLEGE_ID.eq(collegeId)))
                .fetch();
    }

    @Override
    public Result<Record> findByRoleNameAndCollegeIdNeRoleId(String roleName, int collegeId, String roleId) {
        return create.select()
                .from(COLLEGE_ROLE)
                .leftJoin(ROLE)
                .on(ROLE.ROLE_ID.eq(COLLEGE_ROLE.ROLE_ID))
                .where(ROLE.ROLE_NAME.eq(roleName).and(COLLEGE_ROLE.COLLEGE_ID.eq(collegeId)).and(ROLE.ROLE_ID.ne(roleId)))
                .fetch();
    }

    @Override
    public Result<Record> findAllByPage(DataTablesUtil dataTablesUtil) {
        SelectOnConditionStep<Record> selectOnConditionStep = create.select()
                .from(COLLEGE_ROLE)
                .leftJoin(ROLE)
                .on(ROLE.ROLE_ID.eq(COLLEGE_ROLE.ROLE_ID))
                .leftJoin(COLLEGE)
                .on(COLLEGE_ROLE.COLLEGE_ID.eq(COLLEGE.COLLEGE_ID))
                .leftJoin(SCHOOL)
                .on(COLLEGE.SCHOOL_ID.eq(SCHOOL.SCHOOL_ID));
        return queryAllByPage(selectOnConditionStep, dataTablesUtil, false);
    }

    @Override
    public int countAll(DataTablesUtil dataTablesUtil) {
        SelectOnConditionStep<Record1<Integer>> selectOnConditionStep = create.selectCount()
                .from(COLLEGE_ROLE)
                .leftJoin(ROLE)
                .on(ROLE.ROLE_ID.eq(COLLEGE_ROLE.ROLE_ID))
                .leftJoin(COLLEGE)
                .on(COLLEGE_ROLE.COLLEGE_ID.eq(COLLEGE.COLLEGE_ID))
                .leftJoin(SCHOOL)
                .on(COLLEGE.SCHOOL_ID.eq(SCHOOL.SCHOOL_ID));
        return countAll(selectOnConditionStep, dataTablesUtil, true);
    }

    @Override
    public int countByCondition(DataTablesUtil dataTablesUtil) {
        SelectOnConditionStep<Record1<Integer>> selectOnConditionStep = create.selectCount()
                .from(COLLEGE_ROLE)
                .leftJoin(ROLE)
                .on(ROLE.ROLE_ID.eq(COLLEGE_ROLE.ROLE_ID))
                .leftJoin(COLLEGE)
                .on(COLLEGE_ROLE.COLLEGE_ID.eq(COLLEGE.COLLEGE_ID))
                .leftJoin(SCHOOL)
                .on(COLLEGE.SCHOOL_ID.eq(SCHOOL.SCHOOL_ID));
        return countAll(selectOnConditionStep, dataTablesUtil, false);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void save(CollegeRole collegeRole) {
        collegeRoleDao.insert(collegeRole);
    }

    @Override
    public void deleteByRoleId(String roleId) {
        create.deleteFrom(COLLEGE_ROLE)
                .where(COLLEGE_ROLE.ROLE_ID.eq(roleId))
                .execute();
    }

    @Override
    public Condition searchCondition(DataTablesUtil paginationUtil) {
        Condition a = null;
        JSONObject search = paginationUtil.getSearch();
        if (Objects.nonNull(search)) {
            String roleName = StringUtils.trim(search.getString("roleName"));
            String schoolName = StringUtils.trim(search.getString("schoolName"));
            String collegeName = StringUtils.trim(search.getString("collegeName"));
            if (StringUtils.isNotBlank(roleName)) {
                a = ROLE.ROLE_NAME.like(SQLQueryUtil.likeAllParam(roleName));
            }

            if (StringUtils.isNotBlank(schoolName)) {
                if (Objects.isNull(a)) {
                    a = SCHOOL.SCHOOL_NAME.like(SQLQueryUtil.likeAllParam(schoolName));
                } else {
                    a = a.and(SCHOOL.SCHOOL_NAME.like(SQLQueryUtil.likeAllParam(schoolName)));
                }
            }

            if (StringUtils.isNotBlank(collegeName)) {
                if (Objects.isNull(a)) {
                    a = COLLEGE.COLLEGE_NAME.like(SQLQueryUtil.likeAllParam(collegeName));
                } else {
                    a = a.and(COLLEGE.COLLEGE_NAME.like(SQLQueryUtil.likeAllParam(collegeName)));
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
            if (StringUtils.equals("roleName", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = ROLE.ROLE_NAME.asc();
                    sortField[1] = ROLE.ROLE_ID.asc();
                } else {
                    sortField[0] = ROLE.ROLE_NAME.desc();
                    sortField[1] = ROLE.ROLE_ID.desc();
                }
            }

            if (StringUtils.equals("roleEnName", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = ROLE.ROLE_EN_NAME.asc();
                    sortField[1] = ROLE.ROLE_ID.asc();
                } else {
                    sortField[0] = ROLE.ROLE_EN_NAME.desc();
                    sortField[1] = ROLE.ROLE_ID.desc();
                }
            }

            if (StringUtils.equals("schoolName", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = SCHOOL.SCHOOL_NAME.asc();
                    sortField[1] = ROLE.ROLE_ID.asc();
                } else {
                    sortField[0] = SCHOOL.SCHOOL_NAME.desc();
                    sortField[1] = ROLE.ROLE_ID.desc();
                }
            }

            if (StringUtils.equals("collegeName", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = COLLEGE.COLLEGE_NAME.asc();
                    sortField[1] = ROLE.ROLE_ID.asc();
                } else {
                    sortField[0] = COLLEGE.COLLEGE_NAME.desc();
                    sortField[1] = ROLE.ROLE_ID.desc();
                }
            }
        }
        sortToFinish(step, sortField);
    }

    @Override
    public Condition extraCondition(DataTablesUtil paginationUtil) {
        Condition a = null;
        JSONObject search = paginationUtil.getSearch();
        if (Objects.nonNull(search)) {
            String roleType = StringUtils.trim(search.getString("roleType"));
            if (StringUtils.isNotBlank(roleType)) {
                int roleTypeInt = NumberUtils.toInt(roleType);
                a = ROLE.ROLE_TYPE.eq(roleTypeInt);
            }
        }

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

                if (collegeId != 0) {
                    if (Objects.isNull(a)) {
                        a = COLLEGE.COLLEGE_ID.eq(collegeId);
                    } else {
                        a = a.and(COLLEGE.COLLEGE_ID.eq(collegeId));
                    }
                }
            }
        }
        return a;
    }

}
