/*
 * This file is generated by jOOQ.
 */
package top.zbeboy.zone.domain;


import javax.annotation.Generated;

import org.jooq.Index;
import org.jooq.OrderField;
import org.jooq.impl.Internal;

import top.zbeboy.zone.domain.tables.AcademicTitle;
import top.zbeboy.zone.domain.tables.AnswerBank;
import top.zbeboy.zone.domain.tables.AnswerOption;
import top.zbeboy.zone.domain.tables.AnswerRelease;
import top.zbeboy.zone.domain.tables.AnswerResult;
import top.zbeboy.zone.domain.tables.AnswerSolving;
import top.zbeboy.zone.domain.tables.AnswerSubject;
import top.zbeboy.zone.domain.tables.Application;
import top.zbeboy.zone.domain.tables.Authorities;
import top.zbeboy.zone.domain.tables.AuthorizeType;
import top.zbeboy.zone.domain.tables.Building;
import top.zbeboy.zone.domain.tables.College;
import top.zbeboy.zone.domain.tables.CollegeApplication;
import top.zbeboy.zone.domain.tables.CollegeRole;
import top.zbeboy.zone.domain.tables.Course;
import top.zbeboy.zone.domain.tables.Department;
import top.zbeboy.zone.domain.tables.Files;
import top.zbeboy.zone.domain.tables.Grade;
import top.zbeboy.zone.domain.tables.Nation;
import top.zbeboy.zone.domain.tables.Organize;
import top.zbeboy.zone.domain.tables.PoliticalLandscape;
import top.zbeboy.zone.domain.tables.Role;
import top.zbeboy.zone.domain.tables.RoleApplication;
import top.zbeboy.zone.domain.tables.RoleUsers;
import top.zbeboy.zone.domain.tables.School;
import top.zbeboy.zone.domain.tables.Schoolroom;
import top.zbeboy.zone.domain.tables.Science;
import top.zbeboy.zone.domain.tables.Staff;
import top.zbeboy.zone.domain.tables.Student;
import top.zbeboy.zone.domain.tables.SystemConfigure;
import top.zbeboy.zone.domain.tables.SystemMailboxLog;
import top.zbeboy.zone.domain.tables.SystemNotify;
import top.zbeboy.zone.domain.tables.SystemOperatorLog;
import top.zbeboy.zone.domain.tables.SystemSmsLog;
import top.zbeboy.zone.domain.tables.TrainingAttend;
import top.zbeboy.zone.domain.tables.TrainingAttendAuthorities;
import top.zbeboy.zone.domain.tables.TrainingAttendUsers;
import top.zbeboy.zone.domain.tables.TrainingLabs;
import top.zbeboy.zone.domain.tables.TrainingRelease;
import top.zbeboy.zone.domain.tables.TrainingUsers;
import top.zbeboy.zone.domain.tables.UserNotify;
import top.zbeboy.zone.domain.tables.Users;
import top.zbeboy.zone.domain.tables.UsersType;


