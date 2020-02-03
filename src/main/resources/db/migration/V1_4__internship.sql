CREATE TABLE internship_type (
  internship_type_id   INT AUTO_INCREMENT PRIMARY KEY,
  internship_type_name VARCHAR(100) NOT NULL
);

CREATE TABLE internship_release (
  internship_release_id           VARCHAR(64) PRIMARY KEY,
  internship_title                VARCHAR(100) NOT NULL,
  release_time                    TIMESTAMP    NOT NULL,
  username                        VARCHAR(64)  NOT NULL,
  teacher_distribution_start_time DATETIME     NOT NULL,
  teacher_distribution_end_time   DATETIME     NOT NULL,
  start_time                      DATETIME     NOT NULL,
  end_time                        DATETIME     NOT NULL,
  internship_release_is_del       BOOLEAN      NOT NULL,
  internship_type_id              INT          NOT NULL,
  publisher                       VARCHAR(30)  NOT NULL,
  science_id            INT         NOT NULL,
  FOREIGN KEY (username) REFERENCES users (username) ON UPDATE CASCADE ON DELETE CASCADE,
  FOREIGN KEY (science_id) REFERENCES science (science_id),
  FOREIGN KEY (internship_type_id) REFERENCES internship_type (internship_type_id)
);

CREATE TABLE internship_file (
  internship_release_id VARCHAR(64) NOT NULL,
  file_id               VARCHAR(64) NOT NULL,
  FOREIGN KEY (internship_release_id) REFERENCES internship_release (internship_release_id),
  FOREIGN KEY (file_id) REFERENCES files (file_id),
  PRIMARY KEY (internship_release_id, file_id)
);

CREATE TABLE internship_teacher_distribution (
  staff_id              INT          NOT NULL,
  student_id            INT          NOT NULL,
  internship_release_id VARCHAR(64)  NOT NULL,
  username              VARCHAR(200) NOT NULL,
  student_real_name     VARCHAR(30)  NOT NULL,
  assigner              VARCHAR(30)  NOT NULL,
  FOREIGN KEY (staff_id) REFERENCES staff (staff_id) ON DELETE CASCADE,
  FOREIGN KEY (student_id) REFERENCES student (student_id) ON DELETE CASCADE,
  FOREIGN KEY (internship_release_id) REFERENCES internship_release (internship_release_id),
  FOREIGN KEY (username) REFERENCES users (username) ON UPDATE CASCADE ON DELETE CASCADE,
  PRIMARY KEY (staff_id, student_id, internship_release_id)
);

CREATE TABLE internship_apply (
  internship_apply_id    VARCHAR(64) PRIMARY KEY,
  student_id             INT         NOT NULL,
  internship_release_id  VARCHAR(64) NOT NULL,
  internship_apply_state INT         NOT NULL DEFAULT 0,
  reason                 VARCHAR(500),
  change_fill_start_time DATETIME,
  change_fill_end_time   DATETIME,
  apply_time             DATETIME    NOT NULL,
  file_id     VARCHAR(64),
  FOREIGN KEY (student_id) REFERENCES student (student_id) ON DELETE CASCADE,
  FOREIGN KEY (internship_release_id) REFERENCES internship_release (internship_release_id),
  UNIQUE (student_id, internship_release_id)
);

CREATE TABLE internship_change_history (
  internship_change_history_id VARCHAR(64) PRIMARY KEY,
  reason                       VARCHAR(500),
  change_fill_start_time       DATETIME,
  change_fill_end_time         DATETIME,
  student_id                   INT         NOT NULL,
  internship_release_id        VARCHAR(64) NOT NULL,
  apply_time                   DATETIME    NOT NULL,
  state                        INT         NOT NULL,
  FOREIGN KEY (student_id) REFERENCES student (student_id) ON DELETE CASCADE,
  FOREIGN KEY (internship_release_id) REFERENCES internship_release (internship_release_id)
);

CREATE TABLE internship_change_company_history (
  internship_change_company_history_id VARCHAR(64) PRIMARY KEY,
  student_id                           INT         NOT NULL,
  internship_release_id                VARCHAR(64) NOT NULL,
  company_name                         VARCHAR(200),
  company_address                      VARCHAR(500),
  company_contacts                     VARCHAR(10),
  company_tel                          VARCHAR(20),
  change_time                          DATETIME(3) NOT NULL,
  FOREIGN KEY (student_id) REFERENCES student (student_id) ON DELETE CASCADE,
  FOREIGN KEY (internship_release_id) REFERENCES internship_release (internship_release_id)
);

