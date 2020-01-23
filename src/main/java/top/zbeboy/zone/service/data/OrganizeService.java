package top.zbeboy.zone.service.data;

import org.jooq.Record;
import org.jooq.Result;
import top.zbeboy.zone.domain.tables.pojos.Organize;
import top.zbeboy.zone.domain.tables.records.OrganizeRecord;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;

import java.util.Optional;

public interface OrganizeService {

    /**
     * 通过主键查询
     *
     * @param id 主键
     * @return 数据
     */
    Organize findById(int id);

    /**
     * 通过班级id查询所有信息
     * 缓存:是
     *
     * @param id 班级id
     * @return 所有信息
     */
    Optional<Record> findByIdRelation(int id);

    /**
     * 根据年级ID和状态查询全部班级
     *
     * @param gradeId       年级ID
     * @param organizeIsDel 状态
     * @return 全部班级
     */
    Result<OrganizeRecord> findByGradeIdAndOrganizeIsDel(int gradeId, Byte organizeIsDel);

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
