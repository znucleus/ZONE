package top.zbeboy.zone.service.system;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.jooq.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.zbeboy.zone.domain.tables.daos.SystemMailboxLogDao;
import top.zbeboy.zone.domain.tables.pojos.SystemMailboxLog;
import top.zbeboy.zone.service.plugin.PaginationPlugin;
import top.zbeboy.zone.service.util.SQLQueryUtil;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;

import javax.annotation.Resource;
import java.util.Objects;

import static top.zbeboy.zone.domain.Tables.SYSTEM_MAILBOX_LOG;

@Service("systemMailboxLogService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class SystemMailboxLogServiceImpl implements SystemMailboxLogService, PaginationPlugin<DataTablesUtil> {

    private final DSLContext create;

    @Resource
    private SystemMailboxLogDao systemMailboxLogDao;

    @Autowired
    SystemMailboxLogServiceImpl(DSLContext dslContext) {
        create = dslContext;
    }

    @Override
    public Result<Record> findAllByPage(DataTablesUtil dataTablesUtil) {
        return queryAllByPage(create, SYSTEM_MAILBOX_LOG, dataTablesUtil, false);
    }

    @Override
    public int countAll() {
        return countAll(create, SYSTEM_MAILBOX_LOG);
    }

    @Override
    public int countByCondition(DataTablesUtil dataTablesUtil) {
        return countAll(create, SYSTEM_MAILBOX_LOG, dataTablesUtil, false);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void save(SystemMailboxLog systemMailboxLog) {
        systemMailboxLogDao.insert(systemMailboxLog);
    }

    @Override
    public Condition searchCondition(DataTablesUtil paginationUtil) {
        Condition a = null;
        JSONObject search = paginationUtil.getSearch();
        if (Objects.nonNull(search)) {
            String acceptMail = StringUtils.trim(search.getString("acceptMail"));
            if (StringUtils.isNotBlank(acceptMail)) {
                a = SYSTEM_MAILBOX_LOG.ACCEPT_MAIL.like(SQLQueryUtil.likeAllParam(acceptMail));
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
            if (StringUtils.equals("acceptMail", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = SYSTEM_MAILBOX_LOG.ACCEPT_MAIL.asc();
                    sortField[1] = SYSTEM_MAILBOX_LOG.LOG_ID.asc();
                } else {
                    sortField[0] = SYSTEM_MAILBOX_LOG.ACCEPT_MAIL.desc();
                    sortField[1] = SYSTEM_MAILBOX_LOG.LOG_ID.desc();
                }
            }

            if (StringUtils.equals("sendTimeNew", orderColumnName)) {
                sortField = new SortField[1];
                if (isAsc) {
                    sortField[0] = SYSTEM_MAILBOX_LOG.SEND_TIME.asc();
                } else {
                    sortField[0] = SYSTEM_MAILBOX_LOG.SEND_TIME.desc();
                }
            }

            if (StringUtils.equals("sendCondition", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = SYSTEM_MAILBOX_LOG.SEND_CONDITION.asc();
                    sortField[1] = SYSTEM_MAILBOX_LOG.LOG_ID.asc();
                } else {
                    sortField[0] = SYSTEM_MAILBOX_LOG.SEND_CONDITION.desc();
                    sortField[1] = SYSTEM_MAILBOX_LOG.LOG_ID.desc();
                }
            }
        }
        sortToFinish(step, sortField);
    }
}
