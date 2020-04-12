package top.zbeboy.zone.service.register;

import top.zbeboy.zone.domain.tables.pojos.LeaverRegisterDataOption;
import top.zbeboy.zone.domain.tables.records.LeaverRegisterDataOptionRecord;

import java.util.Optional;

public interface LeaverRegisterDataOptionService {

    /**
     * 根据数据id与选项id查询
     *
     * @param leaverRegisterDataId   数据id
     * @param leaverRegisterOptionId 选项id
     * @return 数据
     */
    Optional<LeaverRegisterDataOptionRecord> findByLeaverRegisterDataIdAndLeaverRegisterOptionId(String leaverRegisterDataId, String leaverRegisterOptionId);

    /**
     * 保存
     *
     * @param leaverRegisterDataOption 数据
     */
    void save(LeaverRegisterDataOption leaverRegisterDataOption);
}
