package cn.edu.sdjzu.xg.bysj.dao;

import cn.edu.sdjzu.xg.bysj.domain.Teacher;
import cn.edu.sdjzu.xg.bysj.domain.User;
import cn.edu.sdjzu.xg.bysj.service.UserService;
import util.JdbcHelper;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;

public final class TeacherDao {
	private static TeacherDao teacherDao=new TeacherDao();
	private TeacherDao(){}
	public static TeacherDao getInstance(){
		return teacherDao;
	}
	private static Collection<Teacher> teachers;

	public Collection<Teacher> findAll() throws SQLException {
		teachers = new HashSet<>();
		Connection connection = JdbcHelper.getConn();
		Statement stmt = connection.createStatement();
		ResultSet resultSet = stmt.executeQuery("select * from Teacher");
		//从数据库中取出数据
		while (resultSet.next()){
			teachers.add(new Teacher(resultSet.getInt("id"),
					resultSet.getString("name"),
					resultSet.getString("no"),
					ProfTitleDao.getInstance().find(resultSet.getInt("profTitle_id")),
					DegreeDao.getInstance().find(resultSet.getInt("degree_id")),
					DepartmentDao.getInstance().find(resultSet.getInt("department_id"))));
		}
		JdbcHelper.close(stmt,connection);
		return teachers;
	}
	
	public Teacher find(Integer id) throws SQLException {
		Connection connection = JdbcHelper.getConn();
		PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM teacher where id = ?");
		preparedStatement.setInt(1,id);
		ResultSet resultSet = preparedStatement.executeQuery();
		Teacher teacher = null;
		while (resultSet.next()){
			teacher = new Teacher(resultSet.getInt("id"),
									resultSet.getString("name"),
									resultSet.getString("no"),
									ProfTitleDao.getInstance().find(resultSet.getInt("proftitle_id")),
									DegreeDao.getInstance().find(resultSet.getInt("degree_id")),
									DepartmentDao.getInstance().find(resultSet.getInt("department_id")));
		}
		return teacher;
	}
	
	public boolean update(Teacher teacher) throws SQLException {
		Connection connection = JdbcHelper.getConn();
		PreparedStatement preparedStatement = connection.prepareStatement("UPDATE teacher set name = ?,no = ?,proftitle_id =?,degree_id =? ,department_id =? where id = ?");
		preparedStatement.setString(1,teacher.getName());
		preparedStatement.setString(2,teacher.getNo());
		preparedStatement.setInt(3,teacher.getTitle().getId());
		preparedStatement.setInt(4,teacher.getDegree().getId());
		preparedStatement.setInt(5,teacher.getDepartment().getId());
		preparedStatement.setInt(6,teacher.getId());
		int affectedRowNum = preparedStatement.executeUpdate();
		System.out.println("修改行数为"+ affectedRowNum);
		JdbcHelper.close(preparedStatement,connection);
		return affectedRowNum>0;
	}
	
	public boolean add(Teacher teacher) throws SQLException{
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		int affectedRowNum = 0;
		try {
		connection = JdbcHelper.getConn();
		connection.setAutoCommit(false);
		preparedStatement = connection.prepareStatement("INSERT INTO teacher "+ "(name,no,proftitle_id,degree_id,department_id)" +" VALUES (?,?,?,?,?)",Statement.RETURN_GENERATED_KEYS);
		preparedStatement.setString(1,teacher.getName());
		preparedStatement.setString(2,teacher.getNo());
		preparedStatement.setInt(3,teacher.getTitle().getId());
		preparedStatement.setInt(4,teacher.getDegree().getId());
		preparedStatement.setInt(5,teacher.getDepartment().getId());
		affectedRowNum = preparedStatement.executeUpdate();

		ResultSet resultSet = preparedStatement.getGeneratedKeys();
		int teacherId = -1;
		while(resultSet.next()){
			teacherId = resultSet.getInt(1);
		}
		System.out.println(teacherId);
		teacher.setId(teacherId);
		User user = new User(
		        teacher.getNo(),
                teacher.getNo(),
                teacher
                );
		UserService.getInstance().add(connection,user);
		}catch (SQLException e){
			e.printStackTrace();
			try {
				//回滚当前连接所做的操作
				if(connection !=null){
					connection.rollback();
				}
			}
			catch (SQLException e1){
				e1.printStackTrace();
			}
		} finally {
			try {
				//恢复自动提交
				if (connection!=null){
					connection.setAutoCommit(true);
				}
			}
			catch (SQLException e){
				e.printStackTrace();
			}
			//关闭
			JdbcHelper.close(preparedStatement,connection);
		}
		return affectedRowNum>0;
	}

	public boolean delete(Integer id) throws SQLException{
		Teacher teacher = this.find(id);
		return this.delete(teacher);
	}
	
	public boolean delete(Teacher teacher) throws SQLException{
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
		connection = JdbcHelper.getConn();
		connection.setAutoCommit(false);
		//根据参照完整性先删除user表中对应的teacher_id的记录
		preparedStatement = connection.prepareStatement("delete from user where teacher_id=?");
		preparedStatement.setInt(1,teacher.getId());
		preparedStatement.executeUpdate();
		//删除teacher表中id对应的记录
		preparedStatement = connection.prepareStatement("Delete from teacher WHERE id =?");
		preparedStatement.setInt(1,teacher.getId());
		preparedStatement.executeUpdate();
		}catch (SQLException e){
			e.printStackTrace();
			try {
				//回滚当前连接所做的操作
				if(connection != null){
					connection.rollback();
				}
			} catch (SQLException e1){
				e1.printStackTrace();
			}
		} finally {
			try {
				//恢复自动提交
				if (connection != null){
					connection.setAutoCommit(true);
				}
			}
			catch (SQLException e){
				e.printStackTrace();
			}
			JdbcHelper.close(preparedStatement,connection);
		}
		return true;
	}
}
