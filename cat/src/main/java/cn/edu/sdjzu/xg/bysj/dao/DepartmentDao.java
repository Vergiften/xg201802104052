package cn.edu.sdjzu.xg.bysj.dao;

import cn.edu.sdjzu.xg.bysj.domain.Department;
import cn.edu.sdjzu.xg.bysj.service.SchoolService;
import util.JdbcHelper;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.TreeSet;

public final class DepartmentDao {
	private static Collection<Department> departments;
	private static DepartmentDao departmentDao=new DepartmentDao();
	private DepartmentDao(){}

	public static DepartmentDao getInstance(){
		return departmentDao;
	}


	public Collection<Department> findAll() throws SQLException {
		departments = new HashSet<>();
		Connection connection = JdbcHelper.getConn();
		Statement stmt = connection.createStatement();
		ResultSet resultSet = stmt.executeQuery("select * from Department");
		//从数据库中取出数据
		while (resultSet.next()){
			//System.out.println(resultSet.getString("description"));
			departments.add(new Department(resultSet.getInt("id"), resultSet.getString("description"),
					resultSet.getString("no"),resultSet.getString("remarks"),SchoolDao.getInstance().find(resultSet.getInt("school_id"))));
		}
		JdbcHelper.close(stmt,connection);
		return departments;
	}

	public Department find(Integer id) throws SQLException{
		Connection connection = JdbcHelper.getConn();
		PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM department where id = ?");
		preparedStatement.setInt(1,id);
		ResultSet resultSet = preparedStatement.executeQuery();
		resultSet.next();
		Department department = new Department(resultSet.getInt("id"), resultSet.getString("description"),
				resultSet.getString("no"),resultSet.getString("remarks"), SchoolDao.getInstance().find(resultSet.getInt("school_id")));
		return department;
	}

	public boolean update(Department department) throws SQLException{
		Connection connection = JdbcHelper.getConn();
		PreparedStatement preparedStatement = connection.prepareStatement("UPDATE department set description = ? no =? remarks = ?  school_id = ? where id = ?");
		preparedStatement.setString(1,department.getDescription());
		preparedStatement.setString(2,department.getNo());
		preparedStatement.setString(3,department.getRemarks());
		preparedStatement.setInt(4,department.getSchool().getId());
		preparedStatement.setInt(5,department.getId());
		int affectedRowNum = preparedStatement.executeUpdate();
		System.out.println("修改行数为"+ affectedRowNum);
		JdbcHelper.close(preparedStatement,connection);
		return affectedRowNum>0;
	}

	public boolean add(Department department) throws SQLException {
		//获取数据库连接对象
		Connection connection = JdbcHelper.getConn();
		PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO department "+ "(description,no,remarks,school_id)" +" VALUES (?,?,?,?)");
		preparedStatement.setString(1,department.getDescription());
		preparedStatement.setString(2,department.getNo());
		preparedStatement.setString(3,department.getRemarks());
		preparedStatement.setInt(4,department.getSchool().getId());
		int affectedRowNum = preparedStatement.executeUpdate();
		System.out.println(affectedRowNum);
		JdbcHelper.close(preparedStatement,connection);
		return affectedRowNum>0;
	}

	public boolean delete(Integer id) throws SQLException{
		Department department = this.find(id);
		return this.delete(department);
	}

	public boolean delete(Department department) throws SQLException{
		Connection connection = JdbcHelper.getConn();
		PreparedStatement preparedStatement = connection.prepareStatement("Delete from department WHERE id =?");
		preparedStatement.setInt(1,department.getId());
		int affectedRowNum = preparedStatement.executeUpdate();
		System.out.println(affectedRowNum);
		JdbcHelper.close(preparedStatement,connection);
		return affectedRowNum>0;
	}

	public  Collection<Department> findAllBySchool(int schoolId) throws SQLException {
		Collection<Department> departments = new TreeSet<Department>();
		Connection connection = JdbcHelper.getConn();
		String findAllBySchool_sql = "SELECT * FROM department where school_id = ?";
		PreparedStatement pstmt = connection.prepareStatement(findAllBySchool_sql);
		pstmt.setInt(1,schoolId);
		ResultSet resultSet = pstmt.executeQuery();
		while(resultSet.next()){
			Department department = new Department(
					resultSet.getInt("id"),
					resultSet.getString("description"),
					resultSet.getString("no"),
					resultSet.getString("remarks"),
					SchoolService.getInstance().find(resultSet.getInt("school_id")));
			departments.add(department);
		}
		return departments;
	}
}

