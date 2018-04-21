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


-- 导出 sanshan_auth 的数据库结构
CREATE DATABASE IF NOT EXISTS `sanshan_auth` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `sanshan_auth`;

-- 导出  表 sanshan_auth.auth_client 结构
CREATE TABLE IF NOT EXISTS `auth_client` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) DEFAULT NULL COMMENT '服务编码',
  `secret` varchar(255) DEFAULT NULL COMMENT '服务密钥',
  `name` varchar(255) DEFAULT NULL COMMENT '服务名',
  `locked` char(1) DEFAULT NULL COMMENT '是否锁定',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `crt_time` datetime DEFAULT NULL COMMENT '创建时间',
  `crt_user` varchar(255) DEFAULT NULL COMMENT '创建人',
  `crt_name` varchar(255) DEFAULT NULL COMMENT '创建人姓名',
  `crt_host` varchar(255) DEFAULT NULL COMMENT '创建主机',
  `upd_time` datetime DEFAULT NULL COMMENT '更新时间',
  `upd_user` varchar(255) DEFAULT NULL COMMENT '更新人',
  `upd_name` varchar(255) DEFAULT NULL COMMENT '更新姓名',
  `upd_host` varchar(255) DEFAULT NULL COMMENT '更新主机',
  `attr1` varchar(255) DEFAULT NULL,
  `attr2` varchar(255) DEFAULT NULL,
  `attr3` varchar(255) DEFAULT NULL,
  `attr4` varchar(255) DEFAULT NULL,
  `attr5` varchar(255) DEFAULT NULL,
  `attr6` varchar(255) DEFAULT NULL,
  `attr7` varchar(255) DEFAULT NULL,
  `attr8` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4;

-- 正在导出表  sanshan_auth.auth_client 的数据：~4 rows (大约)
/*!40000 ALTER TABLE `auth_client` DISABLE KEYS */;
INSERT INTO `auth_client` (`id`, `code`, `secret`, `name`, `locked`, `description`, `crt_time`, `crt_user`, `crt_name`, `crt_host`, `upd_time`, `upd_user`, `upd_name`, `upd_host`, `attr1`, `attr2`, `attr3`, `attr4`, `attr5`, `attr6`, `attr7`, `attr8`) VALUES
	(1, 'sanshan-auth', '123456', 'sanshan-auth', '0', NULL, NULL, NULL, NULL, NULL, '2018-04-21 14:35:03', '1', '管理员', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
	(3, 'sanshan-search', '123456', 'sanshan-search', '0', NULL, NULL, NULL, NULL, NULL, '2018-04-21 14:35:05', '', ' ', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
	(6, 'sanshan-main', '123456', 'sanshan-main', '0', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
	(8, 'sanshan-gate', '123456', 'sanshan-gate', '0', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
/*!40000 ALTER TABLE `auth_client` ENABLE KEYS */;

-- 导出  表 sanshan_auth.auth_client_service 结构
CREATE TABLE IF NOT EXISTS `auth_client_service` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `service_id` varchar(255) DEFAULT NULL,
  `client_id` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `crt_time` datetime DEFAULT NULL,
  `crt_user` varchar(255) DEFAULT NULL,
  `crt_name` varchar(255) DEFAULT NULL,
  `crt_host` varchar(255) DEFAULT NULL,
  `attr1` varchar(255) DEFAULT NULL,
  `attr2` varchar(255) DEFAULT NULL,
  `attr3` varchar(255) DEFAULT NULL,
  `attr4` varchar(255) DEFAULT NULL,
  `attr5` varchar(255) DEFAULT NULL,
  `attr6` varchar(255) DEFAULT NULL,
  `attr7` varchar(255) DEFAULT NULL,
  `attr8` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4;

-- 正在导出表  sanshan_auth.auth_client_service 的数据：~0 rows (大约)
/*!40000 ALTER TABLE `auth_client_service` DISABLE KEYS */;
INSERT INTO `auth_client_service` (`id`, `service_id`, `client_id`, `description`, `crt_time`, `crt_user`, `crt_name`, `crt_host`, `attr1`, `attr2`, `attr3`, `attr4`, `attr5`, `attr6`, `attr7`, `attr8`) VALUES
	(1, '1', '3', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
	(2, '1', '6', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
	(3, '1', '8', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
	(4, '3', '1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
	(5, '3', '6', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
	(6, '3', '8', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
	(7, '6', '1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
	(8, '6', '3', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
	(9, '6', '8', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
	(10, '8', '1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
	(11, '8', '3', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
	(12, '8', '6', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
/*!40000 ALTER TABLE `auth_client_service` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
