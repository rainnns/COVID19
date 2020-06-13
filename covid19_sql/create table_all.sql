CREATE DATABASE covid19 ;
-- 1创建全球country表
USE covid19;
CREATE TABLE country(
	id INT PRIMARY KEY AUTO_INCREMENT,
	NAME VARCHAR(255) NOT NULL,
	TIME DATE NOT NULL,
	confirm_sum INT  NOT NULL,
	suspect_sum INT ,
	cured_sum INT NOT NULL,
	dead_sum INT NOT NULL,
	confirm_add INT ,
	suspect_add INT ,
	cured_add INT ,
	dead_add INT 
);

-- 2创建国家gps表
CREATE TABLE country_gps(
	id INT PRIMARY KEY AUTO_INCREMENT,
	NAME VARCHAR(255) NOT NULL,
	longitude FLOAT NOT NULL,
	latitude FLOAT NOT NULL
);

-- 3创建中国城市表
CREATE TABLE china_city(
	id INT PRIMARY KEY AUTO_INCREMENT,
	NAME VARCHAR(255) NOT NULL,
	name_english VARCHAR(255),
	province VARCHAR(255) NOT NULL,
	country VARCHAR(255),
	TIME DATE NOT NULL,
	confirm_sum INT  NOT NULL,
	suspect_sum INT ,
	cured_sum INT NOT NULL,
	dead_sum INT NOT NULL,
	confirm_add INT ,
	suspect_add INT ,
	cured_add INT ,
	dead_add INT 
);

-- 4创建中国城市gps表
CREATE TABLE china_city_gps(
	id INT PRIMARY KEY AUTO_INCREMENT,
	NAME VARCHAR(255) NOT NULL,
	longitude FLOAT NOT NULL,
	latitude FLOAT NOT NULL
);

-- 5创建外国城市表
CREATE TABLE foreign_city(
	id INT PRIMARY KEY AUTO_INCREMENT,
	NAME VARCHAR(255) NOT NULL,
	name_english VARCHAR(255),
	province VARCHAR(255) ,
	country VARCHAR(255) NOT NULL,
	TIME DATE NOT NULL,
	confirm_sum INT  NOT NULL,
	suspect_sum INT ,
	cured_sum INT NOT NULL,
	dead_sum INT NOT NULL,
	confirm_add INT ,
	suspect_add INT ,
	cured_add INT ,
	dead_add INT 
);

-- 6创建外国城市gps表
CREATE TABLE foreign_city_gps(
	id INT PRIMARY KEY AUTO_INCREMENT,
	NAME VARCHAR(255) NOT NULL,
	longitude FLOAT NOT NULL,
	latitude FLOAT NOT NULL
);

-- 7创建新闻表
CREATE TABLE news(
	id INT PRIMARY KEY AUTO_INCREMENT,
	title VARCHAR(255) NOT NULL,
	newsurl VARCHAR(255) NOT NULL
);

SHOW TABLES;

-- 8.创建china社区表

CREATE TABLE `china_community` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `province` VARCHAR(45) DEFAULT NULL,
  `city` VARCHAR(45) DEFAULT NULL,
  `county` VARCHAR(45) DEFAULT NULL,
  `district` VARCHAR(1000) DEFAULT NULL,
  `full_address` VARCHAR(2083) DEFAULT NULL,
  `longitude` FLOAT DEFAULT NULL,
  `latitude` FLOAT DEFAULT NULL,
  `description` VARCHAR(1000) DEFAULT NULL,
  `detail_url` VARCHAR(2083) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=INNODB AUTO_INCREMENT=7821 DEFAULT CHARSET=utf8mb4;

-- 9.创建武汉社区表
ALTER TABLE wuhan_community RENAME TO china_wuhan_community ;
CREATE TABLE `china_wuhan_community` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `district` VARCHAR(45) DEFAULT NULL,
  `confirm` INT(11) DEFAULT NULL,
  `suspect` INT(11) DEFAULT NULL,
  `date` DATE DEFAULT NULL,
  `position` VARCHAR(45) DEFAULT NULL,
  `longitude` FLOAT DEFAULT NULL,
  `latitude` FLOAT DEFAULT NULL,
  `full_address` VARCHAR(1024) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=INNODB AUTO_INCREMENT=792 DEFAULT CHARSET=utf8mb4;