package top.zbeboy.zone.test;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import top.zbeboy.zbase.bean.educational.examination.ExaminationNoticeSmsSubscribeBean;
import top.zbeboy.zbase.bean.training.report.TrainingReportBean;
import top.zbeboy.zbase.feign.educational.examination.EducationalExaminationService;
import top.zbeboy.zbase.tools.web.util.ByteUtil;
import top.zbeboy.zone.service.training.TrainingReportService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestData {

    @Resource
    private EducationalExaminationService educationalExaminationService;

    @Test
    public void testExamination() {
        Optional<List<ExaminationNoticeSmsSubscribeBean>> optional = educationalExaminationService.findSmsSubscribeWithDealState(ByteUtil.toByte(0));
        if(optional.isPresent()){
            List<ExaminationNoticeSmsSubscribeBean> beans = optional.get();
            System.out.println(beans);
        }
    }
}
