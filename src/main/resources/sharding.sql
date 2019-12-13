/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50726
Source Host           : localhost:3306
Source Database       : sharding

Target Server Type    : MYSQL
Target Server Version : 50726
File Encoding         : 65001

Date: 2019-12-13 17:37:38
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `car`
-- ----------------------------
DROP TABLE IF EXISTS `car`;
CREATE TABLE `car` (
  `id` bigint(20) NOT NULL,
  `number` varchar(16) COLLATE utf8mb4_bin NOT NULL,
  `brand` varchar(16) COLLATE utf8mb4_bin NOT NULL,
  `creat_time` datetime NOT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Records of car
-- ----------------------------

-- ----------------------------
-- Table structure for `car_201901`
-- ----------------------------
DROP TABLE IF EXISTS `car_201901`;
CREATE TABLE `car_201901` (
  `id` bigint(20) NOT NULL,
  `number` varchar(16) COLLATE utf8mb4_bin NOT NULL,
  `brand` varchar(16) COLLATE utf8mb4_bin NOT NULL,
  `creat_time` datetime NOT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Records of car_201901
-- ----------------------------

-- ----------------------------
-- Table structure for `car_201902`
-- ----------------------------
DROP TABLE IF EXISTS `car_201902`;
CREATE TABLE `car_201902` (
  `id` bigint(20) NOT NULL,
  `number` varchar(16) COLLATE utf8mb4_bin NOT NULL,
  `brand` varchar(16) COLLATE utf8mb4_bin NOT NULL,
  `creat_time` datetime NOT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Records of car_201902
-- ----------------------------

-- ----------------------------
-- Table structure for `car_201903`
-- ----------------------------
DROP TABLE IF EXISTS `car_201903`;
CREATE TABLE `car_201903` (
  `id` bigint(20) NOT NULL,
  `number` varchar(16) COLLATE utf8mb4_bin NOT NULL,
  `brand` varchar(16) COLLATE utf8mb4_bin NOT NULL,
  `creat_time` datetime NOT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Records of car_201903
-- ----------------------------

-- ----------------------------
-- Table structure for `car_201904`
-- ----------------------------
DROP TABLE IF EXISTS `car_201904`;
CREATE TABLE `car_201904` (
  `id` bigint(20) NOT NULL,
  `number` varchar(16) COLLATE utf8mb4_bin NOT NULL,
  `brand` varchar(16) COLLATE utf8mb4_bin NOT NULL,
  `creat_time` datetime NOT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Records of car_201904
-- ----------------------------

-- ----------------------------
-- Table structure for `car_201905`
-- ----------------------------
DROP TABLE IF EXISTS `car_201905`;
CREATE TABLE `car_201905` (
  `id` bigint(20) NOT NULL,
  `number` varchar(16) COLLATE utf8mb4_bin NOT NULL,
  `brand` varchar(16) COLLATE utf8mb4_bin NOT NULL,
  `creat_time` datetime NOT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Records of car_201905
-- ----------------------------

-- ----------------------------
-- Table structure for `car_201906`
-- ----------------------------
DROP TABLE IF EXISTS `car_201906`;
CREATE TABLE `car_201906` (
  `id` bigint(20) NOT NULL,
  `number` varchar(16) COLLATE utf8mb4_bin NOT NULL,
  `brand` varchar(16) COLLATE utf8mb4_bin NOT NULL,
  `creat_time` datetime NOT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Records of car_201906
-- ----------------------------

-- ----------------------------
-- Table structure for `car_201907`
-- ----------------------------
DROP TABLE IF EXISTS `car_201907`;
CREATE TABLE `car_201907` (
  `id` bigint(20) NOT NULL,
  `number` varchar(16) COLLATE utf8mb4_bin NOT NULL,
  `brand` varchar(16) COLLATE utf8mb4_bin NOT NULL,
  `creat_time` datetime NOT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Records of car_201907
-- ----------------------------

-- ----------------------------
-- Table structure for `car_201908`
-- ----------------------------
DROP TABLE IF EXISTS `car_201908`;
CREATE TABLE `car_201908` (
  `id` bigint(20) NOT NULL,
  `number` varchar(16) COLLATE utf8mb4_bin NOT NULL,
  `brand` varchar(16) COLLATE utf8mb4_bin NOT NULL,
  `creat_time` datetime NOT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Records of car_201908
-- ----------------------------

-- ----------------------------
-- Table structure for `car_201909`
-- ----------------------------
DROP TABLE IF EXISTS `car_201909`;
CREATE TABLE `car_201909` (
  `id` bigint(20) NOT NULL,
  `number` varchar(16) COLLATE utf8mb4_bin NOT NULL,
  `brand` varchar(16) COLLATE utf8mb4_bin NOT NULL,
  `creat_time` datetime NOT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Records of car_201909
-- ----------------------------

-- ----------------------------
-- Table structure for `car_201910`
-- ----------------------------
DROP TABLE IF EXISTS `car_201910`;
CREATE TABLE `car_201910` (
  `id` bigint(20) NOT NULL,
  `number` varchar(16) COLLATE utf8mb4_bin NOT NULL,
  `brand` varchar(16) COLLATE utf8mb4_bin NOT NULL,
  `creat_time` datetime NOT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Records of car_201910
-- ----------------------------

-- ----------------------------
-- Table structure for `car_201911`
-- ----------------------------
DROP TABLE IF EXISTS `car_201911`;
CREATE TABLE `car_201911` (
  `id` bigint(20) NOT NULL,
  `number` varchar(16) COLLATE utf8mb4_bin NOT NULL,
  `brand` varchar(16) COLLATE utf8mb4_bin NOT NULL,
  `creat_time` datetime NOT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Records of car_201911
-- ----------------------------

-- ----------------------------
-- Table structure for `car_201912`
-- ----------------------------
DROP TABLE IF EXISTS `car_201912`;
CREATE TABLE `car_201912` (
  `id` bigint(20) NOT NULL,
  `number` varchar(16) COLLATE utf8mb4_bin NOT NULL,
  `brand` varchar(16) COLLATE utf8mb4_bin NOT NULL,
  `creat_time` datetime NOT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Records of car_201912
-- ----------------------------

-- ----------------------------
-- Table structure for `user`
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint(20) NOT NULL,
  `name` varchar(16) COLLATE utf8mb4_bin DEFAULT NULL,
  `address` varchar(16) COLLATE utf8mb4_bin DEFAULT NULL,
  `tel` varchar(16) COLLATE utf8mb4_bin DEFAULT NULL,
  `email` varchar(32) COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Records of user
-- ----------------------------

-- ----------------------------
-- Table structure for `user_0`
-- ----------------------------
DROP TABLE IF EXISTS `user_0`;
CREATE TABLE `user_0` (
  `id` bigint(20) NOT NULL,
  `name` varchar(16) COLLATE utf8mb4_bin DEFAULT NULL,
  `address` varchar(16) COLLATE utf8mb4_bin DEFAULT NULL,
  `tel` varchar(16) COLLATE utf8mb4_bin DEFAULT NULL,
  `email` varchar(32) COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Records of user_0
-- ----------------------------

-- ----------------------------
-- Table structure for `user_1`
-- ----------------------------
DROP TABLE IF EXISTS `user_1`;
CREATE TABLE `user_1` (
  `id` bigint(20) NOT NULL,
  `name` varchar(16) COLLATE utf8mb4_bin DEFAULT NULL,
  `address` varchar(16) COLLATE utf8mb4_bin DEFAULT NULL,
  `tel` varchar(16) COLLATE utf8mb4_bin DEFAULT NULL,
  `email` varchar(32) COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Records of user_1
-- ----------------------------

-- ----------------------------
-- Table structure for `user_10`
-- ----------------------------
DROP TABLE IF EXISTS `user_10`;
CREATE TABLE `user_10` (
  `id` bigint(20) NOT NULL,
  `name` varchar(16) COLLATE utf8mb4_bin DEFAULT NULL,
  `address` varchar(16) COLLATE utf8mb4_bin DEFAULT NULL,
  `tel` varchar(16) COLLATE utf8mb4_bin DEFAULT NULL,
  `email` varchar(32) COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Records of user_10
-- ----------------------------

-- ----------------------------
-- Table structure for `user_11`
-- ----------------------------
DROP TABLE IF EXISTS `user_11`;
CREATE TABLE `user_11` (
  `id` bigint(20) NOT NULL,
  `name` varchar(16) COLLATE utf8mb4_bin DEFAULT NULL,
  `address` varchar(16) COLLATE utf8mb4_bin DEFAULT NULL,
  `tel` varchar(16) COLLATE utf8mb4_bin DEFAULT NULL,
  `email` varchar(32) COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Records of user_11
-- ----------------------------

-- ----------------------------
-- Table structure for `user_12`
-- ----------------------------
DROP TABLE IF EXISTS `user_12`;
CREATE TABLE `user_12` (
  `id` bigint(20) NOT NULL,
  `name` varchar(16) COLLATE utf8mb4_bin DEFAULT NULL,
  `address` varchar(16) COLLATE utf8mb4_bin DEFAULT NULL,
  `tel` varchar(16) COLLATE utf8mb4_bin DEFAULT NULL,
  `email` varchar(32) COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Records of user_12
-- ----------------------------

-- ----------------------------
-- Table structure for `user_13`
-- ----------------------------
DROP TABLE IF EXISTS `user_13`;
CREATE TABLE `user_13` (
  `id` bigint(20) NOT NULL,
  `name` varchar(16) COLLATE utf8mb4_bin DEFAULT NULL,
  `address` varchar(16) COLLATE utf8mb4_bin DEFAULT NULL,
  `tel` varchar(16) COLLATE utf8mb4_bin DEFAULT NULL,
  `email` varchar(32) COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Records of user_13
-- ----------------------------

-- ----------------------------
-- Table structure for `user_14`
-- ----------------------------
DROP TABLE IF EXISTS `user_14`;
CREATE TABLE `user_14` (
  `id` bigint(20) NOT NULL,
  `name` varchar(16) COLLATE utf8mb4_bin DEFAULT NULL,
  `address` varchar(16) COLLATE utf8mb4_bin DEFAULT NULL,
  `tel` varchar(16) COLLATE utf8mb4_bin DEFAULT NULL,
  `email` varchar(32) COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Records of user_14
-- ----------------------------

-- ----------------------------
-- Table structure for `user_15`
-- ----------------------------
DROP TABLE IF EXISTS `user_15`;
CREATE TABLE `user_15` (
  `id` bigint(20) NOT NULL,
  `name` varchar(16) COLLATE utf8mb4_bin DEFAULT NULL,
  `address` varchar(16) COLLATE utf8mb4_bin DEFAULT NULL,
  `tel` varchar(16) COLLATE utf8mb4_bin DEFAULT NULL,
  `email` varchar(32) COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Records of user_15
-- ----------------------------

-- ----------------------------
-- Table structure for `user_2`
-- ----------------------------
DROP TABLE IF EXISTS `user_2`;
CREATE TABLE `user_2` (
  `id` bigint(20) NOT NULL,
  `name` varchar(16) COLLATE utf8mb4_bin DEFAULT NULL,
  `address` varchar(16) COLLATE utf8mb4_bin DEFAULT NULL,
  `tel` varchar(16) COLLATE utf8mb4_bin DEFAULT NULL,
  `email` varchar(32) COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Records of user_2
-- ----------------------------

-- ----------------------------
-- Table structure for `user_3`
-- ----------------------------
DROP TABLE IF EXISTS `user_3`;
CREATE TABLE `user_3` (
  `id` bigint(20) NOT NULL,
  `name` varchar(16) COLLATE utf8mb4_bin DEFAULT NULL,
  `address` varchar(16) COLLATE utf8mb4_bin DEFAULT NULL,
  `tel` varchar(16) COLLATE utf8mb4_bin DEFAULT NULL,
  `email` varchar(32) COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Records of user_3
-- ----------------------------

-- ----------------------------
-- Table structure for `user_4`
-- ----------------------------
DROP TABLE IF EXISTS `user_4`;
CREATE TABLE `user_4` (
  `id` bigint(20) NOT NULL,
  `name` varchar(16) COLLATE utf8mb4_bin DEFAULT NULL,
  `address` varchar(16) COLLATE utf8mb4_bin DEFAULT NULL,
  `tel` varchar(16) COLLATE utf8mb4_bin DEFAULT NULL,
  `email` varchar(32) COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Records of user_4
-- ----------------------------

-- ----------------------------
-- Table structure for `user_5`
-- ----------------------------
DROP TABLE IF EXISTS `user_5`;
CREATE TABLE `user_5` (
  `id` bigint(20) NOT NULL,
  `name` varchar(16) COLLATE utf8mb4_bin DEFAULT NULL,
  `address` varchar(16) COLLATE utf8mb4_bin DEFAULT NULL,
  `tel` varchar(16) COLLATE utf8mb4_bin DEFAULT NULL,
  `email` varchar(32) COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Records of user_5
-- ----------------------------

-- ----------------------------
-- Table structure for `user_6`
-- ----------------------------
DROP TABLE IF EXISTS `user_6`;
CREATE TABLE `user_6` (
  `id` bigint(20) NOT NULL,
  `name` varchar(16) COLLATE utf8mb4_bin DEFAULT NULL,
  `address` varchar(16) COLLATE utf8mb4_bin DEFAULT NULL,
  `tel` varchar(16) COLLATE utf8mb4_bin DEFAULT NULL,
  `email` varchar(32) COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Records of user_6
-- ----------------------------

-- ----------------------------
-- Table structure for `user_7`
-- ----------------------------
DROP TABLE IF EXISTS `user_7`;
CREATE TABLE `user_7` (
  `id` bigint(20) NOT NULL,
  `name` varchar(16) COLLATE utf8mb4_bin DEFAULT NULL,
  `address` varchar(16) COLLATE utf8mb4_bin DEFAULT NULL,
  `tel` varchar(16) COLLATE utf8mb4_bin DEFAULT NULL,
  `email` varchar(32) COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Records of user_7
-- ----------------------------

-- ----------------------------
-- Table structure for `user_8`
-- ----------------------------
DROP TABLE IF EXISTS `user_8`;
CREATE TABLE `user_8` (
  `id` bigint(20) NOT NULL,
  `name` varchar(16) COLLATE utf8mb4_bin DEFAULT NULL,
  `address` varchar(16) COLLATE utf8mb4_bin DEFAULT NULL,
  `tel` varchar(16) COLLATE utf8mb4_bin DEFAULT NULL,
  `email` varchar(32) COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Records of user_8
-- ----------------------------

-- ----------------------------
-- Table structure for `user_9`
-- ----------------------------
DROP TABLE IF EXISTS `user_9`;
CREATE TABLE `user_9` (
  `id` bigint(20) NOT NULL,
  `name` varchar(16) COLLATE utf8mb4_bin DEFAULT NULL,
  `address` varchar(16) COLLATE utf8mb4_bin DEFAULT NULL,
  `tel` varchar(16) COLLATE utf8mb4_bin DEFAULT NULL,
  `email` varchar(32) COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Records of user_9
-- ----------------------------
