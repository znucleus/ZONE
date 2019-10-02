CREATE TABLE training_release(
  training_release_id VARCHAR(64) PRIMARY KEY,
  title VARCHAR(100) NOT NULL,
  start_date DATE NOT NULL,
  end_date DATE NOT NULL,
  cycle VARCHAR(20) NOT NULL,
  start_time TIME NOT NULL,
  end_time TIME NOT NULL,
  schoolroom_id INT NOT NULL,
  username VARCHAR(64) NOT NULL,
  course_id INT NOT NULL,
  organize_id INT NOT NULL,
  publisher VARCHAR(30) NOT NULL,
  release_time TIMESTAMP NOT NULL,
  FOREIGN KEY(schoolroom_id) REFERENCES
  schoolroom(schoolroom_id),
  FOREIGN KEY(username) REFERENCES
  users(username) ON UPDATE CASCADE ON DELETE CASCADE,
  FOREIGN KEY(course_id) REFERENCES
  course(course_id),
  FOREIGN KEY(organize_id) REFERENCES
  organize(organize_id)
);

CREATE TABLE training_users(
  training_users_id  VARCHAR(64) PRIMARY KEY,
  training_release_id VARCHAR(64) NOT NULL,
  student_id INT NOT NULL,
  FOREIGN KEY(training_release_id) REFERENCES
  training_release(training_release_id) ON DELETE CASCADE,
  FOREIGN KEY(student_id) REFERENCES
  student(student_id),
  UNIQUE (training_release_id,student_id)
);

CREATE TABLE training_attend(
  training_attend_id VARCHAR(64) PRIMARY KEY,
  attend_date DATE NOT NULL,
  attend_room INT NOT NULL,
  training_release_id VARCHAR(64) NOT NULL,
  publish_date TIMESTAMP NOT NULL,
  attend_start_time TIME NOT NULL,
  attend_end_time TIME NOT NULL,
  FOREIGN KEY(attend_room) REFERENCES
  schoolroom(schoolroom_id),
  FOREIGN KEY(training_release_id) REFERENCES
  training_release(training_release_id) ON DELETE CASCADE
);

CREATE TABLE training_attend_authorities(
  authorities_id VARCHAR(64) PRIMARY KEY,
  training_attend_id VARCHAR(64) NOT NULL,
  username VARCHAR(64) NOT NULL,
  operate_type TINYINT NOT NULL,
  expire_date DATETIME NOT NULL,
  FOREIGN KEY(training_attend_id) REFERENCES
  training_attend(training_attend_id) ON DELETE CASCADE,
  FOREIGN KEY(username) REFERENCES
  users(username) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE training_attend_users(
  attend_users_id VARCHAR(64) PRIMARY KEY,
  training_attend_id VARCHAR(64) NOT NULL,
  training_users_id VARCHAR(64) NOT NULL,
  operate_date DATETIME NOT NULL,
  operate TINYINT NOT NULL,
  remark VARCHAR(200),
  FOREIGN KEY(training_attend_id) REFERENCES
  training_attend(training_attend_id) ON DELETE CASCADE,
  FOREIGN KEY(training_users_id) REFERENCES
  training_users(training_users_id) ON DELETE CASCADE,
  UNIQUE (training_attend_id,training_users_id)
);

CREATE TABLE training_labs(
  training_labs_id VARCHAR(64) PRIMARY KEY,
  student_number VARCHAR(20) UNIQUE NOT NULL,
  real_name VARCHAR(10),
  age INT,
  sex VARCHAR(2) ,
  organize VARCHAR(20) ,
  photo VARCHAR(500) ,
  profession VARCHAR(100) ,
  company VARCHAR(100) ,
  achievement VARCHAR(200) ,
  password VARCHAR(800),
  create_date TIMESTAMP NOT NULL
);