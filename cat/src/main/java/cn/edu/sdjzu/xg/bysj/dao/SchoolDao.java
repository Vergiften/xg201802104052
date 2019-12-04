package cn.edu.sdjzu.xg.bysj.dao;

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
}
