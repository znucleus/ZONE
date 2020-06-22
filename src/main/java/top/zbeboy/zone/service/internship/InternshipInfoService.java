package top.zbeboy.zone.service.internship;

import org.jooq.Record;
import org.jooq.Result;
import top.zbeboy.zbase.domain.tables.pojos.InternshipInfo;
import top.zbeboy.zbase.tools.web.util.pagination.DataTablesUtil;
import top.zbeboy.zbase.vo.internship.apply.InternshipApplyAddVo;

import java.util.Optional;

public interface InternshipInfoService {

    /**
     * 分页查询
     *
     * @param dataTablesUtil 工具类
     * @return 分页数据
     */
    Result<Record> findAllByPage(DataTablesUtil dataTablesUtil);

    /**
     * 数据导出
     *
     * @param dataTablesUtil 工具类
     * @return 数据
     */
    Result<Record> export(DataTablesUtil dataTablesUtil);

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
     * 通过实习id与学生id查询
     *
     * @param internshipReleaseId 实习发布id
     * @param studentId           学生id
     * @return 数据
     */
    Optional<Record> findByInternshipReleaseIdAndStudentId(String internshipReleaseId, int studentId);

    /**
     * jooq事务性保存
     *
     * @param internshipApplyAddVo 数据
     */
    void saveWithTransaction(InternshipApplyAddVo internshipApplyAddVo);

    /**
     * 更新
     *
     * @param internshipInfo 数据
     */
    void update(InternshipInfo internshipInfo);

    /**
     * 通过实习id与学生id删除
     *
     * @param internshipReleaseId 实习发布id
     * @param studentId           学生id
     */
    void deleteByInternshipReleaseIdAndStudentId(String internshipReleaseId, int studentId);
}
