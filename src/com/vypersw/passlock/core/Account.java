package com.vypersw.passlock.core;

public class Account 
{
	private String name;
	private String username;
	private String password;
	private String url;
	private String notes;
	private int ID;
	private int groupID;
	private Group group;
	
	public Account(String name, String username, String password)
	{
		this.name = name;
		this.username = username;
		this.password = password;
	}
	
	public Account(int ID, int groupID, String username, String name, String password, String url, String notes)
	{
		this.ID = ID;
		this.groupID = groupID;
		this.name = name;
		this.username = username;
		this.password = password;
		this.url = url;
		this.notes = notes;
	}
	
	public String getName() 
	{
		return name;
	}
	
	public void setName(String name) 
	{
		this.name = name;
	}
	
	public String getPassword() 
	{
		return password;
	}
	
	public void setPassword(String password) 
	{
		this.password = password;
	}	

	public String getUrl() 
	{
		return url;
	}
	
	public void setUrl(String url) 
	{
		this.url = url;
	}

	public String getNotes() 
	{
		return notes;
	}

	public void setNotes(String notes) 
	{
		this.notes = notes;
	}

	public int getID() 
	{
		return ID;
	}

	public void setID(int iD) 
	{
		ID = iD;
	}

	public int getGroupID() 
	{
		return groupID;
	}
	
	public Group getGroup()
	{
		return group;
	}
	
	public void setGroup(Group group)
	{
		this.group = group;
	}

	public void setGroupID(int groupID) 
	{
		this.groupID = groupID;
	}
	
	public void update(String name, String username, char[] password, String url, String notes)
	{
		this.name = name;
		this.username = username;
		this.password = String.valueOf(password);
		this.url = url;
		this.notes = notes;
	}

	public String getUsername() 
	{
		return username;
	}

	public void setUsername(String username) 
	{
		this.username = username;
	}
	
	
	
	
}
