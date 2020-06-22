package top.zbeboy.zone.service.export;

import org.apache.poi.ss.usermodel.Row;
import top.zbeboy.zbase.tools.service.util.ExportUtil;
import top.zbeboy.zone.web.bean.training.users.TrainingUsersBean;

import java.util.List;

public class TrainingUsersExport extends ExportUtil<TrainingUsersBean> {

    // 序号
    private double sequence = 0;

    /**
     * 初始化数据
     *
     * @param data 数据
     */
    public TrainingUsersExport(List<TrainingUsersBean> data) {
        super(data);
    }

    @Override
    public void createHeader(Row row) {
        row.createCell(0).setCellValue("序号");
        row.createCell(1).setCellValue("姓名");
        row.createCell(2).setCellValue("学号");
        row.createCell(3).setCellValue("账号");
        row.createCell(4).setCellValue("手机号");
        row.createCell(5).setCellValue("邮箱");
        row.createCell(6).setCellValue("性别");
        row.createCell(7).setCellValue("备注");
        row.createCell(8).setCellValue("创建时间");
    }

    @Override
    public void createCell(Row row, TrainingUsersBean t) {
        sequence++;
        row.createCell(0).setCellValue(sequence);
        row.createCell(1).setCellValue(t.getRealName());
        row.createCell(2).setCellValue(t.getStudentNumber());
        row.createCell(3).setCellValue(t.getUsername());
        row.createCell(4).setCellValue(t.getMobile());
        row.createCell(5).setCellValue(t.getEmail());
        row.createCell(6).setCellValue(t.getSex());
        row.createCell(7).setCellValue(t.getRemark());
        row.createCell(8).setCellValue(t.getCreateDateStr());
    }
}
