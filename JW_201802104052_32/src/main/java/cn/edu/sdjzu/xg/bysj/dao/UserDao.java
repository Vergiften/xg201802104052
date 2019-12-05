package cn.edu.sdjzu.xg.bysj.dao;

import cn.edu.sdjzu.xg.bysj.domain.User;
import cn.edu.sdjzu.xg.bysj.service.TeacherService;
import cn.edu.sdjzu.xg.bysj.service.UserService;
import util.JdbcHelper;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;

public final class UserDao {
    private static Collection<User> users;
	private static UserDao userDao=new UserDao();
	private UserDao(){}
	public static UserDao getInstance(){
		return userDao;
	}

	public Collection<User> findAll() throws SQLException {
        users = new HashSet<>();
        Connection connection = JdbcHelper.getConn();
        Statement stmt = connection.createStatement();
        ResultSet resultSet = stmt.executeQuery("select * from User");
        //从数据库中取出数据
        while (resultSet.next()){
            users.add(new User(resultSet.getInt("id"),
                    resultSet.getString("username"),
                    resultSet.getString("password"),
                    TeacherService.getInstance().find(resultSet.getInt("teacher_id"))));
        }
        JdbcHelper.close(stmt,connection);
        return users;
	}

	public User findByUsername(String username) throws SQLException {
		Connection connection = JdbcHelper.getConn();
		PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM user where username = ?");
        preparedStatement.setString(1,username);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        User user = new User(resultSet.getInt("id"),
                resultSet.getString("username"),
                resultSet.getString("password"),
                TeacherService.getInstance().find(resultSet.getInt("teacher_id")));
        return user;
	}

	public User find(Integer id) throws SQLException {
		Connection connection = JdbcHelper.getConn();
		PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM user where id = ?");
		preparedStatement.setInt(1,id);
		ResultSet resultSet = preparedStatement.executeQuery();
		User user = null;
		while (resultSet.next()){
            user = new User(resultSet.getInt("id"),
            resultSet.getString("username"),
            resultSet.getString("password"),
            TeacherService.getInstance().find(resultSet.getInt("teacher_id")));
        }
		return user;
	}

    public boolean changePassword(User user) throws SQLException {
        Connection connection = JdbcHelper.getConn();
        PreparedStatement preparedStatement = connection.prepareStatement("UPDATE user SET password = ?");
        preparedStatement.setString(1, user.getPassword());
        int affectedRowNum = preparedStatement.executeUpdate();
        return affectedRowNum > 0;
    }

    public User login(String username,String password) throws SQLException {
        User user = null;
        Connection connection = JdbcHelper.getConn();
        String login_sql = "SELECT * FROM user where username = ? AND password = ?";
        PreparedStatement pstmt = connection.prepareStatement(login_sql);
        pstmt.setString(1,username);
        pstmt.setString(2,password);
        ResultSet resultSet = pstmt.executeQuery();
        if (resultSet.next()){
            user = UserService.getInstance().find(resultSet.getInt("id"));
        }
        return user;
    }

    public boolean add(User user) throws SQLException {
        Connection connection = JdbcHelper.getConn();
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO user(username,password,teacher_id) VALUES" + " (?,?,?)");
        preparedStatement.setString(1, user.getUsername());
        preparedStatement.setString(2,user.getPassword());
        preparedStatement.setInt(3,user.getTeacher().getId());
        int affectedRowNum = preparedStatement.executeUpdate();
        System.out.println("添加了 " + affectedRowNum +" 行记录");
        JdbcHelper.close(preparedStatement,connection);
        return affectedRowNum >0;
	}

	public boolean add(Connection connection,User user) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO user(username,password,teacher_id) VALUES" + " (?,?,?)");
        preparedStatement.setString(1, user.getUsername());
        preparedStatement.setString(2,user.getPassword());
        preparedStatement.setInt(3,user.getTeacher().getId());
        int affectedRowNum = preparedStatement.executeUpdate();
        System.out.println("添加了 " + affectedRowNum +" 行记录");
        preparedStatement.close();
        return affectedRowNum >0;
    }

    public boolean delete(Integer id) throws SQLException {
        User user = this.find(id);
        return this.delete(user);
    }

    public boolean delete(User user) throws SQLException {
        Connection connection = JdbcHelper.getConn();
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM user WHERE id = ?");
        preparedStatement.setInt(1,user.getId());
        int affectedRowNum = preparedStatement.executeUpdate();
        System.out.println("删除了 " + affectedRowNum +" 行记录");
        JdbcHelper.close(preparedStatement,connection);
        return affectedRowNum>0;
    }
}
