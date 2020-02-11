package top.zbeboy.zone.service.internship;

import org.jooq.Record;
import top.zbeboy.zone.domain.tables.pojos.InternshipInfo;
import top.zbeboy.zone.web.vo.internship.apply.InternshipApplyAddVo;

import java.util.Optional;

public interface InternshipInfoService {

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
