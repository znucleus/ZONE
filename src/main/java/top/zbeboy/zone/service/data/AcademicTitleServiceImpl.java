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
import top.zbeboy.zone.domain.tables.daos.AcademicTitleDao;
import top.zbeboy.zone.domain.tables.pojos.AcademicTitle;
import top.zbeboy.zone.domain.tables.records.AcademicTitleRecord;
import top.zbeboy.zone.service.plugin.PaginationPlugin;
import top.zbeboy.zone.service.util.SQLQueryUtil;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

import static top.zbeboy.zone.domain.Tables.ACADEMIC_TITLE;

@Service("academicTitleService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class AcademicTitleServiceImpl implements AcademicTitleService, PaginationPlugin<DataTablesUtil> {

    private final DSLContext create;

    @Resource
    private AcademicTitleDao academicTitleDao;

    @Autowired
    AcademicTitleServiceImpl(DSLContext dslContext) {
        create = dslContext;
    }

    @Override
    public AcademicTitle findById(int id) {
        return academicTitleDao.findById(id);
    }

    @Cacheable(cacheNames = CacheBook.ACADEMIC_TITLES)
    @Override
    public List<AcademicTitle> findAll() {
        return academicTitleDao.findAll();
    }

    @Override
    public List<AcademicTitle> findByAcademicTitleName(String academicTitleName) {
        return academicTitleDao.fetchByAcademicTitleName(academicTitleName);
    }

    @Override
    public Result<AcademicTitleRecord> findByAcademicTitleNameNeAcademicTitleId(String academicTitleName, int academicTitleId) {
        return create.selectFrom(ACADEMIC_TITLE)
                .where(ACADEMIC_TITLE.ACADEMIC_TITLE_NAME.eq(academicTitleName).and(ACADEMIC_TITLE.ACADEMIC_TITLE_ID.ne(academicTitleId)))
                .fetch();
    }

    @Override
    public Result<Record> findAllByPage(DataTablesUtil dataTablesUtil) {
        return queryAllByPage(create, ACADEMIC_TITLE, dataTablesUtil, false);
    }

    @Override
    public int countAll() {
        return countAll(create, ACADEMIC_TITLE);
    }

    @Override
    public int countByCondition(DataTablesUtil dataTablesUtil) {
        return countAll(create, ACADEMIC_TITLE, dataTablesUtil, false);
    }

    @CacheEvict(cacheNames = CacheBook.ACADEMIC_TITLES, allEntries = true)
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void save(AcademicTitle academicTitle) {
        academicTitleDao.insert(academicTitle);
    }

    @CacheEvict(cacheNames = CacheBook.ACADEMIC_TITLES, allEntries = true)
    @Override
    public void update(AcademicTitle academicTitle) {
        academicTitleDao.update(academicTitle);
    }

    @Override
    public Condition searchCondition(DataTablesUtil paginationUtil) {
        Condition a = null;
        JSONObject search = paginationUtil.getSearch();
        if (Objects.nonNull(search)) {
            String academicTitleName = StringUtils.trim(search.getString("academicTitleName"));
            if (StringUtils.isNotBlank(academicTitleName)) {
                a = ACADEMIC_TITLE.ACADEMIC_TITLE_NAME.like(SQLQueryUtil.likeAllParam(academicTitleName));
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
            if (StringUtils.equals("academicTitleId", orderColumnName)) {
                sortField = new SortField[1];
                if (isAsc) {
                    sortField[0] = ACADEMIC_TITLE.ACADEMIC_TITLE_ID.asc();
                } else {
                    sortField[0] = ACADEMIC_TITLE.ACADEMIC_TITLE_ID.desc();
                }
            }

            if (StringUtils.equals("academicTitleName", orderColumnName)) {
                sortField = new SortField[1];
                if (isAsc) {
                    sortField[0] = ACADEMIC_TITLE.ACADEMIC_TITLE_NAME.asc();
                } else {
                    sortField[0] = ACADEMIC_TITLE.ACADEMIC_TITLE_NAME.desc();
                }
            }
        }
        sortToFinish(step, sortField);
    }
}
