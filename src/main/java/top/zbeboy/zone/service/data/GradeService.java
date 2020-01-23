package top.zbeboy.zone.service.data;

import org.jooq.Result;
import top.zbeboy.zone.domain.tables.pojos.Grade;
import top.zbeboy.zone.domain.tables.records.GradeRecord;

import java.util.Optional;

public interface GradeService {

    /**
     * 通过主键查询
     *
     * @param id 主键
     * @return 数据
     */
    Grade findById(int id);

    /**
     * 根据专业ID和状态查询全部年级
     *
     * @param scienceId  专业ID
     * @param gradeIsDel 状态
     * @return 全部年级
     */
    Result<GradeRecord> findByScienceIdAndGradeIsDel(int scienceId, Byte gradeIsDel);

    /**
     * 根据专业ID和年级查询年级
     *
     * @param scienceId 专业ID
     * @param grade     状态
     * @return 年级
     */
    Optional<GradeRecord> findByScienceIdAndGrade(int scienceId, int grade);

    /**
     * 保存
     *
     * @param grade 年级
     */
    GradeRecord save(Grade grade);
}
