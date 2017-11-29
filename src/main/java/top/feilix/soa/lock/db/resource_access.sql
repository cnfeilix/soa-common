/*
Navicat MySQL Data Transfer

Source Server         : 开发库-10.10.0.104
Source Server Version : 50717
Source Host           : 10.10.0.104:3306
Source Database       : finance

Target Server Type    : MYSQL
Target Server Version : 50717
File Encoding         : 65001

Date: 2017-11-29 18:16:01
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `resource_access`
-- ----------------------------
DROP TABLE IF EXISTS `resource_access`;
CREATE TABLE `resource_access` (
  `id` bigint(20) NOT NULL,
  `resource` varchar(128) NOT NULL,
  `state` tinyint(4) NOT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `version` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `resource` (`resource`) USING HASH
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资源访问并发控制表（乐观锁）';

-- ----------------------------
-- Records of resource_access
-- ----------------------------
INSERT INTO `resource_access` VALUES ('1', 'CopTaskService.copInvokeHxyq', '0', '2017-10-31 19:04:30', '2017-11-20 17:59:39', '825');
INSERT INTO `resource_access` VALUES ('2', 'CopSendMoneyService.updateAutoBankTransfer', '0', '2017-10-31 19:05:12', '2017-11-21 10:06:20', '18');
