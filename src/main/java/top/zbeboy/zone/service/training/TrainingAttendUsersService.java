package top.zbeboy.zone.service.training;

import org.jooq.Record11;
import org.jooq.Result;
import top.zbeboy.zone.domain.tables.pojos.TrainingAttendUsers;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;

import java.util.List;

public interface TrainingAttendUsersService {

    /**
     * Query by id
     *
     * @param id primary key
     * @return data
     */
    TrainingAttendUsers findById(String id);

    /**
     * 分页查询
     *
     * @param dataTablesUtil 工具类
     * @return 分页数据
     */
    Result<Record11<String, String, Byte, String, String, String, String, String, String, String, String>> findAllByPage(DataTablesUtil dataTablesUtil);

    /**
     * 数据导出
     *
     * @param dataTablesUtil 工具类
     * @return 数据
     */
    Result<Record11<String, String, Byte, String, String, String, String, String, String, String, String>> export(DataTablesUtil dataTablesUtil);

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
     * 批量保存
     *
     * @param trainingAttendUsers 数据
     */
    void batchSave(List<TrainingAttendUsers> trainingAttendUsers);

    /**
     * update
     *
     * @param trainingAttendUsers data
     */
    void update(TrainingAttendUsers trainingAttendUsers);
}
