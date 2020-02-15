package top.zbeboy.zone.service.internship;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.xwpf.usermodel.*;
import org.jooq.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.zbeboy.zone.config.Workbook;
import top.zbeboy.zone.domain.tables.daos.InternshipJournalDao;
import top.zbeboy.zone.domain.tables.pojos.InternshipJournal;
import top.zbeboy.zone.domain.tables.pojos.InternshipJournalContent;
import top.zbeboy.zone.domain.tables.pojos.Users;
import top.zbeboy.zone.domain.tables.pojos.UsersType;
import top.zbeboy.zone.domain.tables.records.InternshipJournalRecord;
import top.zbeboy.zone.service.data.StudentService;
import top.zbeboy.zone.service.platform.UsersService;
import top.zbeboy.zone.service.platform.UsersTypeService;
import top.zbeboy.zone.service.plugin.PaginationPlugin;
import top.zbeboy.zone.service.util.DateTimeUtil;
import top.zbeboy.zone.service.util.RequestUtil;
import top.zbeboy.zone.service.util.SQLQueryUtil;
import top.zbeboy.zone.web.bean.internship.journal.InternshipJournalBean;
import top.zbeboy.zone.web.util.SmallPropsUtil;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.jooq.impl.DSL.count;
import static top.zbeboy.zone.domain.Tables.INTERNSHIP_JOURNAL;
import static top.zbeboy.zone.domain.Tables.INTERNSHIP_TEACHER_DISTRIBUTION;
import static top.zbeboy.zone.domain.Tables.STUDENT;

