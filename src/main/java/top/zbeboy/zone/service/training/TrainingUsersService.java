package top.zbeboy.zone.service.training;

import top.zbeboy.zone.domain.tables.pojos.TrainingUsers;

import java.util.List;

public interface TrainingUsersService {

    /**
     * 批量保存
     *
     * @param trainingUsers 数据
     */
    void batchSave(List<TrainingUsers> trainingUsers);
}
