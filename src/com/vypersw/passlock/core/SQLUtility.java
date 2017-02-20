package com.vypersw.passlock.core;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SQLUtility extends SQLconnection
{	
	public void createAccountsTable()
	{
		Connection con = null;
		Statement statement = null;
		try
		{
			con = connect();
			statement = con.createStatement();
			String sql = "CREATE TABLE TBL_ACCOUNT " +
					//NOTE! ID = ID OF GROUP!
					"(ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
					"GROUPID INTEGER NOT NULL," +
					" NAME TEXT NOT NULL, " + 
					" USERNAME TEXT NOT NULL, " + 
					" PASSWORD TEXT NOT NULL, " + 
					" URL TEXT Char(50), " + 
					" NOTE TEXT Char(150))"; 
			statement.executeUpdate(sql);
		}
		catch(Exception exception)
		{
			exception.printStackTrace();
		}
		finally
		{
			closeConnections(con, statement);
		}
	}
	
	public void createAccount(Account account)
	{
		Connection con = null;
		Statement statement = null;
		try
		{
			con = connect();
			statement = con.createStatement();
			
			String sql = "INSERT INTO TBL_ACCOUNT " +
					"(GROUPID, NAME, USERNAME, PASSWORD, URL, NOTE)" +
					"VALUES ("+account.getGroupID()+",'"+VyperSecurity.getInstance().encrypt(account.getName())+"','"+VyperSecurity.getInstance().encrypt(account.getUsername())+"','"+ VyperSecurity.getInstance().encrypt(account.getPassword())+"','"+VyperSecurity.getInstance().encrypt(account.getUrl())+"','"+VyperSecurity.getInstance().encrypt(account.getNotes())+"')";
			statement.executeUpdate(sql);
		}
		catch(Exception exception)
		{
			exception.printStackTrace();
		}
		finally
		{
			closeConnections(con, statement);
		}
	}
	
	public void createGroupsTable()
	{
		Connection con = null;
		Statement statement = null;
		try
		{
			con = connect();
			statement = con.createStatement();
			String sql = "CREATE TABLE TBL_GROUP " +
					"(ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
					" NAME TEXT NOT NULL)";
			statement.executeUpdate(sql);
		}
		catch(Exception exception)
		{
			exception.printStackTrace();
		}
		finally
		{
			closeConnections(con, statement);
		}
	}
			
	public void createGroup(Group group)
	{
		Connection con = null;
		Statement statement = null;
		try
		{
			con = connect();
			statement = con.createStatement();
			
			String sql = "INSERT INTO TBL_GROUP " +
					"(NAME)" +
					"VALUES ('"+group.getName()+"')";
			statement.executeUpdate(sql);		
		}
		catch(Exception exception)
		{
			exception.printStackTrace();
		}
		finally
		{
			closeConnections(con, statement);
		}
	}
	
	private void closeConnections(Connection con,Statement statement) 
	{
		try
		{
			if(statement != null) 
			{
				statement.close();
			}
			if(con != null) 
			{
				con.close();
			}			
		}
		catch(Exception exception)
		{
			exception.printStackTrace();
		}
	}
	
	public void getAccounts(Group group)
	{
		Connection con = null;
		Statement statement = null;
		try
		{
			con = connect();
			statement = con.createStatement();
			String sql  = "SELECT * FROM TBL_ACCOUNT WHERE GROUPID ="+group.getId()+";";
			ResultSet rs = statement.executeQuery(sql);
			while(rs.next())
			{
				int id = rs.getInt("ID");
				int groupid = rs.getInt("GROUPID");
				String name = VyperSecurity.getInstance().decrypt(rs.getString("NAME"));
				String username = VyperSecurity.getInstance().decrypt(rs.getString("USERNAME"));
				String pass = VyperSecurity.getInstance().decrypt(rs.getString("PASSWORD"));
				String url = VyperSecurity.getInstance().decrypt(rs.getString("URL"));
				String note = VyperSecurity.getInstance().decrypt(rs.getString("NOTE"));
				Account account = new Account(id, groupid, username, name, pass, url, note);
				group.addAccount(account);
			}
		}
		catch(Exception exception)
		{
			exception.printStackTrace();
		}
		finally
		{
			closeConnections(con, statement);
		}
	}
	
	public void updateAccount(Account account)
	{
		Connection con = null;
		Statement statement = null;
		try
		{
			con = connect();
			statement = con.createStatement();
			String sql  = "UPDATE TBL_ACCOUNT "
					+ "SET GROUPID="+account.getGroupID()+", "
					+ "NAME='"+VyperSecurity.getInstance().encrypt(account.getName())+"', "
					+ "USERNAME='"+VyperSecurity.getInstance().encrypt(account.getUsername())+"', "
					+ "PASSWORD='"+VyperSecurity.getInstance().encrypt(account.getPassword())+"', "
					+ "URL='"+VyperSecurity.getInstance().encrypt(account.getUrl())+"', "
					+ "NOTE='"+VyperSecurity.getInstance().encrypt(account.getNotes())+"' "
					+ "WHERE ID ="+account.getID();
			statement.executeUpdate(sql);
		}
		catch(Exception exception)
		{
			exception.printStackTrace();
		}
		finally
		{
			closeConnections(con, statement);
		}
	}		
	
	public List<Group> getGroups()
	{
		List<Group> groups = new ArrayList<>();
		Connection con = null;
		Statement statement = null;
		try
		{
			con = connect();
			statement = con.createStatement();
			String sql  = "SELECT * FROM TBL_GROUP";
			ResultSet rs = statement.executeQuery(sql);
			while(rs.next())
			{
				int id = rs.getInt("ID");
				String name = rs.getString("NAME");
				Group group = new Group(name, id);
				groups.add(group);
			}
			
			return groups;
		}
		catch(Exception exception)
		{
			exception.printStackTrace();
		}
		finally
		{
			closeConnections(con, statement);
		}
		return null;
		
	}
	
	public void removeGroup(Group group)
	{
		Connection con = null;
		Statement statement = null;
		try
		{
			con = connect();
			statement = con.createStatement();
			String sql  = "DELETE FROM TBL_GROUP WHERE ID ="+group.getId();
			statement.executeUpdate(sql);
		}
		catch(Exception exception)
		{
			exception.printStackTrace();
		}
		finally
		{
			closeConnections(con, statement);
		}
		
	}
	
	public void removeGroupAccounts(Group group)
	{
		Connection con = null;
		Statement statement = null;
		try
		{
			con = connect();
			statement = con.createStatement();
			String sql  = "DELETE FROM TBL_ACCOUNT WHERE GROUPID ="+group.getId();
			statement.executeUpdate(sql);
		}
		catch(Exception exception)
		{
			exception.printStackTrace();
		}
		finally
		{
			closeConnections(con, statement);
		}
		
	}
	
	public void removeAccount(Account account)
	{
		Connection con = null;
		Statement statement = null;
		try
		{
			con = connect();
			statement = con.createStatement();
			String sql  = "DELETE FROM TBL_ACCOUNT WHERE ID ="+account.getID();
			statement.executeUpdate(sql);
		}
		catch(Exception exception)
		{
			exception.printStackTrace();
		}
		finally
		{
			closeConnections(con, statement);
		}
		
	}
	
	
	public int getNextId() 
	{
		Connection con = null;
		Statement statement = null;
		try
		{
			con = connect();
			statement = con.createStatement();
			String sql  = "SELECT MAX(ID) AS ID FROM TBL_GROUP";
			ResultSet rs = statement.executeQuery(sql);
			int id = 0;
			if(rs.next()) 
			{
				id = rs.getInt("ID");
			
			}
			return id;
		}
		catch(Exception exception)
		{
			exception.printStackTrace();
		}
		finally
		{
			closeConnections(con, statement);
		}
		return 0;
	}
	
	public void setEncryptionKey(String key)
	{
		VyperSecurity.getInstance().setEncryptionKey(key);
	}
}
