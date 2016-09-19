package com.vypersw.passlock.core;

import java.util.ArrayList;
import java.util.List;

public class GroupManager 
{
	private static GroupManager instance = null;
	private List<Group> groups;
	private int lastSelected;
	
	private GroupManager()
	{
		groups = new ArrayList<Group>();
	}
	
	public static GroupManager getInstance()
	{
		if(instance == null)
		{
			instance = new GroupManager();
		}
		return instance;
	}

	public List<Group> getGroups() {
		return groups;
	}

	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}
	
	public void addGroup(Group group)
	{
		groups.add(group);
	}
	
	public void removeGroup(Group group)
	{
		for(Account acc:group.getAccounts())
		{
			
		}
		groups.remove(group);
	}
	
	public Group findByName(String name)
	{
		for(Group g: groups)
		{
			if(g.getName().equalsIgnoreCase(name))
			{
				return g;
			}
			
		}
		return null;
	}
		
	public void setLastSelected(int index)
	{
		lastSelected = index;
	}
	
	public int getLastSelected()
	{
		return lastSelected;
	}

}
