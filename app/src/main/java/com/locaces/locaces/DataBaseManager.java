package com.locaces.locaces;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DataBaseManager {

	private static boolean initialized = false;
	private static Connection connection;

	public static void initialize() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		connection = DriverManager.getConnection("jdbc:mysql://sql4.freemysqlhosting.net/sql4101133?" +
				"user=sql4101133&password=5U5vPdxtS2");
		initialized = true;
	}

	public static ResultSet query(String command) throws ClassNotFoundException, SQLException {
		if (!initialized)
			initialize();

		return connection.createStatement().executeQuery(command);
	}

	public static void update(String command) throws ClassNotFoundException, SQLException {
		if (!initialized)
			initialize();
		connection.createStatement().executeUpdate(command);
	}

	public static void close() throws SQLException {
		if (initialized) {
			initialized = false;
			connection.close();
			connection = null;
		}
	}
}
