package top.zbeboy.zone.service.export;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import top.zbeboy.zbase.bean.training.attend.TrainingAttendUsersBean;
import top.zbeboy.zbase.tools.service.util.DateTimeUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

public class TrainingAttendSituationExport {

    // 序号
    private double sequence = 0;
    private double sequence2 = 0;

    private final List<TrainingAttendUsersBean> data;
    private List<LocalDate> data2;
    private List<Map<String, String>> students;

    /**
     * 初始化数据
     *
     * @param data 数据
     */
    public TrainingAttendSituationExport(List<TrainingAttendUsersBean> data) {
        this.data = data;
    }

    public boolean exportExcel(String outputPath, String fileName, String ext) throws IOException {
        boolean isCreate = false;
        Workbook wb = null;
        if (StringUtils.equals(ext, top.zbeboy.zbase.tools.config.Workbook.fileSuffix.xls.name())) {
            wb = new HSSFWorkbook();
        } else if (StringUtils.equals(ext, top.zbeboy.zbase.tools.config.Workbook.fileSuffix.xlsx.name())) {
            wb = new XSSFWorkbook();
        }

        if (Objects.nonNull(wb)) {
            if (Objects.nonNull(data)) {
                Sheet sheet1 = wb.createSheet("考勤情况");
                Row row1 = sheet1.createRow(0);
                createHeader1(row1);
                for (int i = 0; i < data.size(); i++) {
                    row1 = sheet1.createRow(i + 1);
                    createCell1(row1, data.get(i));
                }
            }

            Sheet sheet2 = wb.createSheet("考勤统计");
            Row row2 = sheet2.createRow(0);
            createHeader2(row2);
            if(Objects.nonNull(students) && Objects.nonNull(data2)){
                for (int i = 0; i < students.size(); i++) {
                    row2 = sheet2.createRow(i + 1);
                    createCell2(row2);
                }
            }


            File saveFile = new File(outputPath, fileName + "." + ext);
            if (!saveFile.getParentFile().exists()) {//create file
                saveFile.getParentFile().mkdirs();
            }
            // Write the output to a file
            FileOutputStream fileOut = new FileOutputStream(outputPath + fileName + "." + ext);
            wb.write(fileOut);
            fileOut.close();
            isCreate = true;
        }
        return isCreate;
    }

    public void createHeader1(Row row) {
        row.createCell(0).setCellValue("序号");
        row.createCell(1).setCellValue("姓名");
        row.createCell(2).setCellValue("学号");
        row.createCell(3).setCellValue("日期");
        row.createCell(4).setCellValue("班级");
        row.createCell(5).setCellValue("性别");
        row.createCell(6).setCellValue("状态");
        row.createCell(7).setCellValue("备注");
    }

    public void createHeader2(Row row) {
        row.createCell(0).setCellValue("序号");
        row.createCell(1).setCellValue("学号");
        row.createCell(2).setCellValue("姓名");
        row.createCell(3).setCellValue("性别");
        // 根据考勤日期生成列
        data2 = new ArrayList<>();
        students = new ArrayList<>();
        if (Objects.nonNull(data)) {
            for (TrainingAttendUsersBean bean : data) {
                boolean hasDate = false;
                for (LocalDate d : data2) {
                    if (bean.getAttendDate().isEqual(d)) {
                        hasDate = true;
                        break;
                    }
                }
                if (!hasDate) {
                    data2.add(bean.getAttendDate());
                }

                boolean hasStudent = false;
                for(Map<String, String> map : students){
                    if(StringUtils.equals(bean.getStudentNumber(), map.get("studentNumber"))){
                        hasStudent = true;
                        break;
                    }
                }
                if(!hasStudent){
                    Map<String, String> map = new HashMap<>();
                    map.put("studentNumber", bean.getStudentNumber());
                    map.put("realName", bean.getRealName());
                    map.put("sex", bean.getSex());
                    students.add(map);
                }
            }
        }

        // 默认顺序排序
        data2.sort(LocalDate::compareTo);
        for (int i = 4; i < data2.size(); i++) {
            row.createCell(i).setCellValue(DateTimeUtil.defaultFormatLocalDate(data2.get(i)));
        }

    }

    public void createCell1(Row row, TrainingAttendUsersBean t) {
        sequence++;
        row.createCell(0).setCellValue(sequence);
        row.createCell(1).setCellValue(t.getRealName());
        row.createCell(2).setCellValue(t.getStudentNumber());
        row.createCell(3).setCellValue(DateTimeUtil.defaultFormatLocalDate(t.getAttendDate()));
        row.createCell(4).setCellValue(t.getOrganizeName());
        row.createCell(5).setCellValue(t.getSex());
        row.createCell(6).setCellValue(operate(t.getOperate()));
        row.createCell(7).setCellValue(t.getRemark());
    }

    public void createCell2(Row row) {
        sequence2++;
        row.createCell(0).setCellValue(sequence2);
        // 要一组一组的按照学号
        for(Map<String, String> student : students){
            row.createCell(1).setCellValue(student.get("studentNumber"));
            row.createCell(2).setCellValue(student.get("realName"));
            row.createCell(3).setCellValue(student.get("sex"));
            for (int i = 4; i < data2.size(); i++) {
                row.createCell(i).setCellValue("");
                for (TrainingAttendUsersBean bean : data) {
                    if(StringUtils.equals(bean.getStudentNumber(), student.get("studentNumber")) &&
                    bean.getAttendDate().isEqual(data2.get(i))){
                        row.createCell(i).setCellValue(operate2(bean.getOperate()));
                        break;
                    }
                }
            }
        }
    }

    private String operate(Byte operate) {
        String v = "不存在";
        if (Objects.nonNull(operate)) {
            if (operate == 0) {
                v = "缺席";
            } else if (operate == 1) {
                v = "请假";
            } else if (operate == 2) {
                v = "迟到";
            } else if (operate == 3) {
                v = "正常";
            }
        }
        return v;
    }

    private String operate2(Byte operate) {
        String v = "";
        if (Objects.nonNull(operate)) {
            if (operate == 0) {
                v = "×";
            } else if (operate == 1) {
                v = "s";
            } else if (operate == 2) {
                v = "c";
            } else if (operate == 3) {
                v = "√";
            }
        }
        return v;
    }
}
