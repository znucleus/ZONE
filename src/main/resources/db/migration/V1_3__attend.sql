CREATE TABLE attend_release(
 attend_release_id VARCHAR(64) PRIMARY KEY,
 title VARCHAR(100) NOT NULL,
 attend_start_time TIMESTAMP NOT NULL,
 attend_end_time TIMESTAMP NOT NULL,
 is_auto BOOLEAN NOT NULL DEFAULT 0,
 expire_date TIMESTAMP NOT NULL,
 organize_id INT NOT NULL,
 username VARCHAR(64) NOT NULL,
 release_time TIMESTAMP NOT NULL,
 FOREIGN KEY(organize_id) REFERENCES organize(organize_id),
 FOREIGN KEY(username) REFERENCES users(username) ON UPDATE CASCADE ON DELETE CASCADE
);