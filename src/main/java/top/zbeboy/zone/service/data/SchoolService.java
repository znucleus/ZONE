package top.zbeboy.zone.service.data;

import org.jooq.Record;
import org.jooq.Result;
import top.zbeboy.zone.domain.tables.pojos.School;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;

import java.util.List;

public interface SchoolService {

    /**
     * 根据状态查询全部学校
     *
     * @param schoolIsDel 状态
     * @return 全部学校
     */
    List<School> findBySchoolIsDel(Byte schoolIsDel);

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
}
