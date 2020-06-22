package top.zbeboy.zone.service.export;

import org.apache.poi.ss.usermodel.Row;
import top.zbeboy.zbase.bean.register.leaver.LeaverRegisterDataBean;
import top.zbeboy.zbase.bean.register.leaver.LeaverRegisterOptionBean;
import top.zbeboy.zbase.tools.service.util.ExportUtil;
import top.zbeboy.zbase.tools.web.util.BooleanUtil;

import java.util.List;
import java.util.Objects;

public class LeaverRegisterDataExport extends ExportUtil<LeaverRegisterDataBean> {

    /**
     * 初始化数据
     *
     * @param data 数据
     */
    public LeaverRegisterDataExport(List<LeaverRegisterDataBean> data) {
        super(data);
    }

    @Override
    public void createHeader(Row row) {
        row.createCell(0).setCellValue("姓名");
        row.createCell(1).setCellValue("学号");
        row.createCell(2).setCellValue("班级");
        row.createCell(3).setCellValue("离校地点");
        int j = 4;
        List<LeaverRegisterDataBean> beans = getData();
        if (Objects.nonNull(beans) && beans.size() > 0) {
            List<LeaverRegisterOptionBean> leaverRegisterOptionBeans = beans.get(0).getLeaverRegisterOptions();
            if (Objects.nonNull(leaverRegisterOptionBeans) && leaverRegisterOptionBeans.size() > 0) {
                for (LeaverRegisterOptionBean leaverRegisterOptionBean : leaverRegisterOptionBeans) {
                    row.createCell(j).setCellValue(leaverRegisterOptionBean.getOptionContent());
                    j++;
                }
            }
        }
        row.createCell(j).setCellValue("备注");
    }

    @Override
    public void createCell(Row row, LeaverRegisterDataBean t) {
        row.createCell(0).setCellValue(t.getRealName());
        row.createCell(1).setCellValue(t.getStudentNumber());
        row.createCell(2).setCellValue(t.getOrganizeName());
        row.createCell(3).setCellValue(t.getLeaverAddress());
        int j = 4;
        List<LeaverRegisterOptionBean> leaverRegisterOptionBeans = t.getLeaverRegisterOptions();
        if (Objects.nonNull(leaverRegisterOptionBeans) && leaverRegisterOptionBeans.size() > 0) {
            for (LeaverRegisterOptionBean leaverRegisterOptionBean : leaverRegisterOptionBeans) {
                if (Objects.nonNull(leaverRegisterOptionBean.getIsChecked()) &&
                        BooleanUtil.toBoolean(leaverRegisterOptionBean.getIsChecked())) {
                    row.createCell(j).setCellValue("✔");
                } else {
                    row.createCell(j).setCellValue("");
                }
                j++;
            }
        }
        row.createCell(j).setCellValue(t.getRemark());
    }
}
