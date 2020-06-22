package top.zbeboy.zone.service.internship;

import org.jooq.Record;
import org.jooq.Result;
import top.zbeboy.zbase.domain.tables.pojos.InternshipChangeHistory;

public interface InternshipChangeHistoryService {

    /**
     * 通过实习发布id与学生id查询
     *
     * @param internshipReleaseId 实习发布id
     * @param studentId           学生id
     * @return 数据
     */
    Result<Record> findByInternshipReleaseIdAndStudentId(String internshipReleaseId, int studentId);

    /**
     * 保存
     *
     * @param internshipChangeHistory 数据
     */
    void save(InternshipChangeHistory internshipChangeHistory);

    /**
     * 通过实习id与学生id删除
     *
     * @param internshipReleaseId 实习发布id
     * @param studentId           学生id
     */
    void deleteByInternshipReleaseIdAndStudentId(String internshipReleaseId, int studentId);
}
