package top.zbeboy.zone.service.data;

import top.zbeboy.zone.domain.tables.pojos.Application;
import top.zbeboy.zone.domain.tables.pojos.CollegeApplication;

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
     * 通过院id查询
     *
     * @param collegeId 院id
     * @return 数据
     */
    List<CollegeApplication> findByCollegeId(int collegeId);

    /**
     * 保存
     *
     * @param collegeApplication 表数据
     */
    void batchSave(List<CollegeApplication> collegeApplication);

    /**
     * 通过院id删除
     *
     * @param collegeId 院id
     */
    void deleteByCollegeId(int collegeId);

    /**
     * 通过应用id删除
     *
     * @param applicationId 应用id
     */
    void deleteByApplicationId(String applicationId);
}
