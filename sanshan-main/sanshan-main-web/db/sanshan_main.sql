-- --------------------------------------------------------
-- 主机:                           127.0.0.1
-- 服务器版本:                        5.7.19 - MySQL Community Server (GPL)
-- 服务器操作系统:                      Win64
-- HeidiSQL 版本:                  9.5.0.5196
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- 导出 sanshan_main 的数据库结构
CREATE DATABASE IF NOT EXISTS `sanshan_main` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci */;
USE `sanshan_main`;

-- 导出  表 sanshan_main.blog_vote 结构
CREATE TABLE IF NOT EXISTS `blog_vote` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `blog_id` bigint(20) NOT NULL,
  `favours` int(20) NOT NULL DEFAULT '0',
  `treads` int(20) NOT NULL DEFAULT '0',
  `created` datetime NOT NULL,
  `updated` datetime NOT NULL,
  `extra` varchar(255) CHARACTER SET utf8 DEFAULT '',
  PRIMARY KEY (`id`),
  UNIQUE KEY `blog_vote_id` (`blog_id`) USING BTREE,
  KEY `favours` (`favours`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- 数据导出被取消选择。
-- 导出  表 sanshan_main.ip_blog_vote 结构
CREATE TABLE IF NOT EXISTS `ip_blog_vote` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `ip` varchar(20) CHARACTER SET utf8 NOT NULL DEFAULT '',
  `blog_id` bigint(20) NOT NULL,
  `favour` tinyint(1) DEFAULT '0',
  `tread` tinyint(1) DEFAULT '0',
  `created` datetime NOT NULL,
  `updated` datetime NOT NULL,
  `extra` varchar(255) CHARACTER SET utf8 DEFAULT '',
  PRIMARY KEY (`id`),
  KEY `ip_vote_blog_id` (`blog_id`) USING BTREE,
  KEY `vote_ip` (`ip`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=64 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- 数据导出被取消选择。
-- 导出  表 sanshan_main.markdown_blog 结构
CREATE TABLE IF NOT EXISTS `markdown_blog` (
  `id` bigint(20) NOT NULL,
  `content` longtext CHARACTER SET utf8,
  `time` datetime NOT NULL,
  `tag` varchar(100) CHARACTER SET utf8 DEFAULT '',
  `created` datetime NOT NULL,
  `updated` datetime NOT NULL,
  `user` varchar(20) CHARACTER SET utf8 NOT NULL DEFAULT '',
  `title` varchar(100) CHARACTER SET utf8 DEFAULT '',
  `extra` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `markdown_title` (`title`),
  KEY `markdown_tag` (`tag`),
  KEY `markdown_user` (`user`),
  KEY `markdown_time` (`time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- 数据导出被取消选择。
-- 导出  表 sanshan_main.ueditor_blog 结构
CREATE TABLE IF NOT EXISTS `ueditor_blog` (
  `id` bigint(11) NOT NULL,
  `user` varchar(20) CHARACTER SET utf8 NOT NULL DEFAULT '',
  `content` longtext CHARACTER SET utf8,
  `created` datetime NOT NULL,
  `updated` datetime NOT NULL,
  `time` datetime NOT NULL,
  `title` varchar(100) CHARACTER SET utf8 DEFAULT '',
  `tag` varchar(100) CHARACTER SET utf8 DEFAULT '',
  `extra` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `ueditor_title` (`title`),
  KEY `ueditor_user` (`user`),
  KEY `ueditor_tag` (`tag`),
  KEY `ueditor_time` (`time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- 数据导出被取消选择。
-- 导出  表 sanshan_main.ueditor_file_quote 结构
CREATE TABLE IF NOT EXISTS `ueditor_file_quote` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `filename` varchar(80) CHARACTER SET utf8 NOT NULL,
  `quote` int(11) NOT NULL,
  `created` datetime NOT NULL,
  `updated` datetime NOT NULL,
  `extra` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ueditor_filename` (`filename`),
  KEY `ueditor_file_quote` (`quote`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- 数据导出被取消选择。
-- 导出  表 sanshan_main.ueditor_id_file_map 结构
CREATE TABLE IF NOT EXISTS `ueditor_id_file_map` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `blog_id` bigint(20) NOT NULL,
  `filename` varchar(80) CHARACTER SET utf8 NOT NULL COMMENT '文件的名字 注意长度是80的字符  实际环境是64个字符',
  `created` datetime NOT NULL,
  `updated` datetime NOT NULL,
  `extra` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `ueditor_map_id` (`blog_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- 数据导出被取消选择。
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
