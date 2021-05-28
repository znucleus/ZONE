package top.zbeboy.zone.service.excel;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.xssf.usermodel.XSSFRow;
import top.zbeboy.zbase.domain.tables.pojos.SoftwareSummaryAchievement;
import top.zbeboy.zbase.tools.service.util.DateTimeUtil;
import top.zbeboy.zbase.tools.service.util.ReadExcelUtil;
import top.zbeboy.zbase.tools.service.util.UUIDUtil;

public class SoftwareAchievementExcel extends ReadExcelUtil<SoftwareSummaryAchievement> {

    public SoftwareAchievementExcel(String path) {
        super(path);
    }

    @Override
    public SoftwareSummaryAchievement buildXls(HSSFRow hssfRow) {
        SoftwareSummaryAchievement softwareSummaryAchievement = new SoftwareSummaryAchievement();
        softwareSummaryAchievement.setAchievementId(UUIDUtil.getUUID());
        softwareSummaryAchievement.setRealName(getValue(hssfRow.getCell(1)));
        softwareSummaryAchievement.setSex(getValue(hssfRow.getCell(2)));
        softwareSummaryAchievement.setIdCard(getValue(hssfRow.getCell(3)));
        softwareSummaryAchievement.setMobile(getValue(hssfRow.getCell(4)));
        softwareSummaryAchievement.setScienceName(getValue(hssfRow.getCell(5)));
        softwareSummaryAchievement.setSchoolName(getValue(hssfRow.getCell(6)));
        softwareSummaryAchievement.setOrganizeName(getValue(hssfRow.getCell(7)));
        softwareSummaryAchievement.setLevel(getValue(hssfRow.getCell(8)));
        softwareSummaryAchievement.setExamType(getValue(hssfRow.getCell(9)));
        softwareSummaryAchievement.setMorningResults(getValue(hssfRow.getCell(10)));
        softwareSummaryAchievement.setAfternoonResults(getValue(hssfRow.getCell(11)));
        softwareSummaryAchievement.setThesisResults(getValue(hssfRow.getCell(12)));
        softwareSummaryAchievement.setIdentificationResults(getValue(hssfRow.getCell(13)));
        softwareSummaryAchievement.setPayment(getValue(hssfRow.getCell(14)));
        softwareSummaryAchievement.setRemark(getValue(hssfRow.getCell(15)));
        softwareSummaryAchievement.setExamDate(getValue(hssfRow.getCell(16)));
        softwareSummaryAchievement.setCreateDate(DateTimeUtil.getNowLocalDateTime());
        return softwareSummaryAchievement;
    }

    @Override
    public SoftwareSummaryAchievement buildXlsx(XSSFRow xssfRow) {
        SoftwareSummaryAchievement softwareSummaryAchievement = new SoftwareSummaryAchievement();
        softwareSummaryAchievement.setAchievementId(UUIDUtil.getUUID());
        softwareSummaryAchievement.setRealName(getValue(xssfRow.getCell(1)));
        softwareSummaryAchievement.setSex(getValue(xssfRow.getCell(2)));
        softwareSummaryAchievement.setIdCard(getValue(xssfRow.getCell(3)));
        softwareSummaryAchievement.setMobile(getValue(xssfRow.getCell(4)));
        softwareSummaryAchievement.setScienceName(getValue(xssfRow.getCell(5)));
        softwareSummaryAchievement.setSchoolName(getValue(xssfRow.getCell(6)));
        softwareSummaryAchievement.setOrganizeName(getValue(xssfRow.getCell(7)));
        softwareSummaryAchievement.setLevel(getValue(xssfRow.getCell(8)));
        softwareSummaryAchievement.setExamType(getValue(xssfRow.getCell(9)));
        softwareSummaryAchievement.setMorningResults(getValue(xssfRow.getCell(10)));
        softwareSummaryAchievement.setAfternoonResults(getValue(xssfRow.getCell(11)));
        softwareSummaryAchievement.setThesisResults(getValue(xssfRow.getCell(12)));
        softwareSummaryAchievement.setIdentificationResults(getValue(xssfRow.getCell(13)));
        softwareSummaryAchievement.setPayment(getValue(xssfRow.getCell(14)));
        softwareSummaryAchievement.setRemark(getValue(xssfRow.getCell(15)));
        softwareSummaryAchievement.setExamDate(getValue(xssfRow.getCell(16)));
        softwareSummaryAchievement.setCreateDate(DateTimeUtil.getNowLocalDateTime());
        return softwareSummaryAchievement;
    }
}
