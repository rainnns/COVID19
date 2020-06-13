package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import dao.QueryData;
import dao.QueryMethod;
import domain.Cities;
import domain.ForeignTotal;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/foreignServlet")
public class ForeignServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //1.设置编码格式
        req.setCharacterEncoding("utf-8");
        resp.setContentType("text/html;charset=utf-8");
        resp.setHeader("Access-Control-Allow-Origin","*");//解决跨域问题，开发完毕时应该关闭
        //2.获取请求参数
        String date = req.getParameter("date");
        //3.调用方法，查询数据库
        ForeignTotal foreignTotal = new QueryMethod().quaryForeignTotal(date);

        ForeignTotal china = new QueryMethod().quaryChinaTotal(date);
        //将结果集转换为json格式数据

        //相应数据

            ArrayList<ForeignTotal> totals = new ArrayList<>();
             totals.add(china);
            totals.add(foreignTotal);
            ObjectMapper mapper = new ObjectMapper();
            String f = mapper.writeValueAsString(totals);
            resp.getWriter().append(f);


    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req,resp);
    }
}
