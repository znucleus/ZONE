package top.zbeboy.zone.service.data;

import org.jooq.Record;
import org.jooq.Result;
import top.zbeboy.zone.domain.tables.pojos.Building;
import top.zbeboy.zone.domain.tables.records.BuildingRecord;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;

import java.util.List;
import java.util.Optional;

public interface BuildingService {

    /**
     * 通过主键查询
     *
     * @param id 主键
     * @return 数据
     */
    Building findById(int id);

    /**
     * 通过楼id查询所有信息
     * 缓存:是
     *
     * @param id 楼id
     * @return 所有信息
     */
    Optional<Record> findByIdRelation(int id);

    /**
     * 根据院和状态查询全部楼
     *
     * @param collegeId     院id
     * @param buildingIsDel 状态
     * @return 全部楼
     */
    Result<BuildingRecord> findByCollegeIdAndBuildingIsDel(int collegeId, Byte buildingIsDel);

    /**
     * 根据楼名与院id查询
     *
     * @param buildingName 楼名
     * @param collegeId    院
     * @return 数据
     */
    Result<BuildingRecord> findByBuildingNameAndCollegeId(String buildingName, int collegeId);

    /**
     * 查找院下不等于该楼id的楼名
     *
     * @param buildingName 楼名
     * @param collegeId    院id
     * @param buildingId   楼id
     * @return 数据
     */
    Result<BuildingRecord> findByBuildingNameAndCollegeIdNeBuildingId(String buildingName, int collegeId, int buildingId);

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

    /**
     * 保存
     *
     * @param building 数据
     */
    void save(Building building);

    /**
     * 更新
     *
     * @param building 数据
     */
    void update(Building building);

    /**
     * 更新状态
     *
     * @param ids   ids
     * @param isDel 状态
     */
    void updateIsDel(List<Integer> ids, Byte isDel);
}
