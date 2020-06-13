package test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dao.QueryData;
import dao.QueryMethod;
import domain.*;
import org.junit.Test;

import java.util.List;

public class TestQuery {

    @Test
    public void testQueryCountry(){
        List<Country> ncp_country = new QueryData().quaryCountry();
        ObjectMapper mapper = new ObjectMapper();
        try {
            String json = mapper.writeValueAsString(ncp_country);
            System.out.println(json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testNews(){
        List<News> news = new QueryData().quaryNews();
        ObjectMapper mapper = new ObjectMapper();
        try {
            String s = mapper.writeValueAsString(news);
            System.out.println(s);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }



    @Test
    public void testGlobalCountry(){
        List<Global_Country> global_countries = new QueryMethod().quaryGlobalCountry_gps("2020-03-20");
        ObjectMapper mapper = new ObjectMapper();
        try {
            String s = mapper.writeValueAsString(global_countries);
            System.out.println(s);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGlobal(){
        List<Total> totals = new QueryMethod().quaryGlobalTotal("2020-03-21");
        ObjectMapper mapper = new ObjectMapper();
        try {
            String s = mapper.writeValueAsString(totals);
            System.out.println(s);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void community(){
        List<CommunityName> community_wuhans = new QueryData().quaryCommunity_name(114.475,30.5841);
        ObjectMapper mapper = new ObjectMapper();
        try {
            String s = mapper.writeValueAsString(community_wuhans);
            System.out.println(s);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testForeign(){
        ForeignTotal foreignTotal = new QueryMethod().quaryChinaTotal("2020-03-21");
        System.out.println(foreignTotal);
    }
    @Test
    public void testTrent(){
        List<Country_Today> hubei = new QueryMethod().quaryCountryAll("美国");
        ObjectMapper mapper = new ObjectMapper();
        try {
            String s = mapper.writeValueAsString(hubei);
            System.out.println(s);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testP(){
        List<ChinaCity> globalCountry_19s = new QueryMethod().quaryChinaCity("2020-03-21");
        ObjectMapper mapper = new ObjectMapper();
        try {
            String s = mapper.writeValueAsString(globalCountry_19s);
            System.out.println(s);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testHotMap(){
        List<HotMap> hotMaps = new QueryMethod().quaryHotMap("2020-03-22");
        ObjectMapper mapper = new ObjectMapper();
        try {
            String s = mapper.writeValueAsString(hotMaps);
            System.out.println(s);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }


}

