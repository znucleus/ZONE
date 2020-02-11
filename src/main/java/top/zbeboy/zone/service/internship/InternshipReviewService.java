package top.zbeboy.zone.service.internship;

public interface InternshipReviewService {
    /**
     * 统计一个实习不同状态下的数据
     *
     * @param internshipReleaseId  实习发布id
     * @param internshipApplyState 状态
     * @return 统计结果
     */
    int countByInternshipReleaseIdAndInternshipApplyState(String internshipReleaseId, int internshipApplyState);
}
