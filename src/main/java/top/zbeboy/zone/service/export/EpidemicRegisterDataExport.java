package top.zbeboy.zone.service.export;

import org.apache.poi.ss.usermodel.Row;
import top.zbeboy.zbase.bean.register.epidemic.EpidemicRegisterDataBean;
import top.zbeboy.zbase.tools.service.util.ExportUtil;

import java.util.List;

public class EpidemicRegisterDataExport extends ExportUtil<EpidemicRegisterDataBean> {

    // 序号
    private double sequence = 0;

    /**
     * 初始化数据
     *
     * @param data 数据
     */
    public EpidemicRegisterDataExport(List<EpidemicRegisterDataBean> data) {
        super(data);
    }

    @Override
    public void createHeader(Row row) {
        row.createCell(0).setCellValue("序号");
        row.createCell(1).setCellValue("姓名");
        row.createCell(2).setCellValue("账号");
        row.createCell(3).setCellValue("类型");
        row.createCell(4).setCellValue("状况");
        row.createCell(5).setCellValue("位置");
        row.createCell(6).setCellValue("所属单位");
        row.createCell(7).setCellValue("渠道");
        row.createCell(8).setCellValue("备注");
        row.createCell(9).setCellValue("日期");
    }

    @Override
    public void createCell(Row row, EpidemicRegisterDataBean t) {
        sequence++;
        row.createCell(0).setCellValue(sequence);
        row.createCell(1).setCellValue(t.getRegisterRealName());
        row.createCell(2).setCellValue(t.getRegisterUsername());
        row.createCell(3).setCellValue(t.getRegisterType());
        row.createCell(4).setCellValue(t.getEpidemicStatus());
        row.createCell(5).setCellValue(t.getAddress());
        row.createCell(6).setCellValue(t.getInstitute());
        row.createCell(7).setCellValue(t.getChannelName());
        row.createCell(8).setCellValue(t.getRemark());
        row.createCell(9).setCellValue(t.getRegisterDateStr());
    }
}
