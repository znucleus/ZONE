package top.zbeboy.zone.service.internship;

import org.jooq.Record;
import org.jooq.Result;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;

public interface InternshipStatisticsService {

    /**
     * 分页查询
     *
     * @param dataTablesUtil 工具类
     * @return 分页数据
     */
    Result<Record> findAllByPage(DataTablesUtil dataTablesUtil);

    /**
     * 总数
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
     * 已提交数据 总数
     *
     * @param internshipReleaseId 实习发布id
     * @return 总数
     */
    int countSubmitted(String internshipReleaseId);

    /**
     * 未提交数据 总数
     *
     * @param internshipReleaseId 实习发布id
     * @return 总数
     */
    int countUnSubmitted(String internshipReleaseId);
}
