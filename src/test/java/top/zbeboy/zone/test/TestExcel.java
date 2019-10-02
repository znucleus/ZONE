package top.zbeboy.zone.test;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Ignore;
import org.junit.Test;
import top.zbeboy.zone.domain.tables.pojos.CurriculumContent;
import top.zbeboy.zone.service.util.DateTimeUtil;
import top.zbeboy.zone.web.util.IntegerUtil;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class TestExcel {

    public static final String OFFICE_EXCEL_2003_POSTFIX = "xls";
    public static final String OFFICE_EXCEL_2010_POSTFIX = "xlsx";

    public static final String EMPTY = "";
    public static final String POINT = ".";
    public static final String LIB_PATH = "lib";
    public static final String STUDENT_INFO_XLS_PATH = LIB_PATH + "/student_info" + POINT + OFFICE_EXCEL_2003_POSTFIX;
    public static final String STUDENT_INFO_XLSX_PATH = LIB_PATH + "/student_info" + POINT + OFFICE_EXCEL_2010_POSTFIX;
    public static final String NOT_EXCEL_FILE = " : Not the Excel file!";
    public static final String PROCESSING = "Processing...";

    @Test
    @Ignore
    public void testExcel() throws IOException {
        List<CurriculumContent> curriculumContents = readExcel("D:\\project\\项目分析\\Z.核\\排课系统\\明轩楼教室课表\\明轩楼304.xls");
        curriculumContents.forEach(System.out::println);
    }

    public static String getPostfix(String path) {
        if (path == null || EMPTY.equals(path.trim())) {
            return EMPTY;
        }
        if (path.contains(POINT)) {
            return path.substring(path.lastIndexOf(POINT) + 1, path.length());
        }
        return EMPTY;
    }


    public List<CurriculumContent> readExcel(String path) throws IOException {
        if (path == null || EMPTY.equals(path)) {
            return null;
        } else {
            String postfix = getPostfix(path);
            if (!EMPTY.equals(postfix)) {
                if (OFFICE_EXCEL_2003_POSTFIX.equals(postfix)) {
                    return readXls(path);
                } else if (OFFICE_EXCEL_2010_POSTFIX.equals(postfix)) {
                    return readXlsx(path);
                }
            } else {
                System.out.println(path + NOT_EXCEL_FILE);
            }
        }
        return null;
    }

    public List<CurriculumContent> readXlsx(String path) throws IOException {
        System.out.println(PROCESSING + path);
        InputStream is = new FileInputStream(path);
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
        List<CurriculumContent> list = new ArrayList<>();
        for (int numSheet = 0; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++) {
            XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(numSheet);
            if (xssfSheet == null) {
                continue;
            }
            for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
                XSSFRow xssfRow = xssfSheet.getRow(rowNum);
                if (xssfRow != null) {
                    for (int cellNum = 0; cellNum <= xssfRow.getLastCellNum(); cellNum++) {
                        XSSFCell content = xssfRow.getCell(cellNum);
                        System.out.println(getValue(content));
                    }
                }
            }
        }
        return list;
    }

    public List<CurriculumContent> readXls(String path) throws IOException {
        System.out.println(PROCESSING + path);
        InputStream is = new FileInputStream(path);
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
        List<CurriculumContent> list = new ArrayList<>();
        Map<String, Short> map = new HashMap<>();
        boolean initHead1 = false;
        boolean initHead2 = false;
        boolean initHead3 = false;
        boolean initHead4 = false;
        boolean initHead5 = false;
        boolean initHead6 = false;
        boolean initHead7 = false;
        boolean initHead8 = false;
        int batchNum1 = 0;// 应该数据库当前max
        int batchNum2 = 0;// 应该数据库当前max
        int batchNum3 = 0;// 应该数据库当前max
        int batchNum4 = 0;// 应该数据库当前max
        int batchNum5 = 0;// 应该数据库当前max
        int batchNum6 = 0;// 应该数据库当前max
        int batchNum7 = 0;// 应该数据库当前max
        for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
            HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
            if (hssfSheet == null) {
                continue;
            }
            for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
                HSSFRow hssfRow = hssfSheet.getRow(rowNum);
                if (hssfRow != null) {
                    for (short cellNum = 0; cellNum <= hssfRow.getLastCellNum(); cellNum++) {
                        HSSFCell content = hssfRow.getCell(cellNum);
                        if (Objects.nonNull(content)) {
                            String c = StringUtils.trim(getValue(content));

                            switch (c) {
                                case "星期一":
                                    map.put("星期一", cellNum);
                                    initHead1 = true;
                                    break;
                                case "星期二":
                                    map.put("星期二", cellNum);
                                    initHead2 = true;
                                    break;
                                case "星期三":
                                    map.put("星期三", cellNum);
                                    initHead3 = true;
                                    break;
                                case "星期四":
                                    map.put("星期四", cellNum);
                                    initHead4 = true;
                                    break;
                                case "星期五":
                                    map.put("星期五", cellNum);
                                    initHead5 = true;
                                    break;
                                case "星期六":
                                    map.put("星期六", cellNum);
                                    initHead6 = true;
                                    break;
                                case "星期天":
                                    map.put("星期天", cellNum);
                                    initHead7 = true;
                                    break;
                                case "时间":
                                    map.put("时间", cellNum);
                                    initHead8 = true;
                                    break;
                            }

                            if (!initHead1 || !initHead2 || !initHead3 || !initHead4 || !initHead5 || !initHead6 || !initHead7 || !initHead8) {
                                continue;
                            }

                            if (StringUtils.isNotBlank(c)) {
                                Short ac1 = map.get("星期一");
                                Short ac2 = map.get("星期二");
                                Short ac3 = map.get("星期三");
                                Short ac4 = map.get("星期四");
                                Short ac5 = map.get("星期五");
                                Short ac6 = map.get("星期六");
                                Short ac7 = map.get("星期天");
                                Short acTime = map.get("时间");

                                if (Objects.nonNull(ac1) && ac1 == cellNum) {
                                    batchNum1++;
                                    buildData(batchNum1, c, 1, hssfRow, acTime, list);
                                } else if (Objects.nonNull(ac2) && ac2 == cellNum) {
                                    batchNum2++;
                                    buildData(batchNum2, c, 2, hssfRow, acTime, list);
                                } else if (Objects.nonNull(ac3) && ac3 == cellNum) {
                                    batchNum3++;
                                    buildData(batchNum3, c, 3, hssfRow, acTime, list);
                                } else if (Objects.nonNull(ac4) && ac4 == cellNum) {
                                    batchNum4++;
                                    buildData(batchNum4, c, 4, hssfRow, acTime, list);
                                } else if (Objects.nonNull(ac5) && ac5 == cellNum) {
                                    batchNum5++;
                                    buildData(batchNum5, c, 5, hssfRow, acTime, list);
                                } else if (Objects.nonNull(ac6) && ac6 == cellNum) {
                                    batchNum6++;
                                    buildData(batchNum6, c, 6, hssfRow, acTime, list);
                                } else if (Objects.nonNull(ac7) && ac7 == cellNum) {
                                    batchNum7++;
                                    buildData(batchNum7, c, 7, hssfRow, acTime, list);
                                }
                            }

                        }
                    }
                }
            }
        }
        return list;
    }

    private String getValue(Cell cell) {
        if (cell.getCellType() == CellType.BOOLEAN) {
            return String.valueOf(cell.getBooleanCellValue());
        } else if (cell.getCellType() == CellType.NUMERIC) {
            return String.valueOf(cell.getNumericCellValue());
        } else {
            return String.valueOf(cell.getStringCellValue());
        }
    }

    private void buildData(int batchNum, String c, int week, HSSFRow hssfRow, Short acTime, List<CurriculumContent> list) {
        String[] cs1 = c.split("(\\n|\\r\\n)");
        if (cs1.length > 5) {
            CurriculumContent assignCoursesContent = new CurriculumContent();
            assignCoursesContent.setBatchNum(batchNum);
            assignCoursesContent.setWeek(IntegerUtil.toByte(week));

            assignCoursesContent.setCourseName(cs1[0] + cs1[1]);
            assignCoursesContent.setSchoolroomName(cs1[2]);
            assignCoursesContent.setOrganizeName(cs1[3]);
            assignCoursesContent.setStaffName(cs1[4]);

            String[] cs2 = cs1[5].split("周");

            if (cs2.length > 0) {
                String[] cs3 = cs2[0].split("-");
                if (cs3.length > 0) {
                    assignCoursesContent.setStartWeek(NumberUtils.toByte(cs3[0]));
                }

                if (cs3.length > 1) {
                    assignCoursesContent.setEndWeek(NumberUtils.toByte(cs3[1]));
                }
            }

            if (cs2.length > 1) {
                StringBuilder hoursStr = new StringBuilder();
                char[] hoursChar = cs2[1].toCharArray();
                for (char h : hoursChar) {
                    if (NumberUtils.isDigits(h + "")) {
                        hoursStr.append(h);
                    }
                }
                assignCoursesContent.setHours(NumberUtils.toDouble(hoursStr.toString()));
            }

            HSSFCell content = hssfRow.getCell(acTime);
            if (Objects.nonNull(content)) {
                // 取时间
                String time = StringUtils.trim(getValue(content));
                String[] cs4 = time.split("(\\n|\\r\\n)");
                if (cs4.length > 0) {
                    String[] cs6 = cs4[0].split("-");
                    if (cs6.length > 0) {
                        String firstTime = cs6[0];
                        if (firstTime.length() < 5) {
                            firstTime = "0" + firstTime;
                        }
                        assignCoursesContent.setStartTime(DateTimeUtil.defaultParseSqlTime(firstTime));
                    }

                    String[] cs7 = cs4[cs4.length - 1].split("-");
                    if (cs7.length > 1) {
                        String lastTime = cs7[1];
                        if (lastTime.length() < 5) {
                            lastTime = "0" + lastTime;
                        }
                        assignCoursesContent.setEndTime(DateTimeUtil.defaultParseSqlTime(lastTime));
                    }
                }
            }

            list.add(assignCoursesContent);
        }
    }
}
