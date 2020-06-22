package top.zbeboy.zone.service.training;

import top.zbeboy.zbase.bean.training.report.TrainingReportBean;

import javax.servlet.http.HttpServletRequest;

public interface TrainingReportService {

    /**
     * 保存归档封面
     *
     * @param bean     数据
     * @param request  请求
     * @param isSenior 是否高级
     * @return 路径
     */
    String saveTrainingFile(TrainingReportBean bean, HttpServletRequest request, boolean isSenior);

    /**
     * 保存情况汇总
     *
     * @param bean     数据
     * @param request  请求
     * @param isSenior 是否高级
     * @return 路径
     */
    String saveTrainingSituation(TrainingReportBean bean, HttpServletRequest request, boolean isSenior);

    /**
     * 保存实训报告
     *
     * @param bean     数据
     * @param request  请求
     * @param isSenior 是否高级
     * @return 路径
     */
    String saveTrainingReport(TrainingReportBean bean, HttpServletRequest request, boolean isSenior);
}
