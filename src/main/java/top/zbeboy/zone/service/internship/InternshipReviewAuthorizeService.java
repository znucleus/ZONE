package top.zbeboy.zone.service.internship;

import org.jooq.Record;
import org.jooq.Result;
import top.zbeboy.zone.domain.tables.pojos.InternshipReviewAuthorize;
import top.zbeboy.zone.web.util.pagination.SimplePaginationUtil;

import java.util.Optional;

public interface InternshipReviewAuthorizeService {

    /**
     * 通过实习id与账号查询
     *
     * @param internshipReleaseId 实习发布id
     * @param username            账号
     * @return 数据
     */
    Optional<Record> findByInternshipReleaseIdAndUsername(String internshipReleaseId, String username);

    /**
     * 查询全部数据
     *
     * @param simplePaginationUtil 分页工具
     * @return 数据
     */
    Result<Record> findAll(SimplePaginationUtil simplePaginationUtil);

    /**
     * 总数
     *
     * @return 总数
     */
    int countAll(SimplePaginationUtil paginationUtil);

    /**
     * 保存
     *
     * @param internshipReviewAuthorize 数据
     */
    void save(InternshipReviewAuthorize internshipReviewAuthorize);

    /**
     * 删除
     *
     * @param internshipReleaseId 实习发布id
     * @param username            账号
     */
    void deleteByInternshipReleaseIdAndUsername(String internshipReleaseId, String username);
}
