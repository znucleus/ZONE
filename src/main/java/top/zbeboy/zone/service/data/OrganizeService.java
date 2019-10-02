package top.zbeboy.zone.service.data;

import org.jooq.Record;
import org.jooq.Result;
import top.zbeboy.zone.domain.tables.records.OrganizeRecord;

import java.util.Optional;

public interface OrganizeService {

    /**
     * 通过班级id查询所有信息
     * 缓存:是
     *
     * @param id 班级id
     * @return 所有信息
     */
    Optional<Record> findByIdRelation(int id);

    /**
     * 根据年级ID和状态查询全部班级
     *
     * @param gradeId       年级ID
     * @param organizeIsDel 状态
     * @return 全部班级
     */
    Result<OrganizeRecord> findByGradeIdAndOrganizeIsDel(int gradeId, Byte organizeIsDel);
}
