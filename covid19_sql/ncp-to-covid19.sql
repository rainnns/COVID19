/*一.cities迁移至china_city*/
-- 删除原表无用记录
SELECT COUNT(*) FROM ncp.cities WHERE province IS NULL OR NAME IS NULL OR confirm IS NULL OR cured IS NULL OR dead IS NULL OR TIME IS NULL;
DELETE FROM ncp.`cities` WHERE province IS NULL OR NAME IS NULL OR confirm IS NULL OR cured IS NULL OR dead IS NULL OR TIME IS NULL;
-- 记录转移至新表
INSERT INTO covid19.china_city(province , NAME ,confirm_sum ,cured_sum ,dead_sum ,TIME) SELECT province,NAME,confirm,cured,dead,TIME FROM ncp.cities;
SELECT * FROM covid19.china_city;

/*二.cities_gps 迁移至covid19.china_city_gps*/
INSERT INTO covid19.china_city_gps ( NAME ,longitude ,latitude) SELECT NAME,longitude,latitude FROM ncp.cities_gps;
SELECT * FROM covid19.china_city_gps;

/*三.迁移国家的gps数据*/
DELETE  FROM covid19.country_gps;
INSERT INTO covid19.country_gps (NAME ,longitude,latitude) SELECT country_name,longitude,latitude FROM ncp.`countries_gps`;
SELECT * FROM covid19.country_gps;

/*四.将中国表和世界各国表合并到covid19.country中*/
SELECT * FROM covid19.country;
-- 将中国数据合并到新表covid19.country中
INSERT INTO covid19.country (NAME ,TIME ,confirm_sum,suspect_sum,cured_sum,dead_sum,confirm_add,suspect_add,cured_add,dead_add) SELECT NAME,TIME,confirm_sum,
suspect_sum,cured_sum,dead_sum,confirm_today,suspect_today,cured_today,dead_today FROM ncp.country;
-- 将世界各国的数据合并到新表covid19.country中
INSERT INTO covid19.country ( NAME,TIME,confirm_add,confirm_sum,dead_sum,cured_sum) SELECT  NAME,DATE,confirmAdd ,confirm ,dead ,cured FROM ncp.global_country;

/* 五.将世界各国的城市表迁移至foreign_city表中*/
-- 迁移历史数据
INSERT INTO covid19.foreign_city (NAME,TIME,name_english,country,confirm_add,confirm_sum,suspect_sum,dead_sum,cured_sum) 
SELECT city_name ,city_date,nameMap,country_name,city_confirmAdd,city_confirm,city_suspect,city_dead,city_cured FROM ncp.global_city;
SELECT * FROM covid19.foreign_city;

/*六.迁移新闻数据*/
INSERT INTO covid19.news SELECT * FROM ncp.news; #完全复制，字段同
SELECT * FROM covid19.news; #查询结果

/*七.迁移中国社区数据*/
INSERT INTO covid19.china_community SELECT * FROM ncp.community_country;

/*八.迁移武汉社区数据*/
INSERT INTO covid19.china_wuhan_community SELECT * FROM ncp.community_wuhan;

/*九.迁移海外总数据global-->foreign_total*/
INSERT INTO covid19.foreign_total(TIME ,confirm_sum,cured_sum,dead_sum,confirm_add,cured_add,dead_add) 
SELECT TIME ,confirm,cured,dead,confirmAdd,curedAdd,deadAdd FROM ncp.global;