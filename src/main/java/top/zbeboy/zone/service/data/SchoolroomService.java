package top.zbeboy.zone.service.data;

import org.jooq.Record;
import org.jooq.Result;
import top.zbeboy.zone.domain.tables.records.SchoolroomRecord;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;

public interface SchoolroomService {

    /**
     * 根据状态与楼id查询全部教室
     *
     * @param buildingId      楼id
     * @param schoolroomIsDel 状态
     * @return 全部教室
     */
    Result<SchoolroomRecord> findByBuildingIdAndSchoolroomIsDel(int buildingId, Byte schoolroomIsDel);

    /**
     * 分页查询
     *
     * @param dataTablesUtil 工具类
     * @return 分页数据
     */
    Result<Record> findAllByPage(DataTablesUtil dataTablesUtil);

    /**
     * 应用 总数
     *
     * @return 总数
     */
    int countAll(DataTablesUtil dataTablesUtil);

    /**
     * 根据条件查询总数
     *
     * @return 条件查询总数
     */
    int countByCondition(DataTablesUtil dataTablesUtil);
}
