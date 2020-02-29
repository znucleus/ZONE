package top.zbeboy.zone.service.register;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.jooq.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.zbeboy.zone.domain.tables.daos.EpidemicRegisterDataDao;
import top.zbeboy.zone.domain.tables.pojos.EpidemicRegisterData;
import top.zbeboy.zone.service.plugin.PaginationPlugin;
import top.zbeboy.zone.service.util.DateTimeUtil;
import top.zbeboy.zone.service.util.SQLQueryUtil;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.Objects;

import static top.zbeboy.zone.domain.Tables.CHANNEL;
import static top.zbeboy.zone.domain.Tables.EPIDEMIC_REGISTER_DATA;

@Service("epidemicRegisterDataService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class EpidemicRegisterDataServiceImpl implements EpidemicRegisterDataService, PaginationPlugin<DataTablesUtil> {

    private final DSLContext create;

    @Resource
    private EpidemicRegisterDataDao epidemicRegisterDataDao;

    @Autowired
    EpidemicRegisterDataServiceImpl(DSLContext dslContext) {
        create = dslContext;
    }

    @Override
    public Result<Record> findAllByPage(DataTablesUtil dataTablesUtil) {
        SelectOnConditionStep<Record> selectOnConditionStep =
                create.select()
                        .from(EPIDEMIC_REGISTER_DATA)
                        .leftJoin(CHANNEL)
                        .on(EPIDEMIC_REGISTER_DATA.CHANNEL_ID.eq(CHANNEL.CHANNEL_ID));
        return queryAllByPage(selectOnConditionStep, dataTablesUtil, false);
    }

    @Override
    public int countAll(DataTablesUtil dataTablesUtil) {
        SelectOnConditionStep<Record1<Integer>> selectOnConditionStep = create.selectCount()
                .from(EPIDEMIC_REGISTER_DATA)
                .leftJoin(CHANNEL)
                .on(EPIDEMIC_REGISTER_DATA.CHANNEL_ID.eq(CHANNEL.CHANNEL_ID));
        return countAll(selectOnConditionStep, dataTablesUtil, true);
    }

    @Override
    public int countByCondition(DataTablesUtil dataTablesUtil) {
        SelectOnConditionStep<Record1<Integer>> selectOnConditionStep = create.selectCount()
                .from(EPIDEMIC_REGISTER_DATA)
                .leftJoin(CHANNEL)
                .on(EPIDEMIC_REGISTER_DATA.CHANNEL_ID.eq(CHANNEL.CHANNEL_ID));
        return countAll(selectOnConditionStep, dataTablesUtil, false);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void save(EpidemicRegisterData epidemicRegisterData) {
        epidemicRegisterDataDao.insert(epidemicRegisterData);
    }

    @Override
    public Condition searchCondition(DataTablesUtil paginationUtil) {
        Condition a = null;
        JSONObject search = paginationUtil.getSearch();
        if (Objects.nonNull(search)) {
            String registerRealName = StringUtils.trim(search.getString("registerRealName"));
            String registerUsername = StringUtils.trim(search.getString("registerUsername"));
            String registerType = StringUtils.trim(search.getString("registerType"));
            String channelId = StringUtils.trim(search.getString("channelId"));
            String epidemicStatus = StringUtils.trim(search.getString("epidemicStatus"));
            String address = StringUtils.trim(search.getString("address"));
            String institute = StringUtils.trim(search.getString("institute"));
            String registerDate = StringUtils.trim(search.getString("registerDate"));
            if (StringUtils.isNotBlank(registerRealName)) {
                a = EPIDEMIC_REGISTER_DATA.REGISTER_REAL_NAME.like(SQLQueryUtil.likeAllParam(registerRealName));
            }

            if (StringUtils.isNotBlank(registerUsername)) {
                if (Objects.isNull(a)) {
                    a = EPIDEMIC_REGISTER_DATA.REGISTER_USERNAME.like(SQLQueryUtil.likeAllParam(registerUsername));
                } else {
                    a = a.and(EPIDEMIC_REGISTER_DATA.REGISTER_USERNAME.like(SQLQueryUtil.likeAllParam(registerUsername)));
                }
            }

            if (StringUtils.isNotBlank(registerType)) {
                if (Objects.isNull(a)) {
                    a = EPIDEMIC_REGISTER_DATA.REGISTER_TYPE.like(SQLQueryUtil.likeAllParam(registerType));
                } else {
                    a = a.and(EPIDEMIC_REGISTER_DATA.REGISTER_TYPE.like(SQLQueryUtil.likeAllParam(registerType)));
                }
            }

            if (StringUtils.isNotBlank(channelId)) {
                int channelIdNumber = NumberUtils.toInt(channelId);
                if (Objects.isNull(a)) {
                    a = EPIDEMIC_REGISTER_DATA.CHANNEL_ID.eq(channelIdNumber);
                } else {
                    a = a.and(EPIDEMIC_REGISTER_DATA.CHANNEL_ID.eq(channelIdNumber));
                }
            }

            if (StringUtils.isNotBlank(epidemicStatus)) {
                if (Objects.isNull(a)) {
                    a = EPIDEMIC_REGISTER_DATA.EPIDEMIC_STATUS.like(SQLQueryUtil.likeAllParam(epidemicStatus));
                } else {
                    a = a.and(EPIDEMIC_REGISTER_DATA.EPIDEMIC_STATUS.like(SQLQueryUtil.likeAllParam(epidemicStatus)));
                }
            }

            if (StringUtils.isNotBlank(address)) {
                if (Objects.isNull(a)) {
                    a = EPIDEMIC_REGISTER_DATA.ADDRESS.like(SQLQueryUtil.likeAllParam(address));
                } else {
                    a = a.and(EPIDEMIC_REGISTER_DATA.ADDRESS.like(SQLQueryUtil.likeAllParam(address)));
                }
            }

            if (StringUtils.isNotBlank(institute)) {
                if (Objects.isNull(a)) {
                    a = EPIDEMIC_REGISTER_DATA.INSTITUTE.like(SQLQueryUtil.likeAllParam(institute));
                } else {
                    a = a.and(EPIDEMIC_REGISTER_DATA.INSTITUTE.like(SQLQueryUtil.likeAllParam(institute)));
                }
            }

            if (StringUtils.isNotBlank(registerDate)) {
                Timestamp startTime;
                Timestamp endTime;
                if (registerDate.contains("至")) {
                    String[] arr = registerDate.split(" 至 ");
                    startTime = DateTimeUtil.defaultParseSqlTimestamp(arr[0] + " 00:00:00");
                    endTime = DateTimeUtil.defaultParseSqlTimestamp(arr[1] + " 23:59:59");
                } else {
                    startTime = DateTimeUtil.defaultParseSqlTimestamp(registerDate + " 00:00:00");
                    endTime = DateTimeUtil.defaultParseSqlTimestamp(registerDate + " 23:59:59");
                }

                if (Objects.isNull(a)) {
                    a = EPIDEMIC_REGISTER_DATA.REGISTER_DATE.gt(startTime).and(EPIDEMIC_REGISTER_DATA.REGISTER_DATE.le(endTime));
                } else {
                    a = a.and(EPIDEMIC_REGISTER_DATA.REGISTER_DATE.gt(startTime).and(EPIDEMIC_REGISTER_DATA.REGISTER_DATE.le(endTime)));
                }
            }
        }
        return a;
    }

    @Override
    public Condition extraCondition(DataTablesUtil paginationUtil) {
        Condition a = null;
        JSONObject search = paginationUtil.getSearch();
        if (Objects.nonNull(search)) {
            String epidemicRegisterReleaseId = StringUtils.trim(search.getString("epidemicRegisterReleaseId"));
            a = EPIDEMIC_REGISTER_DATA.EPIDEMIC_REGISTER_RELEASE_ID.eq(epidemicRegisterReleaseId);
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
            if (StringUtils.equals("registerRealName", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = EPIDEMIC_REGISTER_DATA.REGISTER_REAL_NAME.asc();
                    sortField[1] = EPIDEMIC_REGISTER_DATA.REGISTER_DATE.asc();
                } else {
                    sortField[0] = EPIDEMIC_REGISTER_DATA.REGISTER_REAL_NAME.desc();
                    sortField[1] = EPIDEMIC_REGISTER_DATA.REGISTER_DATE.desc();
                }
            }

            if (StringUtils.equals("registerUsername", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = EPIDEMIC_REGISTER_DATA.REGISTER_USERNAME.asc();
                    sortField[1] = EPIDEMIC_REGISTER_DATA.REGISTER_DATE.asc();
                } else {
                    sortField[0] = EPIDEMIC_REGISTER_DATA.REGISTER_USERNAME.desc();
                    sortField[1] = EPIDEMIC_REGISTER_DATA.REGISTER_DATE.desc();
                }
            }

            if (StringUtils.equals("registerType", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = EPIDEMIC_REGISTER_DATA.REGISTER_TYPE.asc();
                    sortField[1] = EPIDEMIC_REGISTER_DATA.REGISTER_DATE.asc();
                } else {
                    sortField[0] = EPIDEMIC_REGISTER_DATA.REGISTER_TYPE.desc();
                    sortField[1] = EPIDEMIC_REGISTER_DATA.REGISTER_DATE.desc();
                }
            }

            if (StringUtils.equals("epidemicStatus", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = EPIDEMIC_REGISTER_DATA.EPIDEMIC_STATUS.asc();
                    sortField[1] = EPIDEMIC_REGISTER_DATA.REGISTER_DATE.asc();
                } else {
                    sortField[0] = EPIDEMIC_REGISTER_DATA.EPIDEMIC_STATUS.desc();
                    sortField[1] = EPIDEMIC_REGISTER_DATA.REGISTER_DATE.desc();
                }
            }

            if (StringUtils.equals("address", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = EPIDEMIC_REGISTER_DATA.ADDRESS.asc();
                    sortField[1] = EPIDEMIC_REGISTER_DATA.REGISTER_DATE.asc();
                } else {
                    sortField[0] = EPIDEMIC_REGISTER_DATA.ADDRESS.desc();
                    sortField[1] = EPIDEMIC_REGISTER_DATA.REGISTER_DATE.desc();
                }
            }

            if (StringUtils.equals("institute", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = EPIDEMIC_REGISTER_DATA.INSTITUTE.asc();
                    sortField[1] = EPIDEMIC_REGISTER_DATA.REGISTER_DATE.asc();
                } else {
                    sortField[0] = EPIDEMIC_REGISTER_DATA.INSTITUTE.desc();
                    sortField[1] = EPIDEMIC_REGISTER_DATA.REGISTER_DATE.desc();
                }
            }

            if (StringUtils.equals("channelName", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = EPIDEMIC_REGISTER_DATA.CHANNEL_ID.asc();
                    sortField[1] = EPIDEMIC_REGISTER_DATA.REGISTER_DATE.asc();
                } else {
                    sortField[0] = EPIDEMIC_REGISTER_DATA.CHANNEL_ID.desc();
                    sortField[1] = EPIDEMIC_REGISTER_DATA.REGISTER_DATE.desc();
                }
            }

            if (StringUtils.equals("remark", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = EPIDEMIC_REGISTER_DATA.REMARK.asc();
                    sortField[1] = EPIDEMIC_REGISTER_DATA.REGISTER_DATE.asc();
                } else {
                    sortField[0] = EPIDEMIC_REGISTER_DATA.REMARK.desc();
                    sortField[1] = EPIDEMIC_REGISTER_DATA.REGISTER_DATE.desc();
                }
            }

            if (StringUtils.equals("registerDateStr", orderColumnName)) {
                sortField = new SortField[1];
                if (isAsc) {
                    sortField[0] = EPIDEMIC_REGISTER_DATA.REGISTER_DATE.asc();
                } else {
                    sortField[0] = EPIDEMIC_REGISTER_DATA.REGISTER_DATE.desc();
                }
            }
        }
        sortToFinish(step, sortField);
    }
}
