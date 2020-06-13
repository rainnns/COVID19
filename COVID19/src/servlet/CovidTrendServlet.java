package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import dao.QueryData;
import dao.QueryMethod;
import domain.Country_Today;
import domain.Province;
import domain.Province_Today;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/covidTrendServlet")
public class CovidTrendServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //1.设置编码格式
        req.setCharacterEncoding("utf-8");
        resp.setContentType("text/html;charset=utf-8");
        resp.setHeader("Access-Control-Allow-Origin","*");//解决跨域问题，开发完毕时应该关闭
        ObjectMapper mapper = new ObjectMapper();
        //2.获取请求参数
        String type1 = req.getParameter("type");
        Integer type = Integer.valueOf(type1);
        String name = req.getParameter("name");
        if (type ==1){     //查询省级数据
            List<Province_Today> province_todays = new QueryData().quaryProvinceToday(name);
            String provinceTrend = mapper.writeValueAsString(province_todays);
            //响应数据
            resp.getWriter().append(provinceTrend);

        }else if (type == 0){  //查询国家数据
           List<Country_Today> countryList = new QueryMethod().quaryCountryAll(name);
            String co = mapper.writeValueAsString(countryList);
            //响应数据
            resp.getWriter().append(co);
        }else if (type == 2){
            //查询中国城市数据
           List<Country_Today>  city_histroy = new QueryMethod().quaryChinaHistroy(name);
            String his = mapper.writeValueAsString(city_histroy);
            resp.getWriter().append(his);

        }

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req,resp);
    }
}
