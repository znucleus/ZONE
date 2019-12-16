package top.zbeboy.zone.service.data;

import top.zbeboy.zone.domain.tables.pojos.Application;

import java.util.List;

public interface CollegeApplicationService {

    /**
     * 通过pid查询
     *
     * @param pid       父id
     * @param collegeId 院id
     * @return 应用
     */
    List<Application> findByPidAndCollegeId(String pid, int collegeId);

    /**
     * 通过应用id删除
     *
     * @param applicationId 应用id
     */
    void deleteByApplicationId(String applicationId);
}
