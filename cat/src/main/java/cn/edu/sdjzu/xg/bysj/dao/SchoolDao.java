package cn.edu.sdjzu.xg.bysj.dao;


import cn.edu.sdjzu.xg.bysj.domain.Degree;
import cn.edu.sdjzu.xg.bysj.domain.School;
import util.JdbcHelper;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;

public final class SchoolDao {
	private static SchoolDao schoolDao = new SchoolDao();
	private static Collection<School> schools;

	public SchoolDao(){}
	
	public static SchoolDao getInstance(){
		return schoolDao;
	}

	public Collection<School> findAll() throws SQLException{
		schools = new HashSet<>();
		Connection connection = JdbcHelper.getConn();
		Statement stmt = connection.createStatement();
		ResultSet resultSet = stmt.executeQuery("select * from School");
		//从数据库中取出数据
		while (resultSet.next()){
			//System.out.println(resultSet.getString("description"));
			schools.add(new School(resultSet.getInt("id"), resultSet.getString("description"),
					resultSet.getString("no"),resultSet.getString("remarks")));
		}
		JdbcHelper.close(stmt,connection);
		return schools;
	}
	
	public School find(Integer id) throws SQLException{
		Connection connection = JdbcHelper.getConn();
		PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM school where id = ?");
		preparedStatement.setInt(1,id);
		ResultSet resultSet = preparedStatement.executeQuery();
		resultSet.next();
		School school = new School(resultSet.getInt("id"), resultSet.getString("description"),
				resultSet.getString("no"),resultSet.getString("remarks"));
		return school;
	}
	
	public boolean update(School school) throws SQLException {
		Connection connection = JdbcHelper.getConn();
		PreparedStatement preparedStatement = connection.prepareStatement("UPDATE degree set description = ? no =? remarks = ? where id = ?");
		preparedStatement.setString(1,school.getDescription());
		preparedStatement.setString(2,school.getNo());
		preparedStatement.setString(3,school.getRemarks());
		preparedStatement.setInt(4,school.getId());
		int affectedRowNum = preparedStatement.executeUpdate();
		System.out.println("修改行数为"+ affectedRowNum);
		JdbcHelper.close(preparedStatement,connection);
		return affectedRowNum>0;
	}

	public boolean add(School school) throws SQLException {
		Connection connection = JdbcHelper.getConn();
		PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO school "+ "(no,description,remarks)" +" VALUES (?,?,?)");
		preparedStatement.setString(1,school.getNo());
		preparedStatement.setString(2,school.getDescription());
		preparedStatement.setString(3,school.getRemarks());
		int affectedRowNum = preparedStatement.executeUpdate();
		System.out.println(affectedRowNum);
		JdbcHelper.close(preparedStatement,connection);
		return affectedRowNum>0;
	}

	public boolean delete(Integer id) throws SQLException{
		School school =this.find(id);
		return this.delete(school);
	}
	
	public boolean delete(School school) throws SQLException{
		Connection connection = JdbcHelper.getConn();
		PreparedStatement preparedStatement = connection.prepareStatement("Delete from degree WHERE id =?");
		preparedStatement.setInt(1,school.getId());
		int affectedRowNum = preparedStatement.executeUpdate();
		System.out.println(affectedRowNum);
		return affectedRowNum>0;
	}

	/**
	 * 使用储存过程中增加一个School对象
	 * @param school 需要存入的school对象（没有id）
	 * @return 存入的school对象（用数据库生成id为参数赋值，使之完整）
	 * @throws SQLException
	 * @throws ClassNotFoundException

	public School addWithSP(School school) throws SQLException, ClassNotFoundException{
		Connection connection = JdbcHelper.getConn();
		//根据连接对象准备可调用的语句对象，sp_addDepartment为存贮过程名称，后面为5个参数
		CallableStatement callableStatement = connection.prepareCall("{CALL sp_addSchool(?,?,?,?)}");
		//将第4个参数设置为输出参数，类型为长整数型（数据库的数据类型）
		callableStatement.registerOutParameter(4,Types.BIGINT);
		callableStatement.setString(1,school.getDescription());
		callableStatement.setString(2,school.getNo());
		callableStatement.setString(3,school.getRemarks());
		//执行可调用的语句callableStatement
		callableStatement.execute();
		//获得四个参数的值，数据库为该记录自动生成的id
		int id = callableStatement.getInt(4);
		school.setId(id);
		callableStatement.close();
		connection.close();
		return school;
	}*/
}
