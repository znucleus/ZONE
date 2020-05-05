package top.zbeboy.zone.service.training;

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
import top.zbeboy.zone.domain.tables.daos.TrainingSpecialDao;
import top.zbeboy.zone.domain.tables.pojos.TrainingSpecial;
import top.zbeboy.zone.service.plugin.PaginationPlugin;
import top.zbeboy.zone.service.util.SQLQueryUtil;
import top.zbeboy.zone.web.util.pagination.SimplePaginationUtil;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.Optional;

import static top.zbeboy.zone.domain.Tables.FILES;
import static top.zbeboy.zone.domain.Tables.TRAINING_SPECIAL;

@Service("trainingSpecialService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class TrainingSpecialServiceImpl implements TrainingSpecialService, PaginationPlugin<SimplePaginationUtil> {

    private final DSLContext create;

    @Resource
    private TrainingSpecialDao trainingSpecialDao;

    @Autowired
    TrainingSpecialServiceImpl(DSLContext dslContext) {
        create = dslContext;
    }

    @Cacheable(cacheNames = CacheBook.TRAINING_SPECIAL, key = "#id")
    @Override
    public Optional<Record> findByIdRelation(String id) {
        return create.select()
                .from(TRAINING_SPECIAL)
                .leftJoin(FILES)
                .on(TRAINING_SPECIAL.COVER.eq(FILES.FILE_ID))
                .where(TRAINING_SPECIAL.TRAINING_SPECIAL_ID.eq(id))
                .fetchOptional();
    }

    @Override
    public Result<Record> findAllByPage(SimplePaginationUtil paginationUtil) {
        SelectOnConditionStep<Record> selectOnConditionStep = create.select()
                .from(TRAINING_SPECIAL)
                .leftJoin(FILES)
                .on(TRAINING_SPECIAL.COVER.eq(FILES.FILE_ID));
        return queryAllByPage(selectOnConditionStep, paginationUtil, false);
    }

    @Override
    public int countAll(SimplePaginationUtil paginationUtil) {
        return countAll(create, TRAINING_SPECIAL, paginationUtil, false);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void save(TrainingSpecial trainingSpecial) {
        trainingSpecialDao.insert(trainingSpecial);
    }

    @CacheEvict(cacheNames = CacheBook.TRAINING_SPECIAL, key = "#trainingSpecial.trainingSpecialId")
    @Override
    public void update(TrainingSpecial trainingSpecial) {
        trainingSpecialDao.update(trainingSpecial);
    }

    @CacheEvict(cacheNames = CacheBook.TRAINING_SPECIAL, key = "#id")
    @Override
    public void deleteById(String id) {
        trainingSpecialDao.deleteById(id);
    }

    @Override
    public Condition searchCondition(SimplePaginationUtil paginationUtil) {
        Condition a = null;
        JSONObject search = paginationUtil.getSearch();
        if (Objects.nonNull(search)) {
            String title = StringUtils.trim(search.getString("title"));
            if (StringUtils.isNotBlank(title)) {
                a = TRAINING_SPECIAL.TITLE.like(SQLQueryUtil.likeAllParam(title));
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
                    sortField[0] = TRAINING_SPECIAL.RELEASE_TIME.asc();
                } else {
                    sortField[0] = TRAINING_SPECIAL.RELEASE_TIME.desc();
                }
            }
        }
        sortToFinish(step, sortField);
    }
}
