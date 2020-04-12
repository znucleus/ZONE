package top.zbeboy.zone.service.register;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.jooq.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.zbeboy.zone.config.Workbook;
import top.zbeboy.zone.domain.tables.daos.LeaverRegisterReleaseDao;
import top.zbeboy.zone.domain.tables.pojos.LeaverRegisterRelease;
import top.zbeboy.zone.domain.tables.pojos.Users;
import top.zbeboy.zone.service.platform.UsersService;
import top.zbeboy.zone.service.plugin.PaginationPlugin;
import top.zbeboy.zone.service.util.SQLQueryUtil;
import top.zbeboy.zone.web.util.pagination.SimplePaginationUtil;

import javax.annotation.Resource;
import java.util.Objects;

import static top.zbeboy.zone.domain.Tables.LEAVER_REGISTER_RELEASE;

@Service("leaverRegisterReleaseService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class LeaverRegisterReleaseServiceImpl implements LeaverRegisterReleaseService, PaginationPlugin<SimplePaginationUtil> {

    private final DSLContext create;

    @Resource
    private LeaverRegisterReleaseDao leaverRegisterReleaseDao;

    @Resource
    private UsersService usersService;

    @Autowired
    LeaverRegisterReleaseServiceImpl(DSLContext dslContext) {
        create = dslContext;
    }

    @Override
    public LeaverRegisterRelease findById(String id) {
        return leaverRegisterReleaseDao.findById(id);
    }

    @Override
    public Result<Record> findAllByPage(SimplePaginationUtil paginationUtil) {
        return queryAllByPage(create, LEAVER_REGISTER_RELEASE, paginationUtil, false);
    }

    @Override
    public int countAll(SimplePaginationUtil paginationUtil) {
        return countAll(create, LEAVER_REGISTER_RELEASE, paginationUtil, false);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void save(LeaverRegisterRelease leaverRegisterRelease) {
        leaverRegisterReleaseDao.insert(leaverRegisterRelease);
    }

    @Override
    public void update(LeaverRegisterRelease leaverRegisterRelease) {
        leaverRegisterReleaseDao.update(leaverRegisterRelease);
    }

    @Override
    public void deleteById(String id) {
        leaverRegisterReleaseDao.deleteById(id);
    }

    @Override
    public Condition searchCondition(SimplePaginationUtil paginationUtil) {
        Condition a = null;
        JSONObject search = paginationUtil.getSearch();
        if (Objects.nonNull(search)) {
            String title = StringUtils.trim(search.getString("title"));
            if (StringUtils.isNotBlank(title)) {
                a = LEAVER_REGISTER_RELEASE.TITLE.like(SQLQueryUtil.likeAllParam(title));
            }
        }
        return a;
    }

    @Override
    public Condition extraCondition(SimplePaginationUtil paginationUtil) {
        Condition a = null;
        JSONObject search = paginationUtil.getSearch();

        Users users = usersService.getUserByChannel(paginationUtil.getChannel(), paginationUtil.getPrincipal());
        if (Objects.nonNull(search)) {
            String dataRange = StringUtils.trim(search.getString("dataRange"));
            if (StringUtils.isBlank(dataRange)) {
                dataRange = "0";// 默认全部
            }

            int dataRangeInt = NumberUtils.toInt(dataRange);

            // 查看与本人相关
            if (dataRangeInt == 1) {
                a = LEAVER_REGISTER_RELEASE.USERNAME.eq(users.getUsername());
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
                    sortField[0] = LEAVER_REGISTER_RELEASE.RELEASE_TIME.asc();
                } else {
                    sortField[0] = LEAVER_REGISTER_RELEASE.RELEASE_TIME.desc();
                }
            }
        }
        sortToFinish(step, sortField);
    }
}
