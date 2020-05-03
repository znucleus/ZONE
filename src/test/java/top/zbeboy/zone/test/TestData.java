package top.zbeboy.zone.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import top.zbeboy.zone.service.training.TrainingReportService;
import top.zbeboy.zone.web.bean.training.report.TrainingReportBean;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestData {

    @Resource
    private TrainingReportService trainingReportService;

    @Test
    public void testTrainingFile(){
        TrainingReportBean bean = new TrainingReportBean();
        bean.setOrganizeName("智电1811");
        bean.setCourseName("Java核心实训");
        bean.setRealName("赵银");
        bean.setYear("2019");
        bean.setMonth("11");
        bean.setDay("10");
        bean.setStartYear("2019");
        bean.setEndYear("2020");
        bean.setTerm("上");
        bean.setScienceName("智能电网信息工程");
        bean.setClassRoom("明轩楼605");
        bean.setOrganizeNum("33");
        bean.setCourseType("实战");
        bean.setStartAndEndDate("2020-05-01 至 2020-05-31");
        bean.setSex("男");
        bean.setAge("26");
        bean.setAcademicTitleName("讲师");

        HttpServletRequest request = null;
        System.out.println(trainingReportService.saveTrainingSituation(bean, request, true));
    }

    @Test
    public void testTrainingReport(){
        TrainingReportBean bean = new TrainingReportBean();
        bean.setOrganizeName("智电1811");
        bean.setCourseName("Java核心实训");
        bean.setRealName("赵银");
        bean.setYear("2019");
        bean.setMonth("11");
        bean.setDay("10");
        bean.setClassRoom("明轩楼605");
        bean.setOrganizeNum("33");
        bean.setStudentName("张三");
        bean.setStudentNumber("2012118505129");

        HttpServletRequest request = null;
        System.out.println(trainingReportService.saveTrainingReport(bean, request, false));
    }
}
