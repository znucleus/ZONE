package top.zbeboy.zone.service.internship;

import org.jooq.Record;

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
}
