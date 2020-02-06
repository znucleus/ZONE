package top.zbeboy.zone.service.internship;

import org.jooq.Record;
import org.jooq.Result;
import top.zbeboy.zone.domain.tables.pojos.InternshipRelease;
import top.zbeboy.zone.web.util.pagination.SimplePaginationUtil;

import java.util.Optional;

public interface InternshipReleaseService {

    /**
     * 通过主键查询
     *
     * @param id 主键
     * @return 数据
     */
    InternshipRelease findById(String id);

    /**
     * 通过主键查询
     *
     * @param id 主键
     * @return 数据
     */
    Optional<Record> findByIdRelation(String id);

    /**
     * 分布查询
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
     * 保存
     *
     * @param internshipRelease 数据
     */
    void save(InternshipRelease internshipRelease);

    /**
     * 更新
     *
     * @param internshipRelease 数据
     */
    void update(InternshipRelease internshipRelease);
}
