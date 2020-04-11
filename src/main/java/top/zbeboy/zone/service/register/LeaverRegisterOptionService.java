package top.zbeboy.zone.service.register;

import org.jooq.Result;
import top.zbeboy.zone.domain.tables.pojos.LeaverRegisterOption;
import top.zbeboy.zone.domain.tables.records.LeaverRegisterOptionRecord;

import java.util.List;

public interface LeaverRegisterOptionService {

    /**
     * 根据主键查询
     *
     * @param id 主键
     * @return 数据
     */
    LeaverRegisterOption findById(String id);

    /**
     * 通过发布id查询
     *
     * @param leaverRegisterReleaseId id
     * @return 数据
     */
    Result<LeaverRegisterOptionRecord> findByLeaverRegisterReleaseId(String leaverRegisterReleaseId);

    /**
     * 根据发布id查询最大序号
     *
     * @param leaverRegisterReleaseId id
     * @return 数据
     */
    byte findMaxSortByLeaverRegisterReleaseId(String leaverRegisterReleaseId);

    /**
     * 保存
     *
     * @param leaverRegisterOptions 数据
     */
    void batchSave(List<LeaverRegisterOption> leaverRegisterOptions);

    /**
     * 更新
     *
     * @param leaverRegisterOption 数据
     */
    void update(LeaverRegisterOption leaverRegisterOption);

    /**
     * 根据主键删除
     *
     * @param id 主键
     */
    void deleteById(String id);
}
