package top.zbeboy.zone.service.internship;

import org.jooq.Record;
import top.zbeboy.zone.domain.tables.pojos.InternshipTeacherDistribution;
import top.zbeboy.zone.web.bean.internship.distribution.InternshipTeacherDistributionBean;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;

import java.util.List;
import java.util.Optional;

public interface InternshipTeacherDistributionService {

    /**
     * 通过实习发布id 和学生id查询
     *
     * @param internshipReleaseId 实习发布id
     * @param studentId           学生id
     * @return 数据
     */
    Optional<Record> findByInternshipReleaseIdAndStudentId(String internshipReleaseId, int studentId);

    /**
     * 分页查询
     *
     * @param dataTablesUtil 工具类
     * @return 分页数据
     */
    List<InternshipTeacherDistributionBean> findAllByPage(DataTablesUtil dataTablesUtil);

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
     * @param internshipTeacherDistribution 数据
     */
    void save(InternshipTeacherDistribution internshipTeacherDistribution);
}
