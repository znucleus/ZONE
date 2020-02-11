package top.zbeboy.zone.service.internship;

import top.zbeboy.zone.domain.tables.pojos.InternshipChangeHistory;

public interface InternshipChangeHistoryService {

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