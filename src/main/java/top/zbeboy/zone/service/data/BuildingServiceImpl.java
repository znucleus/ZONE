package top.zbeboy.zone.service.data;

import org.jooq.DSLContext;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.zbeboy.zone.config.CacheBook;
import top.zbeboy.zone.domain.tables.daos.BuildingDao;
import top.zbeboy.zone.domain.tables.records.BuildingRecord;

import javax.annotation.Resource;

import static top.zbeboy.zone.domain.Tables.BUILDING;

@Service("buildingService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class BuildingServiceImpl implements BuildingService {

    @Resource
    private BuildingDao buildingDao;

    private final DSLContext create;

    @Autowired
    BuildingServiceImpl(DSLContext dslContext) {
        create = dslContext;
    }

    @Cacheable(cacheNames = CacheBook.BUILDINGS, key = "#collegeId + '_' + #buildingIsDel")
    @Override
    public Result<BuildingRecord> findByCollegeIdAndBuildingIsDel(int collegeId, Byte buildingIsDel) {
        return create.selectFrom(BUILDING).where(BUILDING.COLLEGE_ID.eq(collegeId)
                .and(BUILDING.BUILDING_IS_DEL.eq(buildingIsDel))).fetch();
    }
}
