package top.zbeboy.zone.service.internship;

import org.jooq.Record;
import org.jooq.Record2;
import org.jooq.Result;
import top.zbeboy.zone.web.util.pagination.SimplePaginationUtil;

public interface InternshipReviewService {

    /**
     * 分页查询
     *
     * @param paginationUtil 数据
     * @return 数据
     */
    Result<Record> findAllByPage(SimplePaginationUtil paginationUtil);

    /**
     * 总数
     *
     * @return 总数
     */
    int countAll(SimplePaginationUtil paginationUtil);

    /**
     * 统计一个实习不同状态下的数据
     *
     * @param internshipReleaseId  实习发布id
     * @param internshipApplyState 状态
     * @return 统计结果
     */
    int countByInternshipReleaseIdAndInternshipApplyState(String internshipReleaseId, int internshipApplyState);

    /**
     * 查询班级数据
     *
     * @param internshipReleaseId 实习发布id
     * @return 数据
     */
    Result<Record2<Integer, String>> findDistinctOrganize(String internshipReleaseId);
}
