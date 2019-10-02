package top.zbeboy.zone.service.data;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.zbeboy.zone.config.CacheBook;
import top.zbeboy.zone.domain.tables.daos.StudentDao;
import top.zbeboy.zone.domain.tables.pojos.Student;
import top.zbeboy.zone.domain.tables.records.StudentRecord;
import top.zbeboy.zone.service.system.AuthoritiesService;
import top.zbeboy.zone.service.util.BCryptUtil;
import top.zbeboy.zone.web.util.BooleanUtil;
import top.zbeboy.zone.web.vo.data.student.StudentAddVo;

import javax.annotation.Resource;
import java.util.Optional;

import static top.zbeboy.zone.domain.Tables.*;

@Service("studentService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class StudentServiceImpl implements StudentService {

    private final DSLContext create;

    @Resource
    private StudentDao studentDao;

    @Resource
    private AuthoritiesService authoritiesService;

    @Autowired
    StudentServiceImpl(DSLContext dslContext) {
        create = dslContext;
    }

    @Override
    public Optional<StudentRecord> findByUsername(String username) {
        return create.selectFrom(STUDENT)
                .where(STUDENT.USERNAME.eq(username))
                .fetchOptional();
    }

    @Cacheable(cacheNames = CacheBook.STUDENT, key = "#username")
    @Override
    public Optional<Record> findByUsernameRelation(String username) {
        return create.select()
                .from(STUDENT)
                .join(ORGANIZE)
                .on(STUDENT.ORGANIZE_ID.eq(ORGANIZE.ORGANIZE_ID))
                .join(GRADE)
                .on(ORGANIZE.GRADE_ID.eq(GRADE.GRADE_ID))
                .join(SCIENCE)
                .on(GRADE.SCIENCE_ID.eq(SCIENCE.SCIENCE_ID))
                .join(DEPARTMENT)
                .on(SCIENCE.DEPARTMENT_ID.eq(DEPARTMENT.DEPARTMENT_ID))
                .join(COLLEGE)
                .on(DEPARTMENT.COLLEGE_ID.eq(COLLEGE.COLLEGE_ID))
                .join(SCHOOL)
                .on(COLLEGE.SCHOOL_ID.eq(SCHOOL.SCHOOL_ID))
                .where(STUDENT.USERNAME.eq(username))
                .fetchOptional();
    }

    @Override
    public Student findByStudentNumber(String studentNumber) {
        return studentDao.fetchOneByStudentNumber(studentNumber);
    }

    @Override
    public Optional<Record> findNormalByStudentNumberRelation(String studentNumber) {
        return create.select()
                .from(STUDENT)
                .join(USERS)
                .on(STUDENT.USERNAME.eq(USERS.USERNAME))
                .where(STUDENT.STUDENT_NUMBER.eq(studentNumber).and(USERS.VERIFY_MAILBOX.eq(BooleanUtil.toByte(true))).andExists(authoritiesService.existsAuthoritiesSelect()))
                .fetchOptional();
    }

    @Override
    public Result<StudentRecord> findByStudentNumberNeUsername(String studentNumber, String username) {
        return create.selectFrom(STUDENT)
                .where(STUDENT.STUDENT_NUMBER.eq(studentNumber).and(STUDENT.USERNAME.ne(username))).fetch();
    }

    @Override
    public Result<Record> findByOrganizeId(int organizeId) {
        return create.select()
                .from(STUDENT)
                .leftJoin(USERS)
                .on(STUDENT.USERNAME.eq(USERS.USERNAME))
                .where(STUDENT.ORGANIZE_ID.eq(organizeId).and(USERS.VERIFY_MAILBOX.eq(BooleanUtil.toByte(true))).andExists(authoritiesService.existsAuthoritiesSelect()))
                .fetch();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void save(Student student) {
        studentDao.insert(student);
    }

    @Override
    public void saveWithUsers(StudentAddVo studentAddVo) {
        create.transaction(configuration -> {
            DSL.using(configuration)
                    .insertInto(USERS, USERS.USERNAME, USERS.PASSWORD, USERS.ENABLED,
                            USERS.ACCOUNT_NON_EXPIRED, USERS.CREDENTIALS_NON_EXPIRED,
                            USERS.ACCOUNT_NON_LOCKED, USERS.AGREE_PROTOCOL, USERS.USERS_TYPE_ID,
                            USERS.REAL_NAME, USERS.EMAIL, USERS.MOBILE, USERS.AVATAR, USERS.MAILBOX_VERIFY_CODE,
                            USERS.MAILBOX_VERIFY_VALID, USERS.LANG_KEY, USERS.JOIN_DATE)
                    .values(studentAddVo.getUsername(), BCryptUtil.bCryptPassword(studentAddVo.getPassword()),
                            BooleanUtil.toByte(true), BooleanUtil.toByte(true), BooleanUtil.toByte(true),
                            BooleanUtil.toByte(true), studentAddVo.getAgreeProtocol(), studentAddVo.getUsersTypeId(), studentAddVo.getRealName(),
                            studentAddVo.getEmail(), studentAddVo.getMobile(), studentAddVo.getAvatar(), studentAddVo.getMailboxVerifyCode(),
                            studentAddVo.getMailboxVerifyValid(), studentAddVo.getLangKey(), studentAddVo.getJoinDate())
                    .execute();
            DSL.using(configuration)
                    .insertInto(STUDENT, STUDENT.USERNAME, STUDENT.STUDENT_NUMBER, STUDENT.ORGANIZE_ID)
                    .values(studentAddVo.getUsername(), studentAddVo.getStudentNumber(), studentAddVo.getOrganizeId())
                    .execute();
        });
    }

    @CacheEvict(cacheNames = CacheBook.STUDENT, key = "#student.username")
    @Override
    public void update(Student student) {
        studentDao.update(student);
    }
}
