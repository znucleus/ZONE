package top.zbeboy.zone.service.data;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.jooq.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.zbeboy.zone.config.CacheBook;
import top.zbeboy.zone.config.Workbook;
import top.zbeboy.zone.domain.tables.daos.SchoolroomDao;
import top.zbeboy.zone.domain.tables.pojos.Schoolroom;
import top.zbeboy.zone.domain.tables.pojos.Users;
import top.zbeboy.zone.domain.tables.pojos.UsersType;
import top.zbeboy.zone.domain.tables.records.SchoolroomRecord;
import top.zbeboy.zone.feign.data.StaffService;
import top.zbeboy.zone.feign.platform.UsersTypeService;
import top.zbeboy.zone.service.platform.RoleService;
import top.zbeboy.zone.service.platform.UsersService;
import top.zbeboy.zone.service.plugin.PaginationPlugin;
import top.zbeboy.zone.service.util.SQLQueryUtil;
import top.zbeboy.zone.web.bean.data.staff.StaffBean;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static top.zbeboy.zone.domain.Tables.*;

@Service("schoolroomService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class SchoolroomServiceImpl implements SchoolroomService, PaginationPlugin<DataTablesUtil> {

    private final DSLContext create;

    @Resource
    private SchoolroomDao schoolroomDao;

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
    SchoolroomServiceImpl(DSLContext dslContext) {
        create = dslContext;
    }

    @Override
    public Schoolroom findById(int id) {
        return schoolroomDao.findById(id);
    }

    @Cacheable(cacheNames = CacheBook.SCHOOLROOM, key = "#id")
    @Override
    public Optional<Record> findByIdRelation(int id) {
        return create.select()
                .from(SCHOOLROOM)
                .join(BUILDING)
                .on(SCHOOLROOM.BUILDING_ID.eq(BUILDING.BUILDING_ID))
                .join(COLLEGE)
                .on(BUILDING.COLLEGE_ID.eq(COLLEGE.COLLEGE_ID))
                .join(SCHOOL)
                .on(COLLEGE.SCHOOL_ID.eq(SCHOOL.SCHOOL_ID))
                .where(SCHOOLROOM.SCHOOLROOM_ID.eq(id))
                .fetchOptional();
    }

    @Cacheable(cacheNames = CacheBook.SCHOOLROOMS, key = "#buildingId + '_' + #schoolroomIsDel")
    @Override
    public Result<SchoolroomRecord> findByBuildingIdAndSchoolroomIsDel(int buildingId, Byte schoolroomIsDel) {
        return create.selectFrom(SCHOOLROOM).where(SCHOOLROOM.BUILDING_ID.eq(buildingId)
                .and(SCHOOLROOM.SCHOOLROOM_IS_DEL.eq(schoolroomIsDel))).fetch();
    }

    @Override
    public Result<SchoolroomRecord> findByBuildingCodeAndBuildingId(String buildingCode, int buildingId) {
        return create.selectFrom(SCHOOLROOM)
                .where(SCHOOLROOM.BUILDING_CODE.eq(buildingCode).and(SCHOOLROOM.BUILDING_ID.eq(buildingId)))
                .fetch();
    }

    @Override
    public Result<SchoolroomRecord> findByBuildingCodeAndBuildingIdNeSchoolroomId(String buildingCode, int buildingId, int schoolroomId) {
        return create.selectFrom(SCHOOLROOM)
                .where(SCHOOLROOM.BUILDING_CODE.eq(buildingCode).and(SCHOOLROOM.BUILDING_ID.eq(buildingId).and(SCHOOLROOM.SCHOOLROOM_ID.ne(schoolroomId))))
                .fetch();
    }

    @Override
    public Result<Record> findAllByPage(DataTablesUtil dataTablesUtil) {
        SelectOnConditionStep<Record> selectOnConditionStep =
                create.select()
                        .from(SCHOOLROOM)
                        .join(BUILDING)
                        .on(SCHOOLROOM.BUILDING_ID.eq(BUILDING.BUILDING_ID))
                        .join(COLLEGE)
                        .on(BUILDING.COLLEGE_ID.eq(COLLEGE.COLLEGE_ID))
                        .join(SCHOOL)
                        .on(COLLEGE.SCHOOL_ID.eq(SCHOOL.SCHOOL_ID));
        return queryAllByPage(selectOnConditionStep, dataTablesUtil, false);
    }

    @Override
    public int countAll(DataTablesUtil dataTablesUtil) {
        SelectOnConditionStep<Record1<Integer>> selectOnConditionStep = create.selectCount()
                .from(SCHOOLROOM)
                .join(BUILDING)
                .on(SCHOOLROOM.BUILDING_ID.eq(BUILDING.BUILDING_ID))
                .join(COLLEGE)
                .on(BUILDING.COLLEGE_ID.eq(COLLEGE.COLLEGE_ID))
                .join(SCHOOL)
                .on(COLLEGE.SCHOOL_ID.eq(SCHOOL.SCHOOL_ID));
        return countAll(selectOnConditionStep, dataTablesUtil, true);
    }

    @Override
    public int countByCondition(DataTablesUtil dataTablesUtil) {
        SelectOnConditionStep<Record1<Integer>> selectOnConditionStep = create.selectCount()
                .from(SCHOOLROOM)
                .join(BUILDING)
                .on(SCHOOLROOM.BUILDING_ID.eq(BUILDING.BUILDING_ID))
                .join(COLLEGE)
                .on(BUILDING.COLLEGE_ID.eq(COLLEGE.COLLEGE_ID))
                .join(SCHOOL)
                .on(COLLEGE.SCHOOL_ID.eq(SCHOOL.SCHOOL_ID));
        return countAll(selectOnConditionStep, dataTablesUtil, false);
    }

    @CacheEvict(cacheNames = CacheBook.SCHOOLROOMS, allEntries = true)
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void save(Schoolroom schoolroom) {
        schoolroomDao.insert(schoolroom);
    }

    @CacheEvict(cacheNames = {CacheBook.SCHOOLROOM, CacheBook.SCHOOLROOMS}, allEntries = true)
    @Override
    public void update(Schoolroom schoolroom) {
        schoolroomDao.update(schoolroom);
    }

    @CacheEvict(cacheNames = {CacheBook.SCHOOLROOM, CacheBook.SCHOOLROOMS}, allEntries = true)
    @Override
    public void updateIsDel(List<Integer> ids, Byte isDel) {
        ids.forEach(id -> create.update(SCHOOLROOM).set(SCHOOLROOM.SCHOOLROOM_IS_DEL, isDel).where(SCHOOLROOM.SCHOOLROOM_ID.eq(id)).execute());
    }

    @Override
    public Condition searchCondition(DataTablesUtil paginationUtil) {
        Condition a = null;
        JSONObject search = paginationUtil.getSearch();
        if (Objects.nonNull(search)) {
            String schoolName = StringUtils.trim(search.getString("schoolName"));
            String collegeName = StringUtils.trim(search.getString("collegeName"));
            String buildingName = StringUtils.trim(search.getString("buildingName"));
            String buildingCode = StringUtils.trim(search.getString("buildingCode"));
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

            if (StringUtils.isNotBlank(buildingName)) {
                if (Objects.isNull(a)) {
                    a = BUILDING.BUILDING_NAME.like(SQLQueryUtil.likeAllParam(buildingName));
                } else {
                    a = a.and(BUILDING.BUILDING_NAME.like(SQLQueryUtil.likeAllParam(buildingName)));
                }
            }

            if (StringUtils.isNotBlank(buildingCode)) {
                if (Objects.isNull(a)) {
                    a = SCHOOLROOM.BUILDING_CODE.like(SQLQueryUtil.likeAllParam(buildingCode));
                } else {
                    a = a.and(SCHOOLROOM.BUILDING_CODE.like(SQLQueryUtil.likeAllParam(buildingCode)));
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
        return a;
    }

    @Override
    public void sortCondition(SelectConnectByStep<Record> step, DataTablesUtil paginationUtil) {
        String orderColumnName = paginationUtil.getOrderColumnName();
        String orderDir = paginationUtil.getOrderDir();
        boolean isAsc = StringUtils.equalsIgnoreCase("asc", orderDir);
        SortField[] sortField = null;
        if (StringUtils.isNotBlank(orderColumnName)) {
            if (StringUtils.equals("schoolroomId", orderColumnName)) {
                sortField = new SortField[1];
                if (isAsc) {
                    sortField[0] = SCHOOLROOM.SCHOOLROOM_ID.asc();
                } else {
                    sortField[0] = SCHOOLROOM.SCHOOLROOM_ID.desc();
                }
            }

            if (StringUtils.equals("schoolName", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = SCHOOL.SCHOOL_NAME.asc();
                    sortField[1] = SCHOOLROOM.SCHOOLROOM_ID.asc();
                } else {
                    sortField[0] = SCHOOL.SCHOOL_NAME.desc();
                    sortField[1] = SCHOOLROOM.SCHOOLROOM_ID.desc();
                }
            }

            if (StringUtils.equals("collegeName", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = COLLEGE.COLLEGE_NAME.asc();
                    sortField[1] = SCHOOLROOM.SCHOOLROOM_ID.asc();
                } else {
                    sortField[0] = COLLEGE.COLLEGE_NAME.desc();
                    sortField[1] = SCHOOLROOM.SCHOOLROOM_ID.desc();
                }
            }

            if (StringUtils.equals("buildingName", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = BUILDING.BUILDING_NAME.asc();
                    sortField[1] = SCHOOLROOM.SCHOOLROOM_ID.asc();
                } else {
                    sortField[0] = BUILDING.BUILDING_NAME.desc();
                    sortField[1] = SCHOOLROOM.SCHOOLROOM_ID.desc();
                }
            }

            if (StringUtils.equals("buildingCode", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = SCHOOLROOM.BUILDING_CODE.asc();
                    sortField[1] = SCHOOLROOM.SCHOOLROOM_ID.asc();
                } else {
                    sortField[0] = SCHOOLROOM.BUILDING_CODE.desc();
                    sortField[1] = SCHOOLROOM.SCHOOLROOM_ID.desc();
                }
            }

            if (StringUtils.equals("schoolroomIsDel", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = SCHOOLROOM.SCHOOLROOM_IS_DEL.asc();
                    sortField[1] = SCHOOLROOM.SCHOOLROOM_ID.asc();
                } else {
                    sortField[0] = SCHOOLROOM.SCHOOLROOM_IS_DEL.desc();
                    sortField[1] = SCHOOLROOM.SCHOOLROOM_ID.desc();
                }
            }
        }
        sortToFinish(step, sortField);
    }
}