/**
 * A class modelling indexes of tables of the <code>zone</code> schema.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.12.3"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Indexes {

    // -------------------------------------------------------------------------
    // INDEX definitions
    // -------------------------------------------------------------------------

    public static final Index ACADEMIC_TITLE_PRIMARY = Indexes0.ACADEMIC_TITLE_PRIMARY;
    public static final Index ANSWER_BANK_PRIMARY = Indexes0.ANSWER_BANK_PRIMARY;
    public static final Index ANSWER_BANK_USERNAME = Indexes0.ANSWER_BANK_USERNAME;
    public static final Index ANSWER_OPTION_ANSWER_SUBJECT_ID = Indexes0.ANSWER_OPTION_ANSWER_SUBJECT_ID;
    public static final Index ANSWER_OPTION_PRIMARY = Indexes0.ANSWER_OPTION_PRIMARY;
    public static final Index ANSWER_RELEASE_ANSWER_BANK_ID = Indexes0.ANSWER_RELEASE_ANSWER_BANK_ID;
    public static final Index ANSWER_RELEASE_PRIMARY = Indexes0.ANSWER_RELEASE_PRIMARY;
    public static final Index ANSWER_RELEASE_USERNAME = Indexes0.ANSWER_RELEASE_USERNAME;
    public static final Index ANSWER_RESULT_ANSWER_RELEASE_ID = Indexes0.ANSWER_RESULT_ANSWER_RELEASE_ID;
    public static final Index ANSWER_RESULT_PRIMARY = Indexes0.ANSWER_RESULT_PRIMARY;
    public static final Index ANSWER_SOLVING_ANSWER_RELEASE_ID = Indexes0.ANSWER_SOLVING_ANSWER_RELEASE_ID;
    public static final Index ANSWER_SOLVING_ANSWER_SUBJECT_ID = Indexes0.ANSWER_SOLVING_ANSWER_SUBJECT_ID;
    public static final Index ANSWER_SOLVING_PRIMARY = Indexes0.ANSWER_SOLVING_PRIMARY;
    public static final Index ANSWER_SUBJECT_ANSWER_BANK_ID = Indexes0.ANSWER_SUBJECT_ANSWER_BANK_ID;
    public static final Index ANSWER_SUBJECT_PRIMARY = Indexes0.ANSWER_SUBJECT_PRIMARY;
    public static final Index ANSWER_SUBJECT_USERNAME = Indexes0.ANSWER_SUBJECT_USERNAME;
    public static final Index APPLICATION_PRIMARY = Indexes0.APPLICATION_PRIMARY;
    public static final Index AUTHORITIES_PRIMARY = Indexes0.AUTHORITIES_PRIMARY;
    public static final Index AUTHORIZE_TYPE_AUTHORIZE_TYPE_NAME = Indexes0.AUTHORIZE_TYPE_AUTHORIZE_TYPE_NAME;
    public static final Index AUTHORIZE_TYPE_PRIMARY = Indexes0.AUTHORIZE_TYPE_PRIMARY;
    public static final Index BUILDING_COLLEGE_ID = Indexes0.BUILDING_COLLEGE_ID;
    public static final Index BUILDING_PRIMARY = Indexes0.BUILDING_PRIMARY;
    public static final Index COLLEGE_COLLEGE_CODE = Indexes0.COLLEGE_COLLEGE_CODE;
    public static final Index COLLEGE_PRIMARY = Indexes0.COLLEGE_PRIMARY;
    public static final Index COLLEGE_SCHOOL_ID = Indexes0.COLLEGE_SCHOOL_ID;
    public static final Index COLLEGE_APPLICATION_COLLEGE_ID = Indexes0.COLLEGE_APPLICATION_COLLEGE_ID;
    public static final Index COLLEGE_APPLICATION_PRIMARY = Indexes0.COLLEGE_APPLICATION_PRIMARY;
    public static final Index COLLEGE_ROLE_COLLEGE_ID = Indexes0.COLLEGE_ROLE_COLLEGE_ID;
    public static final Index COLLEGE_ROLE_PRIMARY = Indexes0.COLLEGE_ROLE_PRIMARY;
    public static final Index COURSE_COLLEGE_ID = Indexes0.COURSE_COLLEGE_ID;
    public static final Index COURSE_COURSE_CODE = Indexes0.COURSE_COURSE_CODE;
    public static final Index COURSE_PRIMARY = Indexes0.COURSE_PRIMARY;
    public static final Index DEPARTMENT_COLLEGE_ID = Indexes0.DEPARTMENT_COLLEGE_ID;
    public static final Index DEPARTMENT_PRIMARY = Indexes0.DEPARTMENT_PRIMARY;
    public static final Index FILES_PRIMARY = Indexes0.FILES_PRIMARY;
    public static final Index GRADE_PRIMARY = Indexes0.GRADE_PRIMARY;
    public static final Index GRADE_SCIENCE_ID = Indexes0.GRADE_SCIENCE_ID;
    public static final Index NATION_PRIMARY = Indexes0.NATION_PRIMARY;
    public static final Index ORGANIZE_GRADE_ID = Indexes0.ORGANIZE_GRADE_ID;
    public static final Index ORGANIZE_PRIMARY = Indexes0.ORGANIZE_PRIMARY;
    public static final Index POLITICAL_LANDSCAPE_PRIMARY = Indexes0.POLITICAL_LANDSCAPE_PRIMARY;
    public static final Index ROLE_PRIMARY = Indexes0.ROLE_PRIMARY;
    public static final Index ROLE_ROLE_EN_NAME = Indexes0.ROLE_ROLE_EN_NAME;
    public static final Index ROLE_APPLICATION_APPLICATION_ID = Indexes0.ROLE_APPLICATION_APPLICATION_ID;
    public static final Index ROLE_APPLICATION_PRIMARY = Indexes0.ROLE_APPLICATION_PRIMARY;
    public static final Index ROLE_USERS_AUTHORIZE_TYPE_ID = Indexes0.ROLE_USERS_AUTHORIZE_TYPE_ID;
    public static final Index ROLE_USERS_PRIMARY = Indexes0.ROLE_USERS_PRIMARY;
    public static final Index ROLE_USERS_USERNAME = Indexes0.ROLE_USERS_USERNAME;
    public static final Index SCHOOL_PRIMARY = Indexes0.SCHOOL_PRIMARY;
    public static final Index SCHOOLROOM_BUILDING_ID = Indexes0.SCHOOLROOM_BUILDING_ID;
    public static final Index SCHOOLROOM_PRIMARY = Indexes0.SCHOOLROOM_PRIMARY;
    public static final Index SCIENCE_DEPARTMENT_ID = Indexes0.SCIENCE_DEPARTMENT_ID;
    public static final Index SCIENCE_PRIMARY = Indexes0.SCIENCE_PRIMARY;
    public static final Index SCIENCE_SCIENCE_CODE = Indexes0.SCIENCE_SCIENCE_CODE;
    public static final Index STAFF_DEPARTMENT_ID = Indexes0.STAFF_DEPARTMENT_ID;
    public static final Index STAFF_PRIMARY = Indexes0.STAFF_PRIMARY;
    public static final Index STAFF_STAFF_NUMBER = Indexes0.STAFF_STAFF_NUMBER;
    public static final Index STAFF_USERNAME = Indexes0.STAFF_USERNAME;
    public static final Index STUDENT_ORGANIZE_ID = Indexes0.STUDENT_ORGANIZE_ID;
    public static final Index STUDENT_PRIMARY = Indexes0.STUDENT_PRIMARY;
    public static final Index STUDENT_STUDENT_NUMBER = Indexes0.STUDENT_STUDENT_NUMBER;
    public static final Index STUDENT_USERNAME = Indexes0.STUDENT_USERNAME;
    public static final Index SYSTEM_CONFIGURE_PRIMARY = Indexes0.SYSTEM_CONFIGURE_PRIMARY;
    public static final Index SYSTEM_MAILBOX_LOG_PRIMARY = Indexes0.SYSTEM_MAILBOX_LOG_PRIMARY;
    public static final Index SYSTEM_NOTIFY_PRIMARY = Indexes0.SYSTEM_NOTIFY_PRIMARY;
    public static final Index SYSTEM_NOTIFY_SEND_USER = Indexes0.SYSTEM_NOTIFY_SEND_USER;
    public static final Index SYSTEM_OPERATOR_LOG_PRIMARY = Indexes0.SYSTEM_OPERATOR_LOG_PRIMARY;
    public static final Index SYSTEM_SMS_LOG_PRIMARY = Indexes0.SYSTEM_SMS_LOG_PRIMARY;
    public static final Index TRAINING_ATTEND_ATTEND_ROOM = Indexes0.TRAINING_ATTEND_ATTEND_ROOM;
    public static final Index TRAINING_ATTEND_PRIMARY = Indexes0.TRAINING_ATTEND_PRIMARY;
    public static final Index TRAINING_ATTEND_TRAINING_RELEASE_ID = Indexes0.TRAINING_ATTEND_TRAINING_RELEASE_ID;
    public static final Index TRAINING_ATTEND_AUTHORITIES_PRIMARY = Indexes0.TRAINING_ATTEND_AUTHORITIES_PRIMARY;
    public static final Index TRAINING_ATTEND_AUTHORITIES_TRAINING_ATTEND_ID = Indexes0.TRAINING_ATTEND_AUTHORITIES_TRAINING_ATTEND_ID;
    public static final Index TRAINING_ATTEND_AUTHORITIES_USERNAME = Indexes0.TRAINING_ATTEND_AUTHORITIES_USERNAME;
    public static final Index TRAINING_ATTEND_USERS_PRIMARY = Indexes0.TRAINING_ATTEND_USERS_PRIMARY;
    public static final Index TRAINING_ATTEND_USERS_TRAINING_ATTEND_ID = Indexes0.TRAINING_ATTEND_USERS_TRAINING_ATTEND_ID;
    public static final Index TRAINING_ATTEND_USERS_TRAINING_USERS_ID = Indexes0.TRAINING_ATTEND_USERS_TRAINING_USERS_ID;
    public static final Index TRAINING_LABS_PRIMARY = Indexes0.TRAINING_LABS_PRIMARY;
    public static final Index TRAINING_LABS_STUDENT_NUMBER = Indexes0.TRAINING_LABS_STUDENT_NUMBER;
    public static final Index TRAINING_RELEASE_COURSE_ID = Indexes0.TRAINING_RELEASE_COURSE_ID;
    public static final Index TRAINING_RELEASE_ORGANIZE_ID = Indexes0.TRAINING_RELEASE_ORGANIZE_ID;
    public static final Index TRAINING_RELEASE_PRIMARY = Indexes0.TRAINING_RELEASE_PRIMARY;
    public static final Index TRAINING_RELEASE_SCHOOLROOM_ID = Indexes0.TRAINING_RELEASE_SCHOOLROOM_ID;
    public static final Index TRAINING_RELEASE_USERNAME = Indexes0.TRAINING_RELEASE_USERNAME;
    public static final Index TRAINING_USERS_PRIMARY = Indexes0.TRAINING_USERS_PRIMARY;
    public static final Index TRAINING_USERS_STUDENT_ID = Indexes0.TRAINING_USERS_STUDENT_ID;
    public static final Index TRAINING_USERS_TRAINING_RELEASE_ID = Indexes0.TRAINING_USERS_TRAINING_RELEASE_ID;
    public static final Index USER_NOTIFY_ACCEPT_USER = Indexes0.USER_NOTIFY_ACCEPT_USER;
    public static final Index USER_NOTIFY_PRIMARY = Indexes0.USER_NOTIFY_PRIMARY;
    public static final Index USER_NOTIFY_SEND_USER = Indexes0.USER_NOTIFY_SEND_USER;
    public static final Index USERS_AVATAR = Indexes0.USERS_AVATAR;
    public static final Index USERS_EMAIL = Indexes0.USERS_EMAIL;
    public static final Index USERS_MOBILE = Indexes0.USERS_MOBILE;
    public static final Index USERS_PRIMARY = Indexes0.USERS_PRIMARY;
    public static final Index USERS_USERS_TYPE_ID = Indexes0.USERS_USERS_TYPE_ID;
    public static final Index USERS_TYPE_PRIMARY = Indexes0.USERS_TYPE_PRIMARY;
    public static final Index USERS_TYPE_USERS_TYPE_NAME = Indexes0.USERS_TYPE_USERS_TYPE_NAME;

    // -------------------------------------------------------------------------
    // [#1459] distribute members to avoid static initialisers > 64kb
    // -------------------------------------------------------------------------

    private static class Indexes0 {
        public static Index ACADEMIC_TITLE_PRIMARY = Internal.createIndex("PRIMARY", AcademicTitle.ACADEMIC_TITLE, new OrderField[] { AcademicTitle.ACADEMIC_TITLE.ACADEMIC_TITLE_ID }, true);
        public static Index ANSWER_BANK_PRIMARY = Internal.createIndex("PRIMARY", AnswerBank.ANSWER_BANK, new OrderField[] { AnswerBank.ANSWER_BANK.ANSWER_BANK_ID }, true);
        public static Index ANSWER_BANK_USERNAME = Internal.createIndex("username", AnswerBank.ANSWER_BANK, new OrderField[] { AnswerBank.ANSWER_BANK.USERNAME }, false);
        public static Index ANSWER_OPTION_ANSWER_SUBJECT_ID = Internal.createIndex("answer_subject_id", AnswerOption.ANSWER_OPTION, new OrderField[] { AnswerOption.ANSWER_OPTION.ANSWER_SUBJECT_ID }, false);
        public static Index ANSWER_OPTION_PRIMARY = Internal.createIndex("PRIMARY", AnswerOption.ANSWER_OPTION, new OrderField[] { AnswerOption.ANSWER_OPTION.ANSWER_OPTION_ID }, true);
        public static Index ANSWER_RELEASE_ANSWER_BANK_ID = Internal.createIndex("answer_bank_id", AnswerRelease.ANSWER_RELEASE, new OrderField[] { AnswerRelease.ANSWER_RELEASE.ANSWER_BANK_ID }, false);
        public static Index ANSWER_RELEASE_PRIMARY = Internal.createIndex("PRIMARY", AnswerRelease.ANSWER_RELEASE, new OrderField[] { AnswerRelease.ANSWER_RELEASE.ANSWER_RELEASE_ID }, true);
        public static Index ANSWER_RELEASE_USERNAME = Internal.createIndex("username", AnswerRelease.ANSWER_RELEASE, new OrderField[] { AnswerRelease.ANSWER_RELEASE.USERNAME }, false);
        public static Index ANSWER_RESULT_ANSWER_RELEASE_ID = Internal.createIndex("answer_release_id", AnswerResult.ANSWER_RESULT, new OrderField[] { AnswerResult.ANSWER_RESULT.ANSWER_RELEASE_ID }, false);
        public static Index ANSWER_RESULT_PRIMARY = Internal.createIndex("PRIMARY", AnswerResult.ANSWER_RESULT, new OrderField[] { AnswerResult.ANSWER_RESULT.ANSWER_RESULT_ID }, true);
        public static Index ANSWER_SOLVING_ANSWER_RELEASE_ID = Internal.createIndex("answer_release_id", AnswerSolving.ANSWER_SOLVING, new OrderField[] { AnswerSolving.ANSWER_SOLVING.ANSWER_RELEASE_ID }, false);
        public static Index ANSWER_SOLVING_ANSWER_SUBJECT_ID = Internal.createIndex("answer_subject_id", AnswerSolving.ANSWER_SOLVING, new OrderField[] { AnswerSolving.ANSWER_SOLVING.ANSWER_SUBJECT_ID }, false);
        public static Index ANSWER_SOLVING_PRIMARY = Internal.createIndex("PRIMARY", AnswerSolving.ANSWER_SOLVING, new OrderField[] { AnswerSolving.ANSWER_SOLVING.ANSWER_SOLVING_ID }, true);
        public static Index ANSWER_SUBJECT_ANSWER_BANK_ID = Internal.createIndex("answer_bank_id", AnswerSubject.ANSWER_SUBJECT, new OrderField[] { AnswerSubject.ANSWER_SUBJECT.ANSWER_BANK_ID }, false);
        public static Index ANSWER_SUBJECT_PRIMARY = Internal.createIndex("PRIMARY", AnswerSubject.ANSWER_SUBJECT, new OrderField[] { AnswerSubject.ANSWER_SUBJECT.ANSWER_SUBJECT_ID }, true);
        public static Index ANSWER_SUBJECT_USERNAME = Internal.createIndex("username", AnswerSubject.ANSWER_SUBJECT, new OrderField[] { AnswerSubject.ANSWER_SUBJECT.USERNAME }, false);
        public static Index APPLICATION_PRIMARY = Internal.createIndex("PRIMARY", Application.APPLICATION, new OrderField[] { Application.APPLICATION.APPLICATION_ID }, true);
        public static Index AUTHORITIES_PRIMARY = Internal.createIndex("PRIMARY", Authorities.AUTHORITIES, new OrderField[] { Authorities.AUTHORITIES.USERNAME, Authorities.AUTHORITIES.AUTHORITY }, true);
        public static Index AUTHORIZE_TYPE_AUTHORIZE_TYPE_NAME = Internal.createIndex("authorize_type_name", AuthorizeType.AUTHORIZE_TYPE, new OrderField[] { AuthorizeType.AUTHORIZE_TYPE.AUTHORIZE_TYPE_NAME }, true);
        public static Index AUTHORIZE_TYPE_PRIMARY = Internal.createIndex("PRIMARY", AuthorizeType.AUTHORIZE_TYPE, new OrderField[] { AuthorizeType.AUTHORIZE_TYPE.AUTHORIZE_TYPE_ID }, true);
        public static Index BUILDING_COLLEGE_ID = Internal.createIndex("college_id", Building.BUILDING, new OrderField[] { Building.BUILDING.COLLEGE_ID }, false);
        public static Index BUILDING_PRIMARY = Internal.createIndex("PRIMARY", Building.BUILDING, new OrderField[] { Building.BUILDING.BUILDING_ID }, true);
        public static Index COLLEGE_COLLEGE_CODE = Internal.createIndex("college_code", College.COLLEGE, new OrderField[] { College.COLLEGE.COLLEGE_CODE }, true);
        public static Index COLLEGE_PRIMARY = Internal.createIndex("PRIMARY", College.COLLEGE, new OrderField[] { College.COLLEGE.COLLEGE_ID }, true);
        public static Index COLLEGE_SCHOOL_ID = Internal.createIndex("school_id", College.COLLEGE, new OrderField[] { College.COLLEGE.SCHOOL_ID }, false);
        public static Index COLLEGE_APPLICATION_COLLEGE_ID = Internal.createIndex("college_id", CollegeApplication.COLLEGE_APPLICATION, new OrderField[] { CollegeApplication.COLLEGE_APPLICATION.COLLEGE_ID }, false);
        public static Index COLLEGE_APPLICATION_PRIMARY = Internal.createIndex("PRIMARY", CollegeApplication.COLLEGE_APPLICATION, new OrderField[] { CollegeApplication.COLLEGE_APPLICATION.APPLICATION_ID, CollegeApplication.COLLEGE_APPLICATION.COLLEGE_ID }, true);
        public static Index COLLEGE_ROLE_COLLEGE_ID = Internal.createIndex("college_id", CollegeRole.COLLEGE_ROLE, new OrderField[] { CollegeRole.COLLEGE_ROLE.COLLEGE_ID }, false);
        public static Index COLLEGE_ROLE_PRIMARY = Internal.createIndex("PRIMARY", CollegeRole.COLLEGE_ROLE, new OrderField[] { CollegeRole.COLLEGE_ROLE.ROLE_ID, CollegeRole.COLLEGE_ROLE.COLLEGE_ID }, true);
        public static Index COURSE_COLLEGE_ID = Internal.createIndex("college_id", Course.COURSE, new OrderField[] { Course.COURSE.COLLEGE_ID }, false);
        public static Index COURSE_COURSE_CODE = Internal.createIndex("course_code", Course.COURSE, new OrderField[] { Course.COURSE.COURSE_CODE }, true);
        public static Index COURSE_PRIMARY = Internal.createIndex("PRIMARY", Course.COURSE, new OrderField[] { Course.COURSE.COURSE_ID }, true);
        public static Index DEPARTMENT_COLLEGE_ID = Internal.createIndex("college_id", Department.DEPARTMENT, new OrderField[] { Department.DEPARTMENT.COLLEGE_ID }, false);
        public static Index DEPARTMENT_PRIMARY = Internal.createIndex("PRIMARY", Department.DEPARTMENT, new OrderField[] { Department.DEPARTMENT.DEPARTMENT_ID }, true);
        public static Index FILES_PRIMARY = Internal.createIndex("PRIMARY", Files.FILES, new OrderField[] { Files.FILES.FILE_ID }, true);
        public static Index GRADE_PRIMARY = Internal.createIndex("PRIMARY", Grade.GRADE, new OrderField[] { Grade.GRADE.GRADE_ID }, true);
        public static Index GRADE_SCIENCE_ID = Internal.createIndex("science_id", Grade.GRADE, new OrderField[] { Grade.GRADE.SCIENCE_ID }, false);
        public static Index NATION_PRIMARY = Internal.createIndex("PRIMARY", Nation.NATION, new OrderField[] { Nation.NATION.NATION_ID }, true);
        public static Index ORGANIZE_GRADE_ID = Internal.createIndex("grade_id", Organize.ORGANIZE, new OrderField[] { Organize.ORGANIZE.GRADE_ID }, false);
        public static Index ORGANIZE_PRIMARY = Internal.createIndex("PRIMARY", Organize.ORGANIZE, new OrderField[] { Organize.ORGANIZE.ORGANIZE_ID }, true);
        public static Index POLITICAL_LANDSCAPE_PRIMARY = Internal.createIndex("PRIMARY", PoliticalLandscape.POLITICAL_LANDSCAPE, new OrderField[] { PoliticalLandscape.POLITICAL_LANDSCAPE.POLITICAL_LANDSCAPE_ID }, true);
        public static Index ROLE_PRIMARY = Internal.createIndex("PRIMARY", Role.ROLE, new OrderField[] { Role.ROLE.ROLE_ID }, true);
        public static Index ROLE_ROLE_EN_NAME = Internal.createIndex("role_en_name", Role.ROLE, new OrderField[] { Role.ROLE.ROLE_EN_NAME }, true);
        public static Index ROLE_APPLICATION_APPLICATION_ID = Internal.createIndex("application_id", RoleApplication.ROLE_APPLICATION, new OrderField[] { RoleApplication.ROLE_APPLICATION.APPLICATION_ID }, false);
        public static Index ROLE_APPLICATION_PRIMARY = Internal.createIndex("PRIMARY", RoleApplication.ROLE_APPLICATION, new OrderField[] { RoleApplication.ROLE_APPLICATION.ROLE_ID, RoleApplication.ROLE_APPLICATION.APPLICATION_ID }, true);
        public static Index ROLE_USERS_AUTHORIZE_TYPE_ID = Internal.createIndex("authorize_type_id", RoleUsers.ROLE_USERS, new OrderField[] { RoleUsers.ROLE_USERS.AUTHORIZE_TYPE_ID }, false);
        public static Index ROLE_USERS_PRIMARY = Internal.createIndex("PRIMARY", RoleUsers.ROLE_USERS, new OrderField[] { RoleUsers.ROLE_USERS.ROLE_USERS_ID }, true);
        public static Index ROLE_USERS_USERNAME = Internal.createIndex("username", RoleUsers.ROLE_USERS, new OrderField[] { RoleUsers.ROLE_USERS.USERNAME }, false);
        public static Index SCHOOL_PRIMARY = Internal.createIndex("PRIMARY", School.SCHOOL, new OrderField[] { School.SCHOOL.SCHOOL_ID }, true);
        public static Index SCHOOLROOM_BUILDING_ID = Internal.createIndex("building_id", Schoolroom.SCHOOLROOM, new OrderField[] { Schoolroom.SCHOOLROOM.BUILDING_ID }, false);
        public static Index SCHOOLROOM_PRIMARY = Internal.createIndex("PRIMARY", Schoolroom.SCHOOLROOM, new OrderField[] { Schoolroom.SCHOOLROOM.SCHOOLROOM_ID }, true);
        public static Index SCIENCE_DEPARTMENT_ID = Internal.createIndex("department_id", Science.SCIENCE, new OrderField[] { Science.SCIENCE.DEPARTMENT_ID }, false);
        public static Index SCIENCE_PRIMARY = Internal.createIndex("PRIMARY", Science.SCIENCE, new OrderField[] { Science.SCIENCE.SCIENCE_ID }, true);
        public static Index SCIENCE_SCIENCE_CODE = Internal.createIndex("science_code", Science.SCIENCE, new OrderField[] { Science.SCIENCE.SCIENCE_CODE }, true);
        public static Index STAFF_DEPARTMENT_ID = Internal.createIndex("department_id", Staff.STAFF, new OrderField[] { Staff.STAFF.DEPARTMENT_ID }, false);
        public static Index STAFF_PRIMARY = Internal.createIndex("PRIMARY", Staff.STAFF, new OrderField[] { Staff.STAFF.STAFF_ID }, true);
        public static Index STAFF_STAFF_NUMBER = Internal.createIndex("staff_number", Staff.STAFF, new OrderField[] { Staff.STAFF.STAFF_NUMBER }, true);
        public static Index STAFF_USERNAME = Internal.createIndex("username", Staff.STAFF, new OrderField[] { Staff.STAFF.USERNAME }, false);
        public static Index STUDENT_ORGANIZE_ID = Internal.createIndex("organize_id", Student.STUDENT, new OrderField[] { Student.STUDENT.ORGANIZE_ID }, false);
        public static Index STUDENT_PRIMARY = Internal.createIndex("PRIMARY", Student.STUDENT, new OrderField[] { Student.STUDENT.STUDENT_ID }, true);
        public static Index STUDENT_STUDENT_NUMBER = Internal.createIndex("student_number", Student.STUDENT, new OrderField[] { Student.STUDENT.STUDENT_NUMBER }, true);
        public static Index STUDENT_USERNAME = Internal.createIndex("username", Student.STUDENT, new OrderField[] { Student.STUDENT.USERNAME }, false);
        public static Index SYSTEM_CONFIGURE_PRIMARY = Internal.createIndex("PRIMARY", SystemConfigure.SYSTEM_CONFIGURE, new OrderField[] { SystemConfigure.SYSTEM_CONFIGURE.DATA_KEY }, true);
        public static Index SYSTEM_MAILBOX_LOG_PRIMARY = Internal.createIndex("PRIMARY", SystemMailboxLog.SYSTEM_MAILBOX_LOG, new OrderField[] { SystemMailboxLog.SYSTEM_MAILBOX_LOG.LOG_ID }, true);
        public static Index SYSTEM_NOTIFY_PRIMARY = Internal.createIndex("PRIMARY", SystemNotify.SYSTEM_NOTIFY, new OrderField[] { SystemNotify.SYSTEM_NOTIFY.SYSTEM_NOTIFY_ID }, true);
        public static Index SYSTEM_NOTIFY_SEND_USER = Internal.createIndex("send_user", SystemNotify.SYSTEM_NOTIFY, new OrderField[] { SystemNotify.SYSTEM_NOTIFY.SEND_USER }, false);
        public static Index SYSTEM_OPERATOR_LOG_PRIMARY = Internal.createIndex("PRIMARY", SystemOperatorLog.SYSTEM_OPERATOR_LOG, new OrderField[] { SystemOperatorLog.SYSTEM_OPERATOR_LOG.LOG_ID }, true);
        public static Index SYSTEM_SMS_LOG_PRIMARY = Internal.createIndex("PRIMARY", SystemSmsLog.SYSTEM_SMS_LOG, new OrderField[] { SystemSmsLog.SYSTEM_SMS_LOG.LOG_ID }, true);
        public static Index TRAINING_ATTEND_ATTEND_ROOM = Internal.createIndex("attend_room", TrainingAttend.TRAINING_ATTEND, new OrderField[] { TrainingAttend.TRAINING_ATTEND.ATTEND_ROOM }, false);
        public static Index TRAINING_ATTEND_PRIMARY = Internal.createIndex("PRIMARY", TrainingAttend.TRAINING_ATTEND, new OrderField[] { TrainingAttend.TRAINING_ATTEND.TRAINING_ATTEND_ID }, true);
        public static Index TRAINING_ATTEND_TRAINING_RELEASE_ID = Internal.createIndex("training_release_id", TrainingAttend.TRAINING_ATTEND, new OrderField[] { TrainingAttend.TRAINING_ATTEND.TRAINING_RELEASE_ID }, false);
        public static Index TRAINING_ATTEND_AUTHORITIES_PRIMARY = Internal.createIndex("PRIMARY", TrainingAttendAuthorities.TRAINING_ATTEND_AUTHORITIES, new OrderField[] { TrainingAttendAuthorities.TRAINING_ATTEND_AUTHORITIES.AUTHORITIES_ID }, true);
        public static Index TRAINING_ATTEND_AUTHORITIES_TRAINING_ATTEND_ID = Internal.createIndex("training_attend_id", TrainingAttendAuthorities.TRAINING_ATTEND_AUTHORITIES, new OrderField[] { TrainingAttendAuthorities.TRAINING_ATTEND_AUTHORITIES.TRAINING_ATTEND_ID }, false);
        public static Index TRAINING_ATTEND_AUTHORITIES_USERNAME = Internal.createIndex("username", TrainingAttendAuthorities.TRAINING_ATTEND_AUTHORITIES, new OrderField[] { TrainingAttendAuthorities.TRAINING_ATTEND_AUTHORITIES.USERNAME }, false);
        public static Index TRAINING_ATTEND_USERS_PRIMARY = Internal.createIndex("PRIMARY", TrainingAttendUsers.TRAINING_ATTEND_USERS, new OrderField[] { TrainingAttendUsers.TRAINING_ATTEND_USERS.ATTEND_USERS_ID }, true);
        public static Index TRAINING_ATTEND_USERS_TRAINING_ATTEND_ID = Internal.createIndex("training_attend_id", TrainingAttendUsers.TRAINING_ATTEND_USERS, new OrderField[] { TrainingAttendUsers.TRAINING_ATTEND_USERS.TRAINING_ATTEND_ID, TrainingAttendUsers.TRAINING_ATTEND_USERS.TRAINING_USERS_ID }, true);
        public static Index TRAINING_ATTEND_USERS_TRAINING_USERS_ID = Internal.createIndex("training_users_id", TrainingAttendUsers.TRAINING_ATTEND_USERS, new OrderField[] { TrainingAttendUsers.TRAINING_ATTEND_USERS.TRAINING_USERS_ID }, false);
        public static Index TRAINING_LABS_PRIMARY = Internal.createIndex("PRIMARY", TrainingLabs.TRAINING_LABS, new OrderField[] { TrainingLabs.TRAINING_LABS.TRAINING_LABS_ID }, true);
        public static Index TRAINING_LABS_STUDENT_NUMBER = Internal.createIndex("student_number", TrainingLabs.TRAINING_LABS, new OrderField[] { TrainingLabs.TRAINING_LABS.STUDENT_NUMBER }, true);
        public static Index TRAINING_RELEASE_COURSE_ID = Internal.createIndex("course_id", TrainingRelease.TRAINING_RELEASE, new OrderField[] { TrainingRelease.TRAINING_RELEASE.COURSE_ID }, false);
        public static Index TRAINING_RELEASE_ORGANIZE_ID = Internal.createIndex("organize_id", TrainingRelease.TRAINING_RELEASE, new OrderField[] { TrainingRelease.TRAINING_RELEASE.ORGANIZE_ID }, false);
        public static Index TRAINING_RELEASE_PRIMARY = Internal.createIndex("PRIMARY", TrainingRelease.TRAINING_RELEASE, new OrderField[] { TrainingRelease.TRAINING_RELEASE.TRAINING_RELEASE_ID }, true);
        public static Index TRAINING_RELEASE_SCHOOLROOM_ID = Internal.createIndex("schoolroom_id", TrainingRelease.TRAINING_RELEASE, new OrderField[] { TrainingRelease.TRAINING_RELEASE.SCHOOLROOM_ID }, false);
        public static Index TRAINING_RELEASE_USERNAME = Internal.createIndex("username", TrainingRelease.TRAINING_RELEASE, new OrderField[] { TrainingRelease.TRAINING_RELEASE.USERNAME }, false);
        public static Index TRAINING_USERS_PRIMARY = Internal.createIndex("PRIMARY", TrainingUsers.TRAINING_USERS, new OrderField[] { TrainingUsers.TRAINING_USERS.TRAINING_USERS_ID }, true);
        public static Index TRAINING_USERS_STUDENT_ID = Internal.createIndex("student_id", TrainingUsers.TRAINING_USERS, new OrderField[] { TrainingUsers.TRAINING_USERS.STUDENT_ID }, false);
        public static Index TRAINING_USERS_TRAINING_RELEASE_ID = Internal.createIndex("training_release_id", TrainingUsers.TRAINING_USERS, new OrderField[] { TrainingUsers.TRAINING_USERS.TRAINING_RELEASE_ID, TrainingUsers.TRAINING_USERS.STUDENT_ID }, true);
        public static Index USER_NOTIFY_ACCEPT_USER = Internal.createIndex("accept_user", UserNotify.USER_NOTIFY, new OrderField[] { UserNotify.USER_NOTIFY.ACCEPT_USER }, false);
        public static Index USER_NOTIFY_PRIMARY = Internal.createIndex("PRIMARY", UserNotify.USER_NOTIFY, new OrderField[] { UserNotify.USER_NOTIFY.USER_NOTIFY_ID }, true);
        public static Index USER_NOTIFY_SEND_USER = Internal.createIndex("send_user", UserNotify.USER_NOTIFY, new OrderField[] { UserNotify.USER_NOTIFY.SEND_USER }, false);
        public static Index USERS_AVATAR = Internal.createIndex("avatar", Users.USERS, new OrderField[] { Users.USERS.AVATAR }, false);
        public static Index USERS_EMAIL = Internal.createIndex("email", Users.USERS, new OrderField[] { Users.USERS.EMAIL }, true);
        public static Index USERS_MOBILE = Internal.createIndex("mobile", Users.USERS, new OrderField[] { Users.USERS.MOBILE }, true);
        public static Index USERS_PRIMARY = Internal.createIndex("PRIMARY", Users.USERS, new OrderField[] { Users.USERS.USERNAME }, true);
        public static Index USERS_USERS_TYPE_ID = Internal.createIndex("users_type_id", Users.USERS, new OrderField[] { Users.USERS.USERS_TYPE_ID }, false);
        public static Index USERS_TYPE_PRIMARY = Internal.createIndex("PRIMARY", UsersType.USERS_TYPE, new OrderField[] { UsersType.USERS_TYPE.USERS_TYPE_ID }, true);
        public static Index USERS_TYPE_USERS_TYPE_NAME = Internal.createIndex("users_type_name", UsersType.USERS_TYPE, new OrderField[] { UsersType.USERS_TYPE.USERS_TYPE_NAME }, true);
    }
}
