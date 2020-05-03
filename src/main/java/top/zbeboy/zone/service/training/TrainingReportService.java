package top.zbeboy.zone.service.training;

import top.zbeboy.zone.web.bean.training.report.TrainingReportBean;

import javax.servlet.http.HttpServletRequest;

public interface TrainingReportService {

    /**
     * 保存归档封面(高级模板)
     *
     * @param bean    数据
     * @param request 请求
     * @return 路径
     */
    String saveSeniorTrainingFile(TrainingReportBean bean, HttpServletRequest request);
}
