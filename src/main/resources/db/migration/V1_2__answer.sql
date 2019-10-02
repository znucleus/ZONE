CREATE TABLE answer_bank(
  answer_bank_id VARCHAR(64)  PRIMARY KEY,
  bank_name VARCHAR(100) NOT NULL,
  username VARCHAR(64) NOT NULL,
  create_date TIMESTAMP NOT NULL,
  FOREIGN KEY(username) REFERENCES users(username) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE answer_subject(
  answer_subject_id VARCHAR(64) PRIMARY KEY,
  content VARCHAR(300) NOT NULL,
  subject_type TINYINT NOT NULL,
  right_key VARCHAR(10) NOT NULL,
  answer_bank_id VARCHAR(64) NOT NULL,
  username VARCHAR(64) NOT NULL,
  create_date TIMESTAMP NOT NULL,
  score DOUBLE NOT NULL,
  custom_id INT,
  FOREIGN KEY(answer_bank_id) REFERENCES answer_bank(answer_bank_id) ON DELETE CASCADE,
  FOREIGN KEY(username) REFERENCES users(username) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE answer_option(
  answer_option_id VARCHAR(64) PRIMARY KEY,
  option_content VARCHAR(300) NOT NULL,
  sort TINYINT NOT NULL,
  option_key VARCHAR(5) NOT NULL,
  answer_subject_id VARCHAR(64) NOT NULL,
  FOREIGN KEY(answer_subject_id) REFERENCES answer_subject(answer_subject_id) ON DELETE CASCADE
);

CREATE TABLE answer_release(
 answer_release_id VARCHAR(64) PRIMARY KEY,
 title VARCHAR(100) NOT NULL,
 start_time TIMESTAMP NOT NULL,
 end_time TIMESTAMP NOT NULL,
 answer_bank_id VARCHAR(64) NOT NULL,
 username VARCHAR(64) NOT NULL,
 release_time TIMESTAMP NOT NULL,
 FOREIGN KEY(answer_bank_id) REFERENCES answer_bank(answer_bank_id) ON DELETE CASCADE,
 FOREIGN KEY(username) REFERENCES users(username) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE answer_solving(
  answer_solving_id VARCHAR(64) PRIMARY KEY,
  answer_subject_id VARCHAR(64) NOT NULL,
  select_key VARCHAR(10),
  right_key VARCHAR(10) NOT NULL,
  user_id VARCHAR(64) NOT NULL,
  user_name VARCHAR(20),
  answer_release_id VARCHAR(64) NOT NULL,
  FOREIGN KEY(answer_subject_id) REFERENCES answer_subject(answer_subject_id) ON DELETE CASCADE,
  FOREIGN KEY(answer_release_id) REFERENCES answer_release(answer_release_id) ON DELETE CASCADE
);

CREATE TABLE answer_result(
  answer_result_id VARCHAR(64) PRIMARY KEY,
  user_id VARCHAR(64) NOT NULL,
  user_name VARCHAR(20),
  last_socre DOUBLE,
  total_score DOUBLE NOT NULL,
  answer_release_id VARCHAR(64) NOT NULL,
  FOREIGN KEY(answer_release_id) REFERENCES answer_release(answer_release_id) ON DELETE CASCADE
);