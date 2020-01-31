package top.zbeboy.zone.service.system;

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
import top.zbeboy.zone.domain.tables.daos.SystemConfigureDao;
import top.zbeboy.zone.domain.tables.pojos.SystemConfigure;
import top.zbeboy.zone.service.plugin.PaginationPlugin;
import top.zbeboy.zone.service.util.SQLQueryUtil;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;

import javax.annotation.Resource;
import java.util.Objects;

import static top.zbeboy.zone.domain.Tables.SYSTEM_CONFIGURE;

@Service("systemConfigureService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class SystemConfigureServiceImpl implements SystemConfigureService, PaginationPlugin<DataTablesUtil> {

    private final DSLContext create;

    @Resource
    private SystemConfigureDao systemConfigureDao;

    @Autowired
    SystemConfigureServiceImpl(DSLContext dslContext) {
        create = dslContext;
    }

    @Cacheable(cacheNames = CacheBook.SYSTEM_CONFIGURE, key = "#dataKey")
    @Override
    public SystemConfigure findByDataKey(String dataKey) {
        return systemConfigureDao.findById(dataKey);
    }

    @Override
    public Result<Record> findAllByPage(DataTablesUtil dataTablesUtil) {
        return queryAllByPage(create, SYSTEM_CONFIGURE, dataTablesUtil, false);
    }

    @Override
    public int countAll() {
        return countAll(create, SYSTEM_CONFIGURE);
    }

    @Override
    public int countByCondition(DataTablesUtil dataTablesUtil) {
        return countAll(create, SYSTEM_CONFIGURE, dataTablesUtil, false);
    }

    @CacheEvict(cacheNames = CacheBook.SYSTEM_CONFIGURE, key = "#systemConfigure.dataKey")
    @Override
    public void update(SystemConfigure systemConfigure) {
        systemConfigureDao.update(systemConfigure);
    }

    @Override
    public Condition searchCondition(DataTablesUtil paginationUtil) {
        Condition a = null;
        JSONObject search = paginationUtil.getSearch();
        if (Objects.nonNull(search)) {
            String dataKey = StringUtils.trim(search.getString("dataKey"));
            if (StringUtils.isNotBlank(dataKey)) {
                a = SYSTEM_CONFIGURE.DATA_KEY.like(SQLQueryUtil.likeAllParam(dataKey));
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
            if (StringUtils.equals("dataKey", orderColumnName)) {
                sortField = new SortField[1];
                if (isAsc) {
                    sortField[0] = SYSTEM_CONFIGURE.DATA_KEY.asc();
                } else {
                    sortField[0] = SYSTEM_CONFIGURE.DATA_KEY.desc();
                }
            }

            if (StringUtils.equals("dataValue", orderColumnName)) {
                sortField = new SortField[1];
                if (isAsc) {
                    sortField[0] = SYSTEM_CONFIGURE.DATA_VALUE.asc();
                } else {
                    sortField[0] = SYSTEM_CONFIGURE.DATA_VALUE.desc();
                }
            }
        }
        sortToFinish(step, sortField);
    }
}
