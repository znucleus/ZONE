package top.zbeboy.zone.service.internship;

import top.zbeboy.zbase.domain.tables.pojos.InternshipJournalContent;
import top.zbeboy.zbase.domain.tables.records.InternshipJournalContentRecord;

import java.util.Optional;

public interface InternshipJournalContentService {

    /**
     * 根据实习日志id查询
     *
     * @param internshipJournalId 日志id
     * @return 数据
     */
    Optional<InternshipJournalContentRecord> findByInternshipJournalId(String internshipJournalId);

    /**
     * 保存
     *
     * @param internshipJournalContent 数据
     */
    void save(InternshipJournalContent internshipJournalContent);

    /**
     * 更新
     *
     * @param internshipJournalContent 数据
     */
    void update(InternshipJournalContent internshipJournalContent);
}
