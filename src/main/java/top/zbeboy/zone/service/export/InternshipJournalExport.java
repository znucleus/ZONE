package top.zbeboy.zone.service.export;

import org.apache.poi.ss.usermodel.Row;
import top.zbeboy.zone.service.util.ExportUtil;
import top.zbeboy.zone.web.bean.internship.journal.InternshipJournalBean;

import java.util.List;

public class InternshipJournalExport extends ExportUtil<InternshipJournalBean> {

    // 序号
    private double sequence = 0;

    /**
     * 初始化数据
     *
     * @param data 数据
     */
    public InternshipJournalExport(List<InternshipJournalBean> data) {
        super(data);
    }

    @Override
    public void createHeader(Row row) {
        row.createCell(0).setCellValue("序号");
        row.createCell(1).setCellValue("姓名");
        row.createCell(2).setCellValue("学号");
        row.createCell(3).setCellValue("班级");
        row.createCell(4).setCellValue("内容");
        row.createCell(5).setCellValue("日期");
    }

    @Override
    public void createCell(Row row, InternshipJournalBean t) {
        sequence++;
        row.createCell(0).setCellValue(sequence);
        row.createCell(1).setCellValue(t.getStudentName());
        row.createCell(2).setCellValue(t.getStudentNumber());
        row.createCell(3).setCellValue(t.getOrganize());
        row.createCell(4).setCellValue(t.getInternshipJournalContent());
        row.createCell(5).setCellValue(t.getInternshipJournalDateStr());
    }
}
