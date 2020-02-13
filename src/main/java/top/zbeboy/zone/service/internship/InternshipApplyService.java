package top.zbeboy.zone.service.internship;

import org.jooq.Record;
import org.jooq.Record2;
import org.jooq.Result;
import top.zbeboy.zone.domain.tables.pojos.InternshipApply;
import top.zbeboy.zone.web.util.pagination.SimplePaginationUtil;

import java.util.List;
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

    /**
     * 查询班级数据
     *
     * @param internshipReleaseId 实习发布id
     * @return 数据
     */
    Result<Record2<Integer, String>> findDistinctOrganize(String internshipReleaseId);

    /**
     * 分页查询
     *
     * @param paginationUtil 数据
     * @return 数据
     */
    Result<Record> findAllByPage(SimplePaginationUtil paginationUtil);

    /**
     * 总数
     *
     * @return 总数
     */
    int countAll(SimplePaginationUtil paginationUtil);

    /**
     * 更新
     *
     * @param internshipApply 数据
     */
    void update(InternshipApply internshipApply);

    /**
     * 通过实习发布id与申请状态更新状态 定时任务
     *
     * @param curState         当前状态
     * @param updateState         新状态
     */
    void updateState(int curState, int updateState);

    /**
     * 更改超过信息填写时间的申请状态为申请中
     *
     * @param curState       当前状态
     * @param updateState       新状态
     */
    void updateChangeState(List<Integer> curState, int updateState);

    /**
     * 通过实习id与学生id删除
     *
     * @param internshipReleaseId 实习发布id
     * @param studentId           学生id
     */
    void deleteByInternshipReleaseIdAndStudentId(String internshipReleaseId, int studentId);
}
