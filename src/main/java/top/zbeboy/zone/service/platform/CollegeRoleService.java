package top.zbeboy.zone.service.platform;

import org.jooq.Record;
import org.jooq.Result;
import top.zbeboy.zone.domain.tables.pojos.CollegeRole;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;

public interface CollegeRoleService {

    /**
     * 通过角色名和院id查询
     *
     * @param roleName  角色名
     * @param collegeId 院id
     * @return 结果集
     */
    Result<Record> findByRoleNameAndCollegeId(String roleName, int collegeId);

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
     * @param dataTablesUtil 工具类
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
     * @param collegeRole 数据
     */
    void save(CollegeRole collegeRole);
}
