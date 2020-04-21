CREATE TABLE training_release
(
    training_release_id VARCHAR(64) PRIMARY KEY,
    title               VARCHAR(100) NOT NULL,
    start_date          DATE         NOT NULL,
    end_date            DATE         NOT NULL,
    username            VARCHAR(64)  NOT NULL,
    course_id           INT          NOT NULL,
    organize_id         INT          NOT NULL,
    publisher           VARCHAR(30)  NOT NULL,
    release_time        TIMESTAMP    NOT NULL,
    FOREIGN KEY (username) REFERENCES
        users (username) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (course_id) REFERENCES
        course (course_id),
    FOREIGN KEY (organize_id) REFERENCES
        organize (organize_id)
);

CREATE TABLE training_configure
(
    training_configure_id VARCHAR(64) PRIMARY KEY,
    week_day              TINYINT     NOT NULL,
    start_time            TIME        NOT NULL,
    end_time              TIME        NOT NULL,
    schoolroom_id         INT         NOT NULL,
    training_release_id   VARCHAR(64) NOT NULL,
    FOREIGN KEY (schoolroom_id) REFERENCES
        schoolroom (schoolroom_id),
    FOREIGN KEY (training_release_id) REFERENCES
        training_release (training_release_id) ON DELETE CASCADE
);

CREATE TABLE training_users
(
    training_users_id   VARCHAR(64) PRIMARY KEY,
    training_release_id VARCHAR(64) NOT NULL,
    student_id          INT         NOT NULL,
    create_date         TIMESTAMP   NOT NULL,
    remark              VARCHAR(200),
    FOREIGN KEY (training_release_id) REFERENCES
        training_release (training_release_id) ON DELETE CASCADE,
    FOREIGN KEY (student_id) REFERENCES
        student (student_id) ON DELETE CASCADE,
    UNIQUE (training_release_id, student_id)
);

CREATE TABLE training_attend
(
    training_attend_id  VARCHAR(64) PRIMARY KEY,
    attend_date         DATE        NOT NULL,
    attend_room         INT         NOT NULL,
    training_release_id VARCHAR(64) NOT NULL,
    publish_date        TIMESTAMP   NOT NULL,
    attend_start_time   TIME        NOT NULL,
    attend_end_time     TIME        NOT NULL,
    remark              VARCHAR(200),
    FOREIGN KEY (attend_room) REFERENCES
        schoolroom (schoolroom_id),
    FOREIGN KEY (training_release_id) REFERENCES
        training_release (training_release_id) ON DELETE CASCADE
);

CREATE TABLE training_authorities
(
    authorities_id      VARCHAR(64) PRIMARY KEY,
    training_release_id VARCHAR(64) NOT NULL,
    username            VARCHAR(64) NOT NULL,
    valid_date          DATETIME    NOT NULL,
    expire_date         DATETIME    NOT NULL,
    FOREIGN KEY (training_release_id) REFERENCES
        training_release (training_release_id) ON DELETE CASCADE,
    FOREIGN KEY (username) REFERENCES
        users (username) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE training_attend_users
(
    attend_users_id    VARCHAR(64) PRIMARY KEY,
    training_attend_id VARCHAR(64) NOT NULL,
    training_users_id  VARCHAR(64) NOT NULL,
    operate_user       VARCHAR(64) NOT NULL,
    operate_date       DATETIME    NOT NULL,
    operate            TINYINT     NOT NULL,
    remark             VARCHAR(200),
    FOREIGN KEY (training_attend_id) REFERENCES
        training_attend (training_attend_id) ON DELETE CASCADE,
    FOREIGN KEY (training_users_id) REFERENCES
        training_users (training_users_id) ON DELETE CASCADE,
    FOREIGN KEY (operate_user) REFERENCES
        users (username) ON UPDATE CASCADE ON DELETE CASCADE,
    UNIQUE (training_attend_id, training_users_id)
);

CREATE TABLE training_document
(
    training_document_id VARCHAR(64) PRIMARY KEY,
    training_release_id  VARCHAR(64)  NOT NULL,
    document_title       VARCHAR(200) NOT NULL,
    username             VARCHAR(64)  NOT NULL,
    course_id            INT          NOT NULL,
    creator              VARCHAR(30)  NOT NULL,
    create_date          TIMESTAMP    NOT NULL,
    reading              INT          NOT NULL DEFAULT 0,
    is_original          BOOLEAN      NOT NULL DEFAULT 1,
    origin               VARCHAR(500),
    FOREIGN KEY (training_release_id) REFERENCES
        training_release (training_release_id) ON DELETE CASCADE,
    FOREIGN KEY (username) REFERENCES
        users (username) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (course_id) REFERENCES
        course (course_id)
);

CREATE TABLE training_document_content
(
    training_document_id      VARCHAR(64) NOT NULL UNIQUE,
    training_document_content LONGTEXT    NOT NULL,
    FOREIGN KEY (training_document_id) REFERENCES
        training_document (training_document_id) ON DELETE CASCADE
);

CREATE TABLE training_document_file
(
    training_document_id VARCHAR(64) NOT NULL,
    course_id            INT         NOT NULL,
    file_id              VARCHAR(64) NOT NULL,
    username             VARCHAR(64) NOT NULL,
    uploader             VARCHAR(30) NOT NULL,
    create_date          TIMESTAMP   NOT NULL,
    downloads            INT         NOT NULL DEFAULT 0,
    FOREIGN KEY (training_document_id) REFERENCES
        training_document (training_document_id) ON DELETE CASCADE,
    FOREIGN KEY (course_id) REFERENCES
        course (course_id),
    FOREIGN KEY (file_id) REFERENCES files (file_id),
    FOREIGN KEY (username) REFERENCES
        users (username) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE training_special
(
    training_special_id VARCHAR(64) PRIMARY KEY,
    title               VARCHAR(100) NOT NULL,
    username            VARCHAR(64)  NOT NULL,
    publisher           VARCHAR(30)  NOT NULL,
    release_time        TIMESTAMP    NOT NULL,
    FOREIGN KEY (username) REFERENCES
        users (username) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE training_special_file_type
(
    file_type_id        VARCHAR(64) PRIMARY KEY,
    file_type_name      VARCHAR(100) NOT NULL,
    training_special_id VARCHAR(64)  NOT NULL,
    FOREIGN KEY (training_special_id) REFERENCES
        training_special (training_special_id) ON DELETE CASCADE
);

CREATE TABLE training_special_file
(
    file_type_id VARCHAR(64) NOT NULL,
    file_id      VARCHAR(64),
    username     VARCHAR(64) NOT NULL,
    uploader     VARCHAR(30) NOT NULL,
    create_date  TIMESTAMP   NOT NULL,
    downloads    INT         NOT NULL DEFAULT 0,
    FOREIGN KEY (file_type_id) REFERENCES
        training_special_file_type (file_type_id) ON DELETE CASCADE
);