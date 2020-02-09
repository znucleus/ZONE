package top.zbeboy.zone.service.internship;

import org.jooq.Record;

import java.util.Optional;

public interface InternshipApplyService {

    /**
     * 通过实习id与学生id查询
     *
     * @param internshipReleaseId 实习发布id
     * @param studentId           学生id
     * @return 数据
     */
    Optional<Record> findByInternshipReleaseIdAndStudentId(String internshipReleaseId, int studentId);

}
