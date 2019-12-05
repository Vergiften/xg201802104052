package cn.edu.sdjzu.xg.bysj.controller.basic.teacher;

import cn.edu.sdjzu.xg.bysj.domain.Teacher;
import cn.edu.sdjzu.xg.bysj.service.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import util.JSONUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

@WebServlet("/teacher.ctl")
public class TeacherController extends HttpServlet {
    /**
     * PUT http://localhost:8080/Teacher
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    //更新方法
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String teacher_json = JSONUtil.getJSON(request);
        //将JSON字串解析为Degree对象
        Teacher teacherToAdd = JSON.parseObject(teacher_json,Teacher.class);

        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        //到数据库表修改School对象对应的记录
        try {
            TeacherService.getInstance().update(teacherToAdd);
            message.put("message", "修改成功");
        }catch (SQLException e){
            message.put("message", "数据库操作异常");
        }catch(Exception e){
            message.put("message", "网络异常");
        }
        //响应message到前端
        response.getWriter().println(message);
    }

    /**
     * POST http://localhost:8080/Teacher
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    //增加方法
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String teacher_json = JSONUtil.getJSON(request);
        Teacher teacherToAdd = JSON.parseObject(teacher_json,Teacher.class);

        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        //在数据库表中增加School对象
        try {
            TeacherService.getInstance().add(teacherToAdd);
            message.put("message", "增加成功");
        }catch (SQLException e){
            message.put("message", "数据库操作异常");
        }catch(Exception e){
            e.printStackTrace();
            message.put("message", "网络异常");
        }
        //响应message到前端
        response.getWriter().println(message);
    }

    /**
     * DELETE http://localhost:8080/Teacher?id=2
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    //删除方法
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String id_str = request.getParameter("id");
        int id = Integer.parseInt(id_str);

        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        //到数据库表中删除对应的学院
        try {
            TeacherService.getInstance().delete(id);
            message.put("message", "删除成功");
        }catch (SQLException e){
            message.put("message", "数据库操作异常");
        }catch(Exception e){
            message.put("message", "网络异常");
        }
        //响应message到前端
        response.getWriter().println(message);
    }

    /**
     * GET http://localhost:8080/Teacher
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    //查找方法
    protected void doGet(HttpServletRequest request,HttpServletResponse response)
            throws IOException {
        //读取参数id
        String id_str = request.getParameter("id");

        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        try {
            //如果id = null, 表示响应所有学院对象，否则响应id指定的学院对象
            if (id_str == null) {
                responseTeachers(response);
            } else {
                int id = Integer.parseInt(id_str);
                responseTeacher(id, response);
            }
        }catch (SQLException e){
            message.put("message", "数据库操作异常");
            //响应message到前端
            response.getWriter().println(message);
        }catch(Exception e){
            message.put("message", "网络异常");
            //响应message到前端
            response.getWriter().println(message);
        }
    }

    //响应一个学位对象
    private void responseTeacher(int id, HttpServletResponse response)
            throws IOException, SQLException {
        //根据id查找学院
        Teacher teacher = TeacherService.getInstance().find(id);
        String profTitle_json = JSON.toJSONString(teacher);
        response.getWriter().println(profTitle_json);
    }
    //响应所有学位对象
    private void responseTeachers(HttpServletResponse response)
            throws IOException, SQLException {
        //获得所有学院
        Collection<Teacher> teachers = TeacherService.getInstance().findAll();
        String teachers_json = JSON.toJSONString(teachers);
        response.getWriter().println(teachers_json);
    }
}
