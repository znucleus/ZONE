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
 attend_release_sub_id INT AUTO_INCREMENT PRIMARY KEY,
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
 attend_release_id VARCHAR(64) NOT NULL,
 create_date TIMESTAMP NOT NULL,
 remark VARCHAR(200),
 UNIQUE (student_id,attend_release_id),
 FOREIGN KEY(attend_release_id) REFERENCES attend_release(attend_release_id) ON DELETE CASCADE,
 FOREIGN KEY(student_id) REFERENCES student(student_id) ON DELETE CASCADE
);

CREATE TABLE attend_data(
 attend_users_id VARCHAR(64) NOT NULL,
 attend_release_sub_id INT NOT NULL,
 location VARCHAR(200),
 address VARCHAR(300),
 device_same BOOLEAN,
 attend_date TIMESTAMP NOT NULL,
 attend_remark VARCHAR(200),
 PRIMARY KEY(attend_users_id, attend_release_sub_id),
 FOREIGN KEY(attend_release_sub_id) REFERENCES attend_release_sub(attend_release_sub_id) ON DELETE CASCADE,
 FOREIGN KEY(attend_users_id) REFERENCES attend_users(attend_users_id) ON DELETE CASCADE
);

CREATE TABLE attend_map_key(
  id INT PRIMARY KEY,
  map_key VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE wei_xin(
  wei_xin_id INT AUTO_INCREMENT PRIMARY KEY,
  open_id VARCHAR(200) NOT NULL,
  session_key VARCHAR(300),
  union_id VARCHAR(200),
  app_id VARCHAR(64) NOT NULL,
  username VARCHAR(64) NOT NULL,
  res_code VARCHAR(200),
  result VARCHAR(500),
  create_date TIMESTAMP NOT NULL,
  UNIQUE (open_id,app_id,username),
  FOREIGN KEY (username) REFERENCES users (username) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE attend_wx_student_subscribe(
  subscribe_id VARCHAR(64) PRIMARY KEY ,
  template_id VARCHAR(64) NOT NULL,
  page VARCHAR(200),
  data VARCHAR(500),
  mini_program_state VARCHAR(20),
  lang VARCHAR(10),
  student_id INT NOT NULL,
  attend_release_id VARCHAR(64) NOT NULL,
  create_date TIMESTAMP NOT NULL,
  UNIQUE (student_id,attend_release_id),
  FOREIGN KEY(attend_release_id) REFERENCES attend_release(attend_release_id) ON DELETE CASCADE,
  FOREIGN KEY(student_id) REFERENCES student(student_id) ON DELETE CASCADE
);

CREATE TABLE attend_subscribe_log(
  log_id VARCHAR(64) PRIMARY KEY ,
  username VARCHAR(64),
  attend_release_id VARCHAR(64),
  real_name VARCHAR(15),
  open_id VARCHAR(200),
  template_id VARCHAR(64),
  request VARCHAR(500),
  result VARCHAR(500),
  create_date TIMESTAMP NOT NULL
);

CREATE TABLE wei_xin_device(
  device_id VARCHAR(64) PRIMARY KEY ,
  brand VARCHAR(100),
  model VARCHAR(100),
  version VARCHAR(30),
  screen_width DOUBLE,
  screen_height DOUBLE,
  system_info VARCHAR(100),
  platform VARCHAR(30),
  location_authorized BOOLEAN,
  notification_authorized BOOLEAN,
  username VARCHAR(64) NOT NULL,
  create_date TIMESTAMP NOT NULL,
  FOREIGN KEY (username) REFERENCES users (username) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE channel(
  channel_id INT AUTO_INCREMENT PRIMARY KEY,
  channel_name VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE epidemic_register_release(
  epidemic_register_release_id VARCHAR(64) PRIMARY KEY ,
  title VARCHAR(100) NOT NULL,
  username VARCHAR(64) NOT NULL,
  publisher VARCHAR(30) NOT NULL,
  release_time TIMESTAMP NOT NULL,
  FOREIGN KEY(username) REFERENCES
  users(username) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE epidemic_register_data(
  epidemic_register_data_id VARCHAR(64) PRIMARY KEY ,
  location VARCHAR(200),
  address VARCHAR(300),
  epidemic_status VARCHAR(500) NOT NULL,
  register_real_name VARCHAR(30) NOT NULL,
  register_type VARCHAR(50) NOT NULL,
  institute VARCHAR(500) NOT NULL,
  register_date TIMESTAMP NOT NULL,
  epidemic_register_release_id VARCHAR(64) NOT NULL,
  register_username VARCHAR(64) NOT NULL,
  channel_id INT NOT NULL,
  remark VARCHAR(200),
  FOREIGN KEY(epidemic_register_release_id) REFERENCES
  epidemic_register_release(epidemic_register_release_id) ON DELETE CASCADE,
  FOREIGN KEY(register_username) REFERENCES
  users(username) ON UPDATE CASCADE ON DELETE CASCADE,
  FOREIGN KEY (channel_id)REFERENCES
  channel(channel_id)
);

INSERT INTO channel (channel_name) VALUES ('WEB');
INSERT INTO channel (channel_name) VALUES ('API');

CREATE TABLE wei_xin_small_version(
  version VARCHAR(20) PRIMARY KEY,
  remark VARCHAR(200),
  switch BOOLEAN NOT NULL DEFAULT 0,
  create_date TIMESTAMP NOT NULL
);