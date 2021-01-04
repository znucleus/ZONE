package top.zbeboy.zone.service.theory;

import org.jooq.Record;
import org.jooq.Result;
import top.zbeboy.zbase.domain.tables.pojos.TheoryAuthorities;
import top.zbeboy.zbase.domain.tables.records.TheoryAuthoritiesRecord;

import java.util.Optional;

public interface TheoryAuthoritiesService {
    /**
     * 通过主键查询
     *
     * @param id 主键
     * @return 数据
     */
    TheoryAuthorities findById(String id);

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
     * 根据理论发布id和账号查询有效的权限
     *
     * @param theoryReleaseId 理论发布id
     * @param username        账号
     * @return 数据
     */
    Result<TheoryAuthoritiesRecord> findEffectiveByTheoryReleaseIdAndUsername(String theoryReleaseId, String username);

    /**
     * 保存
     *
     * @param theoryAuthorities 数据
     */
    void save(TheoryAuthorities theoryAuthorities);

    /**
     * 更新
     *
     * @param theoryAuthorities 数据
     */
    void update(TheoryAuthorities theoryAuthorities);

    /**
     * 通过主键删除
     *
     * @param id 主键
     */
    void deleteById(String id);
}
