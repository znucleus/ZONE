CREATE TABLE files (
    file_id VARCHAR(64) PRIMARY KEY,
    file_size BIGINT,
	  content_type VARCHAR(20),
    original_file_name VARCHAR(300),
    new_name VARCHAR(300),
    relative_path VARCHAR(800),
    ext VARCHAR(20)
);

CREATE TABLE users_type (
    users_type_id INT AUTO_INCREMENT PRIMARY KEY,
    users_type_name VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE users (
    username VARCHAR(64) PRIMARY KEY,
    password VARCHAR(300) NOT NULL,
    enabled BOOLEAN NOT NULL,
    account_non_expired BOOLEAN NOT NULL,
    credentials_non_expired BOOLEAN NOT NULL,
    account_non_locked BOOLEAN NOT NULL,
	  agree_protocol BOOLEAN NOT NULL,
    users_type_id INT NOT NULL,
    real_name VARCHAR(30),
    email VARCHAR(100) UNIQUE NOT NULL,
    mobile VARCHAR(15) UNIQUE NOT NULL,
    id_card VARCHAR(20) ,
    avatar VARCHAR(64),
    verify_mailbox BOOLEAN,
    mailbox_verify_code VARCHAR(20),
    password_reset_key VARCHAR(20),
    mailbox_verify_valid DATETIME,
    password_reset_key_valid DATETIME,
    lang_key VARCHAR(20),
    join_date DATE,
    FOREIGN KEY (users_type_id)
        REFERENCES users_type (users_type_id),
    FOREIGN KEY (avatar)
        REFERENCES files (file_id)
);

CREATE TABLE role (
    role_id VARCHAR(64) PRIMARY KEY,
    role_name VARCHAR(50) NOT NULL,
    role_en_name VARCHAR(64) UNIQUE NOT NULL,
    role_type INT NOT NULL
);

CREATE TABLE authorities (
    username VARCHAR(64) NOT NULL,
    authority VARCHAR(50) NOT NULL,
    FOREIGN KEY (username)
        REFERENCES users (username) ON UPDATE CASCADE ON DELETE CASCADE,
    PRIMARY KEY (username , authority)
);

CREATE TABLE application (
    application_id VARCHAR(64) PRIMARY KEY,
    application_name VARCHAR(30) NOT NULL,
    application_sort INT,
    application_pid VARCHAR(64) NOT NULL,
    application_url VARCHAR(300) NOT NULL,
    application_code VARCHAR(100) NOT NULL,
    application_en_name VARCHAR(100) NOT NULL,
    icon VARCHAR(100),
    application_data_url_start_with VARCHAR(300)
);

CREATE TABLE role_application (
    role_id VARCHAR(64) NOT NULL,
    application_id VARCHAR(64) NOT NULL,
    FOREIGN KEY (role_id)
        REFERENCES role (role_id),
    FOREIGN KEY (application_id)
        REFERENCES application (application_id),
    PRIMARY KEY (role_id , application_id)
);

CREATE TABLE school (
    school_id INT AUTO_INCREMENT PRIMARY KEY,
    school_name VARCHAR(200) NOT NULL,
    school_is_del BOOLEAN
);

CREATE TABLE college (
    college_id INT AUTO_INCREMENT PRIMARY KEY,
    college_name VARCHAR(200) NOT NULL,
    college_address VARCHAR(500) NOT NULL,
    college_code VARCHAR(20) NOT NULL UNIQUE,
    college_is_del BOOLEAN,
    school_id INT NOT NULL,
    FOREIGN KEY (school_id)
        REFERENCES school (school_id)
);

CREATE TABLE college_application (
    application_id VARCHAR(64) NOT NULL,
    college_id INT NOT NULL,
    FOREIGN KEY (application_id)
        REFERENCES application (application_id),
    FOREIGN KEY (college_id)
        REFERENCES college (college_id),
    PRIMARY KEY (application_id , college_id)
);

CREATE TABLE college_role (
    role_id VARCHAR(64) NOT NULL,
    college_id INT NOT NULL,
    FOREIGN KEY (role_id)
        REFERENCES role (role_id),
    FOREIGN KEY (college_id)
        REFERENCES college (college_id),
    PRIMARY KEY (role_id , college_id)
);

CREATE TABLE department (
    department_id INT AUTO_INCREMENT PRIMARY KEY,
    department_name VARCHAR(200) NOT NULL,
    department_is_del BOOLEAN,
    college_id INT NOT NULL,
    FOREIGN KEY (college_id)
        REFERENCES college (college_id)
);

CREATE TABLE science (
    science_id INT AUTO_INCREMENT PRIMARY KEY,
    science_name VARCHAR(200) NOT NULL,
    science_code VARCHAR(20) NOT NULL UNIQUE,
    science_is_del BOOLEAN,
    department_id INT NOT NULL,
    FOREIGN KEY (department_id)
        REFERENCES department (department_id)
);

CREATE TABLE grade (
    grade_id INT AUTO_INCREMENT PRIMARY KEY,
    grade INT NOT NULL,
    grade_is_del BOOLEAN,
    science_id INT NOT NULL,
    FOREIGN KEY (science_id)
        REFERENCES science (science_id)
);

CREATE TABLE organize (
    organize_id INT AUTO_INCREMENT PRIMARY KEY,
    organize_name VARCHAR(200) NOT NULL,
    organize_is_del BOOLEAN,
    grade_id INT NOT NULL,
    staff_id INT,
    FOREIGN KEY (grade_id)
        REFERENCES grade (grade_id)
);

CREATE TABLE political_landscape (
    political_landscape_id INT AUTO_INCREMENT PRIMARY KEY,
    political_landscape_name VARCHAR(30) NOT NULL
);

CREATE TABLE nation (
    nation_id INT AUTO_INCREMENT PRIMARY KEY,
    nation_name VARCHAR(30) NOT NULL
);

CREATE TABLE academic_title (
    academic_title_id INT AUTO_INCREMENT PRIMARY KEY,
    academic_title_name VARCHAR(30) NOT NULL
);

CREATE TABLE student (
    student_id INT AUTO_INCREMENT PRIMARY KEY,
    student_number VARCHAR(20) UNIQUE NOT NULL,
    birthday DATE,
    sex VARCHAR(2),
    family_residence VARCHAR(200),
    political_landscape_id INT,
    nation_id INT,
    dormitory_number VARCHAR(15),
    parent_name VARCHAR(10),
    parent_contact_phone VARCHAR(15),
    place_origin VARCHAR(200),
    organize_id INT NOT NULL,
    username VARCHAR(64) NOT NULL,
    FOREIGN KEY (organize_id)
        REFERENCES organize (organize_id),
    FOREIGN KEY (username)
        REFERENCES users (username) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE staff (
    staff_id INT AUTO_INCREMENT PRIMARY KEY,
    staff_number VARCHAR(20) UNIQUE NOT NULL,
    birthday DATE,
    sex VARCHAR(2),
    family_residence VARCHAR(200),
    political_landscape_id INT,
    nation_id INT,
    post VARCHAR(50),
    academic_title_id INT,
    department_id INT NOT NULL,
    username VARCHAR(64) NOT NULL,
    FOREIGN KEY (department_id)
        REFERENCES department (department_id),
    FOREIGN KEY (username)
        REFERENCES users (username) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE system_configure(
    data_key VARCHAR(50) PRIMARY KEY ,
    data_value VARCHAR(100)
);

CREATE TABLE system_operator_log(
    log_id VARCHAR(64) PRIMARY KEY ,
    behavior VARCHAR(200) NOT NULL,
    operating_time DATETIME NOT NULL,
    username VARCHAR(200) NOT NULL,
    ip_address VARCHAR(50) NOT NULL
);

CREATE TABLE system_mailbox_log(
    log_id VARCHAR(64) PRIMARY KEY ,
    send_time DATETIME,
    accept_mail VARCHAR(200),
    send_condition VARCHAR(500)
);

CREATE TABLE system_sms_log(
    log_id VARCHAR(64) PRIMARY KEY ,
    send_time DATETIME,
    send_conent VARCHAR(500),
    accept_phone VARCHAR(15),
    send_condition VARCHAR(500)
);

CREATE TABLE building(
  building_id INT PRIMARY KEY,
  building_name VARCHAR(30) NOT NULL,
  building_is_del BOOLEAN,
  college_id INT NOT NULL,
  FOREIGN KEY(college_id) REFERENCES
  college(college_id)
);

CREATE TABLE schoolroom(
  schoolroom_id INT PRIMARY KEY,
  building_id INT NOT NULL,
  building_code VARCHAR(10) NOT NULL,
  schoolroom_is_del BOOLEAN,
  FOREIGN KEY(building_id) REFERENCES
  building(building_id)
);

CREATE TABLE course(
  course_id INT PRIMARY KEY,
  course_name VARCHAR(100) NOT NULL,
  course_credit DOUBLE,
  course_brief VARCHAR(500),
  course_is_del BOOLEAN,
  course_code VARCHAR(20) UNIQUE NOT NULL,
  school_year TINYINT NOT NULL,
  term TINYINT NOT NULL,
  college_id INT NOT NULL,
  FOREIGN KEY(college_id) REFERENCES
  college(college_id)
);

CREATE TABLE user_notify(
  user_notify_id VARCHAR(64)  PRIMARY KEY,
  notify_title VARCHAR(100) NOT NULL,
  notify_content VARCHAR(500) NOT NULL,
  create_date DATETIME NOT NULL,
  notify_type VARCHAR(10) NOT NULL DEFAULT 0,
  is_see BOOLEAN DEFAULT 0,
  send_user VARCHAR(64) NOT NULL,
  accept_user VARCHAR(64) NOT NULL,
  FOREIGN KEY(send_user) REFERENCES users(username) ON UPDATE CASCADE ON DELETE CASCADE,
  FOREIGN KEY(accept_user) REFERENCES users(username) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE system_notify(
  system_notify_id VARCHAR(64)  PRIMARY KEY,
  notify_title VARCHAR(100) NOT NULL,
  notify_content VARCHAR(500) NOT NULL,
  create_date DATETIME NOT NULL,
  valid_date DATETIME NOT NULL,
  expire_date DATETIME NOT NULL,
  notify_type VARCHAR(10) NOT NULL DEFAULT 0,
  send_user VARCHAR(64) NOT NULL,
  FOREIGN KEY(send_user) REFERENCES users(username) ON UPDATE CASCADE ON DELETE CASCADE
);

INSERT INTO files(file_id, file_size, content_type, original_file_name, new_name, relative_path, ext) VALUES (
'1000',4213,'image/jpg','avatar','avatar','images/avatar.jpg','jpg'
);
INSERT INTO users_type (users_type_name) VALUES ('系统');
INSERT INTO users_type (users_type_name) VALUES ('学生');
INSERT INTO users_type (users_type_name) VALUES ('教职工');

INSERT INTO users (username, password, enabled, account_non_expired, credentials_non_expired, account_non_locked,agree_protocol,
                   users_type_id, real_name,
                   email, mobile, id_card, avatar,
                   verify_mailbox, mailbox_verify_code,
                   password_reset_key, mailbox_verify_valid,
                   password_reset_key_valid, lang_key, join_date)
VALUES ('zbeboy', '$2a$10$HKXHRhnhlC1aZQ4hukD0S.zYep/T5A7FULBo7S2UrJsqQCThUxdo2', 1, 1, 1, 1, 1, 1, '赵银','dev@zbeboy.top', '13987614700',
                            '','1000', 1, '', '', NULL, NULL, 'zh-CN', '2016-08-18');

INSERT INTO users (username, password, enabled, account_non_expired, credentials_non_expired, account_non_locked,agree_protocol,
                   users_type_id, real_name,
                   email, mobile, id_card, avatar,
                   verify_mailbox, mailbox_verify_code,
                   password_reset_key, mailbox_verify_valid,
                   password_reset_key_valid, lang_key, join_date)
VALUES ('actuator', '$2a$10$HKXHRhnhlC1aZQ4hukD0S.zYep/T5A7FULBo7S2UrJsqQCThUxdo2', 1, 1, 1, 1, 1, 1, '运维','actuator@zbeboy.top', '13987614701',
                            '','1000', 1, '', '', NULL, NULL, 'zh-CN', '2016-08-18');

INSERT INTO role (role_id, role_name, role_en_name, role_type)
VALUES ('220f8f10263c4a38bc1f6b7c42759594', '系统', 'ROLE_SYSTEM', 1);
INSERT INTO role (role_id, role_name, role_en_name, role_type)
VALUES ('e813c71358fc4691afeafb438ea53919', '管理员', 'ROLE_ADMIN', 1);
INSERT INTO role (role_id, role_name, role_en_name, role_type)
VALUES ('cd5012abe87246f3ae21c86600c6e12a', '运维', 'ROLE_ACTUATOR', 1);

INSERT INTO authorities (username, authority) VALUES ('zbeboy', 'ROLE_SYSTEM');
INSERT INTO authorities (username, authority) VALUES ('zbeboy', 'ROLE_ACTUATOR');
INSERT INTO authorities (username, authority) VALUES ('actuator', 'ROLE_ACTUATOR');

INSERT INTO application (application_id, application_name, application_sort,
                         application_pid, application_url,
                         application_code, application_en_name, icon)
VALUES ('855e14c857094f1bb6df3938c2f86900', '实训', 1000, '0', '#', 'training', 'training', 'fa fa-train');
INSERT INTO application (application_id, application_name, application_sort,
                         application_pid, application_url,
                         application_code, application_en_name, icon)
VALUES ('b5939e89e8794c4e8b2d333a1386fb2a', '实习', 3000, '0', '#', 'internship', 'internship', 'fa fa-coffee');
INSERT INTO application (application_id, application_name, application_sort,
                         application_pid, application_url,
                         application_code, application_en_name, icon)
VALUES ('69fccdabaa5448c2aeaba56456004ac2', '数据', 5000, '0', '#', 'datas', 'datas', 'fa fa-database');
INSERT INTO application (application_id, application_name, application_sort,
                         application_pid, application_url,
                         application_code, application_en_name, icon)
VALUES ('0eb2165a08824c1cac232d975af392b3', '平台', 6000, '0', '#', 'platform', 'platform', 'fa fa-leaf');
INSERT INTO application (application_id, application_name, application_sort,
                         application_pid, application_url,
                         application_code, application_en_name, icon)
VALUES ('e3d45ba55e48462cb47595ce01bba60c', '系统', 7000, '0', '#', 'system', 'system', 'fa fa-sitemap');

INSERT INTO application (application_id, application_name, application_sort,
                         application_pid, application_url,
                         application_code, application_en_name, icon, application_data_url_start_with)
VALUES ('866d5648099242ce8bd400bfece025ee', '实训发布', 1010, '855e14c857094f1bb6df3938c2f86900', '/web/menu/training/release', 'training_release',
        'training_release', '', '/web/training/release');
INSERT INTO application (application_id, application_name, application_sort,
                         application_pid, application_url,
                         application_code, application_en_name, icon, application_data_url_start_with)
VALUES ('552f3507fd004202aa83f371e0059a96', '实训名单', 1015, '855e14c857094f1bb6df3938c2f86900', '/web/menu/training/users', 'training_users',
        'training_users', '', '/web/training/users');
INSERT INTO application (application_id, application_name, application_sort,
                         application_pid, application_url,
                         application_code, application_en_name, icon, application_data_url_start_with)
VALUES ('dd534f15c5174f13a9b4a07eb37f6cb7', '实训考勤', 1020, '855e14c857094f1bb6df3938c2f86900', '/web/menu/training/attend', 'training_attend',
        'training_attend', '', '/web/training/attend');
INSERT INTO application (application_id, application_name, application_sort,
                         application_pid, application_url,
                         application_code, application_en_name, icon, application_data_url_start_with)
VALUES ('c274e184988846a08218cc537ea67c4e', '实训实验室', 1025, '855e14c857094f1bb6df3938c2f86900', '/web/menu/training/labs', 'training_labs',
        'training_labs', '', '/web/training/labs');

INSERT INTO application (application_id, application_name, application_sort,
                         application_pid, application_url,
                         application_code, application_en_name, icon, application_data_url_start_with)
VALUES
  ('492825a2af45482b92f0aea71973deea', '实习发布', 3010, 'b5939e89e8794c4e8b2d333a1386fb2a', '/web/menu/internship/release',
   'internship_release',
   'internship_release', '',
   '/web/internship/release');
INSERT INTO application (application_id, application_name, application_sort,
                         application_pid, application_url,
                         application_code, application_en_name, icon, application_data_url_start_with)
VALUES ('9b671f7e11304beabb8a35c49d9e69e4', '实习教师分配', 3020, 'b5939e89e8794c4e8b2d333a1386fb2a',
        '/web/menu/internship/teacher_distribution',
        'internship_teacher_distribution',
        'internship_teacher_distribution', '', '/web/internship/teacher_distribution');
INSERT INTO application (application_id, application_name, application_sort,
                         application_pid, application_url,
                         application_code, application_en_name, icon, application_data_url_start_with)
VALUES
  ('afed0863997149e9aa1e38930afd93c0', '实习申请', 3030, 'b5939e89e8794c4e8b2d333a1386fb2a', '/web/menu/internship/apply',
   'internship_apply',
   'internship_apply', '', '/web/internship/apply');
INSERT INTO application (application_id, application_name, application_sort,
                         application_pid, application_url,
                         application_code, application_en_name, icon, application_data_url_start_with)
VALUES
  ('4ad4ebdabcf743a48f17e953201d50e7', '实习审核', 3040, 'b5939e89e8794c4e8b2d333a1386fb2a', '/web/menu/internship/review',
   'internship_review',
   'internship_review', '',
   '/web/internship/review');
INSERT INTO application (application_id, application_name, application_sort,
                         application_pid, application_url,
                         application_code, application_en_name, icon, application_data_url_start_with)
VALUES
  ('3d87f7d05f454f51ac407834aeed6cf3', '实习统计', 3050, 'b5939e89e8794c4e8b2d333a1386fb2a',
   '/web/menu/internship/statistical', 'internship_statistical',
   'internship_statistical', '',
   '/web/internship/statistical');
INSERT INTO application (application_id, application_name, application_sort,
                         application_pid, application_url,
                         application_code, application_en_name, icon, application_data_url_start_with)
VALUES
  ('eac4175a8a9b44a380629cbbebc69eb9', '实习日志', 3060, 'b5939e89e8794c4e8b2d333a1386fb2a', '/web/menu/internship/journal',
   'internship_journal',
   'internship_journal', '',
   '/web/internship/journal');
INSERT INTO application (application_id, application_name, application_sort,
                         application_pid, application_url,
                         application_code, application_en_name, icon, application_data_url_start_with)
VALUES
  ('762c6ba3323e4d739b104422d12f24d7', '实习监管', 3070, 'b5939e89e8794c4e8b2d333a1386fb2a', '/web/menu/internship/regulate',
   'internship_regulate',
   'internship_regulate', '',
   '/web/internship/regulate');

INSERT INTO application (application_id, application_name, application_sort,
                         application_pid, application_url,
                         application_code, application_en_name, icon, application_data_url_start_with)
VALUES ('d0c43d82367648578900829bc380d576', '学校数据', 5010, '69fccdabaa5448c2aeaba56456004ac2', '/web/menu/data/school',
        'data_school', 'data_school', '',
        '/web/data/school');
INSERT INTO application (application_id, application_name, application_sort,
                         application_pid, application_url,
                         application_code, application_en_name, icon, application_data_url_start_with)
VALUES ('d964e48c8d5747739ee78f16a0d5d34e', '院数据', 5020, '69fccdabaa5448c2aeaba56456004ac2', '/web/menu/data/college',
        'data_college', 'data_college', '',
        '/web/data/college');
INSERT INTO application (application_id, application_name, application_sort,
                         application_pid, application_url,
                         application_code, application_en_name, icon, application_data_url_start_with)
VALUES
  ('1f694733093949158714580f1bf1d0fa', '系数据', 5030, '69fccdabaa5448c2aeaba56456004ac2', '/web/menu/data/department',
   'data_department', 'data_department',
   '', '/web/data/department');
INSERT INTO application (application_id, application_name, application_sort,
                         application_pid, application_url,
                         application_code, application_en_name, icon, application_data_url_start_with)
VALUES
  ('0ed7aa64d18244d882ee9edfbc8bcb88', '专业数据', 5040, '69fccdabaa5448c2aeaba56456004ac2', '/web/menu/data/science',
   'data_science', 'data_science', '',
   '/web/data/science');
INSERT INTO application (application_id, application_name, application_sort,
                         application_pid, application_url,
                         application_code, application_en_name, icon, application_data_url_start_with)
VALUES
  ('bbbdbeb69a284a2589fc694d962d3636', '班级数据', 5050, '69fccdabaa5448c2aeaba56456004ac2', '/web/menu/data/organize',
   'data_organize', 'data_organize', '',
   '/web/data/organize');
INSERT INTO application (application_id, application_name, application_sort,
                         application_pid, application_url,
                         application_code, application_en_name, icon, application_data_url_start_with)
VALUES
  ('f5ad9804008f4b949ca9652fba155e38', '课程数据', 5060, '69fccdabaa5448c2aeaba56456004ac2', '/web/menu/data/course',
   'data_course', 'data_course', '',
   '/web/data/course');
INSERT INTO application (application_id, application_name, application_sort,
                         application_pid, application_url,
                         application_code, application_en_name, icon, application_data_url_start_with)
VALUES
  ('61e8ccfa0ed74ff8b6c7e50ba72725dc', '楼数据', 5070, '69fccdabaa5448c2aeaba56456004ac2', '/web/menu/data/building',
   'data_building', 'data_building', '',
   '/web/data/building');
INSERT INTO application (application_id, application_name, application_sort,
                         application_pid, application_url,
                         application_code, application_en_name, icon, application_data_url_start_with)
VALUES
  ('d82b367340db4428932ce28a7dd9bb7f', '教室数据', 5080, '69fccdabaa5448c2aeaba56456004ac2', '/web/menu/data/schoolroom',
   'data_schoolroom',
   'data_schoolroom', '', '/web/data/schoolroom');
INSERT INTO application (application_id, application_name, application_sort,
                         application_pid, application_url,
                         application_code, application_en_name, icon, application_data_url_start_with)
VALUES ('88a1e75eecbb4ab782642cfc0b246184', '教职工数据', 5090, '69fccdabaa5448c2aeaba56456004ac2', '/web/menu/data/staff',
        'data_staff', 'data_staff', '',
        '/web/data/staff');
INSERT INTO application (application_id, application_name, application_sort,
                         application_pid, application_url,
                         application_code, application_en_name, icon, application_data_url_start_with)
VALUES
  ('53dcc742fa484a7cbcd4841651c39efd', '学生数据', 5100, '69fccdabaa5448c2aeaba56456004ac2', '/web/menu/data/student',
   'data_student', 'data_student', '',
   '/web/data/student');
INSERT INTO application (application_id, application_name, application_sort,
                         application_pid, application_url,
                         application_code, application_en_name, icon, application_data_url_start_with)
VALUES ('17ca4892fe0744f0a1d0fa1db8af0703', '民族数据', 5110, '69fccdabaa5448c2aeaba56456004ac2', '/web/menu/data/nation',
        'data_nation', 'data_nation', '',
        '/web/data/nation');
INSERT INTO application (application_id, application_name, application_sort,
                         application_pid, application_url,
                         application_code, application_en_name, icon, application_data_url_start_with)
VALUES
  ('783f9fe0a92746ea8c8cda01e9f2f848', '政治面貌数据', 5120, '69fccdabaa5448c2aeaba56456004ac2', '/web/menu/data/politics',
   'data_politics', 'data_politics',
   '', '/web/data/politics');
INSERT INTO application (application_id, application_name, application_sort,
                         application_pid, application_url,
                         application_code, application_en_name, icon, application_data_url_start_with)
VALUES
  ('d58873b68598404fad86d808a28b1400', '职称数据', 5130, '69fccdabaa5448c2aeaba56456004ac2', '/web/menu/data/academic',
   'data_academic', 'data_academic', '',
   '/web/data/academic');
INSERT INTO application (application_id, application_name, application_sort,
                         application_pid, application_url,
                         application_code, application_en_name, icon, application_data_url_start_with)
VALUES
  ('0c2ace393cab4a8c909e0ac09e723e7f', 'Elastic同步', 5140, '69fccdabaa5448c2aeaba56456004ac2', '/web/menu/data/elastic',
   'data_elastic', 'data_elastic',
   '', '/web/data/elastic');

INSERT INTO application (application_id, application_name, application_sort,
                         application_pid, application_url,
                         application_code, application_en_name, icon, application_data_url_start_with)
VALUES
  ('17506dc86a904051a771bb22cd9c31dd', '平台用户', 6010, '0eb2165a08824c1cac232d975af392b3', '/web/menu/platform/users',
   'platform_users', 'platform_users',
   '', '/web/platform/users');
INSERT INTO application (application_id, application_name, application_sort,
                         application_pid, application_url,
                         application_code, application_en_name, icon, application_data_url_start_with)
VALUES
  ('800fd53d557449ee98b59d562c3ed013', '平台角色', 6020, '0eb2165a08824c1cac232d975af392b3', '/web/menu/platform/role',
   'platform_role', 'platform_role', '',
   '/web/platform/role');

INSERT INTO application (application_id, application_name, application_sort,
                         application_pid, application_url,
                         application_code, application_en_name, icon, application_data_url_start_with)
VALUES
  ('753e7add7a25452f949abb9b9a5519bb', '系统应用', 7010, 'e3d45ba55e48462cb47595ce01bba60c', '/web/menu/system/application',
   'system_application',
   'system_application', '',
   '/web/system/application');
INSERT INTO application (application_id, application_name, application_sort,
                         application_pid, application_url,
                         application_code, application_en_name, icon, application_data_url_start_with)
VALUES ('056b34f340544930b19716455a0ea3d2', '系统角色', 7020, 'e3d45ba55e48462cb47595ce01bba60c', '/web/menu/system/role',
        'system_role', 'system_role', '',
        '/web/system/role');
INSERT INTO application (application_id, application_name, application_sort,
                         application_pid, application_url,
                         application_code, application_en_name, icon, application_data_url_start_with)
VALUES ('13783647424340a0b5b716fe0c5d659d', '系统日志', 7030, 'e3d45ba55e48462cb47595ce01bba60c', '/web/menu/system/log',
        'system_log', 'system_log', '',
        '/web/system/log');
INSERT INTO application (application_id, application_name, application_sort,
                         application_pid, application_url,
                         application_code, application_en_name, icon, application_data_url_start_with)
VALUES ('c76085dd8803486c80545145bfd0b4d2', '系统短信', 7040, 'e3d45ba55e48462cb47595ce01bba60c', '/web/menu/system/sms',
        'system_sms', 'system_sms', '',
        '/web/system/sms');
INSERT INTO application (application_id, application_name, application_sort,
                         application_pid, application_url,
                         application_code, application_en_name, icon, application_data_url_start_with)
VALUES
  ('27af0835aaa64ed583b7abf0f26db20d', '系统邮件', 7050, 'e3d45ba55e48462cb47595ce01bba60c', '/web/menu/system/mailbox',
   'system_mailbox', 'system_mailbox',
   '', '/web/system/mailbox');
INSERT INTO application (application_id, application_name, application_sort,
                         application_pid, application_url,
                         application_code, application_en_name, icon, application_data_url_start_with)
VALUES
  ('82b1a7e5bd6c46a3a6a9957f63717d01', '系统状况', 7060, 'e3d45ba55e48462cb47595ce01bba60c', '/web/menu/system/health',
   'system_health', 'system_health',
   '', '/web/system/health');

INSERT INTO role_application (role_id, application_id)
VALUES ('220f8f10263c4a38bc1f6b7c42759594', 'b5939e89e8794c4e8b2d333a1386fb2a');
INSERT INTO role_application (role_id, application_id)
VALUES ('220f8f10263c4a38bc1f6b7c42759594', '69fccdabaa5448c2aeaba56456004ac2');
INSERT INTO role_application (role_id, application_id)
VALUES ('220f8f10263c4a38bc1f6b7c42759594', '0eb2165a08824c1cac232d975af392b3');
INSERT INTO role_application (role_id, application_id)
VALUES ('220f8f10263c4a38bc1f6b7c42759594', 'e3d45ba55e48462cb47595ce01bba60c');
INSERT INTO role_application (role_id, application_id)
VALUES ('220f8f10263c4a38bc1f6b7c42759594', '492825a2af45482b92f0aea71973deea');
INSERT INTO role_application (role_id, application_id)
VALUES ('220f8f10263c4a38bc1f6b7c42759594', '9b671f7e11304beabb8a35c49d9e69e4');
INSERT INTO role_application (role_id, application_id)
VALUES ('220f8f10263c4a38bc1f6b7c42759594', 'afed0863997149e9aa1e38930afd93c0');
INSERT INTO role_application (role_id, application_id)
VALUES ('220f8f10263c4a38bc1f6b7c42759594', '4ad4ebdabcf743a48f17e953201d50e7');
INSERT INTO role_application (role_id, application_id)
VALUES ('220f8f10263c4a38bc1f6b7c42759594', '3d87f7d05f454f51ac407834aeed6cf3');
INSERT INTO role_application (role_id, application_id)
VALUES ('220f8f10263c4a38bc1f6b7c42759594', 'eac4175a8a9b44a380629cbbebc69eb9');
INSERT INTO role_application (role_id, application_id)
VALUES ('220f8f10263c4a38bc1f6b7c42759594', '762c6ba3323e4d739b104422d12f24d7');
INSERT INTO role_application (role_id, application_id)
VALUES ('220f8f10263c4a38bc1f6b7c42759594', 'd0c43d82367648578900829bc380d576');
INSERT INTO role_application (role_id, application_id)
VALUES ('220f8f10263c4a38bc1f6b7c42759594', 'd964e48c8d5747739ee78f16a0d5d34e');
INSERT INTO role_application (role_id, application_id)
VALUES ('220f8f10263c4a38bc1f6b7c42759594', '1f694733093949158714580f1bf1d0fa');
INSERT INTO role_application (role_id, application_id)
VALUES ('220f8f10263c4a38bc1f6b7c42759594', '0ed7aa64d18244d882ee9edfbc8bcb88');
INSERT INTO role_application (role_id, application_id)
VALUES ('220f8f10263c4a38bc1f6b7c42759594', 'bbbdbeb69a284a2589fc694d962d3636');
INSERT INTO role_application (role_id, application_id)
VALUES ('220f8f10263c4a38bc1f6b7c42759594', '61e8ccfa0ed74ff8b6c7e50ba72725dc');
INSERT INTO role_application (role_id, application_id)
VALUES ('220f8f10263c4a38bc1f6b7c42759594', 'd82b367340db4428932ce28a7dd9bb7f');
INSERT INTO role_application (role_id, application_id)
VALUES ('220f8f10263c4a38bc1f6b7c42759594', '88a1e75eecbb4ab782642cfc0b246184');
INSERT INTO role_application (role_id, application_id)
VALUES ('220f8f10263c4a38bc1f6b7c42759594', '53dcc742fa484a7cbcd4841651c39efd');
INSERT INTO role_application (role_id, application_id)
VALUES ('220f8f10263c4a38bc1f6b7c42759594', '17ca4892fe0744f0a1d0fa1db8af0703');
INSERT INTO role_application (role_id, application_id)
VALUES ('220f8f10263c4a38bc1f6b7c42759594', '783f9fe0a92746ea8c8cda01e9f2f848');
INSERT INTO role_application (role_id, application_id)
VALUES ('220f8f10263c4a38bc1f6b7c42759594', 'd58873b68598404fad86d808a28b1400');
INSERT INTO role_application (role_id, application_id)
VALUES ('220f8f10263c4a38bc1f6b7c42759594', '0c2ace393cab4a8c909e0ac09e723e7f');
INSERT INTO role_application (role_id, application_id)
VALUES ('220f8f10263c4a38bc1f6b7c42759594', '17506dc86a904051a771bb22cd9c31dd');
INSERT INTO role_application (role_id, application_id)
VALUES ('220f8f10263c4a38bc1f6b7c42759594', '800fd53d557449ee98b59d562c3ed013');
INSERT INTO role_application (role_id, application_id)
VALUES ('220f8f10263c4a38bc1f6b7c42759594', '753e7add7a25452f949abb9b9a5519bb');
INSERT INTO role_application (role_id, application_id)
VALUES ('220f8f10263c4a38bc1f6b7c42759594', '056b34f340544930b19716455a0ea3d2');
INSERT INTO role_application (role_id, application_id)
VALUES ('220f8f10263c4a38bc1f6b7c42759594', '13783647424340a0b5b716fe0c5d659d');
INSERT INTO role_application (role_id, application_id)
VALUES ('220f8f10263c4a38bc1f6b7c42759594', 'c76085dd8803486c80545145bfd0b4d2');
INSERT INTO role_application (role_id, application_id)
VALUES ('220f8f10263c4a38bc1f6b7c42759594', '27af0835aaa64ed583b7abf0f26db20d');
INSERT INTO role_application (role_id, application_id)
VALUES ('220f8f10263c4a38bc1f6b7c42759594', '855e14c857094f1bb6df3938c2f86900');
INSERT INTO role_application (role_id, application_id)
VALUES ('220f8f10263c4a38bc1f6b7c42759594', 'f5ad9804008f4b949ca9652fba155e38');
INSERT INTO role_application (role_id, application_id)
VALUES ('220f8f10263c4a38bc1f6b7c42759594', '866d5648099242ce8bd400bfece025ee');
INSERT INTO role_application (role_id, application_id)
VALUES ('220f8f10263c4a38bc1f6b7c42759594', '552f3507fd004202aa83f371e0059a96');
INSERT INTO role_application (role_id, application_id)
VALUES ('220f8f10263c4a38bc1f6b7c42759594', 'dd534f15c5174f13a9b4a07eb37f6cb7');
INSERT INTO role_application (role_id, application_id)
VALUES ('220f8f10263c4a38bc1f6b7c42759594', 'c274e184988846a08218cc537ea67c4e');

INSERT INTO role_application (role_id, application_id)
VALUES ('e813c71358fc4691afeafb438ea53919', 'b5939e89e8794c4e8b2d333a1386fb2a');
INSERT INTO role_application (role_id, application_id)
VALUES ('e813c71358fc4691afeafb438ea53919', '69fccdabaa5448c2aeaba56456004ac2');
INSERT INTO role_application (role_id, application_id)
VALUES ('e813c71358fc4691afeafb438ea53919', '0eb2165a08824c1cac232d975af392b3');
INSERT INTO role_application (role_id, application_id)
VALUES ('e813c71358fc4691afeafb438ea53919', '492825a2af45482b92f0aea71973deea');
INSERT INTO role_application (role_id, application_id)
VALUES ('e813c71358fc4691afeafb438ea53919', '9b671f7e11304beabb8a35c49d9e69e4');
INSERT INTO role_application (role_id, application_id)
VALUES ('e813c71358fc4691afeafb438ea53919', 'afed0863997149e9aa1e38930afd93c0');
INSERT INTO role_application (role_id, application_id)
VALUES ('e813c71358fc4691afeafb438ea53919', '4ad4ebdabcf743a48f17e953201d50e7');
INSERT INTO role_application (role_id, application_id)
VALUES ('e813c71358fc4691afeafb438ea53919', '3d87f7d05f454f51ac407834aeed6cf3');
INSERT INTO role_application (role_id, application_id)
VALUES ('e813c71358fc4691afeafb438ea53919', 'eac4175a8a9b44a380629cbbebc69eb9');
INSERT INTO role_application (role_id, application_id)
VALUES ('e813c71358fc4691afeafb438ea53919', '762c6ba3323e4d739b104422d12f24d7');
INSERT INTO role_application (role_id, application_id)
VALUES ('e813c71358fc4691afeafb438ea53919', '1f694733093949158714580f1bf1d0fa');
INSERT INTO role_application (role_id, application_id)
VALUES ('e813c71358fc4691afeafb438ea53919', '0ed7aa64d18244d882ee9edfbc8bcb88');
INSERT INTO role_application (role_id, application_id)
VALUES ('e813c71358fc4691afeafb438ea53919', 'bbbdbeb69a284a2589fc694d962d3636');
INSERT INTO role_application (role_id, application_id)
VALUES ('e813c71358fc4691afeafb438ea53919', '61e8ccfa0ed74ff8b6c7e50ba72725dc');
INSERT INTO role_application (role_id, application_id)
VALUES ('e813c71358fc4691afeafb438ea53919', 'd82b367340db4428932ce28a7dd9bb7f');
INSERT INTO role_application (role_id, application_id)
VALUES ('e813c71358fc4691afeafb438ea53919', '88a1e75eecbb4ab782642cfc0b246184');
INSERT INTO role_application (role_id, application_id)
VALUES ('e813c71358fc4691afeafb438ea53919', '53dcc742fa484a7cbcd4841651c39efd');
INSERT INTO role_application (role_id, application_id)
VALUES ('e813c71358fc4691afeafb438ea53919', '17ca4892fe0744f0a1d0fa1db8af0703');
INSERT INTO role_application (role_id, application_id)
VALUES ('e813c71358fc4691afeafb438ea53919', '783f9fe0a92746ea8c8cda01e9f2f848');
INSERT INTO role_application (role_id, application_id)
VALUES ('e813c71358fc4691afeafb438ea53919', 'd58873b68598404fad86d808a28b1400');
INSERT INTO role_application (role_id, application_id)
VALUES ('e813c71358fc4691afeafb438ea53919', '800fd53d557449ee98b59d562c3ed013');

INSERT INTO role_application (role_id, application_id)
VALUES ('cd5012abe87246f3ae21c86600c6e12a', 'e3d45ba55e48462cb47595ce01bba60c');
INSERT INTO role_application (role_id, application_id)
VALUES ('cd5012abe87246f3ae21c86600c6e12a', '82b1a7e5bd6c46a3a6a9957f63717d01');

INSERT INTO political_landscape (political_landscape_name) VALUES ('群众');
INSERT INTO political_landscape (political_landscape_name) VALUES ('共青团员');
INSERT INTO political_landscape (political_landscape_name) VALUES ('中共预备党员');
INSERT INTO political_landscape (political_landscape_name) VALUES ('中共党员');
INSERT INTO political_landscape (political_landscape_name) VALUES ('民革党员');
INSERT INTO political_landscape (political_landscape_name) VALUES ('民盟盟员');
INSERT INTO political_landscape (political_landscape_name) VALUES ('民建会员');
INSERT INTO political_landscape (political_landscape_name) VALUES ('民进会员');
INSERT INTO political_landscape (political_landscape_name) VALUES ('农工党党员');
INSERT INTO political_landscape (political_landscape_name) VALUES ('致公党党员');
INSERT INTO political_landscape (political_landscape_name) VALUES ('九三学社社员');
INSERT INTO political_landscape (political_landscape_name) VALUES ('台盟盟员');
INSERT INTO political_landscape (political_landscape_name) VALUES ('无党派民主人士');

INSERT INTO nation (nation_name) VALUES ('汉族');
INSERT INTO nation (nation_name) VALUES ('蒙古族');
INSERT INTO nation (nation_name) VALUES ('回族');
INSERT INTO nation (nation_name) VALUES ('藏族');
INSERT INTO nation (nation_name) VALUES ('维吾尔族');
INSERT INTO nation (nation_name) VALUES ('苗族');
INSERT INTO nation (nation_name) VALUES ('彝族');
INSERT INTO nation (nation_name) VALUES ('壮族');
INSERT INTO nation (nation_name) VALUES ('布依族');
INSERT INTO nation (nation_name) VALUES ('朝鲜族');
INSERT INTO nation (nation_name) VALUES ('满族');
INSERT INTO nation (nation_name) VALUES ('侗族');
INSERT INTO nation (nation_name) VALUES ('瑶族');
INSERT INTO nation (nation_name) VALUES ('白族');
INSERT INTO nation (nation_name) VALUES ('土家族');
INSERT INTO nation (nation_name) VALUES ('哈尼族');
INSERT INTO nation (nation_name) VALUES ('哈萨克族');
INSERT INTO nation (nation_name) VALUES ('傣族');
INSERT INTO nation (nation_name) VALUES ('黎族');
INSERT INTO nation (nation_name) VALUES ('傈僳族');
INSERT INTO nation (nation_name) VALUES ('佤族');
INSERT INTO nation (nation_name) VALUES ('畲族');
INSERT INTO nation (nation_name) VALUES ('高山族');
INSERT INTO nation (nation_name) VALUES ('拉祜族');
INSERT INTO nation (nation_name) VALUES ('水族');
INSERT INTO nation (nation_name) VALUES ('东乡族');
INSERT INTO nation (nation_name) VALUES ('纳西族');
INSERT INTO nation (nation_name) VALUES ('景颇族');
INSERT INTO nation (nation_name) VALUES ('柯尔克孜族');
INSERT INTO nation (nation_name) VALUES ('土族');
INSERT INTO nation (nation_name) VALUES ('达斡尔族');
INSERT INTO nation (nation_name) VALUES ('仫佬族');
INSERT INTO nation (nation_name) VALUES ('羌族');
INSERT INTO nation (nation_name) VALUES ('布朗族');
INSERT INTO nation (nation_name) VALUES ('撒拉族');
INSERT INTO nation (nation_name) VALUES ('毛难族');
INSERT INTO nation (nation_name) VALUES ('仡佬族');
INSERT INTO nation (nation_name) VALUES ('锡伯族');
INSERT INTO nation (nation_name) VALUES ('阿昌族');
INSERT INTO nation (nation_name) VALUES ('普米族');
INSERT INTO nation (nation_name) VALUES ('塔吉克族');
INSERT INTO nation (nation_name) VALUES ('怒族');
INSERT INTO nation (nation_name) VALUES ('乌孜别克族');
INSERT INTO nation (nation_name) VALUES ('俄罗斯族');
INSERT INTO nation (nation_name) VALUES ('鄂温克族');
INSERT INTO nation (nation_name) VALUES ('崩龙族');
INSERT INTO nation (nation_name) VALUES ('保安族');
INSERT INTO nation (nation_name) VALUES ('裕固族');
INSERT INTO nation (nation_name) VALUES ('京族');
INSERT INTO nation (nation_name) VALUES ('塔塔尔族');
INSERT INTO nation (nation_name) VALUES ('独龙族');
INSERT INTO nation (nation_name) VALUES ('鄂伦春族');
INSERT INTO nation (nation_name) VALUES ('赫哲族');
INSERT INTO nation (nation_name) VALUES ('门巴族');
INSERT INTO nation (nation_name) VALUES ('珞巴族');
INSERT INTO nation (nation_name) VALUES ('基诺族');

INSERT INTO academic_title (academic_title_name) VALUES ('讲师');
INSERT INTO academic_title (academic_title_name) VALUES ('副教授');
INSERT INTO academic_title (academic_title_name) VALUES ('教授');
INSERT INTO academic_title (academic_title_name) VALUES ('助教');
INSERT INTO academic_title (academic_title_name) VALUES ('工程师');
INSERT INTO academic_title (academic_title_name) VALUES ('高级工程师');
INSERT INTO academic_title (academic_title_name) VALUES ('教授级高级工程师');
INSERT INTO academic_title (academic_title_name) VALUES ('助理工程师');
INSERT INTO academic_title (academic_title_name) VALUES ('实验师');
INSERT INTO academic_title (academic_title_name) VALUES ('助理实验师');
INSERT INTO academic_title (academic_title_name) VALUES ('高级实验师');
INSERT INTO academic_title (academic_title_name) VALUES ('副研究员');
INSERT INTO academic_title (academic_title_name) VALUES ('研究员');
INSERT INTO academic_title (academic_title_name) VALUES ('助理研究员');

INSERT INTO system_configure (data_key, data_value) VALUES ('MAIL_SWITCH','1');
INSERT INTO system_configure (data_key, data_value) VALUES ('MOBILE_SWITCH','1');
INSERT INTO system_configure (data_key, data_value) VALUES ('FORBIDDEN_REGISTER','ISY,ZBEBOY');
INSERT INTO system_configure (data_key, data_value) VALUES ('STATIC_RESOURCES_VERSION','0.01');
