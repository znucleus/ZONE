package top.zbeboy.zone.service.data;

import org.jooq.Record;
import org.jooq.Result;
import top.zbeboy.zone.domain.tables.pojos.Science;
import top.zbeboy.zone.domain.tables.records.ScienceRecord;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;

import java.util.List;

public interface ScienceService {

    /**
     * 通过主键查询
     *
     * @param id 主键
     * @return 数据
     */
    Science findById(int id);

    /**
     * 根据系ID和状态查询全部专业
     *
     * @param departmentId 系ID
     * @param scienceIsDel 状态
     * @return 全部专业
     */
    Result<ScienceRecord> findByDepartmentIdAndScienceIsDel(int departmentId, Byte scienceIsDel);

    /**
     * 系下 专业名查询 注：等于专业名
     *
     * @param scienceName  专业名
     * @param departmentId 系id
     * @return 数据
     */
    Result<ScienceRecord> findByScienceNameAndDepartmentId(String scienceName, int departmentId);

    /**
     * 专业代码查询 注：等于专业代码
     *
     * @param scienceCode 专业代码
     * @return 数据
     */
    List<Science> findByScienceCode(String scienceCode);

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
     * @param science 数据
     */
    void save(Science science);

    /**
     * 更新
     *
     * @param science 数据
     */
    void update(Science science);
}
