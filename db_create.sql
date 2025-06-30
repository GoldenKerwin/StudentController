DROP DATABASE IF EXISTS student_controller;
CREATE DATABASE student_controller CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE student_controller;

-- Create user table
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) DEFAULT NULL COMMENT 'Username',
  `password` varchar(255) DEFAULT NULL COMMENT 'Password',
  `salt` varchar(255) DEFAULT NULL COMMENT 'Password salt',
  `name` varchar(255) DEFAULT NULL COMMENT 'Real name',
  `sch_number` varchar(255) DEFAULT NULL COMMENT 'Student/Teacher ID',
  `mail` varchar(255) DEFAULT NULL COMMENT 'Email',
  `creat_time` datetime DEFAULT NULL COMMENT 'Creation time',
  `last_time` datetime DEFAULT NULL COMMENT 'Last login time',
  `role_id` int(11) DEFAULT 1 COMMENT 'Role ID: 1-Student, 2-Teacher',
  `sch_department` varchar(255) DEFAULT NULL COMMENT 'Class/Department',
  `login_count` int(11) DEFAULT 0 COMMENT 'Login count',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Create student info table
CREATE TABLE `stu_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `sch_number` varchar(255) DEFAULT NULL COMMENT 'Student ID',
  `name` varchar(255) DEFAULT NULL COMMENT 'Name',
  `class_name` varchar(255) DEFAULT NULL COMMENT 'Class',
  `sex` varchar(255) DEFAULT NULL COMMENT 'Gender',
  `address` varchar(255) DEFAULT NULL COMMENT 'Address',
  `father_name` varchar(255) DEFAULT NULL COMMENT 'Father name',
  `father_phone` varchar(255) DEFAULT NULL COMMENT 'Father phone',
  `mather_name` varchar(255) DEFAULT NULL COMMENT 'Mother name',
  `mather_phone` varchar(255) DEFAULT NULL COMMENT 'Mother phone',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Create grade table
CREATE TABLE `grade` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `sch_number` varchar(255) DEFAULT NULL COMMENT 'Student ID',
  `sub_name` varchar(255) DEFAULT NULL COMMENT 'Subject name',
  `results` double DEFAULT NULL COMMENT 'Score',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Insert test user data: student and teacher accounts
-- All passwords are 123456
INSERT INTO `user` (`id`, `username`, `password`, `salt`, `name`, `sch_number`, `mail`, `role_id`, `sch_department`) VALUES 
(1, 'student1', '0e3b8535871f363c41154619b16853d2', 'salt1', 'Zhang San', '2024001', 'student1@example.com', 1, 'Software Engineering 1'),
(2, 'student2', 'f1d3ff3e6754049875d4383a15c32479', 'salt2', 'Li Si', '2024002', 'student2@example.com', 1, 'Software Engineering 2'),
(3, 'student3', '5d41402abc4b2a76b9719d911017c592', 'salt3', 'Wang Wu', '2024003', 'student3@example.com', 1, 'Computer Science 1'),
(4, 'student4', '6d41403def4b2a76b9719d911017c592', 'salt4', 'Zhao Liu', '2024004', 'student4@example.com', 1, 'Computer Science 2'),
(5, 'student5', '7d41404ghi4b2a76b9719d911017c592', 'salt5', 'Qian Qi', '2024005', 'student5@example.com', 1, 'Network Engineering 1'),
(6, 'teacher1', '361873aa3902347318721c828e67c870', 'salt6', 'Teacher One', 'T001', 'teacher1@example.com', 2, 'Computer Science Department'),
(7, 'teacher2', '461873bb3902347318721c828e67c871', 'salt7', 'Teacher Two', 'T002', 'teacher2@example.com', 2, 'Software Engineering Department');

-- Insert student info data
INSERT INTO `stu_info` (`id`, `sch_number`, `name`, `class_name`, `sex`) VALUES 
(1, '2024001', 'Zhang San', 'Software Engineering 1', 'Male'),
(2, '2024002', 'Li Si', 'Software Engineering 2', 'Female'),
(3, '2024003', 'Wang Wu', 'Computer Science 1', 'Male'),
(4, '2024004', 'Zhao Liu', 'Computer Science 2', 'Female'),
(5, '2024005', 'Qian Qi', 'Network Engineering 1', 'Male');

-- Insert grade data
INSERT INTO `grade` (`sch_number`, `sub_name`, `results`) VALUES 
('2024001', 'Computer Networks', 88),
('2024001', 'Operating Systems', 92),
('2024001', 'Data Structures', 85),
('2024001', 'Software Engineering', 90),
('2024002', 'Computer Networks', 76),
('2024002', 'Operating Systems', 85),
('2024002', 'Data Structures', 88),
('2024002', 'Software Engineering', 79),
('2024003', 'Computer Networks', 92),
('2024003', 'Operating Systems', 88),
('2024003', 'Data Structures', 95),
('2024003', 'Software Engineering', 91),
('2024004', 'Computer Networks', 78),
('2024004', 'Operating Systems', 82),
('2024004', 'Data Structures', 75),
('2024004', 'Software Engineering', 80),
('2024005', 'Computer Networks', 85),
('2024005', 'Operating Systems', 89),
('2024005', 'Data Structures', 83),
('2024005', 'Software Engineering', 87);