package top.zbeboy.zone.service.internship;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.jooq.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.zbeboy.zbase.domain.tables.daos.InternshipReviewAuthorizeDao;
import top.zbeboy.zbase.domain.tables.pojos.InternshipReviewAuthorize;
import top.zbeboy.zone.service.plugin.PaginationPlugin;
import top.zbeboy.zbase.tools.web.util.pagination.SimplePaginationUtil;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.Optional;

import static top.zbeboy.zbase.domain.Tables.INTERNSHIP_REVIEW_AUTHORIZE;
import static top.zbeboy.zbase.domain.Tables.USERS;

@Service("internshipReviewAuthorizeService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class InternshipReviewAuthorizeServiceImpl implements InternshipReviewAuthorizeService, PaginationPlugin<SimplePaginationUtil> {

    private final DSLContext create;

    @Resource
    private InternshipReviewAuthorizeDao internshipReviewAuthorizeDao;

    @Autowired
    InternshipReviewAuthorizeServiceImpl(DSLContext dslContext) {
        create = dslContext;
    }

    @Override
    public Optional<Record> findByInternshipReleaseIdAndUsername(String internshipReleaseId, String username) {
        return create.select()
                .from(INTERNSHIP_REVIEW_AUTHORIZE)
                .where(INTERNSHIP_REVIEW_AUTHORIZE.INTERNSHIP_RELEASE_ID.eq(internshipReleaseId)
                        .and(INTERNSHIP_REVIEW_AUTHORIZE.USERNAME.eq(username)))
                .fetchOptional();
    }

    @Override
    public Result<Record> findAll(SimplePaginationUtil simplePaginationUtil) {
        SelectOnConditionStep<Record> selectOnConditionStep = create.select()
                .from(INTERNSHIP_REVIEW_AUTHORIZE)
                .leftJoin(USERS)
                .on(INTERNSHIP_REVIEW_AUTHORIZE.USERNAME.eq(USERS.USERNAME));
        return queryAll(selectOnConditionStep, simplePaginationUtil, true);
    }

    @Override
    public int countAll(SimplePaginationUtil paginationUtil) {
        SelectOnConditionStep<Record1<Integer>> selectOnConditionStep = create.selectCount()
                .from(INTERNSHIP_REVIEW_AUTHORIZE)
                .leftJoin(USERS)
                .on(INTERNSHIP_REVIEW_AUTHORIZE.USERNAME.eq(USERS.USERNAME));
        return countAll(selectOnConditionStep, paginationUtil, true);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void save(InternshipReviewAuthorize internshipReviewAuthorize) {
        internshipReviewAuthorizeDao.insert(internshipReviewAuthorize);
    }

    @Override
    public void deleteByInternshipReleaseIdAndUsername(String internshipReleaseId, String username) {
        create.deleteFrom(INTERNSHIP_REVIEW_AUTHORIZE)
                .where(INTERNSHIP_REVIEW_AUTHORIZE.INTERNSHIP_RELEASE_ID.eq(internshipReleaseId)
                        .and(INTERNSHIP_REVIEW_AUTHORIZE.USERNAME.eq(username)))
                .execute();
    }

    @Override
    public Condition extraCondition(SimplePaginationUtil paginationUtil) {
        Condition a = null;
        JSONObject search = paginationUtil.getSearch();
        if (Objects.nonNull(search)) {
            String internshipReleaseId = StringUtils.trim(search.getString("internshipReleaseId"));
            if (StringUtils.isNotBlank(internshipReleaseId)) {
                a = INTERNSHIP_REVIEW_AUTHORIZE.INTERNSHIP_RELEASE_ID.eq(internshipReleaseId);
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
            if (StringUtils.equals("username", orderColumnName)) {
                sortField = new SortField[1];
                if (isAsc) {
                    sortField[0] = INTERNSHIP_REVIEW_AUTHORIZE.USERNAME.asc();
                } else {
                    sortField[0] = INTERNSHIP_REVIEW_AUTHORIZE.USERNAME.desc();
                }
            }
        }
        sortToFinish(step, sortField);
    }
}