CREATE TABLE internship_college (
  internship_college_id        VARCHAR(64) PRIMARY KEY,
  student_id                   INT          NOT NULL,
  student_username             VARCHAR(64)  NOT NULL,
  internship_release_id        VARCHAR(64)  NOT NULL,
  student_name                 VARCHAR(15)  NOT NULL,
  college_class                VARCHAR(50)  NOT NULL,
  student_sex                  VARCHAR(24)   NOT NULL,
  student_number               VARCHAR(20)  NOT NULL,
  phone_number                 VARCHAR(15)  NOT NULL,
  qq_mailbox                   VARCHAR(100) NOT NULL,
  parental_contact             VARCHAR(48)  NOT NULL,
  headmaster                   VARCHAR(10)  NOT NULL,
  headmaster_contact           VARCHAR(20)  NOT NULL,
  internship_college_name      VARCHAR(200) NOT NULL,
  internship_college_address   VARCHAR(500) NOT NULL,
  internship_college_contacts  VARCHAR(10)  NOT NULL,
  internship_college_tel       VARCHAR(20)  NOT NULL,
  school_guidance_teacher      VARCHAR(10)  NOT NULL,
  school_guidance_teacher_tel  VARCHAR(20)  NOT NULL,
  start_time                   DATE         NOT NULL,
  end_time                     DATE         NOT NULL,
  commitment_book              BOOLEAN,
  safety_responsibility_book   BOOLEAN,
  practice_agreement           BOOLEAN,
  internship_application       BOOLEAN,
  practice_receiving           BOOLEAN,
  security_education_agreement BOOLEAN,
  parental_consent             BOOLEAN,
  FOREIGN KEY (student_id) REFERENCES student (student_id) ON DELETE CASCADE,
  FOREIGN KEY (student_username) REFERENCES users (username) ON UPDATE CASCADE ON DELETE CASCADE,
  FOREIGN KEY (internship_release_id) REFERENCES internship_release (internship_release_id),
  UNIQUE (student_id, internship_release_id)
);

CREATE TABLE internship_company (
  internship_company_id        VARCHAR(64) PRIMARY KEY,
  student_name                 VARCHAR(15)  NOT NULL,
  college_class                VARCHAR(50)  NOT NULL,
  student_sex                  VARCHAR(20)   NOT NULL,
  student_number               VARCHAR(20)  NOT NULL,
  phone_number                 VARCHAR(15)  NOT NULL,
  qq_mailbox                   VARCHAR(100) NOT NULL,
  parental_contact             VARCHAR(48)  NOT NULL,
  headmaster                   VARCHAR(10)  NOT NULL,
  headmaster_contact           VARCHAR(20)  NOT NULL,
  internship_company_name      VARCHAR(200) NOT NULL,
  internship_company_address   VARCHAR(500) NOT NULL,
  internship_company_contacts  VARCHAR(10)  NOT NULL,
  internship_company_tel       VARCHAR(20)  NOT NULL,
  school_guidance_teacher      VARCHAR(10)  NOT NULL,
  school_guidance_teacher_tel  VARCHAR(20)  NOT NULL,
  start_time                   DATE         NOT NULL,
  end_time                     DATE         NOT NULL,
  commitment_book              BOOLEAN,
  safety_responsibility_book   BOOLEAN,
  practice_agreement           BOOLEAN,
  internship_application       BOOLEAN,
  practice_receiving           BOOLEAN,
  security_education_agreement BOOLEAN,
  parental_consent             BOOLEAN,
  student_id                   INT          NOT NULL,
  student_username             VARCHAR(64)  NOT NULL,
  internship_release_id        VARCHAR(64)  NOT NULL,
  FOREIGN KEY (student_id) REFERENCES student (student_id) ON DELETE CASCADE,
  FOREIGN KEY (student_username) REFERENCES users (username) ON UPDATE CASCADE ON DELETE CASCADE,
  FOREIGN KEY (internship_release_id) REFERENCES internship_release (internship_release_id),
  UNIQUE (student_id, internship_release_id)
);

