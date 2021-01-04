package top.zbeboy.zone.service.theory;

import org.jooq.Record;
import org.jooq.Result;
import top.zbeboy.zbase.domain.tables.pojos.TheoryConfigure;

import java.util.Optional;

public interface TheoryConfigureService {
    /**
     * 通过主键查询
     *
     * @param id 主键
     * @return 数据
     */
    TheoryConfigure findById(String id);

    /**
     * 通过主键关联查询
     *
     * @param id 主键
     * @return 数据
     */
    Optional<Record> findByIdRelation(String id);

    /**
     * 根据理论发布id关联查询
     *
     * @param theoryReleaseId 理论发布id
     * @return 数据
     */
    Result<Record> findByTheoryReleaseIdRelation(String theoryReleaseId);

    /**
     * 自动生成理论考勤
     *
     * @param dayOfWeek 星期
     * @return 数据
     */
    Result<Record> findIsAuto(byte dayOfWeek);

    /**
     * 保存
     *
     * @param theoryConfigure 数据
     */
    void save(TheoryConfigure theoryConfigure);

    /**
     * 更新
     *
     * @param theoryConfigure 数据
     */
    void update(TheoryConfigure theoryConfigure);

    /**
     * 通过主键删除
     *
     * @param id 主键
     */
    void deleteById(String id);
}
