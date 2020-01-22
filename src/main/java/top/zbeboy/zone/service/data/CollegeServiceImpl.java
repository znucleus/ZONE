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
import top.zbeboy.zone.domain.tables.daos.CollegeDao;
import top.zbeboy.zone.domain.tables.pojos.College;
import top.zbeboy.zone.domain.tables.records.CollegeRecord;
import top.zbeboy.zone.service.plugin.PaginationPlugin;
import top.zbeboy.zone.service.util.SQLQueryUtil;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static top.zbeboy.zone.domain.Tables.COLLEGE;
import static top.zbeboy.zone.domain.Tables.SCHOOL;

@Service("collegeService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class CollegeServiceImpl implements CollegeService, PaginationPlugin<DataTablesUtil> {

    private final DSLContext create;

    @Resource
    private CollegeDao collegeDao;

    @Autowired
    CollegeServiceImpl(DSLContext dslContext) {
        create = dslContext;
    }

    @Override
    public College findById(int id) {
        return collegeDao.findById(id);
    }

    @Cacheable(cacheNames = CacheBook.COLLEGE, key = "#id")
    @Override
    public Optional<Record> findByIdRelation(int id) {
        return create.select()
                .from(COLLEGE)
                .join(SCHOOL)
                .on(COLLEGE.SCHOOL_ID.eq(SCHOOL.SCHOOL_ID))
                .where(COLLEGE.COLLEGE_ID.eq(id))
                .fetchOptional();
    }

    @Cacheable(cacheNames = CacheBook.COLLEGES, key = "#schoolId + '_' + #collegeIsDel")
    @Override
    public Result<CollegeRecord> findBySchoolIdAndCollegeIsDel(int schoolId, Byte collegeIsDel) {
        return create.selectFrom(COLLEGE).where(COLLEGE.SCHOOL_ID.eq(schoolId)
                .and(COLLEGE.COLLEGE_IS_DEL.eq(collegeIsDel))).fetch();
    }

    @Override
    public Result<CollegeRecord> findByCollegeNameAndSchoolId(String collegeName, int schoolId) {
        return create.selectFrom(COLLEGE)
                .where(COLLEGE.COLLEGE_NAME.eq(collegeName).and(COLLEGE.SCHOOL_ID.eq(schoolId)))
                .fetch();
    }

    @Override
    public Result<CollegeRecord> findByCollegeNameAndSchoolIdNeCollegeId(String collegeName, int collegeId, int schoolId) {
        return create.selectFrom(COLLEGE)
                .where(COLLEGE.COLLEGE_NAME.eq(collegeName).and(COLLEGE.COLLEGE_ID.ne(collegeId)).and(COLLEGE.SCHOOL_ID.eq(schoolId)))
                .fetch();
    }

    @Override
    public List<College> findByCollegeCode(String collegeCode) {
        return collegeDao.fetchByCollegeCode(collegeCode);
    }

    @Override
    public Result<CollegeRecord> findByCollegeCodeNeCollegeId(String collegeCode, int collegeId) {
        return create.selectFrom(COLLEGE)
                .where(COLLEGE.COLLEGE_CODE.eq(collegeCode).and(COLLEGE.COLLEGE_ID.ne(collegeId)))
                .fetch();
    }

    @Override
    public Result<Record> findAllByPage(DataTablesUtil dataTablesUtil) {
        SelectOnConditionStep<Record> selectOnConditionStep =
                create.select()
                        .from(COLLEGE)
                        .join(SCHOOL)
                        .on(COLLEGE.SCHOOL_ID.eq(SCHOOL.SCHOOL_ID));
        return queryAllByPage(selectOnConditionStep, dataTablesUtil, false);
    }

    @Override
    public int countAll() {
        return countAll(create, COLLEGE);
    }

    @Override
    public int countByCondition(DataTablesUtil dataTablesUtil) {
        SelectOnConditionStep<Record1<Integer>> selectOnConditionStep =
                create.selectCount()
                        .from(COLLEGE)
                        .join(SCHOOL)
                        .on(COLLEGE.SCHOOL_ID.eq(SCHOOL.SCHOOL_ID));
        return countAll(selectOnConditionStep, dataTablesUtil, false);
    }

    @CacheEvict(cacheNames = CacheBook.COLLEGES, allEntries = true)
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void save(College college) {
        collegeDao.insert(college);
    }

    @CacheEvict(cacheNames = {CacheBook.COLLEGE, CacheBook.COLLEGES}, allEntries = true)
    @Override
    public void update(College college) {
        collegeDao.update(college);
    }

    @CacheEvict(cacheNames = {CacheBook.COLLEGE, CacheBook.COLLEGES}, allEntries = true)
    @Override
    public void updateIsDel(List<Integer> ids, Byte isDel) {
        ids.forEach(id -> create.update(COLLEGE).set(COLLEGE.COLLEGE_IS_DEL, isDel).where(COLLEGE.COLLEGE_ID.eq(id)).execute());
    }

    @Override
    public Condition searchCondition(DataTablesUtil paginationUtil) {
        Condition a = null;
        JSONObject search = paginationUtil.getSearch();
        if (Objects.nonNull(search)) {
            String schoolName = StringUtils.trim(search.getString("schoolName"));
            String collegeName = StringUtils.trim(search.getString("collegeName"));
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
            if (StringUtils.equals("collegeId", orderColumnName)) {
                sortField = new SortField[1];
                if (isAsc) {
                    sortField[0] = COLLEGE.COLLEGE_ID.asc();
                } else {
                    sortField[0] = COLLEGE.COLLEGE_ID.desc();
                }
            }

            if (StringUtils.equals("schoolName", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = SCHOOL.SCHOOL_NAME.asc();
                    sortField[1] = COLLEGE.COLLEGE_ID.asc();
                } else {
                    sortField[0] = SCHOOL.SCHOOL_NAME.desc();
                    sortField[1] = COLLEGE.COLLEGE_ID.desc();
                }
            }

            if (StringUtils.equals("collegeName", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = COLLEGE.COLLEGE_NAME.asc();
                    sortField[1] = COLLEGE.COLLEGE_ID.asc();
                } else {
                    sortField[0] = COLLEGE.COLLEGE_NAME.desc();
                    sortField[1] = COLLEGE.COLLEGE_ID.desc();
                }
            }

            if (StringUtils.equals("collegeCode", orderColumnName)) {
                sortField = new SortField[1];
                if (isAsc) {
                    sortField[0] = COLLEGE.COLLEGE_CODE.asc();
                } else {
                    sortField[0] = COLLEGE.COLLEGE_CODE.desc();
                }
            }

            if (StringUtils.equals("collegeAddress", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = COLLEGE.COLLEGE_ADDRESS.asc();
                    sortField[1] = COLLEGE.COLLEGE_ID.asc();
                } else {
                    sortField[0] = COLLEGE.COLLEGE_ADDRESS.desc();
                    sortField[1] = COLLEGE.COLLEGE_ID.desc();
                }
            }


            if (StringUtils.equals("collegeIsDel", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = COLLEGE.COLLEGE_IS_DEL.asc();
                    sortField[1] = COLLEGE.COLLEGE_ID.asc();
                } else {
                    sortField[0] = COLLEGE.COLLEGE_IS_DEL.desc();
                    sortField[1] = COLLEGE.COLLEGE_ID.desc();
                }
            }
        }
        sortToFinish(step, sortField);
    }
}