@Service("internshipJournalService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class InternshipJournalServiceImpl implements InternshipJournalService, PaginationPlugin<DataTablesUtil> {

    private final Logger log = LoggerFactory.getLogger(InternshipJournalServiceImpl.class);

    private final DSLContext create;

    @Resource
    private InternshipJournalDao internshipJournalDao;

    @Resource
    private UsersService usersService;

    @Resource
    private UsersTypeService usersTypeService;

    @Resource
    private StudentService studentService;

    @Autowired
    InternshipJournalServiceImpl(DSLContext dslContext) {
        create = dslContext;
    }

    @Override
    public InternshipJournal findById(String id) {
        return internshipJournalDao.findById(id);
    }

    @Override
    public Result<InternshipJournalRecord> findByInternshipReleaseIdAndStudentId(String internshipReleaseId, int studentId) {
        return create.selectFrom(INTERNSHIP_JOURNAL)
                .where(INTERNSHIP_JOURNAL.INTERNSHIP_RELEASE_ID.eq(internshipReleaseId).and(INTERNSHIP_JOURNAL.STUDENT_ID.eq(studentId)))
                .fetch();
    }

    @Override
    public Result<InternshipJournalRecord> findByInternshipReleaseIdAndStaffId(String internshipReleaseId, int staffId) {
        return create.selectFrom(INTERNSHIP_JOURNAL)
                .where(INTERNSHIP_JOURNAL.INTERNSHIP_RELEASE_ID.eq(internshipReleaseId).and(INTERNSHIP_JOURNAL.STAFF_ID.eq(staffId)))
                .fetch();
    }

    @Override
    public Result<Record> findAllByPage(DataTablesUtil dataTablesUtil) {
        return queryAllByPage(create, INTERNSHIP_JOURNAL, dataTablesUtil, false);
    }

    @Override
    public int countAll(DataTablesUtil dataTablesUtil) {
        return countAll(create, INTERNSHIP_JOURNAL, dataTablesUtil, true);
    }

    @Override
    public int countByCondition(DataTablesUtil dataTablesUtil) {
        return countAll(create, INTERNSHIP_JOURNAL, dataTablesUtil, false);
    }

    @Override
    public Result<? extends Record3<String, String, ?>> countByInternshipReleaseIdAndStaffId(String internshipReleaseId, int staffId) {
        String countAlias = InternshipJournalBean.JOURNAL_NUM;
        SelectHavingStep<Record3<String, Integer, Integer>> journalTable =
                create.select(INTERNSHIP_JOURNAL.STUDENT_NUMBER,
                        INTERNSHIP_JOURNAL.STUDENT_ID,
                        count(INTERNSHIP_JOURNAL.INTERNSHIP_JOURNAL_ID).as(countAlias))
                        .from(INTERNSHIP_JOURNAL)
                .where(INTERNSHIP_JOURNAL.INTERNSHIP_RELEASE_ID.eq(internshipReleaseId)
                        .and(INTERNSHIP_JOURNAL.STAFF_ID.eq(staffId)))
                .groupBy(INTERNSHIP_JOURNAL.STUDENT_ID);
        return create.select(INTERNSHIP_TEACHER_DISTRIBUTION.STUDENT_REAL_NAME,
                journalTable.field(INTERNSHIP_JOURNAL.STUDENT_NUMBER),
                journalTable.field(countAlias))
                .from(INTERNSHIP_TEACHER_DISTRIBUTION)
                .leftJoin(journalTable)
                .on(INTERNSHIP_TEACHER_DISTRIBUTION.STUDENT_ID.eq(journalTable.field(INTERNSHIP_JOURNAL.STUDENT_ID)))
                .where(INTERNSHIP_TEACHER_DISTRIBUTION.INTERNSHIP_RELEASE_ID.eq(internshipReleaseId)
                        .and(INTERNSHIP_TEACHER_DISTRIBUTION.STAFF_ID.eq(staffId)))
                .fetch();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void save(InternshipJournal internshipJournal) {
        internshipJournalDao.insert(internshipJournal);
    }

    @Async
    @Override
    public void saveWord(InternshipJournal internshipJournal, InternshipJournalContent internshipJournalContent, Users users, HttpServletRequest request) {
        String outputPath = saveInternshipJournal(internshipJournal, internshipJournalContent, users, request);
        internshipJournal.setInternshipJournalWord(outputPath);
        update(internshipJournal);
    }

    @Override
    public void update(InternshipJournal internshipJournal) {
        internshipJournalDao.update(internshipJournal);
    }

    @Override
    public void deleteById(String id) {
        internshipJournalDao.deleteById(id);
    }

    @Override
    public Condition searchCondition(DataTablesUtil paginationUtil) {
        Condition a = null;
        JSONObject search = paginationUtil.getSearch();
        if (Objects.nonNull(search)) {
            String studentName = StringUtils.trim(search.getString("studentName"));
            String studentNumber = StringUtils.trim(search.getString("studentNumber"));
            String organize = StringUtils.trim(search.getString("organize"));
            if (StringUtils.isNotBlank(studentName)) {
                a = INTERNSHIP_JOURNAL.STUDENT_NAME.like(SQLQueryUtil.likeAllParam(studentName));
            }

            if (StringUtils.isNotBlank(studentNumber)) {
                if (Objects.isNull(a)) {
                    a = INTERNSHIP_JOURNAL.STUDENT_NUMBER.like(SQLQueryUtil.likeAllParam(studentNumber));
                } else {
                    a = a.and(INTERNSHIP_JOURNAL.STUDENT_NUMBER.like(SQLQueryUtil.likeAllParam(studentNumber)));
                }
            }

            if (StringUtils.isNotBlank(organize)) {
                if (Objects.isNull(a)) {
                    a = INTERNSHIP_JOURNAL.ORGANIZE.like(SQLQueryUtil.likeAllParam(organize));
                } else {
                    a = a.and(INTERNSHIP_JOURNAL.ORGANIZE.like(SQLQueryUtil.likeAllParam(organize)));
                }
            }
        }
        return a;
    }

    @Override
    public Condition extraCondition(DataTablesUtil paginationUtil) {
        Condition a = null;
        JSONObject search = paginationUtil.getSearch();
        if (Objects.nonNull(search)) {
            String internshipReleaseId = StringUtils.trim(search.getString("internshipReleaseId"));
            a = INTERNSHIP_JOURNAL.INTERNSHIP_RELEASE_ID.eq(internshipReleaseId);

            String dataRange = StringUtils.trim(search.getString("dataRange"));
            if (StringUtils.isBlank(dataRange)) {
                dataRange = "0";// 默认全部
            }

            int dataRangeInt = NumberUtils.toInt(dataRange);
            // 个人
            if (dataRangeInt == 1) {
                int studentId = 0;
                Users users = usersService.getUserFromSession();
                UsersType usersType = usersTypeService.findById(users.getUsersTypeId());
                if (Objects.nonNull(usersType)) {
                    if (StringUtils.equals(Workbook.STUDENT_USERS_TYPE, usersType.getUsersTypeName())) {
                        Optional<Record> record = studentService.findByUsernameRelation(users.getUsername());
                        if (record.isPresent()) {
                            studentId = record.get().get(STUDENT.STUDENT_ID);
                        }
                    }
                }
                a = a.and(INTERNSHIP_JOURNAL.STUDENT_ID.eq(studentId));
            } else if (dataRangeInt == 2) {
                // 小组
                String staffId = StringUtils.trim(search.getString("staffId"));
                List<Integer> staffs = SmallPropsUtil.StringIdsToNumberList(staffId);
                a = a.and(INTERNSHIP_JOURNAL.STAFF_ID.in(staffs));
            }
        }
        return a;
    }

    @Override
    public void sortCondition(SelectConnectByStep<Record> step, DataTablesUtil paginationUtil) {
        String orderColumnName = paginationUtil.getOrderColumnName();
        String orderDir = paginationUtil.getOrderDir();
        boolean isAsc = StringUtils.equalsIgnoreCase("asc", orderDir);
        SortField[] sortField = null;
        if (StringUtils.isNotBlank(orderColumnName)) {
            if (StringUtils.equals("studentName", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = INTERNSHIP_JOURNAL.STUDENT_NAME.asc();
                    sortField[1] = INTERNSHIP_JOURNAL.CREATE_DATE.asc();
                } else {
                    sortField[0] = INTERNSHIP_JOURNAL.STUDENT_NAME.desc();
                    sortField[1] = INTERNSHIP_JOURNAL.CREATE_DATE.desc();
                }
            }

            if (StringUtils.equals("studentNumber", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = INTERNSHIP_JOURNAL.STUDENT_NUMBER.asc();
                    sortField[1] = INTERNSHIP_JOURNAL.CREATE_DATE.asc();
                } else {
                    sortField[0] = INTERNSHIP_JOURNAL.STUDENT_NUMBER.desc();
                    sortField[1] = INTERNSHIP_JOURNAL.CREATE_DATE.desc();
                }
            }

            if (StringUtils.equals("organize", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = INTERNSHIP_JOURNAL.ORGANIZE.asc();
                    sortField[1] = INTERNSHIP_JOURNAL.CREATE_DATE.asc();
                } else {
                    sortField[0] = INTERNSHIP_JOURNAL.ORGANIZE.desc();
                    sortField[1] = INTERNSHIP_JOURNAL.CREATE_DATE.desc();
                }
            }

            if (StringUtils.equals("schoolGuidanceTeacher", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = INTERNSHIP_JOURNAL.SCHOOL_GUIDANCE_TEACHER.asc();
                    sortField[1] = INTERNSHIP_JOURNAL.CREATE_DATE.asc();
                } else {
                    sortField[0] = INTERNSHIP_JOURNAL.SCHOOL_GUIDANCE_TEACHER.desc();
                    sortField[1] = INTERNSHIP_JOURNAL.CREATE_DATE.desc();
                }
            }

            if (StringUtils.equals("createDateStr", orderColumnName)) {
                sortField = new SortField[1];
                if (isAsc) {
                    sortField[0] = INTERNSHIP_JOURNAL.CREATE_DATE.asc();
                } else {
                    sortField[0] = INTERNSHIP_JOURNAL.CREATE_DATE.desc();
                }
            }
        }
        sortToFinish(step, sortField);
    }

    /**
     * 保存数据
     *
     * @param internshipJournal        数据
     * @param internshipJournalContent 内容
     * @param users                    用户
     * @param request                  请求
     * @return 路径
     */
    private String saveInternshipJournal(InternshipJournal internshipJournal, InternshipJournalContent internshipJournalContent, Users users, HttpServletRequest request) {
        String outputPath = "";
        try {
            String templatePath = Workbook.INTERNSHIP_JOURNAL_FILE_PATH;
            InputStream is = new FileInputStream(templatePath);
            Map<String, String> cellMap = new HashMap<>();
            cellMap.put("${studentName}", internshipJournal.getStudentName());
            cellMap.put("${studentNumber}", internshipJournal.getStudentNumber());
            cellMap.put("${organize}", internshipJournal.getOrganize());
            cellMap.put("${schoolGuidanceTeacher}", internshipJournal.getSchoolGuidanceTeacher());
            cellMap.put("${graduationPracticeCompanyName}", internshipJournal.getGraduationPracticeCompanyName());

            Map<String, String> paraMap = new HashMap<>();
            paraMap.put("${internshipJournalContent}", internshipJournalContent.getInternshipJournalContent());
            paraMap.put("${date}", DateTimeUtil.formatSqlDate(internshipJournalContent.getInternshipJournalDate(), "yyyy年MM月dd日"));

            XWPFDocument doc = new XWPFDocument(is);

            Iterator<XWPFTable> itTable = doc.getTablesIterator();
            while (itTable.hasNext()) {
                XWPFTable table = itTable.next();
                int rcount = table.getNumberOfRows();
                for (int i = 0; i < rcount; i++) {
                    XWPFTableRow row = table.getRow(i);
                    List<XWPFTableCell> cells = row.getTableCells();
                    for (XWPFTableCell cell : cells) {
                        List<XWPFParagraph> itParas = cell.getParagraphs();
                        for (XWPFParagraph itPara : itParas) {

                            List<XWPFRun> runs = itPara.getRuns();
                            for (XWPFRun run : runs) {
                                String oneparaString = run.getText(
                                        run.getTextPosition());

                                for (Map.Entry<String, String> entry : paraMap
                                        .entrySet()) {
                                    oneparaString = oneparaString.replace(
                                            entry.getKey(), entry.getValue());
                                }

                                run.setText(oneparaString, 0);
                            }
                        }

                        String cellTextString = cell.getText();
                        for (Map.Entry<String, String> e : cellMap.entrySet()) {
                            if (cellTextString.contains(e.getKey())) {

                                cellTextString = cellTextString.replace(e.getKey(),
                                        e.getValue());
                                cell.removeParagraph(0);
                                cell.setText(cellTextString);
                            }

                        }

                    }
                }
            }

            String path = RequestUtil.getRealPath(request) + Workbook.internshipJournalPath(users);
            String filename = internshipJournal.getStudentName() + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")) + ".docx";
            File saveFile = new File(path);
            if (!saveFile.exists()) {
                saveFile.mkdirs();
            }
            OutputStream os = new FileOutputStream(path + filename);
            //把doc输出到输出流中
            doc.write(os);
            log.info("Save journal path {}", path);
            outputPath = Workbook.internshipJournalPath(users) + filename;
            this.closeStream(os);
            this.closeStream(is);
            log.info("Save internship journal finish, the path is {}", outputPath);
        } catch (IOException e) {
            log.error("Save internship journal error,error is {}", e);
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
