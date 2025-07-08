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
  `test_no` varchar(255) DEFAULT NULL COMMENT 'Test Number',
  `operator` varchar(255) DEFAULT NULL COMMENT 'Created by',
  `updater` varchar(255) DEFAULT NULL COMMENT 'Updated by',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Create role table
CREATE TABLE `role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(50) NOT NULL COMMENT 'Role name',
  `role_desc` varchar(255) DEFAULT NULL COMMENT 'Role description',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Create permission table
CREATE TABLE `permission` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `permission_name` varchar(50) NOT NULL COMMENT 'Permission name',
  `permission_desc` varchar(255) DEFAULT NULL COMMENT 'Permission description',
  `permission_code` varchar(50) NOT NULL COMMENT 'Permission code',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Create role-permission mapping table
CREATE TABLE `role_permission` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_id` int(11) NOT NULL,
  `permission_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_permission` (`role_id`,`permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Create attendance table
CREATE TABLE `attendance` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `sch_number` varchar(255) NOT NULL COMMENT '学号',
  `date` date NOT NULL COMMENT '日期',
  `status` varchar(20) NOT NULL COMMENT '状态：正常/迟到/早退/缺席/请假',
  `course` varchar(100) DEFAULT NULL COMMENT '课程',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `operator` varchar(255) DEFAULT NULL COMMENT '操作人',
  PRIMARY KEY (`id`),
  KEY `idx_sch_number_date` (`sch_number`, `date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
-- Create file_upload table
CREATE TABLE `file_upload` (
  `id` int NOT NULL AUTO_INCREMENT,
  `original_filename` varchar(255) NOT NULL,
  `stored_filename` varchar(255) NOT NULL,
  `file_path` varchar(255) NOT NULL,
  `file_size` bigint DEFAULT NULL,
  `file_type` varchar(50) NOT NULL,
  `uploader_id` int NOT NULL,
  `student_id` int DEFAULT NULL,
  `course_name` varchar(100) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `stored_filename` (`stored_filename`),
  KEY `uploader_id` (`uploader_id`),
  KEY `student_id` (`student_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Insert test user data: student and teacher accounts
-- All passwords are 123456
INSERT INTO `user` (`id`, `username`, `password`, `salt`, `name`, `sch_number`, `mail`, `role_id`, `sch_department`) VALUES 
(1, 'student1', '0e3b8535871f363c41154619b16853d2', 'salt1', 'Zhang San', '2024001', 'student1@example.com', 1, 'Software Engineering 1'),
(2, 'student2', 'f1d3ff3e6754049875d4383a15c32479', 'salt2', 'Li Si', '2024002', 'student2@example.com', 1, 'Software Engineering 2'),
(3, 'student3', '5d41402abc4b2a76b9719d911017c592', 'salt3', 'Wang Wu', '2024003', 'student3@example.com', 1, 'Computer Science 1'),
(4, 'student4', '6d41403def4b2a76b9719d911017c592', 'salt4', 'Zhao Liu', '2024004', 'student4@example.com', 1, 'Computer Science 2'),
(5, 'student5', '7d41404ghi4b2a76b9719d911017c592', 'salt5', 'Qian Qi', '2024005', 'student5@example.com', 1, 'Network Engineering 1'),
(6, 'teacher1', '361873aa3902347318721c828e67c870', 'salt6', 'Teacher One', 'T001', 'teacher1@example.com', 2, 'Computer Science Department'),
(7, 'teacher2', '461873bb3902347318721c828e67c871', 'salt7', 'Teacher Two', 'T002', 'teacher2@example.com', 2, 'Software Engineering Department'),
(8, 'admin', '5d41402abc4b2a76b9719d911017c592', 'salt8', 'Admin', 'A001', 'admin@example.com', 3, 'Administration Department'),
(9, 'counselor', '6d41403def4b2a76b9719d911017c592', 'salt9', 'Counselor One', 'C001', 'counselor@example.com', 4, 'Student Affairs Department');

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

-- Insert role data
INSERT INTO `role` (`id`, `role_name`, `role_desc`) VALUES
(1, '学生', '普通学生用户'),
(2, '教师', '教师用户'),
(3, '管理员', '系统管理员'),
(4, '辅导员', '班级辅导员');

-- Insert permission data
INSERT INTO `permission` VALUES
(1, '查看个人信息', '查看自己的个人信息', 'stu:info:view'),
(2, '修改个人信息', '修改自己的个人信息', 'stu:info:edit'),
(3, '查看个人成绩', '查看自己的成绩', 'stu:grade:view'),
(4, '查看所有学生信息', '查看所有学生的信息', 'teacher:stuinfo:view'),
(5, '查看所有学生成绩', '查看所有学生的成绩', 'teacher:grade:view'),
(6, '录入学生成绩', '为学生录入成绩', 'teacher:grade:add'),
(7, '修改学生成绩', '修改学生的成绩', 'teacher:grade:edit'),
(8, '系统管理', '管理系统设置', 'admin:system:manage'),
(9, '用户管理', '管理系统用户', 'admin:user:manage'),
(10, '班级管理', '管理班级信息', 'counselor:class:manage'),
(11, '学生考勤管理', '管理学生考勤', 'teacher:attendance:manage'),
(12, '查看考勤记录', '查看自己的考勤记录', 'stu:attendance:view');

-- Insert role-permission mapping data
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES
(1, 1), (1, 2), (1, 3), (1, 12),
(2, 1), (2, 2), (2, 3), (2, 4), (2, 5), (2, 6), (2, 7), (2, 11),
(3, 1), (3, 2), (3, 3), (3, 4), (3, 5), (3, 6), (3, 7), (3, 8), (3, 9), (3, 10), (3, 11),
(4, 1), (4, 2), (4, 3), (4, 4), (4, 5), (4, 10), (4, 11);

-- Insert sample attendance data
INSERT INTO `attendance` (`sch_number`, `date`, `status`, `course`, `remark`, `operator`) VALUES
('2024001', '2024-01-15', '正常', 'Computer Networks', NULL, 'Teacher One'),
('2024001', '2024-01-16', '正常', 'Operating Systems', NULL, 'Teacher One'),
('2024001', '2024-01-17', '迟到', 'Data Structures', '迟到10分钟', 'Teacher One'),
('2024002', '2024-01-15', '正常', 'Computer Networks', NULL, 'Teacher One'),
('2024002', '2024-01-16', '缺席', 'Operating Systems', '无故缺席', 'Teacher One'),
('2024002', '2024-01-17', '正常', 'Data Structures', NULL, 'Teacher One'),
('2024003', '2024-01-15', '早退', 'Computer Networks', '提前20分钟离开', 'Teacher One'),
('2024003', '2024-01-16', '正常', 'Operating Systems', NULL, 'Teacher One'),
('2024003', '2024-01-17', '正常', 'Data Structures', NULL, 'Teacher One'),
('2024004', '2024-01-15', '请假', 'Computer Networks', '病假', 'Teacher One'),
('2024004', '2024-01-16', '请假', 'Operating Systems', '病假', 'Teacher One'),
('2024004', '2024-01-17', '正常', 'Data Structures', NULL, 'Teacher One'),
('2024005', '2024-01-15', '正常', 'Computer Networks', NULL, 'Teacher One'),
('2024005', '2024-01-16', '正常', 'Operating Systems', NULL, 'Teacher One'),
('2024005', '2024-01-17', '正常', 'Data Structures', NULL, 'Teacher One');
