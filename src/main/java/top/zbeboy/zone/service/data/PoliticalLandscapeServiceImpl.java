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
import top.zbeboy.zone.domain.tables.daos.PoliticalLandscapeDao;
import top.zbeboy.zone.domain.tables.pojos.PoliticalLandscape;
import top.zbeboy.zone.domain.tables.records.PoliticalLandscapeRecord;
import top.zbeboy.zone.service.plugin.PaginationPlugin;
import top.zbeboy.zone.service.util.SQLQueryUtil;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

import static top.zbeboy.zone.domain.Tables.POLITICAL_LANDSCAPE;

@Service("politicalLandscapeService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class PoliticalLandscapeServiceImpl implements PoliticalLandscapeService, PaginationPlugin<DataTablesUtil> {

    private final DSLContext create;

    @Resource
    private PoliticalLandscapeDao politicalLandscapeDao;

    @Autowired
    PoliticalLandscapeServiceImpl(DSLContext dslContext) {
        create = dslContext;
    }

    @Override
    public PoliticalLandscape findById(int id) {
        return politicalLandscapeDao.findById(id);
    }

    @Cacheable(cacheNames = CacheBook.POLITICAL_LANDSCAPES)
    @Override
    public List<PoliticalLandscape> findAll() {
        return politicalLandscapeDao.findAll();
    }

    @Override
    public List<PoliticalLandscape> findByPoliticalLandscapeName(String politicalLandscapeName) {
        return politicalLandscapeDao.fetchByPoliticalLandscapeName(politicalLandscapeName);
    }

    @Override
    public Result<PoliticalLandscapeRecord> findByPoliticalLandscapeNameNePoliticalLandscapeId(String politicalLandscapeName, int politicalLandscapeId) {
        return create.selectFrom(POLITICAL_LANDSCAPE)
                .where(POLITICAL_LANDSCAPE.POLITICAL_LANDSCAPE_NAME.eq(politicalLandscapeName).and(POLITICAL_LANDSCAPE.POLITICAL_LANDSCAPE_ID.ne(politicalLandscapeId)))
                .fetch();
    }

    @Override
    public Result<Record> findAllByPage(DataTablesUtil dataTablesUtil) {
        return queryAllByPage(create, POLITICAL_LANDSCAPE, dataTablesUtil, false);
    }

    @Override
    public int countAll() {
        return countAll(create, POLITICAL_LANDSCAPE);
    }

    @Override
    public int countByCondition(DataTablesUtil dataTablesUtil) {
        return countAll(create, POLITICAL_LANDSCAPE, dataTablesUtil, false);
    }

    @CacheEvict(cacheNames = CacheBook.POLITICAL_LANDSCAPES, allEntries = true)
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void save(PoliticalLandscape politicalLandscape) {
        politicalLandscapeDao.insert(politicalLandscape);
    }

    @CacheEvict(cacheNames = CacheBook.POLITICAL_LANDSCAPES, allEntries = true)
    @Override
    public void update(PoliticalLandscape politicalLandscape) {
        politicalLandscapeDao.update(politicalLandscape);
    }

    @Override
    public Condition searchCondition(DataTablesUtil paginationUtil) {
        Condition a = null;
        JSONObject search = paginationUtil.getSearch();
        if (Objects.nonNull(search)) {
            String politicalLandscapeName = StringUtils.trim(search.getString("politicalLandscapeName"));
            if (StringUtils.isNotBlank(politicalLandscapeName)) {
                a = POLITICAL_LANDSCAPE.POLITICAL_LANDSCAPE_NAME.like(SQLQueryUtil.likeAllParam(politicalLandscapeName));
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
            if (StringUtils.equals("politicalLandscapeId", orderColumnName)) {
                sortField = new SortField[1];
                if (isAsc) {
                    sortField[0] = POLITICAL_LANDSCAPE.POLITICAL_LANDSCAPE_ID.asc();
                } else {
                    sortField[0] = POLITICAL_LANDSCAPE.POLITICAL_LANDSCAPE_ID.desc();
                }
            }

            if (StringUtils.equals("politicalLandscapeName", orderColumnName)) {
                sortField = new SortField[1];
                if (isAsc) {
                    sortField[0] = POLITICAL_LANDSCAPE.POLITICAL_LANDSCAPE_NAME.asc();
                } else {
                    sortField[0] = POLITICAL_LANDSCAPE.POLITICAL_LANDSCAPE_NAME.desc();
                }
            }
        }
        sortToFinish(step, sortField);
    }
}
