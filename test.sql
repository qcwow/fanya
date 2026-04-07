/*
 Navicat Premium Dump SQL

 Source Server         : local
 Source Server Type    : MySQL
 Source Server Version : 90400 (9.4.0)
 Source Host           : localhost:3306
 Source Schema         : test

 Target Server Type    : MySQL
 Target Server Version : 90400 (9.4.0)
 File Encoding         : 65001

 Date: 06/04/2026 16:24:58
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for db_course
-- ----------------------------
DROP TABLE IF EXISTS `db_course`;
CREATE TABLE `db_course` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '内部课程ID',
  `platform_course_id` varchar(64) NOT NULL COMMENT '外部平台课程ID',
  `platform_id` varchar(64) DEFAULT NULL COMMENT '所属平台ID',
  `course_name` varchar(255) NOT NULL COMMENT '课程名称',
  `school_id` varchar(64) DEFAULT NULL COMMENT '学校ID',
  `school_name` varchar(255) DEFAULT NULL COMMENT '学校名称',
  `term` varchar(32) DEFAULT NULL COMMENT '学期',
  `credit` float DEFAULT NULL COMMENT '学分',
  `period` int DEFAULT NULL COMMENT '学时',
  `course_cover` varchar(500) DEFAULT NULL COMMENT '课程封面URL',
  `teacher_info` json DEFAULT NULL COMMENT '教师信息列表(JSON存储数组)',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_platform_course` (`platform_course_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='课程基础信息表';

-- ----------------------------
-- Records of db_course
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for db_learning_progress
-- ----------------------------
DROP TABLE IF EXISTS `db_learning_progress`;
CREATE TABLE `db_learning_progress` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL COMMENT '学生ID',
  `lesson_id` bigint NOT NULL COMMENT '智课ID',
  `current_section_id` bigint DEFAULT NULL COMMENT '当前学习的小节ID',
  `progress_percent` float DEFAULT '0' COMMENT '章节学习进度(0-100)',
  `total_progress` float DEFAULT '0' COMMENT '整门课总进度',
  `last_operate_time` datetime DEFAULT NULL COMMENT '最后操作时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_lesson` (`user_id`,`lesson_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='学习进度追踪表';

-- ----------------------------
-- Records of db_learning_progress
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for db_lesson
-- ----------------------------
DROP TABLE IF EXISTS `db_lesson`;
CREATE TABLE `db_lesson` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '内部智课ID',
  `course_id` verchar(64) NOT NULL COMMENT '关联内部课程ID',
  `user_id` verchar(64) NOT NULL COMMENT '创建教师ID',
  `file_name` varchar(255) DEFAULT NULL COMMENT '原始文件名',
  `file_size` bigint DEFAULT NULL COMMENT '文件大小(字节)',
  `file_type` varchar(16) DEFAULT NULL COMMENT 'ppt/pdf',
  `file_url` varchar(500) DEFAULT NULL COMMENT '文件存储路径',
  `page_count` int DEFAULT NULL COMMENT '总页数',
  `task_status` enum('processing','completed','failed') DEFAULT 'processing' COMMENT '任务状态',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='智课主表';

-- ----------------------------
-- Records of db_lesson
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for db_lesson_audio
-- ----------------------------
DROP TABLE IF EXISTS `db_lesson_audio`;
CREATE TABLE `db_lesson_audio` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'audioId',
  `script_id` bigint NOT NULL COMMENT '关联脚本ID',
  `voice_type` varchar(32) DEFAULT NULL COMMENT '发音人类型',
  `audio_url` varchar(500) DEFAULT NULL COMMENT '全本音频URL',
  `format` varchar(16) DEFAULT 'mp3',
  `bit_rate` int DEFAULT NULL COMMENT '比特率',
  `total_duration` int DEFAULT NULL COMMENT '总时长',
  `file_size` bigint DEFAULT NULL COMMENT '文件大小',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='语音合成记录表';

-- ----------------------------
-- Records of db_lesson_audio
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for db_lesson_script
-- ----------------------------
DROP TABLE IF EXISTS `db_lesson_script`;
CREATE TABLE `db_lesson_script` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '脚本ID(scriptId)',
  `lesson_id` bigint NOT NULL COMMENT '关联智课ID',
  `teaching_style` varchar(32) DEFAULT 'standard' COMMENT '讲授风格',
  `speech_speed` varchar(16) DEFAULT 'normal' COMMENT '语速',
  `custom_opening` text COMMENT '自定义开场白',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='智课脚本主表';

-- ----------------------------
-- Records of db_lesson_script
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for db_lesson_script_section
-- ----------------------------
DROP TABLE IF EXISTS `db_lesson_script_section`;
CREATE TABLE `db_lesson_script_section` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `script_id` bigint NOT NULL COMMENT '关联脚本ID',
  `section_name` varchar(255) DEFAULT NULL COMMENT '小节名称',
  `content` text COMMENT '讲授正文内容',
  `duration` int DEFAULT NULL COMMENT '预计时长(秒)',
  `related_chapter_id` bigint DEFAULT NULL COMMENT '关联课件结构ID',
  `related_page` varchar(64) DEFAULT NULL COMMENT '关联页码',
  `key_points` json DEFAULT NULL COMMENT '关键知识点标签(JSON数组)',
  `sort_order` int DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='脚本小节内容表';

-- ----------------------------
-- Records of db_lesson_script_section
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for db_lesson_structure
-- ----------------------------
DROP TABLE IF EXISTS `db_lesson_structure`;
CREATE TABLE `db_lesson_structure` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `lesson_id` bigint NOT NULL COMMENT '关联智课ID',
  `parent_id` bigint DEFAULT '0' COMMENT '父节点ID(实现层级结构)',
  `title` varchar(255) NOT NULL COMMENT '章节/知识点名称',
  `is_key_point` tinyint(1) DEFAULT '0' COMMENT '是否为重点',
  `page_range` varchar(64) DEFAULT NULL COMMENT '对应课件页码范围(如3-5)',
  `sort_order` int DEFAULT '0' COMMENT '排序',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='课件章节结构表';

-- ----------------------------
-- Records of db_lesson_structure
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for db_qa_record
-- ----------------------------
DROP TABLE IF EXISTS `db_qa_record`;
CREATE TABLE `db_qa_record` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'answerId',
  `user_id` bigint NOT NULL COMMENT '提问学生ID',
  `lesson_id` bigint NOT NULL COMMENT '关联智课ID',
  `session_id` varchar(64) NOT NULL COMMENT '会话ID(用于多轮对话串联)',
  `question_type` enum('text','voice') DEFAULT 'text',
  `question_content` text NOT NULL COMMENT '提问内容(文字或语音URL)',
  `answer_content` text COMMENT 'AI生成的回答',
  `current_section_id` bigint DEFAULT NULL COMMENT '提问时所处的小节ID',
  `understanding_level` varchar(16) DEFAULT NULL COMMENT '学生理解程度: none/partial/full',
  `related_knowledge` json DEFAULT NULL COMMENT '关联知识点信息',
  `suggestions` json DEFAULT NULL COMMENT '追问建议',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_session` (`session_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='问答交互记录表';

-- ----------------------------
-- Records of db_qa_record
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for db_user
-- ----------------------------
DROP TABLE IF EXISTS `db_user`;
CREATE TABLE `db_user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint DEFAULT NULL,
  `platform_user_id` varchar(64) DEFAULT NULL COMMENT '外部平台用户ID',
  `username` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `role` int DEFAULT NULL,
  `school_id` varchar(64) DEFAULT NULL COMMENT '所属学校ID',
  `real_name` varchar(128) DEFAULT NULL COMMENT '真实姓名',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_name` (`username`),
  UNIQUE KEY `uk_platform_user` (`platform_user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of db_user
-- ----------------------------
BEGIN;
INSERT INTO `db_user` (`id`, `user_id`, `platform_user_id`, `username`, `password`, `role`, `school_id`, `real_name`) VALUES (1, NULL, NULL, 'test', '$2a$10$DkTnRurSftrtZKsTMDKH8.f6iRVP/6bMgtf8CABgzUVvlLvhBK4VC', 1, NULL, NULL);
INSERT INTO `db_user` (`id`, `user_id`, `platform_user_id`, `username`, `password`, `role`, `school_id`, `real_name`) VALUES (3, NULL, NULL, 'admin', '$2a$10$HgY9wb0qCpIZAUaSXePib.ih4UJB/qIhoayVj5XGMZdGQMEQVUy2G', 2, NULL, NULL);
INSERT INTO `db_user` (`id`, `user_id`, `platform_user_id`, `username`, `password`, `role`, `school_id`, `real_name`) VALUES (4, NULL, NULL, 'mjx', '$2a$10$5kPNRpbSTaOeROrVunHN0egaRLLswWmT/l2EXhUYpj6CspDkpmjRC', 1, NULL, NULL);
INSERT INTO `db_user` (`id`, `user_id`, `platform_user_id`, `username`, `password`, `role`, `school_id`, `real_name`) VALUES (5, NULL, NULL, 'ldj', '$2a$10$z.2LBONyDj7boo0zQ7J9WOX63mOLRJXFZnzzftY9bM.AND.2VSgaG', 1, NULL, NULL);
INSERT INTO `db_user` (`id`, `user_id`, `platform_user_id`, `username`, `password`, `role`, `school_id`, `real_name`) VALUES (6, NULL, NULL, 'lxh', '$2a$10$9RGIJ2jFf7CKV6nUwmLoWud3bE/7Zz.NI5o167CrkNaZuu5fdNoHK', 1, NULL, NULL);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
