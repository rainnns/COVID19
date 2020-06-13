package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import dao.QueryData;
import dao.QueryMethod;
import domain.HeatMap;
import domain.HotMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/hotMapServlet")
public class HotMapServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("text/html;charset=utf-8");
        resp.setHeader("Access-Control-Allow-Origin","*");//解决跨域问题，开发完毕时应该关闭

        String date = req.getParameter("date");
        List<HotMap> hotMaps =new QueryMethod().quaryHotMap(date);
        //获取查询结果集合
        //将结果集转换为json格式数据
        ObjectMapper mapper = new ObjectMapper();
        String features = mapper.writeValueAsString(hotMaps);
        //相应数据

        resp.getWriter().append(features);

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }
}
