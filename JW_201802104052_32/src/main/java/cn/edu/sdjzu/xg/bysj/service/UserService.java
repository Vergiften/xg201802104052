package cn.edu.sdjzu.xg.bysj.service;


import cn.edu.sdjzu.xg.bysj.dao.UserDao;
import cn.edu.sdjzu.xg.bysj.domain.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;

public final class UserService {
	private UserDao userDao = UserDao.getInstance();
	private static UserService userService = new UserService();
	public UserService() {}
	
	public static UserService getInstance(){
		return UserService.userService;
	}

	public Collection<User> getUsers() throws SQLException {
		return userDao.findAll();
	}

	public boolean changePassword(User user) throws SQLException {
		return userDao.changePassword(user);
	}

	public User find(Integer id) throws SQLException {
		return userDao.find(id);
	}

	public User findByUsername(String username) throws SQLException {
		return userDao.findByUsername(username);
	}

	public User login(String username, String password) throws SQLException {
		return userDao.login(username, password);
	}

	public boolean add(User user) throws SQLException {
		return userDao.add(user);
	}

	public boolean add(Connection connection, User user) throws SQLException {
		return userDao.add(connection,user);
	}

	public boolean delete(Integer id) throws SQLException{
		User user = this.find(id);
		return userDao.delete(user);
	}

	public boolean delete(User user) throws SQLException{
		return userDao.delete(user);
	}
}
