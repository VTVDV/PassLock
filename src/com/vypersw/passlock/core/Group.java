package com.vypersw.passlock.core;

import java.util.ArrayList;
import java.util.List;

public class Group 
{
	private String name;
	private int id;
	private List<Account> accounts;
	
	public Group(String name, int id)
	{
		accounts = new ArrayList<Account>();
		this.name = name;
		this.id = id;
	}
	
	public Group(String name)
	{
		accounts = new ArrayList<Account>();
		this.name = name;
	}
	
	public String getName() 
	{
		return name;
	}
	
	public void setName(String name) 
	{
		this.name = name;
	}
	
	public int getId() 
	{
		return id;
	}
	
	public void setId(int id) 
	{
		this.id = id;
	}

	public List<Account> getAccounts() 
	{
		return accounts;
	}
		
	public void addAccount(Account account)
	{
		accounts.add(account);
		account.setGroup(this);
	}
	
	public void removeAccount(Account account)
	{
		accounts.remove(account);
	}
	
}
