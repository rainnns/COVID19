package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import dao.QueryData;
import dao.QueryMethod;
import domain.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/colorServlet")
public class ColorServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("text/html;charset=utf-8");
        resp.setHeader("Access-Control-Allow-Origin","*");//解决跨域问题，开发完毕时应该关闭。
        ObjectMapper mapper = new ObjectMapper();

        String date = req.getParameter("date");
        String num = req.getParameter("type");
        Integer type = Integer.valueOf(num);
        if (type == 0){
            //type=0，返回所有国家的name,累计确诊，当日新增和现有确诊
            //获取查询结果集合
            List<GlobalCountry_19> globalCountry_19s = new QueryMethod().quaryGlobalCountry(date);
            //将结果集转换为json格式数据
            String jsonlist = mapper.writeValueAsString(globalCountry_19s);
            //相应数据
            resp.getWriter().append(jsonlist);
        }else if (type == 1){
            //type=1，返回中国所有省的name,累计确诊，当日新增和现有确诊
            //3.调用方法，查询数据库
            List<GlobalCountry_19> globalCountry_19s = new QueryMethod().quaryProvince(date);
            //利用ObjectMapper将对象转换成json格式
            String jsonlist = mapper.writeValueAsString(globalCountry_19s);
            //响应数据
            resp.getWriter().append(jsonlist);
        }else if (type ==2){
            //type=2,返回当天中国城市的累计确诊，当日新增，现有确诊
            List<ChinaCity> china_city = new QueryMethod().quaryChinaCity(date);
            String jsonlist = mapper.writeValueAsString(china_city);
            //响应数据
            resp.getWriter().append(jsonlist);
        }else if (type == 3){

           List<ChinaCity> foreign = new QueryMethod().quaryForeignCity(date);
            String jsonlist = mapper.writeValueAsString(foreign);
            //响应数据
            resp.getWriter().append(jsonlist);
        }





    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }
}
