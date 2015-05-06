package me.jjservices.cccontroller.api.storage;

import java.sql.Connection;
import java.sql.SQLException;

import me.jjservices.cccontroller.core.CCSCore;

import com.zaxxer.hikari.HikariDataSource;


public final class MySQL {

	private static HikariDataSource hikari;
	
	private static void initPool() {
		
		String address  = CCSCore.INSTANCE.getConfig().getString("database.address");
		String dbname   = CCSCore.INSTANCE.getConfig().getString("database.dbname");
		String username = CCSCore.INSTANCE.getConfig().getString("database.username");
		String password = CCSCore.INSTANCE.getConfig().getString("database.password");
		
		hikari = new HikariDataSource();
		
		hikari.setMaximumPoolSize(10);
		hikari.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MySqlDataSource");
		hikari.addDataSourceProperty("serverName", address);
		hikari.addDataSourceProperty("port", "3306");
		hikari.addDataSourceProperty("databaseName", dbname);
		hikari.addDataSourceProperty("user", username);
		hikari.addDataSourceProperty("password", password);
		
	}
	
	public static Connection getConnection() throws SQLException{
		
		if(hikari == null) {
			initPool();
		}
		
		return hikari.getConnection();
	}
	
	public static void shutdown() {
		hikari.shutdown();
		hikari = null;
	}
	
	public static void closeConnection(Connection conn) {
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	/* SQL Utils */
	
	public static void updateTable() {
		
	}
	public static void createTable() {
		
	}

	
	
}
