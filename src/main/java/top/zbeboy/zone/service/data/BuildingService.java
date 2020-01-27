package top.zbeboy.zone.service.data;

import org.jooq.Record;
import org.jooq.Result;
import top.zbeboy.zone.domain.tables.records.BuildingRecord;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;

public interface BuildingService {

    /**
     * 根据院和状态查询全部楼
     *
     * @param collegeId     院id
     * @param buildingIsDel 状态
     * @return 全部楼
     */
    Result<BuildingRecord> findByCollegeIdAndBuildingIsDel(int collegeId, Byte buildingIsDel);

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
