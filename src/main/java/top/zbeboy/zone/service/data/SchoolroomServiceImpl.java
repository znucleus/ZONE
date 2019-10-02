package top.zbeboy.zone.service.data;

import org.jooq.DSLContext;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.zbeboy.zone.config.CacheBook;
import top.zbeboy.zone.domain.tables.records.SchoolroomRecord;

import static top.zbeboy.zone.domain.Tables.SCHOOLROOM;

@Service("schoolroomService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class SchoolroomServiceImpl implements SchoolroomService {

    private final DSLContext create;

    @Autowired
    SchoolroomServiceImpl(DSLContext dslContext) {
        create = dslContext;
    }

    @Cacheable(cacheNames = CacheBook.SCHOOLROOMS, key = "#buildingId + '_' + #schoolroomIsDel")
    @Override
    public Result<SchoolroomRecord> findByBuildingIdAndSchoolroomIsDel(int buildingId, Byte schoolroomIsDel) {
        return create.selectFrom(SCHOOLROOM).where(SCHOOLROOM.BUILDING_ID.eq(buildingId)
                .and(SCHOOLROOM.SCHOOLROOM_IS_DEL.eq(schoolroomIsDel))).fetch();
    }
}
