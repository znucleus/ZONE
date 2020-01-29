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
import top.zbeboy.zone.domain.tables.daos.NationDao;
import top.zbeboy.zone.domain.tables.pojos.Nation;
import top.zbeboy.zone.domain.tables.records.NationRecord;
import top.zbeboy.zone.service.plugin.PaginationPlugin;
import top.zbeboy.zone.service.util.SQLQueryUtil;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

import static top.zbeboy.zone.domain.Tables.NATION;

@Service("nationService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class NationServiceImpl implements NationService, PaginationPlugin<DataTablesUtil> {

    private final DSLContext create;

    @Resource
    private NationDao nationDao;

    @Autowired
    NationServiceImpl(DSLContext dslContext) {
        create = dslContext;
    }

    @Override
    public Nation findById(int id) {
        return nationDao.findById(id);
    }

    @Cacheable(cacheNames = CacheBook.NATIONS)
    @Override
    public List<Nation> findAll() {
        return nationDao.findAll();
    }

    @Override
    public List<Nation> findByNationName(String nationName) {
        return nationDao.fetchByNationName(nationName);
    }

    @Override
    public Result<NationRecord> findByNationNameNeNationId(String nationName, int nationId) {
        return create.selectFrom(NATION)
                .where(NATION.NATION_NAME.eq(nationName).and(NATION.NATION_ID.ne(nationId)))
                .fetch();
    }

    @Override
    public Result<Record> findAllByPage(DataTablesUtil dataTablesUtil) {
        return queryAllByPage(create, NATION, dataTablesUtil, false);
    }

    @Override
    public int countAll() {
        return countAll(create, NATION);
    }

    @Override
    public int countByCondition(DataTablesUtil dataTablesUtil) {
        return countAll(create, NATION, dataTablesUtil, false);
    }

    @CacheEvict(cacheNames = CacheBook.NATIONS, allEntries = true)
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void save(Nation nation) {
        nationDao.insert(nation);
    }

    @CacheEvict(cacheNames = CacheBook.NATIONS, allEntries = true)
    @Override
    public void update(Nation nation) {
        nationDao.update(nation);
    }

    @Override
    public Condition searchCondition(DataTablesUtil paginationUtil) {
        Condition a = null;
        JSONObject search = paginationUtil.getSearch();
        if (Objects.nonNull(search)) {
            String nationName = StringUtils.trim(search.getString("nationName"));
            if (StringUtils.isNotBlank(nationName)) {
                a = NATION.NATION_NAME.like(SQLQueryUtil.likeAllParam(nationName));
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
            if (StringUtils.equals("nationId", orderColumnName)) {
                sortField = new SortField[1];
                if (isAsc) {
                    sortField[0] = NATION.NATION_ID.asc();
                } else {
                    sortField[0] = NATION.NATION_ID.desc();
                }
            }

            if (StringUtils.equals("nationName", orderColumnName)) {
                sortField = new SortField[1];
                if (isAsc) {
                    sortField[0] = NATION.NATION_NAME.asc();
                } else {
                    sortField[0] = NATION.NATION_NAME.desc();
                }
            }
        }
        sortToFinish(step, sortField);
    }
}
