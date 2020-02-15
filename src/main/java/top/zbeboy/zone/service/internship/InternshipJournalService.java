package top.zbeboy.zone.service.internship;

import org.jooq.Record;
import org.jooq.Result;
import top.zbeboy.zone.domain.tables.pojos.InternshipJournal;
import top.zbeboy.zone.domain.tables.pojos.InternshipJournalContent;
import top.zbeboy.zone.domain.tables.pojos.Users;
import top.zbeboy.zone.domain.tables.records.InternshipJournalRecord;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;

import javax.servlet.http.HttpServletRequest;

public interface InternshipJournalService {

    /**
     * 根据主键查询
     *
     * @param id 主键
     * @return 数据
     */
    InternshipJournal findById(String id);

    /**
     * 通过实习发布id与学生id查询
     *
     * @param internshipReleaseId 实习发布id
     * @param studentId           学生id
     * @return 数据
     */
    Result<InternshipJournalRecord> findByInternshipReleaseIdAndStudentId(String internshipReleaseId, int studentId);

    /**
     * 通过实习发布id与教职工id查询
     *
     * @param internshipReleaseId 实习发布id
     * @param staffId             教职工id
     * @return 数据
     */
    Result<InternshipJournalRecord> findByInternshipReleaseIdAndStaffId(String internshipReleaseId, int staffId);

    /**
     * 分页查询
     *
     * @param dataTablesUtil 工具类
     * @return 分页数据
     */
    Result<Record> findAllByPage(DataTablesUtil dataTablesUtil);

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
     * @param internshipJournal 数据
     */
    void save(InternshipJournal internshipJournal);

    /**
     * 保存word文件
     *
     * @param internshipJournal        数据
     * @param internshipJournalContent 内容
     * @param users                    用户
     * @param request                  请求
     */
    void saveWord(InternshipJournal internshipJournal, InternshipJournalContent internshipJournalContent, Users users, HttpServletRequest request);

    /**
     * 更新
     *
     * @param internshipJournal 数据
     */
    void update(InternshipJournal internshipJournal);

    /**
     * 通过主键删除
     *
     * @param id 主键
     */
    void deleteById(String id);
}
