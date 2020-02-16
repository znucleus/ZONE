package top.zbeboy.zone.service.internship;

import org.jooq.Record;
import org.jooq.Result;
import top.zbeboy.zone.domain.tables.pojos.InternshipRegulate;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;

public interface InternshipRegulateService {

    /**
     * 通过主键查询
     *
     * @param id 主键
     * @return 数据
     */
    InternshipRegulate findById(String id);

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
     * @param internshipRegulate 数据
     */
    void save(InternshipRegulate internshipRegulate);

    /**
     * 更新
     *
     * @param internshipRegulate 数据
     */
    void update(InternshipRegulate internshipRegulate);
}
