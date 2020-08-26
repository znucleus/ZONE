package top.zbeboy.zone.service.export;

import org.apache.poi.ss.usermodel.Row;
import top.zbeboy.zbase.bean.campus.roster.RosterDataBean;
import top.zbeboy.zbase.tools.service.util.DateTimeUtil;
import top.zbeboy.zbase.tools.service.util.ExportUtil;

import java.util.List;

public class RosterDataExport extends ExportUtil<RosterDataBean> {

    // 序号
    private double sequence = 0;

    /**
     * 初始化数据
     *
     * @param data 数据
     */
    public RosterDataExport(List<RosterDataBean> data) {
        super(data);
    }

    @Override
    public void createHeader(Row row) {
        row.createCell(0).setCellValue("序号");
        row.createCell(1).setCellValue("学号");
        row.createCell(2).setCellValue("姓名");
        row.createCell(3).setCellValue("姓名拼音");
        row.createCell(4).setCellValue("性别");
        row.createCell(5).setCellValue("出生年月");
        row.createCell(6).setCellValue("身份证号");
        row.createCell(7).setCellValue("民族");
        row.createCell(8).setCellValue("政治面貌");
        row.createCell(9).setCellValue("班级名称");
        row.createCell(10).setCellValue("省份");
        row.createCell(11).setCellValue("所属地区");
        row.createCell(12).setCellValue("乘车区间");
        row.createCell(13).setCellValue("家长姓名");
        row.createCell(14).setCellValue("家长联系电话");
        row.createCell(15).setCellValue("家长联系地址");
        row.createCell(16).setCellValue("邮政编码");
        row.createCell(17).setCellValue("本人联系方式");
        row.createCell(18).setCellValue("宿舍号");
    }

    @Override
    public void createCell(Row row, RosterDataBean t) {
        sequence++;
        row.createCell(0).setCellValue(sequence);
        row.createCell(1).setCellValue(t.getStudentNumber());
        row.createCell(2).setCellValue(t.getRealName());
        row.createCell(3).setCellValue(t.getNamePinyin());
        row.createCell(4).setCellValue(t.getSex());
        row.createCell(5).setCellValue(DateTimeUtil.defaultFormatSqlDate(t.getBirthday()));
        row.createCell(6).setCellValue(t.getIdCard());
        row.createCell(7).setCellValue(t.getNationName());
        row.createCell(8).setCellValue(t.getPoliticalLandscapeName());
        row.createCell(9).setCellValue(t.getOrganizeName());
        row.createCell(10).setCellValue(t.getProvince());
        row.createCell(11).setCellValue(t.getRegion());
        row.createCell(12).setCellValue(t.getBusSection());
        row.createCell(13).setCellValue(t.getParentName());
        row.createCell(14).setCellValue(t.getParentContactPhone());
        row.createCell(15).setCellValue(t.getParentContactAddress());
        row.createCell(16).setCellValue(t.getZipCode());
        row.createCell(17).setCellValue(t.getPhoneNumber());
        row.createCell(18).setCellValue(t.getDormitoryNumber());
    }
}
