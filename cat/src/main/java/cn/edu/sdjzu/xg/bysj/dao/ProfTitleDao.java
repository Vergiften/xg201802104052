package cn.edu.sdjzu.xg.bysj.dao;

import cn.edu.sdjzu.xg.bysj.domain.ProfTitle;
import util.JdbcHelper;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;

public final class ProfTitleDao {
	private static ProfTitleDao profTitleDao=new ProfTitleDao();
	private ProfTitleDao(){}
	public static ProfTitleDao getInstance(){
		return profTitleDao;
	}
	private static Collection<ProfTitle> profTitles;
	public Collection<ProfTitle> findAll() throws SQLException {
		profTitles = new HashSet<>();
		Connection connection = JdbcHelper.getConn();
		Statement stmt = connection.createStatement();
		ResultSet resultSet = stmt.executeQuery("select * from ProfTitle");
		//从数据库中取出数据
		while (resultSet.next()){
			//System.out.println(resultSet.getString("description"));
			profTitles.add(new ProfTitle(resultSet.getInt("id"), resultSet.getString("description"),
					resultSet.getString("no"),resultSet.getString("remarks")));
		}
		JdbcHelper.close(stmt,connection);
		return profTitles;
	}

	public ProfTitle find(Integer id) throws SQLException {
		Connection connection = JdbcHelper.getConn();
		PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM profTitle where id = ?");
		preparedStatement.setInt(1,id);
		ResultSet resultSet = preparedStatement.executeQuery();
		resultSet.next();
		ProfTitle profTitle = new ProfTitle(resultSet.getInt("id"), resultSet.getString("description"),
				resultSet.getString("no"),resultSet.getString("remarks"));
		return profTitle;
	}

	public boolean update(ProfTitle profTitle) throws SQLException {
		Connection connection = JdbcHelper.getConn();
		PreparedStatement preparedStatement = connection.prepareStatement("UPDATE department set no = ? description = ? remarks = ? where id = ?");
		preparedStatement.setString(1,profTitle.getNo());
		preparedStatement.setString(2,profTitle.getDescription());
		preparedStatement.setString(3,profTitle.getRemarks());
		preparedStatement.setInt(4,profTitle.getId());
		int affectedRowNum = preparedStatement.executeUpdate();
		System.out.println("修改行数为"+ affectedRowNum);
		JdbcHelper.close(preparedStatement,connection);
		return affectedRowNum>0;
	}

	public boolean add(ProfTitle profTitle) throws SQLException {
		Connection connection = JdbcHelper.getConn();
		PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO profTitle "+ "(no,description,remarks)" +" VALUES (?,?,?)");
		preparedStatement.setString(1,profTitle.getNo());
		preparedStatement.setString(2,profTitle.getDescription());
		preparedStatement.setString(3,profTitle.getRemarks());
		int affectedRowNum = preparedStatement.executeUpdate();
		System.out.println(affectedRowNum);
		JdbcHelper.close(preparedStatement,connection);
		return affectedRowNum>0;
	}

	public boolean delete(Integer id) throws SQLException{
		ProfTitle profTitle = this.find(id);
		return this.delete(profTitle);
	}

	public boolean delete(ProfTitle profTitle) throws SQLException{
		Connection connection = JdbcHelper.getConn();
		PreparedStatement preparedStatement = connection.prepareStatement("Delete from profTitle WHERE id =?");
		preparedStatement.setInt(1,profTitle.getId());
		int affectedRowNum = preparedStatement.executeUpdate();
		System.out.println(affectedRowNum);
		return affectedRowNum>0;
	}
}

