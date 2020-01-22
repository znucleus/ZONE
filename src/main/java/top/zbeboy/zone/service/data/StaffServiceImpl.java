package top.zbeboy.zone.service.data;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.zbeboy.zone.config.CacheBook;
import top.zbeboy.zone.config.Workbook;
import top.zbeboy.zone.domain.tables.daos.StaffDao;
import top.zbeboy.zone.domain.tables.pojos.Staff;
import top.zbeboy.zone.domain.tables.pojos.Users;
import top.zbeboy.zone.domain.tables.pojos.UsersType;
import top.zbeboy.zone.domain.tables.records.AuthoritiesRecord;
import top.zbeboy.zone.domain.tables.records.StaffRecord;
import top.zbeboy.zone.service.platform.RoleService;
import top.zbeboy.zone.service.platform.UsersService;
import top.zbeboy.zone.service.platform.UsersTypeService;
import top.zbeboy.zone.service.plugin.PaginationPlugin;
import top.zbeboy.zone.service.util.BCryptUtil;
import top.zbeboy.zone.service.util.SQLQueryUtil;
import top.zbeboy.zone.web.util.BooleanUtil;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;
import top.zbeboy.zone.web.vo.data.staff.StaffAddVo;

import javax.annotation.Resource;
import java.sql.Date;
import java.util.Objects;
import java.util.Optional;

import static top.zbeboy.zone.domain.Tables.*;

