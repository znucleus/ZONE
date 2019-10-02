package top.zbeboy.zone.service.data;

import org.jooq.Result;
import top.zbeboy.zone.domain.tables.records.BuildingRecord;

public interface BuildingService {

    /**
     * 根据院和状态查询全部楼
     *
     * @param collegeId     院id
     * @param buildingIsDel 状态
     * @return 全部楼
     */
    Result<BuildingRecord> findByCollegeIdAndBuildingIsDel(int collegeId, Byte buildingIsDel);
}
