package top.zbeboy.zone.service.data;

import org.jooq.Record;
import org.jooq.Result;
import top.zbeboy.zone.domain.tables.pojos.Science;
import top.zbeboy.zone.domain.tables.records.ScienceRecord;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;

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
