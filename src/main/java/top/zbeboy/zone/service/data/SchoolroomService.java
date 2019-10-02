package top.zbeboy.zone.service.data;

import org.jooq.Result;
import top.zbeboy.zone.domain.tables.records.SchoolroomRecord;

public interface SchoolroomService {

    /**
     * 根据状态与楼id查询全部教室
     *
     * @param buildingId      楼id
     * @param schoolroomIsDel 状态
     * @return 全部教室
     */
    Result<SchoolroomRecord> findByBuildingIdAndSchoolroomIsDel(int buildingId, Byte schoolroomIsDel);
}
