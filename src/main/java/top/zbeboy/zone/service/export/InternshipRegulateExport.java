package top.zbeboy.zone.service.export;

import org.apache.poi.ss.usermodel.Row;
import top.zbeboy.zbase.tools.service.util.DateTimeUtil;
import top.zbeboy.zbase.tools.service.util.ExportUtil;
import top.zbeboy.zone.web.bean.internship.regulate.InternshipRegulateBean;

import java.util.List;

public class InternshipRegulateExport extends ExportUtil<InternshipRegulateBean> {

    // 序号
    private double sequence = 0;

    /**
     * 初始化数据
     *
     * @param data 数据
     */
    public InternshipRegulateExport(List<InternshipRegulateBean> data) {
        super(data);
    }

    @Override
    public void createHeader(Row row) {
        row.createCell(0).setCellValue("序号");
        row.createCell(1).setCellValue("学生姓名");
        row.createCell(2).setCellValue("学号");
        row.createCell(3).setCellValue("联系方式");
        row.createCell(4).setCellValue("实习内容");
        row.createCell(5).setCellValue("实习进展（周报）");
        row.createCell(6).setCellValue("汇报信息途径（电话、QQ、邮箱、见面沟通等）");
        row.createCell(7).setCellValue("汇报日期");
        row.createCell(8).setCellValue("指导教师");
        row.createCell(9).setCellValue("备注（有无变更单位、脱岗或联系不上等具体情况备注）");
    }

    @Override
    public void createCell(Row row, InternshipRegulateBean t) {
        sequence++;
        row.createCell(0).setCellValue(sequence);
        row.createCell(1).setCellValue(t.getStudentName());
        row.createCell(2).setCellValue(t.getStudentNumber());
        row.createCell(3).setCellValue(t.getStudentTel());
        row.createCell(4).setCellValue(t.getInternshipContent());
        row.createCell(5).setCellValue(t.getInternshipProgress());
        row.createCell(6).setCellValue(t.getReportWay());
        row.createCell(7).setCellValue(DateTimeUtil.formatSqlDate(t.getReportDate(), DateTimeUtil.YEAR_MONTH_DAY_FORMAT));
        row.createCell(8).setCellValue(t.getSchoolGuidanceTeacher());
        row.createCell(9).setCellValue(t.getTliy());
    }
}