@Service("staffService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class StaffServiceImpl implements StaffService, PaginationPlugin<DataTablesUtil> {

    private final DSLContext create;

    @Resource
    private StaffDao staffDao;

    @Resource
    private UsersService usersService;

    @Resource
    private RoleService roleService;

    @Resource
    private UsersTypeService usersTypeService;

    @Resource
    private StaffService staffService;

    @Resource
    private StudentService studentService;

    @Autowired
    StaffServiceImpl(DSLContext dslContext) {
        create = dslContext;
    }

    @Override
    public Optional<StaffRecord> findByUsername(String username) {
        return create.selectFrom(STAFF)
                .where(STAFF.USERNAME.eq(username))
                .fetchOptional();
    }

    @Cacheable(cacheNames = CacheBook.STAFF, key = "#username")
    @Override
    public Optional<Record> findByUsernameRelation(String username) {
        return create.select()
                .from(STAFF)
                .join(DEPARTMENT)
                .on(STAFF.DEPARTMENT_ID.eq(DEPARTMENT.DEPARTMENT_ID))
                .join(COLLEGE)
                .on(DEPARTMENT.COLLEGE_ID.eq(COLLEGE.COLLEGE_ID))
                .join(SCHOOL)
                .on(COLLEGE.SCHOOL_ID.eq(SCHOOL.SCHOOL_ID))
                .where(STAFF.USERNAME.eq(username))
                .fetchOptional();
    }

    @Override
    public Result<Record> findAdmin(String authority, int collegeId) {
        return create.select()
                .from(STAFF)
                .join(USERS)
                .on(STAFF.USERNAME.eq(USERS.USERNAME))
                .join(AUTHORITIES)
                .on(STAFF.USERNAME.eq(AUTHORITIES.USERNAME))
                .join(DEPARTMENT)
                .on(STAFF.DEPARTMENT_ID.eq(DEPARTMENT.DEPARTMENT_ID))
                .where(AUTHORITIES.AUTHORITY.eq(authority).and(DEPARTMENT.COLLEGE_ID.eq(collegeId)))
                .fetch();
    }

    @Override
    public Staff findByStaffNumber(String staffNumber) {
        return staffDao.fetchOneByStaffNumber(staffNumber);
    }

    @Override
    public Result<StaffRecord> findByStaffNumberNeUsername(String staffNumber, String username) {
        return create.selectFrom(STAFF)
                .where(STAFF.STAFF_NUMBER.eq(staffNumber).and(STAFF.USERNAME.ne(username))).fetch();
    }

    @Override
    public Result<Record21<String, String, String, String, String, Byte, String, String, String, String, String, String, String, Date, String, String, String, Byte, Byte, String, Date>> findAllByPage(DataTablesUtil dataTablesUtil) {
        SelectOnConditionStep<Record21<String, String, String, String, String, Byte, String, String, String, String, String, String, String, Date, String, String, String, Byte, Byte, String, Date>> selectConditionStep =
                create.select(USERS.REAL_NAME, STAFF.STAFF_NUMBER, USERS.USERNAME, USERS.EMAIL, USERS.MOBILE, USERS.VERIFY_MAILBOX,
                        DSL.listAgg(ROLE.ROLE_NAME, " ").withinGroupOrderBy(ROLE.ROLE_NAME).as("roleName"),
                        SCHOOL.SCHOOL_NAME, COLLEGE.COLLEGE_NAME, DEPARTMENT.DEPARTMENT_NAME, ACADEMIC_TITLE.ACADEMIC_TITLE_NAME,
                        STAFF.POST, STAFF.SEX, STAFF.BIRTHDAY, NATION.NATION_NAME, POLITICAL_LANDSCAPE.POLITICAL_LANDSCAPE_NAME,
                        STAFF.FAMILY_RESIDENCE, USERS.ENABLED, USERS.ACCOUNT_NON_LOCKED, USERS.LANG_KEY, USERS.JOIN_DATE)
                        .from(STAFF)
                        .join(DEPARTMENT)
                        .on(STAFF.DEPARTMENT_ID.eq(DEPARTMENT.DEPARTMENT_ID))
                        .join(COLLEGE)
                        .on(DEPARTMENT.COLLEGE_ID.eq(COLLEGE.COLLEGE_ID))
                        .join(SCHOOL)
                        .on(COLLEGE.SCHOOL_ID.eq(SCHOOL.SCHOOL_ID))
                        .join(USERS)
                        .on(STAFF.USERNAME.eq(USERS.USERNAME))
                        .leftJoin(AUTHORITIES)
                        .on(USERS.USERNAME.eq(AUTHORITIES.USERNAME))
                        .leftJoin(ROLE)
                        .on(ROLE.ROLE_EN_NAME.eq(AUTHORITIES.AUTHORITY))
                        .leftJoin(NATION)
                        .on(STAFF.NATION_ID.eq(NATION.NATION_ID))
                        .leftJoin(POLITICAL_LANDSCAPE)
                        .on(STAFF.POLITICAL_LANDSCAPE_ID.eq(POLITICAL_LANDSCAPE.POLITICAL_LANDSCAPE_ID))
                        .leftJoin(ACADEMIC_TITLE)
                        .on(STAFF.ACADEMIC_TITLE_ID.eq(ACADEMIC_TITLE.ACADEMIC_TITLE_ID));
        return queryAllByPage(selectConditionStep, dataTablesUtil, false, USERS.USERNAME);
    }

    @Override
    public int countAll(DataTablesUtil dataTablesUtil) {
        SelectOnConditionStep<Record1<Integer>> selectOnConditionStep = create.selectCount()
                .from(STAFF)
                .join(USERS)
                .on(STAFF.USERNAME.eq(USERS.USERNAME))
                .join(DEPARTMENT)
                .on(STAFF.DEPARTMENT_ID.eq(DEPARTMENT.DEPARTMENT_ID))
                .join(COLLEGE)
                .on(DEPARTMENT.COLLEGE_ID.eq(COLLEGE.COLLEGE_ID))
                .join(SCHOOL)
                .on(COLLEGE.SCHOOL_ID.eq(SCHOOL.SCHOOL_ID));
        return countAll(selectOnConditionStep, dataTablesUtil, true);
    }

    @Override
    public int countByCondition(DataTablesUtil dataTablesUtil) {
        SelectOnConditionStep<Record1<Integer>> selectOnConditionStep = create.selectCount()
                .from(STAFF)
                .join(USERS)
                .on(STAFF.USERNAME.eq(USERS.USERNAME))
                .join(DEPARTMENT)
                .on(STAFF.DEPARTMENT_ID.eq(DEPARTMENT.DEPARTMENT_ID))
                .join(COLLEGE)
                .on(DEPARTMENT.COLLEGE_ID.eq(COLLEGE.COLLEGE_ID))
                .join(SCHOOL)
                .on(COLLEGE.SCHOOL_ID.eq(SCHOOL.SCHOOL_ID));
        return countAll(selectOnConditionStep, dataTablesUtil, false);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void save(Staff staff) {
        staffDao.insert(staff);
    }

    @Override
    public void saveWithUsers(StaffAddVo staffAddVo) {
        create.transaction(configuration -> {
            DSL.using(configuration)
                    .insertInto(USERS, USERS.USERNAME, USERS.PASSWORD, USERS.ENABLED,
                            USERS.ACCOUNT_NON_EXPIRED, USERS.CREDENTIALS_NON_EXPIRED,
                            USERS.ACCOUNT_NON_LOCKED, USERS.AGREE_PROTOCOL, USERS.USERS_TYPE_ID,
                            USERS.REAL_NAME, USERS.EMAIL, USERS.MOBILE, USERS.AVATAR, USERS.MAILBOX_VERIFY_CODE,
                            USERS.MAILBOX_VERIFY_VALID, USERS.LANG_KEY, USERS.JOIN_DATE)
                    .values(staffAddVo.getUsername(), BCryptUtil.bCryptPassword(staffAddVo.getPassword()),
                            BooleanUtil.toByte(true), BooleanUtil.toByte(true), BooleanUtil.toByte(true),
                            BooleanUtil.toByte(true), staffAddVo.getAgreeProtocol(), staffAddVo.getUsersTypeId(), staffAddVo.getRealName(),
                            staffAddVo.getEmail(), staffAddVo.getMobile(), staffAddVo.getAvatar(), staffAddVo.getMailboxVerifyCode(),
                            staffAddVo.getMailboxVerifyValid(), staffAddVo.getLangKey(), staffAddVo.getJoinDate())
                    .execute();
            DSL.using(configuration)
                    .insertInto(STAFF, STAFF.USERNAME, STAFF.STAFF_NUMBER, STAFF.DEPARTMENT_ID)
                    .values(staffAddVo.getUsername(), staffAddVo.getStaffNumber(), staffAddVo.getDepartmentId())
                    .execute();
        });
    }

    @CacheEvict(cacheNames = CacheBook.STAFF, key = "#staff.username")
    @Override
    public void update(Staff staff) {
        staffDao.update(staff);
    }

    public Result<Record21<String, String, String, String, String, Byte, String, String, String, String, String, String, String, Date, String, String, String, Byte, Byte, String, Date>> queryAllByPage(SelectOnConditionStep<Record21<String, String, String, String, String, Byte, String, String, String, String, String, String, String, Date, String, String, String, Byte, Byte, String, Date>> selectOnConditionStep,
                                                                                                                                                                                                         DataTablesUtil paginationUtil, boolean useExtraCondition, GroupField... groupFields) {
        Result<Record21<String, String, String, String, String, Byte, String, String, String, String, String, String, String, Date, String, String, String, Byte, Byte, String, Date>> records;
        Condition a = useExtraCondition(paginationUtil, useExtraCondition);
        if (Objects.isNull(a)) {
            groupBy(selectOnConditionStep, groupFields);
            sortCondition(selectOnConditionStep, paginationUtil);
            pagination(selectOnConditionStep, paginationUtil);
            records = selectOnConditionStep.fetch();
        } else {
            SelectConditionStep<Record21<String, String, String, String, String, Byte, String, String, String, String, String, String, String, Date, String, String, String, Byte, Byte, String, Date>> selectConditionStep = selectOnConditionStep.where(a);
            groupBy(selectConditionStep, groupFields);
            sortCondition(selectConditionStep, paginationUtil);
            pagination(selectConditionStep, paginationUtil);
            records = selectConditionStep.fetch();
        }
        return records;
    }

    @Override
    public Condition searchCondition(DataTablesUtil paginationUtil) {
        Condition a = null;
        JSONObject search = paginationUtil.getSearch();
        if (Objects.nonNull(search)) {
            String school = StringUtils.trim(search.getString("school"));
            String college = StringUtils.trim(search.getString("college"));
            String department = StringUtils.trim(search.getString("department"));
            String staffNumber = StringUtils.trim(search.getString("staffNumber"));
            String realName = StringUtils.trim(search.getString("realName"));
            String username = StringUtils.trim(search.getString("username"));
            String email = StringUtils.trim(search.getString("email"));
            String mobile = StringUtils.trim(search.getString("mobile"));

            if (StringUtils.isNotBlank(school)) {
                a = SCHOOL.SCHOOL_NAME.like(SQLQueryUtil.likeAllParam(school));
            }

            if (StringUtils.isNotBlank(college)) {
                if (Objects.isNull(a)) {
                    a = COLLEGE.COLLEGE_NAME.like(SQLQueryUtil.likeAllParam(college));
                } else {
                    a = a.and(COLLEGE.COLLEGE_NAME.like(SQLQueryUtil.likeAllParam(college)));
                }
            }

            if (StringUtils.isNotBlank(department)) {
                if (Objects.isNull(a)) {
                    a = DEPARTMENT.DEPARTMENT_NAME.like(SQLQueryUtil.likeAllParam(department));
                } else {
                    a = a.and(DEPARTMENT.DEPARTMENT_NAME.like(SQLQueryUtil.likeAllParam(department)));
                }
            }

            if (StringUtils.isNotBlank(staffNumber)) {
                if (Objects.isNull(a)) {
                    a = STAFF.STAFF_NUMBER.like(SQLQueryUtil.likeAllParam(staffNumber));
                } else {
                    a = a.and(STAFF.STAFF_NUMBER.like(SQLQueryUtil.likeAllParam(staffNumber)));
                }
            }

            if (StringUtils.isNotBlank(realName)) {
                if (Objects.isNull(a)) {
                    a = USERS.REAL_NAME.like(SQLQueryUtil.likeAllParam(realName));
                } else {
                    a = a.and(USERS.REAL_NAME.like(SQLQueryUtil.likeAllParam(realName)));
                }
            }

            if (StringUtils.isNotBlank(username)) {
                if (Objects.isNull(a)) {
                    a = USERS.USERNAME.like(SQLQueryUtil.likeAllParam(username));
                } else {
                    a = a.and(USERS.USERNAME.like(SQLQueryUtil.likeAllParam(username)));
                }
            }

            if (StringUtils.isNotBlank(email)) {
                if (Objects.isNull(a)) {
                    a = USERS.EMAIL.like(SQLQueryUtil.likeAllParam(email));
                } else {
                    a = a.and(USERS.EMAIL.like(SQLQueryUtil.likeAllParam(email)));
                }
            }

            if (StringUtils.isNotBlank(mobile)) {
                if (Objects.isNull(a)) {
                    a = USERS.MOBILE.like(SQLQueryUtil.likeAllParam(mobile));
                } else {
                    a = a.and(USERS.MOBILE.like(SQLQueryUtil.likeAllParam(mobile)));
                }
            }
        }
        return a;
    }

    public void sortCondition(SelectConnectByStep<Record21<String, String, String, String, String, Byte, String, String, String, String, String, String, String, Date, String, String, String, Byte, Byte, String, Date>> step, DataTablesUtil paginationUtil) {
        String orderColumnName = paginationUtil.getOrderColumnName();
        String orderDir = paginationUtil.getOrderDir();
        boolean isAsc = StringUtils.equalsIgnoreCase("asc", orderDir);
        SortField[] sortField = null;
        if (StringUtils.isNotBlank(orderColumnName)) {
            if (StringUtils.equals("realName", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = USERS.REAL_NAME.asc();
                    sortField[1] = STAFF.STAFF_ID.asc();
                } else {
                    sortField[0] = USERS.REAL_NAME.desc();
                    sortField[1] = STAFF.STAFF_ID.desc();
                }
            }

            if (StringUtils.equals("username", orderColumnName)) {
                sortField = new SortField[1];
                if (isAsc) {
                    sortField[0] = USERS.USERNAME.asc();
                } else {
                    sortField[0] = USERS.USERNAME.desc();
                }
            }

            if (StringUtils.equals("staffNumber", orderColumnName)) {
                sortField = new SortField[1];
                if (isAsc) {
                    sortField[0] = STAFF.STAFF_NUMBER.asc();
                } else {
                    sortField[0] = STAFF.STAFF_NUMBER.desc();
                }
            }

            if (StringUtils.equals("email", orderColumnName)) {
                sortField = new SortField[1];
                if (isAsc) {
                    sortField[0] = USERS.EMAIL.asc();
                } else {
                    sortField[0] = USERS.EMAIL.desc();
                }
            }

            if (StringUtils.equals("mobile", orderColumnName)) {
                sortField = new SortField[1];
                if (isAsc) {
                    sortField[0] = USERS.MOBILE.asc();
                } else {
                    sortField[0] = USERS.MOBILE.desc();
                }
            }

            if (StringUtils.equals("idCard", orderColumnName)) {
                sortField = new SortField[1];
                if (isAsc) {
                    sortField[0] = USERS.ID_CARD.asc();
                } else {
                    sortField[0] = USERS.ID_CARD.desc();
                }
            }

            if (StringUtils.equals("schoolName", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = SCHOOL.SCHOOL_NAME.asc();
                    sortField[1] = STAFF.STAFF_ID.asc();
                } else {
                    sortField[0] = SCHOOL.SCHOOL_NAME.desc();
                    sortField[1] = STAFF.STAFF_ID.desc();
                }
            }

            if (StringUtils.equals("collegeName", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = COLLEGE.COLLEGE_NAME.asc();
                    sortField[1] = STAFF.STAFF_ID.asc();
                } else {
                    sortField[0] = COLLEGE.COLLEGE_NAME.desc();
                    sortField[1] = STAFF.STAFF_ID.desc();
                }
            }

            if (StringUtils.equals("departmentName", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = DEPARTMENT.DEPARTMENT_NAME.asc();
                    sortField[1] = STAFF.STAFF_ID.asc();
                } else {
                    sortField[0] = DEPARTMENT.DEPARTMENT_NAME.desc();
                    sortField[1] = STAFF.STAFF_ID.desc();
                }
            }

            if (StringUtils.equals("academicTitleName", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = ACADEMIC_TITLE.ACADEMIC_TITLE_NAME.asc();
                    sortField[1] = STAFF.STAFF_ID.asc();
                } else {
                    sortField[0] = ACADEMIC_TITLE.ACADEMIC_TITLE_NAME.desc();
                    sortField[1] = STAFF.STAFF_ID.desc();
                }
            }

            if (StringUtils.equals("post", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = STAFF.POST.asc();
                    sortField[1] = STAFF.STAFF_ID.asc();
                } else {
                    sortField[0] = STAFF.POST.desc();
                    sortField[1] = STAFF.STAFF_ID.desc();
                }
            }

            if (StringUtils.equals("sex", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = STAFF.SEX.asc();
                    sortField[1] = STAFF.STAFF_ID.asc();
                } else {
                    sortField[0] = STAFF.SEX.desc();
                    sortField[1] = STAFF.STAFF_ID.desc();
                }
            }

            if (StringUtils.equals("birthday", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = STAFF.BIRTHDAY.asc();
                    sortField[1] = STAFF.STAFF_ID.asc();
                } else {
                    sortField[0] = STAFF.BIRTHDAY.desc();
                    sortField[1] = STAFF.STAFF_ID.desc();
                }
            }

            if (StringUtils.equals("nationName", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = NATION.NATION_NAME.asc();
                    sortField[1] = STAFF.STAFF_ID.asc();
                } else {
                    sortField[0] = NATION.NATION_NAME.desc();
                    sortField[1] = STAFF.STAFF_ID.desc();
                }
            }

            if (StringUtils.equals("politicalLandscapeName", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = POLITICAL_LANDSCAPE.POLITICAL_LANDSCAPE_NAME.asc();
                    sortField[1] = STAFF.STAFF_ID.asc();
                } else {
                    sortField[0] = POLITICAL_LANDSCAPE.POLITICAL_LANDSCAPE_NAME.desc();
                    sortField[1] = STAFF.STAFF_ID.desc();
                }
            }

            if (StringUtils.equals("familyResidence", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = STAFF.FAMILY_RESIDENCE.asc();
                    sortField[1] = STAFF.STAFF_ID.asc();
                } else {
                    sortField[0] = STAFF.FAMILY_RESIDENCE.desc();
                    sortField[1] = STAFF.STAFF_ID.desc();
                }
            }

            if (StringUtils.equals("enabled", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = USERS.ENABLED.asc();
                    sortField[1] = STAFF.STAFF_ID.asc();
                } else {
                    sortField[0] = USERS.ENABLED.desc();
                    sortField[1] = STAFF.STAFF_ID.desc();
                }
            }

            if (StringUtils.equals("accountNonLocked", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = USERS.ACCOUNT_NON_LOCKED.asc();
                    sortField[1] = STAFF.STAFF_ID.asc();
                } else {
                    sortField[0] = USERS.ACCOUNT_NON_LOCKED.desc();
                    sortField[1] = STAFF.STAFF_ID.desc();
                }
            }

            if (StringUtils.equals("langKey", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = USERS.LANG_KEY.asc();
                    sortField[1] = STAFF.STAFF_ID.asc();
                } else {
                    sortField[0] = USERS.LANG_KEY.desc();
                    sortField[1] = STAFF.STAFF_ID.desc();
                }
            }

            if (StringUtils.equals("joinDate", orderColumnName)) {
                sortField = new SortField[1];
                if (isAsc) {
                    sortField[0] = USERS.JOIN_DATE.asc();
                } else {
                    sortField[0] = USERS.JOIN_DATE.desc();
                }
            }
        }
        sortFinish(step, sortField);
    }

    private void sortFinish(SelectConnectByStep<Record21<String, String, String, String, String, Byte, String, String, String, String, String, String, String, Date, String, String, String, Byte, Byte, String, Date>> step, SortField... sortField) {
        if (Objects.nonNull(sortField)) {
            step.orderBy(sortField);
        }
    }

    @Override
    public Condition extraCondition(DataTablesUtil paginationUtil) {
        Users users = usersService.getUserFromSession();
        Condition a = USERS.USERNAME.ne(users.getUsername());
        JSONObject search = paginationUtil.getSearch();
        if (Objects.nonNull(search)) {
            String audited = StringUtils.trim(search.getString("audited"));
            if (StringUtils.isBlank(audited)) {
                audited = "1";
            }
            int auditedInt = NumberUtils.toInt(audited);
            // 已审核
            if (auditedInt == 1) {
                if (roleService.isCurrentUserInRole(Workbook.authorities.ROLE_SYSTEM.name())) {
                    Select<AuthoritiesRecord> select = create.selectFrom(AUTHORITIES)
                            .where(AUTHORITIES.USERNAME.eq(USERS.USERNAME).and(AUTHORITIES.AUTHORITY.ne(Workbook.authorities.ROLE_SYSTEM.name())));
                    a = a.andExists(select);
                } else {
                    Select<AuthoritiesRecord> select = create.selectFrom(AUTHORITIES)
                            .where(AUTHORITIES.USERNAME.eq(USERS.USERNAME)
                                    .and(AUTHORITIES.AUTHORITY.ne(Workbook.authorities.ROLE_SYSTEM.name()))
                                    .and(AUTHORITIES.AUTHORITY.ne(Workbook.authorities.ROLE_ACTUATOR.name()))
                                    .and(AUTHORITIES.AUTHORITY.ne(Workbook.authorities.ROLE_ADMIN.name())));
                    a = a.andExists(select);
                }

            } else if (auditedInt == 2) {
                // 未审核
                Select<AuthoritiesRecord> select = create.selectFrom(AUTHORITIES)
                        .where(AUTHORITIES.USERNAME.eq(USERS.USERNAME));
                a = a.andNotExists(select);
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

                if (Objects.isNull(a)) {
                    a = COLLEGE.COLLEGE_ID.eq(collegeId);
                } else {
                    a = a.and(COLLEGE.COLLEGE_ID.eq(collegeId));
                }
            }
        }

        return a;
    }

    private void groupBy(SelectConnectByStep<Record21<String, String, String, String, String, Byte, String, String, String, String, String, String, String, Date, String, String, String, Byte, Byte, String, Date>> step, GroupField... groupFields) {
        if (Objects.nonNull(groupFields)) {
            step.groupBy(groupFields);
        }
    }

    public void pagination(SelectConnectByStep<Record21<String, String, String, String, String, Byte, String, String, String, String, String, String, String, Date, String, String, String, Byte, Byte, String, Date>> step, DataTablesUtil paginationUtil) {
        int start = paginationUtil.getStart();
        int length = paginationUtil.getLength();

        step.limit(start, length);
    }
}
