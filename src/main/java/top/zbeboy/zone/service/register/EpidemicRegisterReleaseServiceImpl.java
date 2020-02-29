package top.zbeboy.zone.service.register;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.jooq.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.zbeboy.zone.domain.tables.daos.EpidemicRegisterReleaseDao;
import top.zbeboy.zone.domain.tables.pojos.EpidemicRegisterRelease;
import top.zbeboy.zone.service.plugin.PaginationPlugin;
import top.zbeboy.zone.service.util.SQLQueryUtil;
import top.zbeboy.zone.web.util.pagination.SimplePaginationUtil;

import javax.annotation.Resource;
import java.util.Objects;

import static top.zbeboy.zone.domain.Tables.EPIDEMIC_REGISTER_RELEASE;

@Service("epidemicRegisterReleaseService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class EpidemicRegisterReleaseServiceImpl implements EpidemicRegisterReleaseService, PaginationPlugin<SimplePaginationUtil> {

    private final DSLContext create;

    @Resource
    private EpidemicRegisterReleaseDao epidemicRegisterReleaseDao;

    @Autowired
    EpidemicRegisterReleaseServiceImpl(DSLContext dslContext) {
        create = dslContext;
    }

    @Override
    public EpidemicRegisterRelease findById(String id) {
        return epidemicRegisterReleaseDao.findById(id);
    }

    @Override
    public Result<Record> findAllByPage(SimplePaginationUtil paginationUtil) {
        return queryAllByPage(create, EPIDEMIC_REGISTER_RELEASE, paginationUtil, false);
    }

    @Override
    public int countAll(SimplePaginationUtil paginationUtil) {
        return countAll(create, EPIDEMIC_REGISTER_RELEASE, paginationUtil, false);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void save(EpidemicRegisterRelease epidemicRegisterRelease) {
        epidemicRegisterReleaseDao.insert(epidemicRegisterRelease);
    }

    @Override
    public void update(EpidemicRegisterRelease epidemicRegisterRelease) {
        epidemicRegisterReleaseDao.update(epidemicRegisterRelease);
    }

    @Override
    public void deleteById(String id) {
        epidemicRegisterReleaseDao.deleteById(id);
    }

    @Override
    public Condition searchCondition(SimplePaginationUtil paginationUtil) {
        Condition a = null;
        JSONObject search = paginationUtil.getSearch();
        if (Objects.nonNull(search)) {
            String title = StringUtils.trim(search.getString("title"));
            if (StringUtils.isNotBlank(title)) {
                a = EPIDEMIC_REGISTER_RELEASE.TITLE.like(SQLQueryUtil.likeAllParam(title));
            }
        }
        return a;
    }

    @Override
    public void sortCondition(SelectConnectByStep<Record> step, SimplePaginationUtil paginationUtil) {
        String orderColumnName = paginationUtil.getOrderColumnName();
        String orderDir = paginationUtil.getOrderDir();
        boolean isAsc = StringUtils.equalsIgnoreCase("asc", orderDir);
        SortField[] sortField = null;
        if (StringUtils.isNotBlank(orderColumnName)) {
            if (StringUtils.equals("releaseTime", orderColumnName)) {
                sortField = new SortField[1];
                if (isAsc) {
                    sortField[0] = EPIDEMIC_REGISTER_RELEASE.RELEASE_TIME.asc();
                } else {
                    sortField[0] = EPIDEMIC_REGISTER_RELEASE.RELEASE_TIME.desc();
                }
            }
        }
        sortToFinish(step, sortField);
    }
}
