package top.zbeboy.zone.service.attend;

import top.zbeboy.zone.domain.tables.pojos.AttendUsers;

import java.util.List;

public interface AttendUsersService {

    /**
     * 保存
     *
     * @param attendUsers 数据
     */
    void batchSave(List<AttendUsers> attendUsers);
}
