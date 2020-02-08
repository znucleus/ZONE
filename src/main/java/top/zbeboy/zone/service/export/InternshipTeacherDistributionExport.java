package top.zbeboy.zone.service.export;

import org.apache.poi.ss.usermodel.Row;
import top.zbeboy.zone.service.util.ExportUtil;
import top.zbeboy.zone.web.bean.internship.distribution.InternshipTeacherDistributionBean;

import java.util.List;

public class InternshipTeacherDistributionExport extends ExportUtil<InternshipTeacherDistributionBean> {

    // 序号
    private double sequence = 0;

    /**
     * 初始化数据
     *
     * @param data 数据
     */
    public InternshipTeacherDistributionExport(List<InternshipTeacherDistributionBean> data) {
        super(data);
    }

    @Override
    public void createHeader(Row row) {
        row.createCell(0).setCellValue("序号");
        row.createCell(1).setCellValue("学生姓名");
        row.createCell(2).setCellValue("学生ID");
        row.createCell(3).setCellValue("学生学号");
        row.createCell(4).setCellValue("指导教师");
        row.createCell(5).setCellValue("指导教师ID");
        row.createCell(6).setCellValue("指导教师工号");
        row.createCell(7).setCellValue("分配人");
        row.createCell(8).setCellValue("分配人ID");
    }

    @Override
    public void createCell(Row row, InternshipTeacherDistributionBean t) {
        sequence++;
        row.createCell(0).setCellValue(sequence);
        row.createCell(1).setCellValue(t.getStudentRealName());
        row.createCell(2).setCellValue(t.getStudentUsername());
        row.createCell(3).setCellValue(t.getStudentNumber());
        row.createCell(4).setCellValue(t.getStaffRealName());
        row.createCell(5).setCellValue(t.getStaffUsername());
        row.createCell(6).setCellValue(t.getStaffNumber());
        row.createCell(7).setCellValue(t.getAssigner());
        row.createCell(8).setCellValue(t.getUsername());
    }
}
