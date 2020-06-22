package top.zbeboy.zone.service.internship;

import org.jooq.Record;
import org.jooq.Result;
import top.zbeboy.zbase.domain.tables.pojos.InternshipFile;

public interface InternshipFileService {

    /**
     * 通过实习id查询
     *
     * @param internshipReleaseId 实习id
     * @return 文件信息
     */
    Result<Record> findByInternshipReleaseId(String internshipReleaseId);

    /**
     * 保存
     *
     * @param internshipFile 实习文件
     */
    void save(InternshipFile internshipFile);

    /**
     * 通过实习id删除
     *
     * @param internshipReleaseId 实习id
     */
    void deleteByInternshipReleaseId(String internshipReleaseId);

    /**
     * 通过文件id与实习id删除
     *
     * @param fileId              文件id
     * @param internshipReleaseId 实习id
     */
    void deleteByFileIdAndInternshipReleaseId(String fileId, String internshipReleaseId);
}
