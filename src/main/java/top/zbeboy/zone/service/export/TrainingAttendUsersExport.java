package top.zbeboy.zone.service.export;

import org.apache.poi.ss.usermodel.Row;
import top.zbeboy.zone.service.util.ExportUtil;
import top.zbeboy.zone.web.bean.training.attend.TrainingAttendUsersBean;

import java.util.List;

public class TrainingAttendUsersExport extends ExportUtil<TrainingAttendUsersBean> {

    // 序号
    private double sequence = 0;

    /**
     * 初始化数据
     *
     * @param data 数据
     */
    public TrainingAttendUsersExport(List<TrainingAttendUsersBean> data) {
        super(data);
    }

    @Override
    public void createHeader(Row row) {
        row.createCell(0).setCellValue("序号");
        row.createCell(1).setCellValue("姓名");
        row.createCell(2).setCellValue("学号");
        row.createCell(3).setCellValue("班级");
        row.createCell(4).setCellValue("手机号");
        row.createCell(5).setCellValue("邮箱");
        row.createCell(6).setCellValue("性别");
        row.createCell(7).setCellValue("状态");
        row.createCell(8).setCellValue("备注");
        row.createCell(9).setCellValue("操作账号");
    }

    @Override
    public void createCell(Row row, TrainingAttendUsersBean t) {
        sequence++;
        row.createCell(0).setCellValue(sequence);
        row.createCell(1).setCellValue(t.getRealName());
        row.createCell(2).setCellValue(t.getStudentNumber());
        row.createCell(3).setCellValue(t.getOrganizeName());
        row.createCell(4).setCellValue(t.getMobile());
        row.createCell(5).setCellValue(t.getEmail());
        row.createCell(6).setCellValue(t.getSex());
        row.createCell(7).setCellValue(operate(t.getOperate()));
        row.createCell(8).setCellValue(t.getRemark());
        row.createCell(9).setCellValue(t.getOperateUser());
    }

    private String operate(Byte operate) {
        String v = "";
        if (operate == 0) {
            v = "缺席";
        } else if (operate == 1) {
            v = "请假";
        } else if (operate == 2) {
            v = "迟到";
        } else if (operate == 3) {
            v = "正常";
        }
        return v;
    }
}
