package top.zbeboy.zone.service.attend;

import top.zbeboy.zone.domain.tables.pojos.AttendReleaseSub;

import java.util.List;

public interface AttendReleaseSubService {

    /**
     * 复制主表
     *
     * @param attendReleaseId 发布id
     */
    void copyAttendRelease(String attendReleaseId);

    /**
     * 保存
     *
     * @param attendReleaseSubs 数据
     */
    void batchSave(List<AttendReleaseSub> attendReleaseSubs);
}
