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
import top.zbeboy.zone.domain.tables.daos.RoleApplyDao;
import top.zbeboy.zone.domain.tables.pojos.RoleApply;
import top.zbeboy.zone.domain.tables.pojos.Users;
import top.zbeboy.zone.domain.tables.pojos.UsersType;
import top.zbeboy.zone.service.data.StaffService;
import top.zbeboy.zone.service.data.StudentService;
import top.zbeboy.zone.service.plugin.PaginationPlugin;
import top.zbeboy.zone.service.util.SQLQueryUtil;
import top.zbeboy.zone.web.util.ByteUtil;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.Optional;

import static top.zbeboy.zone.domain.Tables.*;

@Service("roleUsersService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class RoleApplyServiceImpl implements RoleApplyService, PaginationPlugin<DataTablesUtil> {

    private final DSLContext create;

    @Resource
    private RoleApplyDao roleApplyDao;

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
    RoleApplyServiceImpl(DSLContext dslContext) {
        create = dslContext;
    }

    @Override
    public RoleApply findById(String id) {
        return roleApplyDao.findById(id);
    }

    @Override
    public Optional<Record> findByIdRelation(String id) {
        return create.select()
                .from(ROLE_APPLY)
                .leftJoin(AUTHORIZE_TYPE)
                .on(ROLE_APPLY.AUTHORIZE_TYPE_ID.eq(AUTHORIZE_TYPE.AUTHORIZE_TYPE_ID))
                .where(ROLE_APPLY.ROLE_APPLY_ID.eq(id))
                .fetchOptional();
    }

    @Override
    public Result<Record> findAllByPage(DataTablesUtil dataTablesUtil) {
        SelectOnConditionStep<Record> selectOnConditionStep = create.select()
                .from(ROLE_APPLY)
                .leftJoin(USERS)
                .on(ROLE_APPLY.USERNAME.eq(USERS.USERNAME))
                .leftJoin(COLLEGE_ROLE)
                .on(COLLEGE_ROLE.ROLE_ID.eq(ROLE_APPLY.ROLE_ID))
                .leftJoin(ROLE)
                .on(COLLEGE_ROLE.ROLE_ID.eq(ROLE.ROLE_ID))
                .leftJoin(AUTHORIZE_TYPE)
                .on(ROLE_APPLY.AUTHORIZE_TYPE_ID.eq(AUTHORIZE_TYPE.AUTHORIZE_TYPE_ID));
        return queryAllByPage(selectOnConditionStep, dataTablesUtil, false);
    }

    @Override
    public int countAll(DataTablesUtil dataTablesUtil) {
        SelectOnConditionStep<Record1<Integer>> selectOnConditionStep = create.selectCount()
                .from(ROLE_APPLY)
                .leftJoin(USERS)
                .on(ROLE_APPLY.USERNAME.eq(USERS.USERNAME))
                .leftJoin(COLLEGE_ROLE)
                .on(COLLEGE_ROLE.ROLE_ID.eq(ROLE_APPLY.ROLE_ID))
                .leftJoin(ROLE)
                .on(COLLEGE_ROLE.ROLE_ID.eq(ROLE.ROLE_ID))
                .leftJoin(AUTHORIZE_TYPE)
                .on(ROLE_APPLY.AUTHORIZE_TYPE_ID.eq(AUTHORIZE_TYPE.AUTHORIZE_TYPE_ID));
        return countAll(selectOnConditionStep, dataTablesUtil, true);
    }

    @Override
    public int countByCondition(DataTablesUtil dataTablesUtil) {
        SelectOnConditionStep<Record1<Integer>> selectOnConditionStep = create.selectCount()
                .from(ROLE_APPLY)
                .leftJoin(USERS)
                .on(ROLE_APPLY.USERNAME.eq(USERS.USERNAME))
                .leftJoin(COLLEGE_ROLE)
                .on(COLLEGE_ROLE.ROLE_ID.eq(ROLE_APPLY.ROLE_ID))
                .leftJoin(ROLE)
                .on(COLLEGE_ROLE.ROLE_ID.eq(ROLE.ROLE_ID))
                .leftJoin(AUTHORIZE_TYPE)
                .on(ROLE_APPLY.AUTHORIZE_TYPE_ID.eq(AUTHORIZE_TYPE.AUTHORIZE_TYPE_ID));
        return countAll(selectOnConditionStep, dataTablesUtil, false);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void save(RoleApply roleApply) {
        roleApplyDao.insert(roleApply);
    }

    @Override
    public void update(RoleApply roleApply) {
        roleApplyDao.update(roleApply);
    }

    @Override
    public void deleteById(String id) {
        roleApplyDao.deleteById(id);
    }

    @Override
    public void deleteByRoleId(String roleId) {
        create.deleteFrom(ROLE_APPLY)
                .where(ROLE_APPLY.ROLE_ID.eq(roleId))
                .execute();
    }

    @Override
    public Condition searchCondition(DataTablesUtil paginationUtil) {
        Condition a = null;
        JSONObject search = paginationUtil.getSearch();
        if (Objects.nonNull(search)) {
            String username = StringUtils.trim(search.getString("username"));
            String realName = StringUtils.trim(search.getString("realName"));
            String roleName = StringUtils.trim(search.getString("roleName"));
            if (StringUtils.isNotBlank(username)) {
                a = ROLE_APPLY.USERNAME.like(SQLQueryUtil.likeAllParam(username));
            }

            if (StringUtils.isNotBlank(realName)) {
                if (Objects.isNull(a)) {
                    a = USERS.REAL_NAME.like(SQLQueryUtil.likeAllParam(realName));
                } else {
                    a = a.and(USERS.REAL_NAME.like(SQLQueryUtil.likeAllParam(realName)));
                }
            }

            if (StringUtils.isNotBlank(roleName)) {
                if (Objects.isNull(a)) {
                    a = ROLE.ROLE_NAME.like(SQLQueryUtil.likeAllParam(roleName));
                } else {
                    a = a.and(ROLE.ROLE_NAME.like(SQLQueryUtil.likeAllParam(roleName)));
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
            if (StringUtils.equals("realName", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = USERS.REAL_NAME.asc();
                    sortField[1] = ROLE_APPLY.ROLE_APPLY_ID.asc();
                } else {
                    sortField[0] = USERS.REAL_NAME.desc();
                    sortField[1] = ROLE_APPLY.ROLE_APPLY_ID.desc();
                }
            }

            if (StringUtils.equals("username", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = ROLE_APPLY.USERNAME.asc();
                    sortField[1] = ROLE_APPLY.ROLE_APPLY_ID.asc();
                } else {
                    sortField[0] = ROLE_APPLY.USERNAME.desc();
                    sortField[1] = ROLE_APPLY.ROLE_APPLY_ID.desc();
                }
            }

            if (StringUtils.equals("authorizeTypeName", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = AUTHORIZE_TYPE.AUTHORIZE_TYPE_NAME.asc();
                    sortField[1] = ROLE_APPLY.ROLE_APPLY_ID.asc();
                } else {
                    sortField[0] = AUTHORIZE_TYPE.AUTHORIZE_TYPE_NAME.desc();
                    sortField[1] = ROLE_APPLY.ROLE_APPLY_ID.desc();
                }
            }

            if (StringUtils.equals("dataScope", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = ROLE_APPLY.DATA_SCOPE.asc();
                    sortField[1] = ROLE_APPLY.ROLE_APPLY_ID.asc();
                } else {
                    sortField[0] = ROLE_APPLY.DATA_SCOPE.desc();
                    sortField[1] = ROLE_APPLY.ROLE_APPLY_ID.desc();
                }
            }

            if (StringUtils.equals("dataName", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = ROLE_APPLY.DATA_ID.asc();
                    sortField[1] = ROLE_APPLY.ROLE_APPLY_ID.asc();
                } else {
                    sortField[0] = ROLE_APPLY.DATA_ID.desc();
                    sortField[1] = ROLE_APPLY.ROLE_APPLY_ID.desc();
                }
            }

            if (StringUtils.equals("roleName", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = ROLE.ROLE_NAME.asc();
                    sortField[1] = ROLE_APPLY.ROLE_APPLY_ID.asc();
                } else {
                    sortField[0] = ROLE.ROLE_NAME.desc();
                    sortField[1] = ROLE_APPLY.ROLE_APPLY_ID.desc();
                }
            }

            if (StringUtils.equals("duration", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = ROLE_APPLY.DURATION.asc();
                    sortField[1] = ROLE_APPLY.ROLE_APPLY_ID.asc();
                } else {
                    sortField[0] = ROLE_APPLY.DURATION.desc();
                    sortField[1] = ROLE_APPLY.ROLE_APPLY_ID.desc();
                }
            }

            if (StringUtils.equals("validDate", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = ROLE_APPLY.VALID_DATE.asc();
                    sortField[1] = ROLE_APPLY.ROLE_APPLY_ID.asc();
                } else {
                    sortField[0] = ROLE_APPLY.VALID_DATE.desc();
                    sortField[1] = ROLE_APPLY.ROLE_APPLY_ID.desc();
                }
            }

            if (StringUtils.equals("expireDate", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = ROLE_APPLY.EXPIRE_DATE.asc();
                    sortField[1] = ROLE_APPLY.ROLE_APPLY_ID.asc();
                } else {
                    sortField[0] = ROLE_APPLY.EXPIRE_DATE.desc();
                    sortField[1] = ROLE_APPLY.ROLE_APPLY_ID.desc();
                }
            }

            if (StringUtils.equals("applyStatus", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = ROLE_APPLY.APPLY_STATUS.asc();
                    sortField[1] = ROLE_APPLY.ROLE_APPLY_ID.asc();
                } else {
                    sortField[0] = ROLE_APPLY.APPLY_STATUS.desc();
                    sortField[1] = ROLE_APPLY.ROLE_APPLY_ID.desc();
                }
            }

            if (StringUtils.equals("createDate", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = ROLE_APPLY.CREATE_DATE.asc();
                    sortField[1] = ROLE_APPLY.ROLE_APPLY_ID.asc();
                } else {
                    sortField[0] = ROLE_APPLY.CREATE_DATE.desc();
                    sortField[1] = ROLE_APPLY.ROLE_APPLY_ID.desc();
                }
            }

            if (StringUtils.equals("reason", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = ROLE_APPLY.REASON.asc();
                    sortField[1] = ROLE_APPLY.ROLE_APPLY_ID.asc();
                } else {
                    sortField[0] = ROLE_APPLY.REASON.desc();
                    sortField[1] = ROLE_APPLY.ROLE_APPLY_ID.desc();
                }
            }

            if (StringUtils.equals("refuse", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = ROLE_APPLY.REFUSE.asc();
                    sortField[1] = ROLE_APPLY.ROLE_APPLY_ID.asc();
                } else {
                    sortField[0] = ROLE_APPLY.REFUSE.desc();
                    sortField[1] = ROLE_APPLY.ROLE_APPLY_ID.desc();
                }
            }
        }
        sortToFinish(step, sortField);
    }

    @Override
    public Condition extraCondition(DataTablesUtil paginationUtil) {
        Condition a = null;
        JSONObject search = paginationUtil.getSearch();

        Users users = usersService.getUserFromSession();
        if (Objects.nonNull(search)) {
            String dataRange = StringUtils.trim(search.getString("dataRange"));
            if (StringUtils.isNotBlank(dataRange)) {
                int dataRangeInt = NumberUtils.toInt(dataRange);
                // 个人
                if (dataRangeInt == 1) {
                    a = ROLE_APPLY.USERNAME.eq(users.getUsername());
                } else if (dataRangeInt == 2) {
                    // 待审核
                    a = ROLE_APPLY.APPLY_STATUS.eq(ByteUtil.toByte(0));
                }
            }
        }

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

                if (collegeId != 0) {
                    if (Objects.isNull(a)) {
                        a = COLLEGE_ROLE.COLLEGE_ID.eq(collegeId);
                    } else {
                        a = a.and(COLLEGE_ROLE.COLLEGE_ID.eq(collegeId));
                    }
                }
            }
        }
        return a;
    }
}
