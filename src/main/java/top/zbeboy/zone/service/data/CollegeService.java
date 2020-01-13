package top.zbeboy.zone.service.data;

import org.jooq.Record;
import org.jooq.Result;
import top.zbeboy.zone.domain.tables.records.CollegeRecord;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;

import java.util.Optional;

public interface CollegeService {

    /**
     * 通过id关联查询
     *
     * @param id 院id
     * @return 数据
     */
    Optional<Record> findByIdRelation(int id);

    /**
     * 根据学校ID和状态查询全部院
     *
     * @param schoolId     学校ID
     * @param collegeIsDel 状态
     * @return 全部院
     */
    Result<CollegeRecord> findBySchoolIdAndCollegeIsDel(int schoolId, Byte collegeIsDel);

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
