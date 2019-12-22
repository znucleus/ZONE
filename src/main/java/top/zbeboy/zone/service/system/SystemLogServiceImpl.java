package top.zbeboy.zone.service.system;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.jooq.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import top.zbeboy.zone.domain.tables.daos.SystemOperatorLogDao;
import top.zbeboy.zone.domain.tables.pojos.SystemOperatorLog;
import top.zbeboy.zone.service.plugin.PaginationPlugin;
import top.zbeboy.zone.service.util.SQLQueryUtil;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;

import javax.annotation.Resource;
import java.util.Objects;

import static top.zbeboy.zone.domain.Tables.SYSTEM_OPERATOR_LOG;

@Service("systemLogService")
public class SystemLogServiceImpl implements SystemLogService, PaginationPlugin<DataTablesUtil> {

    private final DSLContext create;

    @Resource
    private SystemOperatorLogDao systemOperatorLogDao;

    @Autowired
    SystemLogServiceImpl(DSLContext dslContext) {
        create = dslContext;
    }

    @Override
    public Result<Record> findAllByPage(DataTablesUtil dataTablesUtil) {
        return queryAllByPage(create, SYSTEM_OPERATOR_LOG, dataTablesUtil, false);
    }

    @Override
    public int countAll() {
        return countAll(create, SYSTEM_OPERATOR_LOG);
    }

    @Override
    public int countByCondition(DataTablesUtil dataTablesUtil) {
        return countAll(create, SYSTEM_OPERATOR_LOG, dataTablesUtil, false);
    }

    @Async
    @Override
    public void save(SystemOperatorLog systemOperatorLog) {
        systemOperatorLogDao.insert(systemOperatorLog);
    }

    @Override
    public Condition searchCondition(DataTablesUtil paginationUtil) {
        Condition a = null;
        JSONObject search = paginationUtil.getSearch();
        if (Objects.nonNull(search)) {
            String username = StringUtils.trim(search.getString("username"));
            String behavior = StringUtils.trim(search.getString("behavior"));
            String ipAddress = StringUtils.trim(search.getString("ipAddress"));
            if (StringUtils.isNotBlank(username)) {
                a = SYSTEM_OPERATOR_LOG.USERNAME.like(SQLQueryUtil.likeAllParam(username));
            }

            if (StringUtils.isNotBlank(behavior)) {
                if (Objects.isNull(a)) {
                    a = SYSTEM_OPERATOR_LOG.BEHAVIOR.like(SQLQueryUtil.likeAllParam(behavior));
                } else {
                    a = a.and(SYSTEM_OPERATOR_LOG.BEHAVIOR.like(SQLQueryUtil.likeAllParam(behavior)));
                }
            }

            if (StringUtils.isNotBlank(ipAddress)) {
                if (Objects.isNull(a)) {
                    a = SYSTEM_OPERATOR_LOG.IP_ADDRESS.like(SQLQueryUtil.likeAllParam(ipAddress));
                } else {
                    a = a.and(SYSTEM_OPERATOR_LOG.IP_ADDRESS.like(SQLQueryUtil.likeAllParam(ipAddress)));
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
            if (StringUtils.equals("username", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = SYSTEM_OPERATOR_LOG.USERNAME.asc();
                    sortField[1] = SYSTEM_OPERATOR_LOG.OPERATING_TIME.asc();
                } else {
                    sortField[0] = SYSTEM_OPERATOR_LOG.USERNAME.desc();
                    sortField[1] = SYSTEM_OPERATOR_LOG.OPERATING_TIME.desc();
                }
            }

            if (StringUtils.equals("behavior", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = SYSTEM_OPERATOR_LOG.BEHAVIOR.asc();
                    sortField[1] = SYSTEM_OPERATOR_LOG.OPERATING_TIME.asc();
                } else {
                    sortField[0] = SYSTEM_OPERATOR_LOG.BEHAVIOR.desc();
                    sortField[1] = SYSTEM_OPERATOR_LOG.OPERATING_TIME.desc();
                }
            }

            if (StringUtils.equals("operatingTimeNew", orderColumnName)) {
                sortField = new SortField[1];
                if (isAsc) {
                    sortField[0] = SYSTEM_OPERATOR_LOG.OPERATING_TIME.asc();
                } else {
                    sortField[0] = SYSTEM_OPERATOR_LOG.OPERATING_TIME.desc();
                }
            }

            if (StringUtils.equals("ipAddress", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = SYSTEM_OPERATOR_LOG.IP_ADDRESS.asc();
                    sortField[1] = SYSTEM_OPERATOR_LOG.OPERATING_TIME.asc();
                } else {
                    sortField[0] = SYSTEM_OPERATOR_LOG.IP_ADDRESS.desc();
                    sortField[1] = SYSTEM_OPERATOR_LOG.OPERATING_TIME.desc();
                }
            }
        }
        sortToFinish(step, sortField);
    }
}
