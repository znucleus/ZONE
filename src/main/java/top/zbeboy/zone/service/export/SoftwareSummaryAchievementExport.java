package top.zbeboy.zone.service.export;

import org.apache.poi.ss.usermodel.Row;
import top.zbeboy.zbase.bean.achievement.software.SoftwareSummaryAchievementBean;
import top.zbeboy.zbase.tools.service.util.ExportUtil;

import java.util.List;

public class SoftwareSummaryAchievementExport extends ExportUtil<SoftwareSummaryAchievementBean> {

    // 序号
    private double sequence = 0;

    /**
     * 初始化数据
     *
     * @param data 数据
     */
    public SoftwareSummaryAchievementExport(List<SoftwareSummaryAchievementBean> data) {
        super(data);
    }

    @Override
    public void createHeader(Row row) {
        row.createCell(0).setCellValue("序号");
        row.createCell(1).setCellValue("姓名");
        row.createCell(2).setCellValue("性别");
        row.createCell(3).setCellValue("身份证号码");
        row.createCell(4).setCellValue("电话");
        row.createCell(5).setCellValue("所学专业");
        row.createCell(6).setCellValue("所在学校");
        row.createCell(7).setCellValue("专业班级");
        row.createCell(8).setCellValue("级别");
        row.createCell(9).setCellValue("报考类别");
        row.createCell(10).setCellValue("上午成绩");
        row.createCell(11).setCellValue("下午成绩");
        row.createCell(12).setCellValue("论文成绩");
        row.createCell(13).setCellValue("鉴定结果");
        row.createCell(14).setCellValue("缴费情况");
        row.createCell(15).setCellValue("备注");
        row.createCell(16).setCellValue("考试时间");
        row.createCell(17).setCellValue("创建时间");
    }

    @Override
    public void createCell(Row row, SoftwareSummaryAchievementBean t) {
        sequence++;
        row.createCell(0).setCellValue(sequence);
        row.createCell(1).setCellValue(t.getRealName());
        row.createCell(2).setCellValue(t.getSex());
        row.createCell(3).setCellValue(t.getIdCard());
        row.createCell(4).setCellValue(t.getMobile());
        row.createCell(5).setCellValue(t.getScienceName());
        row.createCell(6).setCellValue(t.getSchoolName());
        row.createCell(7).setCellValue(t.getOrganizeName());
        row.createCell(8).setCellValue(t.getLevel());
        row.createCell(9).setCellValue(t.getExamType());
        row.createCell(10).setCellValue(t.getMorningResults());
        row.createCell(11).setCellValue(t.getAfternoonResults());
        row.createCell(12).setCellValue(t.getThesisResults());
        row.createCell(13).setCellValue(t.getIdentificationResults());
        row.createCell(14).setCellValue(t.getPayment());
        row.createCell(15).setCellValue(t.getRemark());
        row.createCell(16).setCellValue(t.getExamDate());
        row.createCell(17).setCellValue(t.getCreateDateStr());
    }
}