CREATE TABLE graduation_practice_college (
  graduation_practice_college_id       VARCHAR(64) PRIMARY KEY,
  student_name                         VARCHAR(15)  NOT NULL,
  college_class                        VARCHAR(50)  NOT NULL,
  student_sex                          VARCHAR(20)   NOT NULL,
  student_number                       VARCHAR(20)  NOT NULL,
  phone_number                         VARCHAR(15)  NOT NULL,
  qq_mailbox                           VARCHAR(100) NOT NULL,
  parental_contact                     VARCHAR(48)  NOT NULL,
  headmaster                           VARCHAR(10)  NOT NULL,
  headmaster_contact                   VARCHAR(20)  NOT NULL,
  graduation_practice_college_name     VARCHAR(200) NOT NULL,
  graduation_practice_college_address  VARCHAR(500) NOT NULL,
  graduation_practice_college_contacts VARCHAR(10)  NOT NULL,
  graduation_practice_college_tel      VARCHAR(20)  NOT NULL,
  school_guidance_teacher              VARCHAR(10)  NOT NULL,
  school_guidance_teacher_tel          VARCHAR(20)  NOT NULL,
  start_time                           DATE         NOT NULL,
  end_time                             DATE         NOT NULL,
  commitment_book                      BOOLEAN,
  safety_responsibility_book           BOOLEAN,
  practice_agreement                   BOOLEAN,
  internship_application               BOOLEAN,
  practice_receiving                   BOOLEAN,
  security_education_agreement         BOOLEAN,
  parental_consent                     BOOLEAN,
  student_id                           INT          NOT NULL,
  student_username                     VARCHAR(64)  NOT NULL,
  internship_release_id                VARCHAR(64)  NOT NULL,
  FOREIGN KEY (student_id) REFERENCES student (student_id) ON DELETE CASCADE,
  FOREIGN KEY (student_username) REFERENCES users (username) ON UPDATE CASCADE ON DELETE CASCADE,
  FOREIGN KEY (internship_release_id) REFERENCES internship_release (internship_release_id),
  UNIQUE (student_id, internship_release_id)
);

CREATE TABLE graduation_practice_unify (
  graduation_practice_unify_id       VARCHAR(64) PRIMARY KEY,
  student_name                       VARCHAR(15)  NOT NULL,
  college_class                      VARCHAR(50)  NOT NULL,
  student_sex                        VARCHAR(20)   NOT NULL,
  student_number                     VARCHAR(20)  NOT NULL,
  phone_number                       VARCHAR(15)  NOT NULL,
  qq_mailbox                         VARCHAR(100) NOT NULL,
  parental_contact                   VARCHAR(48)  NOT NULL,
  headmaster                         VARCHAR(10)  NOT NULL,
  headmaster_contact                 VARCHAR(20)  NOT NULL,
  graduation_practice_unify_name     VARCHAR(200) NOT NULL,
  graduation_practice_unify_address  VARCHAR(500) NOT NULL,
  graduation_practice_unify_contacts VARCHAR(10)  NOT NULL,
  graduation_practice_unify_tel      VARCHAR(20)  NOT NULL,
  school_guidance_teacher            VARCHAR(10)  NOT NULL,
  school_guidance_teacher_tel        VARCHAR(20)  NOT NULL,
  start_time                         DATE         NOT NULL,
  end_time                           DATE         NOT NULL,
  commitment_book                    BOOLEAN,
  safety_responsibility_book         BOOLEAN,
  practice_agreement                 BOOLEAN,
  internship_application             BOOLEAN,
  practice_receiving                 BOOLEAN,
  security_education_agreement       BOOLEAN,
  parental_consent                   BOOLEAN,
  student_id                         INT          NOT NULL,
  student_username                   VARCHAR(64)  NOT NULL,
  internship_release_id              VARCHAR(64)  NOT NULL,
  FOREIGN KEY (student_id) REFERENCES student (student_id) ON DELETE CASCADE,
  FOREIGN KEY (student_username) REFERENCES users (username) ON UPDATE CASCADE ON DELETE CASCADE,
  FOREIGN KEY (internship_release_id) REFERENCES internship_release (internship_release_id),
  UNIQUE (student_id, internship_release_id)
);

