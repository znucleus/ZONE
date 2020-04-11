package top.zbeboy.zone.service.register;

import top.zbeboy.zone.domain.tables.pojos.LeaverRegisterOption;

import java.util.List;

public interface LeaverRegisterOptionService {

    /**
     * 保存
     *
     * @param leaverRegisterOptions 数据
     */
    void batchSave(List<LeaverRegisterOption> leaverRegisterOptions);
}
