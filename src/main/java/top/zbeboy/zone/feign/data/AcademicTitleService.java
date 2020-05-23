package top.zbeboy.zone.feign.data;

import org.jooq.Record;
import org.jooq.Result;
import top.zbeboy.zone.domain.tables.pojos.AcademicTitle;
import top.zbeboy.zone.domain.tables.records.AcademicTitleRecord;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;

import java.util.List;

public interface AcademicTitleService {

    /**
     * 根据主键查询
     *
     * @param id 主键
     * @return 数据
     */
    AcademicTitle findById(int id);

    /**
     * 查询全部
     *
     * @return 全部数据
     */
    List<AcademicTitle> findAll();

    /**
     * 通过职称查询
     *
     * @param academicTitleName 职称
     * @return 职称
     */
    List<AcademicTitle> findByAcademicTitleName(String academicTitleName);

    /**
     * 通过职称查询 注：不等于职称id
     *
     * @param academicTitleName 职称
     * @param academicTitleId   职称id
     * @return 职称
     */
    Result<AcademicTitleRecord> findByAcademicTitleNameNeAcademicTitleId(String academicTitleName, int academicTitleId);

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
    int countAll();

    /**
     * 根据条件查询总数
     *
     * @return 条件查询总数
     */
    int countByCondition(DataTablesUtil dataTablesUtil);

    /**
     * 保存
     *
     * @param academicTitle 数据
     */
    void save(AcademicTitle academicTitle);

    /**
     * 更新
     *
     * @param academicTitle 数据
     */
    void update(AcademicTitle academicTitle);
}
