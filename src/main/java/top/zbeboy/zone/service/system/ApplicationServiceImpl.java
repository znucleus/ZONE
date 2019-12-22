package top.zbeboy.zone.service.system;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.jooq.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.zbeboy.zone.config.CacheBook;
import top.zbeboy.zone.domain.tables.daos.ApplicationDao;
import top.zbeboy.zone.domain.tables.pojos.Application;
import top.zbeboy.zone.domain.tables.records.ApplicationRecord;
import top.zbeboy.zone.service.plugin.PaginationPlugin;
import top.zbeboy.zone.service.util.SQLQueryUtil;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static top.zbeboy.zone.domain.Tables.APPLICATION;

@Service("applicationService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class ApplicationServiceImpl implements ApplicationService, PaginationPlugin<DataTablesUtil> {

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
    public List<Application> findByPid(String pid) {
        List<Application> applications = new ArrayList<>();
        Result<ApplicationRecord> applicationRecords = create.selectFrom(APPLICATION)
                .where(APPLICATION.APPLICATION_PID.eq(pid))
                .orderBy(APPLICATION.APPLICATION_SORT.asc())
                .fetch();
        if (applicationRecords.isNotEmpty()) {
            applications = applicationRecords.into(Application.class);
        }
        return applications;
    }

    @Override
    public Result<ApplicationRecord> findInPids(List<String> pids) {
        return create.selectFrom(APPLICATION)
                .where(APPLICATION.APPLICATION_PID.in(pids))
                .fetch();
    }

    @Override
    public List<Application> findByApplicationName(String applicationName) {
        return applicationDao.fetchByApplicationName(applicationName);
    }

    @Override
    public Result<ApplicationRecord> findByApplicationNameNeApplicationId(String applicationName, String applicationId) {
        return create.selectFrom(APPLICATION)
                .where(APPLICATION.APPLICATION_NAME.eq(applicationName).and(APPLICATION.APPLICATION_ID.ne(applicationId)))
                .fetch();
    }

    @Override
    public List<Application> findByApplicationEnName(String applicationEnName) {
        return applicationDao.fetchByApplicationEnName(applicationEnName);
    }

    @Override
    public Result<ApplicationRecord> findByApplicationEnNameNeApplicationId(String applicationEnName, String applicationId) {
        return create.selectFrom(APPLICATION)
                .where(APPLICATION.APPLICATION_EN_NAME.eq(applicationEnName).and(APPLICATION.APPLICATION_ID.ne(applicationId)))
                .fetch();
    }

    @Override
    public List<Application> findByApplicationUrl(String applicationUrl) {
        return applicationDao.fetchByApplicationUrl(applicationUrl);
    }

    @Override
    public Result<ApplicationRecord> findByApplicationUrlNeApplicationId(String applicationUrl, String applicationId) {
        return create.selectFrom(APPLICATION)
                .where(APPLICATION.APPLICATION_URL.eq(applicationUrl).and(APPLICATION.APPLICATION_ID.ne(applicationId)))
                .fetch();
    }

    @Override
    public List<Application> findByApplicationCode(String applicationCode) {
        return applicationDao.fetchByApplicationCode(applicationCode);
    }

    @Override
    public Result<ApplicationRecord> findByApplicationCodeNeApplicationId(String applicationCode, String applicationId) {
        return create.selectFrom(APPLICATION)
                .where(APPLICATION.APPLICATION_CODE.eq(applicationCode).and(APPLICATION.APPLICATION_ID.ne(applicationId)))
                .fetch();
    }

    @Override
    public Result<Record> findAllByPage(DataTablesUtil dataTablesUtil) {
        return queryAllByPage(create, APPLICATION, dataTablesUtil, false);
    }

    @Override
    public int countAll() {
        return countAll(create, APPLICATION);
    }

    @Override
    public int countByCondition(DataTablesUtil dataTablesUtil) {
        return countAll(create, APPLICATION, dataTablesUtil, false);
    }

    @CacheEvict(cacheNames = {CacheBook.MENU, CacheBook.ROLES_APPLICATION}, allEntries = true)
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void save(Application application) {
        applicationDao.insert(application);
    }

    @CacheEvict(cacheNames = {CacheBook.MENU, CacheBook.ROLES_APPLICATION}, allEntries = true)
    @Override
    public void update(Application application) {
        applicationDao.update(application);
    }

    @Override
    public void deletes(List<String> ids) {
        applicationDao.deleteById(ids);
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
