package top.zbeboy.zone.feign.data;

import org.jooq.Record;
import org.jooq.Result;
import top.zbeboy.zone.domain.tables.pojos.Schoolroom;
import top.zbeboy.zone.domain.tables.records.SchoolroomRecord;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;

import java.util.List;
import java.util.Optional;

public interface SchoolroomService {

    /**
     * 通过主键查询
     *
     * @param id 主键
     * @return 数据
     */
    Schoolroom findById(int id);

    /**
     * 通过教室id查询所有信息
     * 缓存:是
     *
     * @param id 教室id
     * @return 所有信息
     */
    Optional<Record> findByIdRelation(int id);

    /**
     * 根据状态与楼id查询全部教室
     *
     * @param buildingId      楼id
     * @param schoolroomIsDel 状态
     * @return 全部教室
     */
    Result<SchoolroomRecord> findByBuildingIdAndSchoolroomIsDel(int buildingId, Byte schoolroomIsDel);

    /**
     * 通过教室与楼id查询
     *
     * @param buildingCode 教室
     * @param buildingId   楼id
     * @return 数据
     */
    Result<SchoolroomRecord> findByBuildingCodeAndBuildingId(String buildingCode, int buildingId);

    /**
     * 检验一栋里是否有相同教室名
     *
     * @param buildingCode 教室
     * @param schoolroomId 教室id
     * @param buildingId   楼id
     * @return 数据
     */
    Result<SchoolroomRecord> findByBuildingCodeAndBuildingIdNeSchoolroomId(String buildingCode, int buildingId, int schoolroomId);

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
     * @param schoolroom 数据
     */
    void save(Schoolroom schoolroom);

    /**
     * 更新
     *
     * @param schoolroom 数据
     */
    void update(Schoolroom schoolroom);

    /**
     * 更新状态
     *
     * @param ids   ids
     * @param isDel 状态
     */
    void updateIsDel(List<Integer> ids, Byte isDel);
}
