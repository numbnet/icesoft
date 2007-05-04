DROP DATABASE IF EXISTS register;
CREATE DATABASE IF NOT EXISTS register;
USE register;

GRANT ALL ON *.* TO 'test'@'localhost'
IDENTIFIED BY 'test'
WITH MAX_QUERIES_PER_HOUR 0 MAX_CONNECTIONS_PER_HOUR 0 MAX_UPDATES_PER_HOUR 0 ;


DROP TABLE IF EXISTS students;
CREATE TABLE students (
	student_id int NOT NULL AUTO_INCREMENT,
	first_name varchar(50) NOT NULL,
	last_name varchar(50) NOT NULL,
	address varchar(100) NOT NULL,
	PRIMARY KEY (student_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS courses;
CREATE TABLE courses (
	course_id int NOT NULL AUTO_INCREMENT,
	course_name varchar(100) NOT NULL,
	description varchar(200) NOT NULL,
	PRIMARY KEY (course_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS student_courses;
CREATE TABLE student_courses (
	student_id int NOT NULL,
	course_id int NOT NULL,
	PRIMARY KEY (student_id, course_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO courses (course_name, description) VALUES("ICE101", "Introduction to ICEfaces");
INSERT INTO courses (course_name, description) VALUES("ICE201", "Intermediate ICEfaces techniques");
INSERT INTO courses (course_name, description) VALUES("ICE301", "Advanced ICEfaces techniques");
INSERT INTO courses (course_name, description) VALUES("AJAX101", "AJAX basics");
INSERT INTO courses (course_name, description) VALUES("JAVA101", "JAVA for beginners");
INSERT INTO courses (course_name, description) VALUES("HIB101", "Introduction to Hibernate");
INSERT INTO courses (course_name, description) VALUES("JSF201", "Advanced JSF techniques");
INSERT INTO courses (course_name, description) VALUES("SQL101", "Beginner SQL programming");

INSERT INTO students (first_name, last_name, address) VALUES("test1", "test1", "test1");
INSERT INTO students (first_name, last_name, address) VALUES("test2", "test2", "test2");
INSERT INTO students (first_name, last_name, address) VALUES("test3", "test3", "test3");
