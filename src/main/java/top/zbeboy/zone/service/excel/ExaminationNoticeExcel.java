package top.zbeboy.zone.service.excel;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.xssf.usermodel.XSSFRow;
import top.zbeboy.zbase.domain.tables.pojos.ExaminationNoticeDetail;
import top.zbeboy.zbase.tools.service.util.ReadExcelUtil;
import top.zbeboy.zbase.tools.service.util.UUIDUtil;

import java.util.HashMap;
import java.util.Map;

public class ExaminationNoticeExcel extends ReadExcelUtil<ExaminationNoticeDetail> {

    public ExaminationNoticeExcel(String path, int dataIndex) {
        super(path, dataIndex);
    }

    @Override
    public Map<String, String> buildXlsxTitle(XSSFRow xssfRow) {
        Map<String, String> title = new HashMap<>();
        title.put("title", getValue(xssfRow.getCell(0)));
        return title;
    }

    @Override
    public Map<String, String> buildXlsTitle(HSSFRow hssfRow) {
        Map<String, String> title = new HashMap<>();
        title.put("title", getValue(hssfRow.getCell(0)));
        return title;
    }

    @Override
    public ExaminationNoticeDetail buildXls(HSSFRow hssfRow) {
        ExaminationNoticeDetail examinationNoticeDetail = null;
        if (StringUtils.isNotBlank(getValue(hssfRow.getCell(0)))) {
            examinationNoticeDetail = new ExaminationNoticeDetail();
            examinationNoticeDetail.setExaminationNoticeDetailId(UUIDUtil.getUUID());
            examinationNoticeDetail.setSerialNumber(Byte.parseByte(getValue(hssfRow.getCell(0))));
            examinationNoticeDetail.setCourseNumber(getValue(hssfRow.getCell(1)));
            examinationNoticeDetail.setCourseName(getValue(hssfRow.getCell(2)));
            examinationNoticeDetail.setOrganizeName(getValue(hssfRow.getCell(3)));
            examinationNoticeDetail.setPeopleNumber(Byte.parseByte(getValue(hssfRow.getCell(4))));
            examinationNoticeDetail.setSerialNumberRange(getValue(hssfRow.getCell(5)));
            examinationNoticeDetail.setExamWeek(Byte.parseByte(getValue(hssfRow.getCell(6))));
            examinationNoticeDetail.setExamDate(getValue(hssfRow.getCell(7)));
            examinationNoticeDetail.setWeekday(getValue(hssfRow.getCell(8)));
            examinationNoticeDetail.setExamTime(getValue(hssfRow.getCell(9)));
            examinationNoticeDetail.setExamClassroom(getValue(hssfRow.getCell(10)));
            examinationNoticeDetail.setInvigilator(getValue(hssfRow.getCell(11)));
            examinationNoticeDetail.setChiefExaminer(getValue(hssfRow.getCell(12)));
            examinationNoticeDetail.setMobiles(getValue(hssfRow.getCell(13)));
        }

        return examinationNoticeDetail;
    }

    @Override
    public ExaminationNoticeDetail buildXlsx(XSSFRow xssfRow) {
        ExaminationNoticeDetail examinationNoticeDetail = null;
        if (StringUtils.isNotBlank(getValue(xssfRow.getCell(0)))) {
            examinationNoticeDetail = new ExaminationNoticeDetail();
            examinationNoticeDetail.setExaminationNoticeDetailId(UUIDUtil.getUUID());
            examinationNoticeDetail.setSerialNumber(Byte.parseByte(getValue(xssfRow.getCell(0))));
            examinationNoticeDetail.setCourseNumber(getValue(xssfRow.getCell(1)));
            examinationNoticeDetail.setCourseName(getValue(xssfRow.getCell(2)));
            examinationNoticeDetail.setOrganizeName(getValue(xssfRow.getCell(3)));
            examinationNoticeDetail.setPeopleNumber(Byte.parseByte(getValue(xssfRow.getCell(4))));
            examinationNoticeDetail.setSerialNumberRange(getValue(xssfRow.getCell(5)));
            examinationNoticeDetail.setExamWeek(Byte.parseByte(getValue(xssfRow.getCell(6))));
            examinationNoticeDetail.setExamDate(getValue(xssfRow.getCell(7)));
            examinationNoticeDetail.setWeekday(getValue(xssfRow.getCell(8)));
            examinationNoticeDetail.setExamTime(getValue(xssfRow.getCell(9)));
            examinationNoticeDetail.setExamClassroom(getValue(xssfRow.getCell(10)));
            examinationNoticeDetail.setInvigilator(getValue(xssfRow.getCell(11)));
            examinationNoticeDetail.setChiefExaminer(getValue(xssfRow.getCell(12)));
            examinationNoticeDetail.setMobiles(getValue(xssfRow.getCell(13)));
        }
        return examinationNoticeDetail;
    }
}
