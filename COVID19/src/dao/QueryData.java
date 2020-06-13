package dao;

import domain.*;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import utils.JDBCUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class QueryData {
    JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    public List<Country> quaryCountry() {
        String sql = "SELECT * from country;";
        List<Country> countryList = template.query(sql, new BeanPropertyRowMapper<Country>(Country.class));

        return countryList;
    }

    public List<News> quaryNews(){
        String sql = "SELECT * from news;";
        List<News> newsList = template.query(sql, new BeanPropertyRowMapper<News>(News.class));
        return newsList;
    }



    public List<HeatMap> quaryHeat(){
        String sql = "select * from cities;";
        List<HeatMap> heatMaps = template.query(sql, new BeanPropertyRowMapper<HeatMap>(HeatMap.class));
        //根据cities的name遍历表cities_gps;
        for (HeatMap h:heatMaps) {
            String name = h.getName();
            String  s ="select * from cities_gps where name = '" + name + "';";
            String  s1 = "select count(*) from cities_gps where name = '" + name + "';";
            int count = template.queryForObject(s1, Integer.class);
            if (count == 1){
                CitiesGps gps = template.queryForObject(s, new BeanPropertyRowMapper<CitiesGps>(CitiesGps.class));
                h.setLatitude(gps.getLatitude());
                h.setLongitude(gps.getLongitude());
            }
        }
        return heatMaps;
    }


    /*根据3种请求类型（1，2，3），和时间参数。请求各个城市数据*/
    //根据请求时间和类型返回值。(郑)
//    public List<Features> quaryType(String time, String num) {
//        //获取数据库中相关数据
//        String sql = "select * from cities where time = ? ;";
//        List<HeatMap> heatMaps = template.query(sql, new BeanPropertyRowMapper<HeatMap>(HeatMap.class),time);
//
//        //根据cities的name遍历表cities_gps并存入到feature对象中;
//
//        List<Features> featuresList = new ArrayList<>();
//
//        for (HeatMap h:heatMaps) {
//
//            Features f = new Features();
//            Coordinates coordinates = new Coordinates();
//
//            String name = h.getName();
//            String  s ="select * from cities_gps where name = '" + name + "';";
//            String  s1 = "select count(*) from cities_gps where name = '" + name + "';";
//            //根据cities的name来查询坐标
//            int count = template.queryForObject(s1, Integer.class);
//            if (count == 1){
//                //存入GPS数据
//                coordinates = template.queryForObject(s, new BeanPropertyRowMapper<Coordinates>(Coordinates.class));
//                //根据请求类型计算第三个可变值
//                switch (num){
//                    case 1+"":
//                        int confirm = h.getConfirm();
//                        coordinates.setPeople(confirm);
//                        break;
//                    case 2+"":
//                        int confirm_today = h.getConfirm_today();
//                        coordinates.setPeople(confirm_today);
//                        break;
//                    case 3+"":
//                        int i = h.getConfirm() - h.getCured() -h.getDead();
//                        coordinates.setPeople(i);
//                        break;
//                }
//
//            }
//            //设置对象的值，并封装到集合中返回
//            f.setType("Point");
//            f.setCoordinates(coordinates);
//            featuresList.add(f);
//        }
//        return featuresList;
//    }

    /*根据3种请求类型（1，2，3），和时间参数。请求各个城市数据*/
    //根据请求时间和类型返回值。(郑)
    public List<Features> quaryType(String time, String num) {
        //获取数据库中相关数据
        String sql = "select id,name,confirm,cured,dead from cities where time = ? ;";
        List<HeatMap> heatMaps = template.query(sql, new BeanPropertyRowMapper<HeatMap>(HeatMap.class),time);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //根据cities的name遍历表cities_gps并存入到feature对象中;

        List<Features> featuresList = new ArrayList<>();

        for (HeatMap h:heatMaps) {

            Features f = new Features();
            Coordinates coordinates = new Coordinates();

            String name = h.getName();
            String  s ="select * from cities_gps where name = '" + name + "';";
            String  s1 = "select count(*) from cities_gps where name = '" + name + "';";
            //根据cities的name来查询坐标
            int count = template.queryForObject(s1, Integer.class);
            if (count == 1){
                //存入GPS数据
                coordinates = template.queryForObject(s, new BeanPropertyRowMapper<Coordinates>(Coordinates.class));
                //根据请求类型计算第三个可变值
                switch (num){
                    case 1+"":
                        int confirm = h.getConfirm();
                        coordinates.setPeople(confirm);
                        break;
                    case 2+"":
                        //将当前时间减去1天
                        Date beforeTime = null;
                        try {
                            beforeTime = sdf.parse(time);

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        Calendar cd = Calendar.getInstance();
                        cd.setTime(beforeTime);
                        cd.add(Calendar.DATE,-1);
                        Date time2 = cd.getTime();
                        String time_new = sdf.format(time2);
                        //查询头一天的confirm数据
                        String s2 = "select confirm from cities where time = ? and name = ?;";
                        String s3 = "select count(*) from cities where time = ? and name = ?;";
                        int c = template.queryForObject(s3, Integer.class,time_new,name);
                        if (c==1){
                            int confirm_before = template.queryForObject(s2, Integer.class,time_new,name);
                            int confirm_today = h.getConfirm()-confirm_before;

                            coordinates.setPeople(confirm_today);
                        }


                        break;
                    case 3+"":
                        int i = h.getConfirm() - h.getCured() -h.getDead();
                        coordinates.setPeople(i);
                        break;
                }

            }
            //设置对象的值，并封装到集合中返回
            f.setType("Point");
            f.setCoordinates(coordinates);
            featuresList.add(f);
        }
        return featuresList;
    }

    /*根据省、时间字段，请求对应城市数据*/
    public List<Cities> quaryCities(String province, String date) {
        String sql = "select * from cities where province = ? and time = ? ;";
        List<Cities> cities = template.query(sql, new BeanPropertyRowMapper<>(Cities.class), province, date);
        return cities;
    }





    //武汉社区查询
    public List<Community_Wuhan> quaryCommunity(double lon,double lat) {
        String sql = "select * from china_wuhan_community where date = '2020-02-14';";
        List<Community_Wuhan> community = template.query(sql, new BeanPropertyRowMapper<>(Community_Wuhan.class));
        String sql2 = "SELECT district,longitude,latitude FROM china_wuhan_community  WHERE  longitude <= ? AND longitude >=?  AND latitude <= ? AND latitude >= ? AND DATE = '2020-02-14'  ;";
        List<CommunityName> communityNameList = template.query(sql2, new BeanPropertyRowMapper<>(CommunityName.class), lon + 0.03, lon - 0.03, lat + 0.03, lat - 0.03);
        for (CommunityName c:communityNameList) {
            //遍历社区，存储距离目标的distance
            double lon1 = c.getLongitude();
            double lat1 = c.getLatitude();
            double distance = getDistance(lon,lat,lon1,lat1);
           // c.setDistance(distance);
            //将查询到的社区封装起来
            Community_Wuhan wuhan = new Community_Wuhan();
            wuhan.setDistrict(c.getDistrict());
            wuhan.setLatitude((float) distance);
            community.add(wuhan);

        }
        return community;
    }

   private  double rad(double d) {
        return d * Math.PI / 180.0;
    }
    private double getDistance(double lon, double lat, double lon1, double lat1) {
            double radLat1 = rad(lat1);
             double radLat = rad(lat);
            double a = radLat1 - radLat;
              double b = rad(lon1) - rad(lon);
             double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)+ Math.cos(radLat1) * Math.cos(radLat)* Math.pow(Math.sin(b / 2), 2)));
            s = s * 6378.137;
            s = Math.round(s * 10000d) / 10000d;
            s = s * 1000;
             return s;

    }

    //取消
    public List<CommunityName> quaryCommunity_name(double lon,double lat) {

        String sql = "SELECT district FROM china_wuhan_community  WHERE  longitude <= ? AND longitude >=?  AND latitude <= ? AND latitude >= ? AND DATE = '2020-02-14'  ;";
        List<CommunityName> name = template.query(sql, new BeanPropertyRowMapper<>(CommunityName.class),lon+0.1,lon-0.1,lat+0.1,lat-0.1);

        return name;
    }

    //根据省份名称获取该省每天的数据变化
    public List<Province_Today> quaryProvinceToday(String province) {
        String sql = "SELECT province,time ,SUM(confirm_sum) as confirm_sum ,SUM(cured_sum) as cured_sum,SUM(dead_sum) as dead_sum " +
                "from china_city  WHERE province= ? GROUP BY time ORDER BY time ASC;";
        List<Province_Today> province_todays = template.query(sql, new BeanPropertyRowMapper<>(Province_Today.class), province);
        //再将新增数据，封装进去
        int confirm_yesterday = 0;
        int cured_yesterday = 0;
        int dead_yesterday = 0;
        for (Province_Today p : province_todays) {
            //设置现有确诊
            p.setConfirm_now(p.getConfirm_sum()-p.getDead_sum()-p.getCured_sum());
            p.setConfirm_add(p.getConfirm_sum()-confirm_yesterday);
            p.setCured_add(p.getCured_sum()-cured_yesterday);
            p.setDead_add(p.getDead_sum()-dead_yesterday);

            confirm_yesterday = p.getConfirm_sum();
            cured_yesterday = p.getCured_sum();
            dead_yesterday = p.getDead_sum();

        }
        //手动修改第一个数据的新增数
        Province_Today province_first = province_todays.get(0);
        province_first.setConfirm_add(0);
        province_first.setCured_add(0);
        province_first.setDead_add(0);
        province_todays.set(0,province_first);
        //返回查询结果
        return province_todays;
    }

    /**
     * 根据城市名，展示每天的数据。
     * */
    public List<Cities_Today> quaryCitiesToday(String citie) {
        String sql = "SELECT name ,time ,confirm,cured , dead from cities where  name =? ORDER BY time ASC;";
        List<Cities_Today> cities_todays = template.query(sql, new BeanPropertyRowMapper<>(Cities_Today.class), citie);
        //再将新增数据，封装进去
        int confirm_yesterday = 0;
        int cured_yesterday = 0;
        int dead_yesterday = 0;
        for (Cities_Today p : cities_todays) {
            p.setConfirm_today(p.getConfirm()-confirm_yesterday);
            p.setCured_today(p.getCured()-cured_yesterday);
            p.setDead_today(p.getDead()-dead_yesterday);

            confirm_yesterday = p.getConfirm();
            cured_yesterday = p.getCured();
            dead_yesterday = p.getDead();

        }
        //手动修改第一个数据的新增数
        Cities_Today cities_first = cities_todays.get(0);
        cities_first.setConfirm_today(0);
        cities_first.setCured_today(0);
        cities_first.setDead_today(0);
        cities_todays.set(0,cities_first);
        //返回查询结果
        return cities_todays;
    }



}
