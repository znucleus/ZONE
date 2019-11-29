package top.zbeboy.zone.service.system;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.jooq.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.zbeboy.zone.domain.tables.daos.ApplicationDao;
import top.zbeboy.zone.domain.tables.pojos.Application;
import top.zbeboy.zone.service.plugin.PaginationPlugin;
import top.zbeboy.zone.service.util.SQLQueryUtil;
import top.zbeboy.zone.web.bean.notify.UserNotifyBean;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;

import javax.annotation.Resource;
import java.util.Objects;

import static top.zbeboy.zone.domain.Tables.APPLICATION;

@Service("applicationService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class ApplicationServiceImpl implements ApplicationService, PaginationPlugin<UserNotifyBean, DataTablesUtil> {

    private final DSLContext create;

    @Resource
    private ApplicationDao applicationDao;

    @Autowired
    ApplicationServiceImpl(DSLContext dslContext) {
        create = dslContext;
    }

    @Override
    public Application findById(String id) {
        return applicationDao.findById(id);
    }

    @Override
    public Result<Record> findAllByPage(DataTablesUtil dataTablesUtil) {
        return queryAllByPage(create, APPLICATION, dataTablesUtil);
    }

    @Override
    public int countAll() {
        return countAll(create, APPLICATION);
    }

    @Override
    public int countByCondition(DataTablesUtil dataTablesUtil) {
        return countAll(create, APPLICATION, dataTablesUtil);
    }

    @Override
    public Condition searchCondition(DataTablesUtil paginationUtil) {
        Condition a = null;
        JSONObject search = paginationUtil.getSearch();
        if (Objects.nonNull(search)) {
            String applicationName = StringUtils.trim(search.getString("applicationName"));
            String applicationEnName = StringUtils.trim(search.getString("applicationEnName"));
            String applicationCode = StringUtils.trim(search.getString("applicationCode"));
            if (StringUtils.isNotBlank(applicationName)) {
                a = APPLICATION.APPLICATION_NAME.like(SQLQueryUtil.likeAllParam(applicationName));
            }

            if (StringUtils.isNotBlank(applicationEnName)) {
                if (Objects.isNull(a)) {
                    a = APPLICATION.APPLICATION_EN_NAME.like(SQLQueryUtil.likeAllParam(applicationEnName));
                } else {
                    a = a.and(APPLICATION.APPLICATION_EN_NAME.like(SQLQueryUtil.likeAllParam(applicationEnName)));
                }
            }

            if (StringUtils.isNotBlank(applicationCode)) {
                if (Objects.isNull(a)) {
                    a = APPLICATION.APPLICATION_CODE.like(SQLQueryUtil.likeAllParam(applicationCode));
                } else {
                    a = a.and(APPLICATION.APPLICATION_CODE.like(SQLQueryUtil.likeAllParam(applicationCode)));
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
            if (StringUtils.equals("applicationName", orderColumnName)) {
                sortField = new SortField[1];
                if (isAsc) {
                    sortField[0] = APPLICATION.APPLICATION_NAME.asc();
                } else {
                    sortField[0] = APPLICATION.APPLICATION_NAME.desc();
                }
            }

            if (StringUtils.equals("applicationEnName", orderColumnName)) {
                sortField = new SortField[1];
                if (isAsc) {
                    sortField[0] = APPLICATION.APPLICATION_EN_NAME.asc();
                } else {
                    sortField[0] = APPLICATION.APPLICATION_EN_NAME.desc();
                }
            }

            if (StringUtils.equals("applicationPid", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = APPLICATION.APPLICATION_PID.asc();
                    sortField[1] = APPLICATION.APPLICATION_ID.asc();
                } else {
                    sortField[0] = APPLICATION.APPLICATION_PID.desc();
                    sortField[1] = APPLICATION.APPLICATION_ID.desc();
                }
            }

            if (StringUtils.equals("applicationUrl", orderColumnName)) {
                sortField = new SortField[1];
                if (isAsc) {
                    sortField[0] = APPLICATION.APPLICATION_URL.asc();
                } else {
                    sortField[0] = APPLICATION.APPLICATION_URL.desc();
                }
            }

            if (StringUtils.equals("icon", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = APPLICATION.ICON.asc();
                    sortField[1] = APPLICATION.APPLICATION_ID.asc();
                } else {
                    sortField[0] = APPLICATION.ICON.desc();
                    sortField[1] = APPLICATION.APPLICATION_ID.desc();
                }
            }

            if (StringUtils.equals("applicationSort", orderColumnName)) {
                sortField = new SortField[1];
                if (isAsc) {
                    sortField[0] = APPLICATION.APPLICATION_SORT.asc();
                } else {
                    sortField[0] = APPLICATION.APPLICATION_SORT.desc();
                }
            }

            if (StringUtils.equals("applicationCode", orderColumnName)) {
                sortField = new SortField[1];
                if (isAsc) {
                    sortField[0] = APPLICATION.APPLICATION_CODE.asc();
                } else {
                    sortField[0] = APPLICATION.APPLICATION_CODE.desc();
                }
            }

            if (StringUtils.equals("applicationDataUrlStartWith", orderColumnName)) {
                sortField = new SortField[1];
                if (isAsc) {
                    sortField[0] = APPLICATION.APPLICATION_DATA_URL_START_WITH.asc();
                } else {
                    sortField[0] = APPLICATION.APPLICATION_DATA_URL_START_WITH.desc();
                }
            }
        }
        sortToFinish(step, sortField);
    }
}
