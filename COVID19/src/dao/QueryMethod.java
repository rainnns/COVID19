package dao;

import domain.*;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import utils.JDBCUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class QueryMethod {
    JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    /*1请求全球各国数据*/
    public List<GlobalCountry_19> quaryGlobalCountry(String time) {
        //全球国家数据
        String sql = "SELECT NAME ,TIME ,confirm_sum,confirm_add,confirm_sum-cured_sum-dead_sum AS confirm_now FROM country WHERE TIME = ? order by confirm_sum DESC ;";
        List<GlobalCountry_19> globalCountryList = template.query(sql, new BeanPropertyRowMapper<>(GlobalCountry_19.class), time);
        return globalCountryList;
    }

    /**
     * 2。根据时间：请求中国城市数据和世界国家数据以及经纬度
     */
    public List<Global_Country> quaryGlobalCountry_gps(String date) {
        String sql = "SELECT country.name , country.confirm_sum ,country.cured_sum ,country.dead_sum ,country_gps.longitude, " +
                "country_gps.latitude FROM country INNER JOIN country_gps ON country.name=country_gps.name  WHERE time = ? order by country.confirm_sum DESC;";

        List<Global_Country> global_countries = template.query(sql, new BeanPropertyRowMapper<>(Global_Country.class), date);
        return global_countries;
    }

    //返回城市数据和gps信息
    public List<Cities2> quaryCities2(String date) {
        //将省级数据先封装起来
        String sql = "SELECT province ,SUM(confirm_sum) as confirm_sum ,SUM(cured_sum) as cured_sum,SUM(dead_sum) as dead_sum from china_city WHERE time= ? GROUP BY province ORDER BY confirm_sum Desc;";
        List<Cities2> cities2 = template.query(sql, new BeanPropertyRowMapper<>(Cities2.class), date);
        //根据省级数据，遍历sql,获取城市数据，
        for (Cities2 c : cities2) {
            String sql2 = "select china_city.name , china_city.confirm_sum  ,china_city.cured_sum,china_city.dead_sum,china_city_gps.latitude," +
                    "china_city_gps.longitude from china_city  INNER join china_city_gps on china_city.name=china_city_gps.name where time = ? and  province=? order by china_city.confirm_sum DESC ;";

            c.setCities(template.query(sql2, new BeanPropertyRowMapper<>(Cities2_1.class), date, c.getProvince()));
        }
        return cities2;
    }


    /*3.全球总计和中国总计*/
    public List<Total> quaryGlobalTotal(String date) {
        List<Total> total = new ArrayList<>(); //统计集合
        String sql = "select * from country where time = ? and name = '中国';";
        Total china = template.queryForObject(sql, new BeanPropertyRowMapper<>(Total.class), date);
        total.add(china);  //中国数据

        String s = "select count(*) from foreign_total where time = ? ;";
        Integer count = template.queryForObject(s, Integer.class, date);
        if (count == 1){
            String foreign = "select * from foreign_total where time = ? ;";
            Total foreignTotal = template.queryForObject(foreign, new BeanPropertyRowMapper<>(Total.class), date);  //国外数据

            Total global = new Total();
            global.setName("全球");
            global.setConfirm_sum(china.getConfirm_sum() + foreignTotal.getConfirm_sum());
            global.setCured_sum(china.getCured_sum() + foreignTotal.getCured_sum());
            global.setDead_sum(china.getDead_sum() + foreignTotal.getDead_sum());
            total.add(global);   //全球总数
        }else
            total.add(null);

        return total;

    }

    //4国外疫情8大指标
    public ForeignTotal quaryForeignTotal(String date) {
        String s = "select count(*) from foreign_total where time = ?";
        Integer count = template.queryForObject(s, Integer.class, date);
        if (count == 1){
            String sql = "select * from foreign_total where time = ?";
            ForeignTotal foreignTotal = template.queryForObject(sql, new BeanPropertyRowMapper<>(ForeignTotal.class), date);
            foreignTotal.setName("海外疫情");
            foreignTotal.setConfirm_now(foreignTotal.getConfirm_sum() - foreignTotal.getCured_sum() - foreignTotal.getDead_sum());
            foreignTotal.setConfirm_now_add(foreignTotal.getConfirm_add() - foreignTotal.getCured_add() - foreignTotal.getDead_add());
            return foreignTotal;
        }else
            return null;


    }

    //5中国疫情数据8大指标
    public ForeignTotal quaryChinaTotal(String date) {
        String sql = "select * from country where time = ? and name = ?";
        ForeignTotal china = template.queryForObject(sql, new BeanPropertyRowMapper<>(ForeignTotal.class), date, "中国");
        //补充现有确诊和新增现有确诊
        china.setConfirm_now(china.getConfirm_sum() - china.getCured_sum() - china.getDead_sum());
        china.setConfirm_now_add(china.getConfirm_add() - china.getCured_add() - china.getDead_add());
        return china;
    }

    /*6.根据时间，请求省级数据*/
    public List<GlobalCountry_19> quaryProvince(String date) {
        //聚合查询+分组查询
        String sql = "SELECT province as name,time,SUM(confirm_sum) as confirm_sum ,SUM(confirm_sum) -SUM(cured_sum) -SUM(dead_sum) as confirm_now from china_city WHERE time= ? GROUP BY province order by confirm_sum Desc;";
        List<GlobalCountry_19> provinces = template.query(sql, new BeanPropertyRowMapper<>(GlobalCountry_19.class), date);

        //接下来做差求confirm_add
        //1.将当前时间减去1天
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date beforeTime = null;
        try {
            beforeTime = sdf.parse(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cd = Calendar.getInstance();
        cd.setTime(beforeTime);
        cd.add(Calendar.DATE, -1);
        Date time2 = cd.getTime();
        String time_new = sdf.format(time2); //天数减1
        for (GlobalCountry_19 p : provinces) {
            String name = p.getName();
            //2.计算头一天的confirm_sum
            String sql1 = "select count(*) from china_city where time = ? and province =? ";
            Integer count = template.queryForObject(sql1, Integer.class, time_new, name);
            if (count == 1) {
                String sql2 = "select SUM(confirm_sum) AS confirm_yesterday from china_city where time = ? and province =? ";
                Integer confirm_before = template.queryForObject(sql2, Integer.class, time_new, name);
                p.setConfirm_add(p.getConfirm_sum() - confirm_before);
            } else
                p.setConfirm_add(0);


        }
        return provinces;

    }

    public List<ChinaCity> quaryChinaCity(String time) {
        String sql = "SELECT province ,NAME, TIME ,confirm_sum ,confirm_sum-cured_sum-dead_sum AS confirm_now FROM china_city WHERE TIME = ?;";
        List<ChinaCity> chinaCities = template.query(sql, new BeanPropertyRowMapper<>(ChinaCity.class), time);

        //接下来做差求confirm_add
        //1.将当前时间减去1天
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date beforeTime = null;
        try {
            beforeTime = sdf.parse(time);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cd = Calendar.getInstance();
        cd.setTime(beforeTime);
        cd.add(Calendar.DATE, -1);
        Date time2 = cd.getTime();
        String time_new = sdf.format(time2); //天数减1

        //找到每个城市的头一天confirm_sum
        for (ChinaCity c : chinaCities) {
            String name = c.getName();
            String sql1 = "select count(*)  from china_city where time = ? and NAME =? ";
            Integer count = template.queryForObject(sql1, Integer.class, time_new, name);
            if (count == 1) {
                String sql2 = "select confirm_sum  from china_city where time = ? and NAME =? ";
                Integer confirm_before = template.queryForObject(sql2, Integer.class, time_new, name);
                c.setConfirm_add(c.getConfirm_sum() - confirm_before);
            } else
                c.setConfirm_add(0);  //如果找不到头一天的数据，则将新增默认为0

        }

        return chinaCities;
    }

    /*返回国外城市数据*/
    public List<ChinaCity> quaryForeignCity(String date) {
        String sql = "SELECT country as province,NAME ,TIME ,confirm_sum,confirm_add,confirm_sum-cured_sum-dead_sum AS confirm_now FROM foreign_city WHERE TIME = ?;";
        List<ChinaCity> foreignCity = template.query(sql, new BeanPropertyRowMapper<>(ChinaCity.class), date);

        return foreignCity;
    }

    //热力图功能
    public List<HotMap> quaryHotMap(String time) {
        //1.将中国城市数据入
        String sql = "SELECT china_city.name ,china_city.confirm_sum,china_city.confirm_sum-china_city.cured_sum-china_city.dead_sum AS confirm_now ,china_city_gps.longitude,china_city_gps.latitude " +
                "FROM china_city INNER JOIN china_city_gps ON china_city.name=china_city_gps.name WHERE TIME = ? ORDER BY china_city.confirm_sum DESC ;";
        List<HotMap> hotMapList = template.query(sql, new BeanPropertyRowMapper<>(HotMap.class), time);

        //接下来做差求confirm_add
        //1.将当前时间减去1天
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date beforeTime = null;
        try {
            beforeTime = sdf.parse(time);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cd = Calendar.getInstance();
        cd.setTime(beforeTime);
        cd.add(Calendar.DATE, -1);
        Date time2 = cd.getTime();
        String time_new = sdf.format(time2); //天数减1

        //通过map集合来存储头一天的数据。
        String sql4 = "select name,confirm_sum from china_city where  time = ? ; ";
        List<Map<String, Object>> maps = template.queryForList(sql4, time_new);

        for (HotMap h : hotMapList) {
            String name = h.getName();
            for (Map<String, Object> m : maps) {
                if (m.get("name").equals(name)) {
                    Object confirm_sum_yesterday = m.get("confirm_sum");
                    h.setConfirm_add(h.getConfirm_sum() - (Integer) confirm_sum_yesterday); //做差求新增
                }

            }
        }

        //2.获取国外的国家信息
        String sql_country = "SELECT country.name ,country.confirm_sum,country.confirm_add,country.confirm_sum-country.cured_sum-country.dead_sum " +
                "AS confirm_now,country_gps.longitude,country_gps.latitude  FROM country INNER JOIN country_gps ON country.name = country_gps.name  " +
                "WHERE TIME = ? and country.name != '中国' ORDER BY country.confirm_sum DESC;";
        List<HotMap> country_ = template.query(sql_country, new BeanPropertyRowMapper<>(HotMap.class), time);
        for (HotMap h1 : country_) {
            hotMapList.add(h1);
        }


        return hotMapList;
    }




    //查询某国家的所有时间的数据
    public List<Country_Today> quaryCountryAll(String name) {
        String sql = "select name ,time ,confirm_sum,cured_sum,dead_sum,confirm_sum-cured_sum-dead_sum as confirm_now ,confirm_add from country where name  = ? ;";
        List<Country_Today> countryTodays = template.query(sql, new BeanPropertyRowMapper<>(Country_Today.class), name);
        return countryTodays;
    }

    //查询中国城市的历史数据。
    public List<Country_Today> quaryChinaHistroy(String name) {
        String sql = "SELECT NAME ,TIME ,confirm_sum,cured_sum ,dead_sum,confirm_sum-cured_sum-dead_sum AS " +
                "confirm_now FROM china_city WHERE NAME = ? ORDER BY TIME DESC;";
        List<Country_Today> city_history = template.query(sql, new BeanPropertyRowMapper<>(Country_Today.class), name);
        //计算confirm_add
        for (Country_Today ch : city_history) {
            String time = ch.getTime();
            //1.将当前时间减去1天
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date beforeTime = null;
            try {
                beforeTime = sdf.parse(time);

            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar cd = Calendar.getInstance();
            cd.setTime(beforeTime);
            cd.add(Calendar.DATE, -1);
            Date time2 = cd.getTime();
            String time_new = sdf.format(time2); //天数减1

            String sql2 = "select count(*) from china_city where time = ? and name = ?";
            Integer i = template.queryForObject(sql2, Integer.class, time_new, name);

            if (i == 1) {
                String sql3 = "select confirm_sum from china_city where time = ? and name = ?";
                Integer confirm_yesterday = template.queryForObject(sql3, Integer.class, time_new, name);
                ch.setConfirm_add(ch.getConfirm_sum() - confirm_yesterday);
            } else {
                ch.setConfirm_add(0);//查询不到头一天的记录，则新增设置为0
            }

        }

        Collections.reverse(city_history);//集合反转
        return city_history;

    }



}
