package top.zbeboy.zone.service.training;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.zbeboy.zone.config.Workbook;
import top.zbeboy.zone.service.util.RequestUtil;
import top.zbeboy.zone.web.bean.training.report.TrainingReportBean;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service("trainingReportService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class TrainingReportServiceImpl implements TrainingReportService {

    private final Logger log = LoggerFactory.getLogger(TrainingReportServiceImpl.class);

    @Override
    public String saveTrainingFile(TrainingReportBean bean, HttpServletRequest request, boolean isSenior) {
        Map<String, String> paraMap = new HashMap<>();
        if (isSenior) {
            paraMap.put("${organizeName}", bean.getOrganizeName());
            paraMap.put("${courseName}", bean.getCourseName());
            paraMap.put("${startYear}", bean.getStartYear());
            paraMap.put("${endYear}", bean.getEndYear());
            paraMap.put("${term}", bean.getTerm());
        } else {
            paraMap.put("${organizeName}", "");
            paraMap.put("${courseName}", "");
            paraMap.put("${startYear}", "");
            paraMap.put("${endYear}", "");
            paraMap.put("${term}", "");
        }
        paraMap.put("${realName}", bean.getRealName());

        paraMap.put("${year}", bean.getYear());
        paraMap.put("${month}", bean.getMonth());
        paraMap.put("${day}", bean.getDay());
        return saveFile(paraMap, bean.getRealName(), Workbook.TRAINING_FILE_PATH, request);
    }

    @Override
    public String saveTrainingSituation(TrainingReportBean bean, HttpServletRequest request, boolean isSenior) {
        Map<String, String> paraMap = new HashMap<>();
        if (isSenior) {
            paraMap.put("${organizeName}", bean.getOrganizeName());
            paraMap.put("${courseName}", bean.getCourseName());
            paraMap.put("${startYear}", bean.getStartYear());
            paraMap.put("${endYear}", bean.getEndYear());
            paraMap.put("${term}", bean.getTerm());
            paraMap.put("${scienceName}", bean.getScienceName());
            paraMap.put("${classRoom}", bean.getClassRoom());
            paraMap.put("${organizeNum}", bean.getOrganizeNum());
            paraMap.put("${courseType}", bean.getCourseType());
            paraMap.put("${startDate}", bean.getStartDate());
            paraMap.put("${endDate}", bean.getEndDate());
        } else {
            paraMap.put("${organizeName}", "");
            paraMap.put("${courseName}", "");
            paraMap.put("${startYear}", "");
            paraMap.put("${endYear}", "");
            paraMap.put("${term}", "");
            paraMap.put("${scienceName}", "");
            paraMap.put("${classRoom}", "");
            paraMap.put("${organizeNum}", "");
            paraMap.put("${courseType}", "");
            paraMap.put("${startDate}", "");
            paraMap.put("${endDate}", "");
        }
        paraMap.put("${realName}", bean.getRealName());
        paraMap.put("${year}", bean.getYear());
        paraMap.put("${month}", bean.getMonth());
        paraMap.put("${day}", bean.getDay());
        return saveFile(paraMap, bean.getRealName(), Workbook.TRAINING_SITUATION_PATH, request);
    }

    public String saveFile(Map<String, String> paraMap, String realName, String templatePath, HttpServletRequest request) {
        String outputPath = "";
        try {
            InputStream is = new FileInputStream(templatePath);

            XWPFDocument document = new XWPFDocument(is);
            // 替换段落中的指定文字
            Iterator<XWPFParagraph> itPara = document.getParagraphsIterator();
            while (itPara.hasNext()) {
                XWPFParagraph paragraph = itPara.next();
                List<XWPFRun> runs = paragraph.getRuns();
                for (XWPFRun run : runs) {
                    String oneparaString = run.getText(run.getTextPosition());
                    if (StringUtils.isBlank(oneparaString)) {
                        continue;
                    }
                    for (Map.Entry<String, String> entry :
                            paraMap.entrySet()) {
                        System.out.println("test1 : " + oneparaString);
                        oneparaString = oneparaString.replace(entry.getKey(), entry.getValue());
                    }
                    run.setText(oneparaString, 0);
                }

            }

            String path = RequestUtil.getRealPath(request) + Workbook.trainingReportPath();
            String filename = realName + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")) + ".docx";
            File saveFile = new File(path);
            if (!saveFile.exists()) {
                saveFile.mkdirs();
            }
            OutputStream os = new FileOutputStream(path + filename);
            //把doc输出到输出流中
            document.write(os);
            log.info("Save senior training file path {}", path);
            outputPath = Workbook.trainingReportPath() + filename;
            this.closeStream(os);
            this.closeStream(is);
            log.info("Save senior training file finish, the path is {}", outputPath);
        } catch (IOException e) {
            log.error("Save senior training file error,error is {}", e);
            return outputPath;
        }
        return outputPath;
    }

    /**
     * 关闭输入流
     *
     * @param is 流
     */
    private void closeStream(InputStream is) {
        if (is != null) {
            try {
                is.close();
            } catch (IOException e) {
                log.error("Close file is error, error {}", e);
            }
        }
    }

    /**
     * 关闭输出流
     *
     * @param os 流
     */
    private void closeStream(OutputStream os) {
        if (os != null) {
            try {
                os.close();
            } catch (IOException e) {
                log.error("Close file is error, error {}", e);
            }
        }
    }
}
