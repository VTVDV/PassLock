package com.vypersw.passlock.core;

import java.sql.*;

public abstract class SQLconnection 
{
	private Connection connection;
	
	public Connection connect() throws ClassNotFoundException, SQLException 
	{
		connection = null;
		Class.forName("org.sqlite.JDBC");
		connection = DriverManager.getConnection("jdbc:sqlite:passlock.db");
		return connection;
	}	
}
