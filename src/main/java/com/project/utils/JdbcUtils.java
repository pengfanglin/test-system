package com.project.utils;

import java.sql.*;

/**
 * jdbc连接
 */
public class JdbcUtils {
	public static Connection con = null;
	static{
		String URL = "jdbc:mysql://101.132.111.96:3306/ai_kang_shui_dian?useSSL=false&serverTimezone=UTC&characterEncoding=UTF-8";
		String USERNAME = "root";
		String PASSWORD = "123456";
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection(URL,USERNAME,PASSWORD);
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	public static Connection getConnection(){
		return con;
	}
	public static void close(Connection connection,PreparedStatement preparedStatement,ResultSet resultSet) {
		try {
			if(resultSet!=null) {
				resultSet.close();
			}
			if(preparedStatement!=null) {
				preparedStatement.close();
			}
			if(connection!=null) {
				connection.close();
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
