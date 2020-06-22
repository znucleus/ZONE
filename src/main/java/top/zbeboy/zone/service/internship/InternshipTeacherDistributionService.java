package top.zbeboy.zone.service.internship;

import org.jooq.Record;
import org.jooq.Record2;
import org.jooq.Result;
import top.zbeboy.zbase.bean.internship.distribution.InternshipTeacherDistributionBean;
import top.zbeboy.zbase.domain.tables.pojos.InternshipTeacherDistribution;
import top.zbeboy.zbase.tools.web.util.pagination.DataTablesUtil;

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
     * 通过实习发布id 和学生id查询
     *
     * @param internshipReleaseId 实习发布id
     * @param staffId             教职工id
     * @return 数据
     */
    Result<Record> findByInternshipReleaseIdAndStaffId(String internshipReleaseId, int staffId);

    /**
     * 根据发布id查询所有指导教师
     *
     * @param internshipReleaseId 发布id
     * @return 教师们
     */
    Result<Record2<Integer, String>> findByInternshipReleaseIdAndDistinctStaffId(String internshipReleaseId);

    /**
     * 分页查询
     *
     * @param dataTablesUtil 工具类
     * @return 分页数据
     */
    List<InternshipTeacherDistributionBean> findAllByPage(DataTablesUtil dataTablesUtil);

    /**
     * 数据导出
     *
     * @param dataTablesUtil 工具类
     * @return 数据
     */
    List<InternshipTeacherDistributionBean> export(DataTablesUtil dataTablesUtil);

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

    /**
     * 批量保存
     *
     * @param internshipTeacherDistribution 数据
     */
    void batchSave(List<InternshipTeacherDistribution> internshipTeacherDistribution);

    /**
     * 更新
     *
     * @param internshipTeacherDistribution 数据
     */
    void updateStaff(InternshipTeacherDistribution internshipTeacherDistribution);

    /**
     * 通过实习发布id删除
     *
     * @param internshipReleaseId 实习发布id
     */
    void deleteByInternshipReleaseId(String internshipReleaseId);

    /**
     * 通过实习发布id 和 学生id删除
     *
     * @param internshipReleaseId 实习发布id
     * @param studentId           学生id
     */
    void deleteByInternshipReleaseIdAndStudentId(String internshipReleaseId, int studentId);

    /**
     * 删除未申请学生的分配
     *
     * @param internshipReleaseId 实习发布Id
     */
    void deleteNotApply(String internshipReleaseId);
}
