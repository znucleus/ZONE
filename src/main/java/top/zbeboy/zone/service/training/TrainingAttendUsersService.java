package top.zbeboy.zone.service.training;

import top.zbeboy.zone.domain.tables.pojos.TrainingAttendUsers;

import java.util.List;

public interface TrainingAttendUsersService {

    /**
     * 批量保存
     *
     * @param trainingAttendUsers 数据
     */
    void batchSave(List<TrainingAttendUsers> trainingAttendUsers);
}
