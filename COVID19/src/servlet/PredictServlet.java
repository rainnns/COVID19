package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import domain.Predict;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

@WebServlet("/predictServlet")

public class PredictServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.setCharacterEncoding("utf-8");

        String type = req.getParameter("type");
        String obj_name = req.getParameter("obj_name");

        String absolutePath1 = this.getServletContext().getRealPath("/SIR.py") ;
//        String absolute_Path = "D:\\javawebtest\\out\\artifacts\\javawebtest_war_exploded\\python\\SIR.py" ;
        Runtime runtime = Runtime.getRuntime();
        Process proc = null;
        ArrayList<String> cmd = new ArrayList<>();
        cmd.add("python");
        cmd.add("-u"); //!!!==加上参数u让脚本实时输出==!!!
        cmd.add(absolutePath1);
        cmd.add(type);  //type
        cmd.add(obj_name);    //中国
        proc = runtime.exec(cmd.toArray(new String[0]));
        BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream(), Charset.forName("GBK")));
        String line;
        ArrayList<Predict> results = new ArrayList<>();
        DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        while ((line = in.readLine()) != null) {
            System.out.println(line);
            Date time = null ;
            try {
                time = format1.parse(line.split(":")[0]);
            } catch (ParseException e) {
                e.printStackTrace();
            };
            int confirm_now = Integer.parseInt(line.split(":")[1]);
            Predict predict = new Predict();
            predict.setTime(time);
            predict.setConfirm_now(confirm_now);
            results.add(predict);
        }
        in.close();
        try {
            proc.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //将结果集转换为json格式数据
        ObjectMapper mapper = new ObjectMapper();
        String jsonlist = mapper.writeValueAsString(results);
        //相应数据
        resp.setContentType("text/html;charset=utf-8");
        resp.setHeader("Access-Control-Allow-Origin","*");//解决跨域问题，开发完毕时应该关闭
        resp.getWriter().append(jsonlist);

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }
}

