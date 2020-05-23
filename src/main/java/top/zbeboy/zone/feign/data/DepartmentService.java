package top.zbeboy.zone.feign.data;

import org.jooq.Record;
import org.jooq.Result;
import top.zbeboy.zone.domain.tables.pojos.Department;
import top.zbeboy.zone.domain.tables.records.DepartmentRecord;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;

import java.util.List;
import java.util.Optional;

public interface DepartmentService {

    /**
     * 通过主键查询
     *
     * @param id 主键
     * @return 数据
     */
    Department findById(int id);

    /**
     * 通过系id查询所有信息
     * 缓存:是
     *
     * @param id 系id
     * @return 所有信息
     */
    Optional<Record> findByIdRelation(int id);

    /**
     * 根据院ID和状态查询全部系
     *
     * @param collegeId       院ID
     * @param departmentIsDel 状态
     * @return 全部系
     */
    Result<DepartmentRecord> findByCollegeIdAndDepartmentIsDel(int collegeId, Byte departmentIsDel);

    /**
     * 院下 系名查询 注：等于系名
     *
     * @param departmentName 系名
     * @param collegeId      院id
     * @return 数据
     */
    Result<DepartmentRecord> findByDepartmentNameAndCollegeId(String departmentName, int collegeId);

    /**
     * 查找院下不等于该系id的系名
     *
     * @param departmentName 系名
     * @param departmentId   系id
     * @param collegeId      院id
     * @return 数据
     */
    Result<DepartmentRecord> findByDepartmentNameAndCollegeIdNeDepartmentId(String departmentName, int collegeId, int departmentId);

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
     * @param department 数据
     */
    void save(Department department);

    /**
     * 更新
     *
     * @param department 数据
     */
    void update(Department department);

    /**
     * 更新状态
     *
     * @param ids   ids
     * @param isDel 状态
     */
    void updateIsDel(List<Integer> ids, Byte isDel);
}
