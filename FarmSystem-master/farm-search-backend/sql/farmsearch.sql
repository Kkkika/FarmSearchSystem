-- MySQL dump 10.13  Distrib 8.0.34, for Win64 (x86_64)
--
-- Host: localhost    Database: farmsearch
-- ------------------------------------------------------
-- Server version	8.0.34

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `admin`
--

DROP TABLE IF EXISTS `admin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `admin` (
  `admin_id` int NOT NULL AUTO_INCREMENT COMMENT '管理员编号',
  `admin_name` varchar(20) NOT NULL COMMENT '管理员登录名称',
  `admin_password` varchar(255) NOT NULL COMMENT '管理员登录密码（BCrypt加密）',
  `remarks` varchar(200) DEFAULT NULL COMMENT '备注',
  `role` varchar(20) NOT NULL DEFAULT 'NORMAL' COMMENT '管理员角色：ADMIN-高级管理员, NORMAL-普通管理员',
  PRIMARY KEY (`admin_id`),
  KEY `idx_admin_name` (`admin_name`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='系统管理员表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admin`
--

LOCK TABLES `admin` WRITE;
/*!40000 ALTER TABLE `admin` DISABLE KEYS */;
INSERT INTO `admin` VALUES (5,'admin','$2a$12$Vg4t1vgrhFnDAr2Q7hKoj.khj1Razs4a52peUEqiqcgw/BqKDhw0.','系统默认管理员','ADMIN');
/*!40000 ALTER TABLE `admin` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `city`
--

DROP TABLE IF EXISTS `city`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `city` (
  `city_id` int NOT NULL AUTO_INCREMENT COMMENT '市行政区编号',
  `city_name` varchar(10) NOT NULL COMMENT '市行政区名称',
  `prov_id` int NOT NULL COMMENT '所属省编号',
  `remarks` varchar(200) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`city_id`),
  KEY `idx_prov_id` (`prov_id`),
  KEY `idx_city_name` (`city_name`),
  CONSTRAINT `city_ibfk_1` FOREIGN KEY (`prov_id`) REFERENCES `province` (`prov_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='市行政区域信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `city`
--

LOCK TABLES `city` WRITE;
/*!40000 ALTER TABLE `city` DISABLE KEYS */;
INSERT INTO `city` VALUES (1,'北京市',1,'首都'),(2,'上海市',2,'经济中心'),(3,'广州市',3,'广东省会'),(4,'深圳市',3,'经济特区'),(5,'南京市',4,'江苏省会'),(6,'成都市',5,'四川省会');
/*!40000 ALTER TABLE `city` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `farm_batch`
--

DROP TABLE IF EXISTS `farm_batch`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `farm_batch` (
  `fb_id` int NOT NULL AUTO_INCREMENT COMMENT '编号（代替batch_id与farm_id的复合主键）',
  `batch_id` varchar(20) NOT NULL COMMENT '所属养殖企业产品批号',
  `farm_id` int NOT NULL COMMENT '所属养殖企业编号',
  `type` varchar(20) NOT NULL COMMENT '产品品种',
  `agc_id` varchar(30) NOT NULL COMMENT '动物检疫合格证编号',
  `test_name` varchar(20) NOT NULL COMMENT '官方检疫员名称',
  `batch_date` datetime DEFAULT NULL,
  `state` int NOT NULL DEFAULT '1' COMMENT '批号状态：1: 待发布；2: 已发布；3: 已下架',
  `remarks` varchar(200) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`fb_id`),
  KEY `idx_farm_id` (`farm_id`),
  KEY `idx_state` (`state`),
  KEY `idx_farm_state` (`farm_id`,`state`),
  KEY `idx_batch_id` (`batch_id`),
  CONSTRAINT `farm_batch_ibfk_1` FOREIGN KEY (`farm_id`) REFERENCES `node_info` (`node_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='养殖企业产品批号信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `farm_batch`
--

LOCK TABLES `farm_batch` WRITE;
/*!40000 ALTER TABLE `farm_batch` DISABLE KEYS */;
INSERT INTO `farm_batch` VALUES (1,'F202301001',1,'三黄鸡','AG202301001','检疫员A','2023-01-15 00:00:00',2,'首批出栏'),(2,'F202302001',1,'清远鸡','AG202302001','检疫员B','2023-02-20 00:00:00',2,'优质品种'),(3,'F202303001',5,'草鸡','AG202303001','检疫员C','2023-03-10 00:00:00',1,'待出栏'),(4,'F202304001',1,'乌鸡','AG202304001','检疫员A','2023-04-05 00:00:00',2,'药用价值高'),(5,'F202305001',5,'蛋鸡','AG202305001','检疫员D','2023-05-12 00:00:00',3,'已下架处理');
/*!40000 ALTER TABLE `farm_batch` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `node_info`
--

DROP TABLE IF EXISTS `node_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `node_info` (
  `node_id` int NOT NULL AUTO_INCREMENT COMMENT '节点企业编号',
  `code` varchar(20) NOT NULL COMMENT '登录编码',
  `password` varchar(255) NOT NULL COMMENT '登录密码（BCrypt加密）',
  `name` varchar(100) NOT NULL COMMENT '节点企业名称',
  `type` int NOT NULL COMMENT '企业类型：1: 养殖企业；2: 屠宰企业；3: 批发商；4: 零售商',
  `prov_id` int NOT NULL COMMENT '所在省编号',
  `city_id` int NOT NULL COMMENT '所在市编号',
  `address` varchar(200) NOT NULL COMMENT '节点企业地址',
  `business_id` varchar(30) NOT NULL COMMENT '营业执照代码（所有节点必填）',
  `ep_id` varchar(30) DEFAULT NULL COMMENT '动物防疫条件合格证编号（养殖企业必填）',
  `eia_id` varchar(30) DEFAULT NULL COMMENT '环境影响评价资质证书编号（屠宰、养殖企业必填）',
  `cir_id` varchar(30) DEFAULT NULL COMMENT '食品流通许可证编号（批发商必填）',
  `fb_id` varchar(30) DEFAULT NULL COMMENT '食品经营许可证编号（批发商、零售商必填）',
  `corporation` varchar(10) NOT NULL COMMENT '法定代表人',
  `telephone` varchar(20) NOT NULL COMMENT '联系电话',
  `reg_date` datetime DEFAULT NULL,
  `remarks` varchar(200) DEFAULT NULL COMMENT '备注',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '状态：0-正常，1-禁用',
  `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`node_id`),
  KEY `city_id` (`city_id`),
  KEY `idx_type` (`type`),
  KEY `idx_prov_city` (`prov_id`,`city_id`),
  KEY `idx_status` (`status`),
  KEY `idx_name` (`name`),
  CONSTRAINT `node_info_ibfk_1` FOREIGN KEY (`prov_id`) REFERENCES `province` (`prov_id`),
  CONSTRAINT `node_info_ibfk_2` FOREIGN KEY (`city_id`) REFERENCES `city` (`city_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='节点企业信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `node_info`
--

LOCK TABLES `node_info` WRITE;
/*!40000 ALTER TABLE `node_info` DISABLE KEYS */;
INSERT INTO `node_info` VALUES (1,'farm001','$2a$12$7DsKH2O9yHMDd0B3LwDmCO3ChHSqVNMhUNxwwOo5eLZdNOnOMTlz.','阳光养殖有限公司',1,3,3,'广州市天河区养殖路1号','91440101MA5ABC1234','GD12345678','EIA2023001',NULL,NULL,'张三','13800138001','2020-01-15 00:00:00','专业肉鸡养殖',0,NULL,'2025-09-29 19:43:26'),(2,'slau001','$2a$12$vS3Foc.lazAja1/Op6bd/ueQP/UY310UNZsS/vu0gYWX9F9lrGndW','鲜美屠宰加工厂',2,3,4,'深圳市宝安区屠宰路2号','91440300MA5DEF5678',NULL,'EIA2023002',NULL,NULL,'李四','13800138002','2020-03-20 00:00:00','现代化屠宰加工',0,NULL,'2025-09-29 19:43:26'),(3,'whol001','$2a$12$SVAwyNOSn3v4h71kdxBK5.ABHaCxJpSEjLHMqMs29t2r93nLu7CDa','华南批发市场',3,3,3,'广州市白云区批发路3号','91440101MA5GHI9012',NULL,NULL,'SP2023001','JY2023001','王五','13800138003','2020-05-10 00:00:00','大型肉类批发',0,NULL,'2025-09-29 19:43:26'),(4,'reta001','$2a$12$hBHhB/Ujo.gLvdLouh5GC.jRc.1ttKLv2QavAFt71o86WtEU6d0S6','惠民超市',4,3,4,'深圳市南山区零售路4号','91440300MA5JKL3456',NULL,NULL,NULL,'JY2023002','赵六','13800138004','2020-07-25 00:00:00','社区连锁超市',0,NULL,'2025-09-29 19:43:26'),(5,'farm002','$2a$12$h97eGDLuUoSWivv5.9JUWuuh.OBllk3c5vfq6L3p4xDlNfdWnLz56','绿色生态养殖场',1,4,5,'南京市江宁区生态路5号','91320101MA5MNO7890','JS87654321','EIA2023003',NULL,NULL,'钱七','13800138005','2020-09-30 00:00:00','有机养殖基地',0,NULL,'2025-09-29 19:43:26');
/*!40000 ALTER TABLE `node_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `province`
--

DROP TABLE IF EXISTS `province`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `province` (
  `prov_id` int NOT NULL AUTO_INCREMENT COMMENT '省行政区编号',
  `prov_name` varchar(10) NOT NULL COMMENT '省行政区名称',
  `remarks` varchar(200) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`prov_id`),
  KEY `idx_prov_name` (`prov_name`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='省行政区信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `province`
--

LOCK TABLES `province` WRITE;
/*!40000 ALTER TABLE `province` DISABLE KEYS */;
INSERT INTO `province` VALUES (1,'北京市','直辖市'),(2,'上海市','直辖市'),(3,'广东省','华南地区'),(4,'江苏省','华东地区'),(5,'四川省','西南地区');
/*!40000 ALTER TABLE `province` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reta_batch`
--

DROP TABLE IF EXISTS `reta_batch`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reta_batch` (
  `rb_id` int NOT NULL AUTO_INCREMENT COMMENT '编号（代替batch_id与reta_id的复合主键）',
  `batch_id` varchar(20) NOT NULL COMMENT '所属零售商产品批号',
  `reta_id` int NOT NULL COMMENT '所属零售商编号',
  `wb_id` int NOT NULL COMMENT '所属批发商进场编号',
  `type` varchar(20) NOT NULL COMMENT '产品品种',
  `batch_date` datetime DEFAULT NULL,
  `source_id` varchar(20) DEFAULT NULL COMMENT '溯源标识码',
  `source_qr` mediumtext COMMENT '溯源二维码',
  `state` int NOT NULL DEFAULT '1' COMMENT '批号状态：1: 新建；2: 待确认；3: 已确认；4: 已下架',
  `remarks` varchar(200) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`rb_id`),
  KEY `idx_reta_id` (`reta_id`),
  KEY `idx_wb_id` (`wb_id`),
  KEY `idx_state` (`state`),
  KEY `idx_source_id` (`source_id`),
  KEY `idx_reta_state` (`reta_id`,`state`),
  KEY `idx_batch_id` (`batch_id`),
  CONSTRAINT `reta_batch_ibfk_1` FOREIGN KEY (`reta_id`) REFERENCES `node_info` (`node_id`),
  CONSTRAINT `reta_batch_ibfk_2` FOREIGN KEY (`wb_id`) REFERENCES `whol_batch` (`wb_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='零售商产品批号信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reta_batch`
--

LOCK TABLES `reta_batch` WRITE;
/*!40000 ALTER TABLE `reta_batch` DISABLE KEYS */;
INSERT INTO `reta_batch` VALUES (1,'R202301001',4,1,'新鲜鸡肉','2023-01-18 00:00:00','TR400040929125668416','QR_CODE_001',3,'已确认'),(2,'R202302001',4,2,'优质鸡腿','2023-02-23 00:00:00','SRC202302001','QR_CODE_002',2,'热销中'),(3,'R202303001',4,2,'养生乌鸡','2023-04-08 00:00:00','SRC202303001','QR_CODE_003',2,'已确认'),(4,'R202304001',4,1,'生态草鸡','2023-03-13 00:00:00','SRC202304001','QR_CODE_004',2,'新建入库'),(5,'R202305001',4,2,'冷冻鸡翅','2023-01-19 00:00:00','SRC202305001','QR_CODE_005',4,'已下架'),(7,'2414',4,1,'2352','2025-09-28 12:57:29',NULL,NULL,4,'');
/*!40000 ALTER TABLE `reta_batch` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `slau_batch`
--

DROP TABLE IF EXISTS `slau_batch`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `slau_batch` (
  `sb_id` int NOT NULL AUTO_INCREMENT COMMENT '编号（代替batch_id与slau_id的复合主键）',
  `batch_id` varchar(20) NOT NULL COMMENT '所属屠宰企业产品批号',
  `slau_id` int NOT NULL COMMENT '所属屠宰企业编号',
  `fb_id` int NOT NULL COMMENT '所属养殖企业进场编号',
  `type` varchar(20) NOT NULL COMMENT '产品品种',
  `qua_id` varchar(30) NOT NULL COMMENT '肉类检验检疫合格证编号',
  `test_name` varchar(20) NOT NULL COMMENT '官方检验员名称',
  `batch_date` datetime DEFAULT NULL,
  `state` int NOT NULL DEFAULT '1' COMMENT '批号状态：1: 新建；2: 待确认；3: 已确认；4: 已下架',
  `remarks` varchar(200) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`sb_id`),
  KEY `idx_slau_id` (`slau_id`),
  KEY `idx_fb_id` (`fb_id`),
  KEY `idx_state` (`state`),
  KEY `idx_slau_state` (`slau_id`,`state`),
  KEY `idx_batch_id` (`batch_id`),
  CONSTRAINT `slau_batch_ibfk_1` FOREIGN KEY (`slau_id`) REFERENCES `node_info` (`node_id`),
  CONSTRAINT `slau_batch_ibfk_2` FOREIGN KEY (`fb_id`) REFERENCES `farm_batch` (`fb_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='屠宰企业产品批号信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `slau_batch`
--

LOCK TABLES `slau_batch` WRITE;
/*!40000 ALTER TABLE `slau_batch` DISABLE KEYS */;
INSERT INTO `slau_batch` VALUES (1,'S202301001',2,1,'白条鸡','QU202301001','检验员A','2023-01-16 00:00:00',3,'已完成加工'),(2,'S202302001',2,2,'分割鸡','QU202302001','检验员B','2023-02-21 00:00:00',3,'优质分割'),(3,'S202303001',2,4,'乌鸡','QU202303001','检验员C','2023-04-06 00:00:00',3,'已确认'),(4,'S202304001',2,2,'草鸡','QU202304001','检验员A','2023-03-11 00:00:00',1,'新建批次'),(5,'S202305001',2,1,'鸡翅','QU202305001','检验员D','2023-01-17 00:00:00',4,'已下架'),(7,'12314',2,1,'124','214','23525','2025-09-28 12:45:05',1,'');
/*!40000 ALTER TABLE `slau_batch` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `whol_batch`
--

DROP TABLE IF EXISTS `whol_batch`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `whol_batch` (
  `wb_id` int NOT NULL AUTO_INCREMENT COMMENT '编号（代替batch_id与whol_id的复合主键）',
  `batch_id` varchar(20) NOT NULL COMMENT '所属批发商产品批号',
  `whol_id` int NOT NULL COMMENT '所属批发商编号',
  `sb_id` int NOT NULL COMMENT '所属屠宰企业进场编号',
  `type` varchar(20) NOT NULL COMMENT '产品品种',
  `batch_date` datetime DEFAULT NULL,
  `state` int NOT NULL DEFAULT '1' COMMENT '批号状态：1: 新建；2: 待确认；3: 已确认；4: 已下架',
  `remarks` varchar(200) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`wb_id`),
  KEY `idx_whol_id` (`whol_id`),
  KEY `idx_sb_id` (`sb_id`),
  KEY `idx_state` (`state`),
  KEY `idx_whol_state` (`whol_id`,`state`),
  KEY `idx_batch_id` (`batch_id`),
  CONSTRAINT `whol_batch_ibfk_1` FOREIGN KEY (`whol_id`) REFERENCES `node_info` (`node_id`),
  CONSTRAINT `whol_batch_ibfk_2` FOREIGN KEY (`sb_id`) REFERENCES `slau_batch` (`sb_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='批发商产品批号信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `whol_batch`
--

LOCK TABLES `whol_batch` WRITE;
/*!40000 ALTER TABLE `whol_batch` DISABLE KEYS */;
INSERT INTO `whol_batch` VALUES (1,'W202301001',3,1,'白条鸡','2023-01-17 00:00:00',3,'已确认收货'),(2,'W202302001',3,2,'鸡腿','2023-02-22 00:00:00',3,'热销产品'),(3,'W202303001',3,3,'乌鸡','2023-04-07 00:00:00',2,'待确认'),(4,'W202304001',3,3,'草鸡','2023-03-12 00:00:00',2,'待确认'),(5,'W202305001',3,1,'鸡翅','2023-01-18 00:00:00',4,'已下架处理'),(7,'2141241',3,2,'121','2025-09-28 09:14:04',1,'');
/*!40000 ALTER TABLE `whol_batch` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-10-04  9:22:47
