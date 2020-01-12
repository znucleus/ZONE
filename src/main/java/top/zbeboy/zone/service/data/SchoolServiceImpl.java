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
import top.zbeboy.zone.domain.tables.daos.SchoolDao;
import top.zbeboy.zone.domain.tables.pojos.School;
import top.zbeboy.zone.domain.tables.records.SchoolRecord;
import top.zbeboy.zone.service.plugin.PaginationPlugin;
import top.zbeboy.zone.service.util.SQLQueryUtil;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

import static top.zbeboy.zone.domain.Tables.SCHOOL;

@Service("schoolService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class SchoolServiceImpl implements SchoolService, PaginationPlugin<DataTablesUtil> {

    private final DSLContext create;

    @Resource
    private SchoolDao schoolDao;

    @Autowired
    SchoolServiceImpl(DSLContext dslContext) {
        create = dslContext;
    }

    @Override
    public School findById(int id) {
        return schoolDao.findById(id);
    }

    @Override
    public List<School> findBySchoolName(String schoolName) {
        return schoolDao.fetchBySchoolName(schoolName);
    }

    @Override
    public Result<SchoolRecord> findBySchoolNameNeSchoolId(String schoolName, int schoolId) {
        return create.selectFrom(SCHOOL)
                .where(SCHOOL.SCHOOL_NAME.eq(schoolName).and(SCHOOL.SCHOOL_ID.ne(schoolId)))
                .fetch();
    }

    @Cacheable(cacheNames = CacheBook.SCHOOLS)
    @Override
    public List<School> findBySchoolIsDel(Byte schoolIsDel) {
        return schoolDao.fetchBySchoolIsDel(schoolIsDel);
    }

    @Override
    public Result<Record> findAllByPage(DataTablesUtil dataTablesUtil) {
        return queryAllByPage(create, SCHOOL, dataTablesUtil, false);
    }

    @Override
    public int countAll() {
        return countAll(create, SCHOOL);
    }

    @Override
    public int countByCondition(DataTablesUtil dataTablesUtil) {
        return countAll(create, SCHOOL, dataTablesUtil, false);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void save(School school) {
        schoolDao.insert(school);
    }

    @Override
    public void update(School school) {
        schoolDao.update(school);
    }

    @Override
    public void updateIsDel(List<Integer> ids, Byte isDel) {
        ids.forEach(id -> create.update(SCHOOL).set(SCHOOL.SCHOOL_IS_DEL, isDel).where(SCHOOL.SCHOOL_ID.eq(id)).execute());
    }

    @Override
    public Condition searchCondition(DataTablesUtil paginationUtil) {
        Condition a = null;
        JSONObject search = paginationUtil.getSearch();
        if (Objects.nonNull(search)) {
            String schoolName = StringUtils.trim(search.getString("schoolName"));
            if (StringUtils.isNotBlank(schoolName)) {
                a = SCHOOL.SCHOOL_NAME.like(SQLQueryUtil.likeAllParam(schoolName));
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
            if (StringUtils.equals("schoolId", orderColumnName)) {
                sortField = new SortField[1];
                if (isAsc) {
                    sortField[0] = SCHOOL.SCHOOL_ID.asc();
                } else {
                    sortField[0] = SCHOOL.SCHOOL_ID.desc();
                }
            }

            if (StringUtils.equals("schoolName", orderColumnName)) {
                sortField = new SortField[1];
                if (isAsc) {
                    sortField[0] = SCHOOL.SCHOOL_NAME.asc();
                } else {
                    sortField[0] = SCHOOL.SCHOOL_NAME.desc();
                }
            }

            if (StringUtils.equals("schoolIsDel", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = SCHOOL.SCHOOL_IS_DEL.asc();
                    sortField[1] = SCHOOL.SCHOOL_ID.asc();
                } else {
                    sortField[0] = SCHOOL.SCHOOL_IS_DEL.desc();
                    sortField[1] = SCHOOL.SCHOOL_ID.desc();
                }
            }
        }
        sortToFinish(step, sortField);
    }
}
