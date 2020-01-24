CREATE TABLE attend_release(
 attend_release_id VARCHAR(64) PRIMARY KEY,
 title VARCHAR(100) NOT NULL,
 attend_start_time TIMESTAMP NOT NULL,
 attend_end_time TIMESTAMP NOT NULL,
 is_auto BOOLEAN NOT NULL DEFAULT 0,
 valid_date TIMESTAMP NOT NULL,
 expire_date TIMESTAMP NOT NULL,
 organize_id INT NOT NULL,
 username VARCHAR(64) NOT NULL,
 release_time TIMESTAMP NOT NULL,
 FOREIGN KEY(organize_id) REFERENCES organize(organize_id),
 FOREIGN KEY(username) REFERENCES users(username) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE attend_release_sub(
 attend_release_sub_id VARCHAR(64) PRIMARY KEY,
 title VARCHAR(100) NOT NULL,
 attend_start_time TIMESTAMP NOT NULL,
 attend_end_time TIMESTAMP NOT NULL,
 is_auto BOOLEAN NOT NULL DEFAULT 0,
 valid_date TIMESTAMP NOT NULL,
 expire_date TIMESTAMP NOT NULL,
 organize_id INT NOT NULL,
 username VARCHAR(64) NOT NULL,
 attend_release_id VARCHAR(64) NOT NULL,
 release_time TIMESTAMP NOT NULL,
 FOREIGN KEY(attend_release_id) REFERENCES attend_release(attend_release_id) ON DELETE CASCADE,
 FOREIGN KEY(organize_id) REFERENCES organize(organize_id),
 FOREIGN KEY(username) REFERENCES users(username) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE attend_users(
 attend_users_id VARCHAR(64) PRIMARY KEY,
 student_id INT NOT NULL,
 status TINYINT NOT NULL DEFAULT 0,
 attend_release_sub_id VARCHAR(64) NOT NULL,
 create_date TIMESTAMP NOT NULL,
 operate_date TIMESTAMP,
 remark VARCHAR(200),
 FOREIGN KEY(attend_release_sub_id) REFERENCES attend_release_sub(attend_release_sub_id) ON DELETE CASCADE,
 FOREIGN KEY(student_id) REFERENCES student(student_id) ON DELETE CASCADE
);