package top.zbeboy.zone.service.export;

import org.apache.poi.ss.usermodel.Row;
import top.zbeboy.zbase.domain.tables.pojos.InternshipInfo;
import top.zbeboy.zbase.tools.service.util.DateTimeUtil;
import top.zbeboy.zbase.tools.service.util.ExportUtil;

import java.util.List;

public class InternshipInfoExport extends ExportUtil<InternshipInfo> {

    // 序号
    private double sequence = 0;

    /**
     * 初始化数据
     *
     * @param data 数据
     */
    public InternshipInfoExport(List<InternshipInfo> data) {
        super(data);
    }

    @Override
    public void createHeader(Row row) {
        row.createCell(0).setCellValue("序号");
        row.createCell(1).setCellValue("学生姓名");
        row.createCell(2).setCellValue("专业班级");
        row.createCell(3).setCellValue("性别");
        row.createCell(4).setCellValue("学号");
        row.createCell(5).setCellValue("电话号码");
        row.createCell(6).setCellValue("邮箱（qq）");
        row.createCell(7).setCellValue("父母联系方式");
        row.createCell(8).setCellValue("班主任");
        row.createCell(9).setCellValue("联系方式");
        row.createCell(10).setCellValue("实习单位名称");
        row.createCell(11).setCellValue("实习单位地址");
        row.createCell(12).setCellValue("实习单位联系人");
        row.createCell(13).setCellValue("联系方式");
        row.createCell(14).setCellValue("校内指导教师");
        row.createCell(15).setCellValue("联系方式");
        row.createCell(16).setCellValue("实习期间");
        row.createCell(17).setCellValue("承诺书");
        row.createCell(18).setCellValue("安全责任书");
        row.createCell(19).setCellValue("实践协议书");
        row.createCell(20).setCellValue("实习申请书");
        row.createCell(21).setCellValue("实习接收");
        row.createCell(22).setCellValue("安全教育协议");
        row.createCell(23).setCellValue("父母同意书");
    }

    @Override
    public void createCell(Row row, InternshipInfo t) {
        sequence++;
        row.createCell(0).setCellValue(sequence);
        row.createCell(1).setCellValue(t.getStudentName());
        row.createCell(2).setCellValue(t.getOrganizeName());
        row.createCell(3).setCellValue(t.getStudentSex());
        row.createCell(4).setCellValue(t.getStudentNumber());
        row.createCell(5).setCellValue(t.getMobile());
        row.createCell(6).setCellValue(t.getQqMailbox());
        row.createCell(7).setCellValue(t.getParentContactPhone());
        row.createCell(8).setCellValue(t.getHeadmaster());
        row.createCell(9).setCellValue(t.getHeadmasterTel());
        row.createCell(10).setCellValue(t.getCompanyName());
        row.createCell(11).setCellValue(t.getCompanyAddress());
        row.createCell(12).setCellValue(t.getCompanyContact());
        row.createCell(13).setCellValue(t.getCompanyMobile());
        row.createCell(14).setCellValue(t.getSchoolGuidanceTeacher());
        row.createCell(15).setCellValue(t.getSchoolGuidanceTeacherTel());
        row.createCell(16).setCellValue(DateTimeUtil.defaultFormatSqlDate(t.getStartTime()) + '至' + DateTimeUtil.defaultFormatSqlDate(t.getEndTime()));
        row.createCell(17).setCellValue(dealByte(t.getCommitmentBook()));
        row.createCell(18).setCellValue(dealByte(t.getSafetyResponsibilityBook()));
        row.createCell(19).setCellValue(dealByte(t.getPracticeAgreement()));
        row.createCell(20).setCellValue(dealByte(t.getInternshipApplication()));
        row.createCell(21).setCellValue(dealByte(t.getPracticeReceiving()));
        row.createCell(22).setCellValue(dealByte(t.getSecurityEducationAgreement()));
        row.createCell(23).setCellValue(dealByte(t.getParentalConsent()));
    }

    private String dealByte(Byte b) {
        String v;
        if (b != null && b == 1) {
            v = "已交";
        } else {
            v = "未交";
        }
        return v;
    }
}