CREATE TABLE graduation_practice_company (
  graduation_practice_company_id       VARCHAR(64) PRIMARY KEY,
  student_name                         VARCHAR(15)  NOT NULL,
  college_class                        VARCHAR(50)  NOT NULL,
  student_sex                          VARCHAR(20)   NOT NULL,
  student_number                       VARCHAR(20)  NOT NULL,
  phone_number                         VARCHAR(15)  NOT NULL,
  qq_mailbox                           VARCHAR(100) NOT NULL,
  parental_contact                     VARCHAR(48)  NOT NULL,
  headmaster                           VARCHAR(10)  NOT NULL,
  headmaster_contact                   VARCHAR(20)  NOT NULL,
  graduation_practice_company_name     VARCHAR(200) NOT NULL,
  graduation_practice_company_address  VARCHAR(500) NOT NULL,
  graduation_practice_company_contacts VARCHAR(10)  NOT NULL,
  graduation_practice_company_tel      VARCHAR(20)  NOT NULL,
  school_guidance_teacher              VARCHAR(10)  NOT NULL,
  school_guidance_teacher_tel          VARCHAR(20)  NOT NULL,
  start_time                           DATE         NOT NULL,
  end_time                             DATE         NOT NULL,
  commitment_book                      BOOLEAN,
  safety_responsibility_book           BOOLEAN,
  practice_agreement                   BOOLEAN,
  internship_application               BOOLEAN,
  practice_receiving                   BOOLEAN,
  security_education_agreement         BOOLEAN,
  parental_consent                     BOOLEAN,
  student_id                           INT          NOT NULL,
  student_username                     VARCHAR(64)  NOT NULL,
  internship_release_id                VARCHAR(64)  NOT NULL,
  FOREIGN KEY (student_id) REFERENCES student (student_id) ON DELETE CASCADE,
  FOREIGN KEY (student_username) REFERENCES users (username) ON UPDATE CASCADE ON DELETE CASCADE,
  FOREIGN KEY (internship_release_id) REFERENCES internship_release (internship_release_id),
  UNIQUE (student_id, internship_release_id)
);

CREATE TABLE internship_journal (
  internship_journal_id            VARCHAR(64) PRIMARY KEY,
  student_name                     VARCHAR(30)  NOT NULL,
  student_number                   VARCHAR(20)  NOT NULL,
  organize                         VARCHAR(200) NOT NULL,
  school_guidance_teacher          VARCHAR(30)  NOT NULL,
  graduation_practice_company_name VARCHAR(200) NOT NULL,
  create_date                      DATETIME     NOT NULL,
  student_id                       INT          NOT NULL,
  internship_release_id            VARCHAR(64)  NOT NULL,
  staff_id                         INT          NOT NULL,
  internship_journal_word          VARCHAR(500) NOT NULL,
  is_see_staff                     BOOLEAN      NOT NULL DEFAULT 0,
  FOREIGN KEY (student_id) REFERENCES student (student_id) ON DELETE CASCADE,
  FOREIGN KEY (internship_release_id) REFERENCES internship_release (internship_release_id),
  FOREIGN KEY (staff_id) REFERENCES staff (staff_id) ON DELETE CASCADE
);

CREATE TABLE internship_journal_content (
  internship_journal_id            VARCHAR(64) UNIQUE,
  internship_journal_content       LONGTEXT         NOT NULL,
  internship_journal_html          LONGTEXT         NOT NULL,
  internship_journal_date          DATE         NOT NULL,
  FOREIGN KEY (internship_journal_id) REFERENCES internship_journal (internship_journal_id)
);

CREATE TABLE internship_regulate (
  internship_regulate_id  VARCHAR(64) PRIMARY KEY,
  student_name            VARCHAR(30)  NOT NULL,
  student_number          VARCHAR(20)  NOT NULL,
  student_tel             VARCHAR(15)  NOT NULL,
  internship_content      VARCHAR(200) NOT NULL,
  internship_progress     VARCHAR(200) NOT NULL,
  report_way              VARCHAR(20)  NOT NULL,
  report_date             DATE         NOT NULL,
  school_guidance_teacher VARCHAR(30)  NOT NULL,
  tliy                    VARCHAR(200),
  create_date             DATETIME     NOT NULL,
  student_id              INT          NOT NULL,
  internship_release_id   VARCHAR(64)  NOT NULL,
  staff_id                INT          NOT NULL,
  FOREIGN KEY (student_id) REFERENCES student (student_id) ON DELETE CASCADE,
  FOREIGN KEY (internship_release_id) REFERENCES internship_release (internship_release_id),
  FOREIGN KEY (staff_id) REFERENCES staff (staff_id) ON DELETE CASCADE
);

INSERT INTO internship_type (internship_type_name) VALUES ('顶岗实习(留学院)');
INSERT INTO internship_type (internship_type_name) VALUES ('校外自主实习(去单位)');
INSERT INTO internship_type (internship_type_name) VALUES ('毕业实习(校内)');
INSERT INTO internship_type (internship_type_name) VALUES ('毕业实习(学校统一组织校外实习)');
INSERT INTO internship_type (internship_type_name) VALUES ('毕业实习(学生校外自主实习)');