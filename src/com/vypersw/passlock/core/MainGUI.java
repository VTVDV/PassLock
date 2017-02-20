package com.vypersw.passlock.core;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.JButton;
import net.miginfocom.swing.MigLayout;
import javax.swing.JList;
import javax.swing.JOptionPane;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import javax.swing.JLabel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;

import java.awt.Component;
import javax.swing.JComboBox;
import javax.swing.UIManager;
import java.awt.Color;
import javax.swing.ListSelectionModel;
import javax.swing.JCheckBox;

public class MainGUI extends JFrame 
{

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private GroupManager shingleton;
	private MainGUI gui = this;
	private JComboBox groupCombo;
	private JCheckBox allBox;
	private SQLUtility sqlite;
	JList accList;
	DefaultListModel listModel = new DefaultListModel();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) 
	{
		EventQueue.invokeLater(new Runnable() 
		{
			public void run() 
			{
				try 
				{
					MainGUI frame = new MainGUI();
					frame.setVisible(true);
				} catch (Exception e) 
				{
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainGUI() 
	{
		sqlite = new SQLUtility();
		
		File file = new File("passlock.db");
		if(file != null && !file.exists())
		{
			sqlite.createGroupsTable();
			sqlite.createAccountsTable();
		}
		FileUtils.getInstance().loadProperties(sqlite);
		
		shingleton = GroupManager.getInstance();
		shingleton.setGroups(sqlite.getGroups());
		for(Group group : shingleton.getGroups())
		{
			sqlite.getAccounts(group);
		}
		if(shingleton.getGroups().size()>0)
		{
			getGroupList(shingleton.getGroups().get(0));	
		}
 		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 350);		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Accounts", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout(0, 0));
		
		JLabel accountsLabel = new JLabel("Double-click on an account below for more details:");
		panel.add(accountsLabel, BorderLayout.NORTH);
		
		accList = new JList<Account>(listModel);
		accList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		panel.add(accList, BorderLayout.CENTER);
		accList.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent event)
			{
				JList list = (JList)event.getSource();
				if(event.getClickCount() == 2)
				{
					new AccountGui(gui, ((Account) list.getSelectedValue()) ).setVisible(true);
				}
			}
		});
		accList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> accList, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component renderer = super.getListCellRendererComponent(accList, value, index, isSelected, cellHasFocus);
                if (renderer instanceof JLabel && value instanceof Account) {
                    // Here value will be of the Type 'CD'
                    ((JLabel) renderer).setText(((Account) value).getName());
                }
                return renderer;
            }
        });
		
		JPanel groupPanel = new JPanel();
		contentPane.add(groupPanel, BorderLayout.SOUTH);
				
		
		
		JLabel groupLabel = new JLabel("Show this group:");
		groupPanel.add(groupLabel);
		
		groupCombo = new JComboBox();
		for (Group g: shingleton.getGroups())
		{
			groupCombo.addItem(g.getName());
		}
		groupPanel.add(groupCombo);
		groupCombo.addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent e) 
			{
				if(!allBox.isSelected())
				{
					updateList();
				}
			}
			
		});
		setWidth();
		
		allBox = new JCheckBox("Show All Accounts");
		groupPanel.add(allBox);
		allBox.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) 
			{
				if(allBox.isSelected())
				{
					listModel.clear();
					getAllList();	
				}
				else
				{
					listModel.clear();
					Group group = shingleton.findByName((String) groupCombo.getSelectedItem());
					getGroupList(group);	
				}
							
			}
			
		}
		);
		
		JPanel createPanel = new JPanel();
		createPanel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Options", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		contentPane.add(createPanel, BorderLayout.WEST);
		createPanel.setLayout(new MigLayout("", "[95px]", "[][][][][199px][][]"));
		
		JButton groupButton = new JButton("Create Group");
		createPanel.add(groupButton, "cell 0 0");
		groupButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				String newGroup = JOptionPane.showInputDialog("Please enter a name for the group:");
				if(newGroup.trim().length() > 0)
				{
					Group group = new Group(newGroup);
					shingleton.addGroup(group);					
					sqlite.createGroup(group);
					group.setId(sqlite.getNextId());
					groupCombo.addItem(group.getName());
					groupCombo.setSelectedItem(group.getName());
					shingleton.setLastSelected(groupCombo.getSelectedIndex());
					System.out.println(group.getId());
					setWidth();
				}
				else
				{
					JOptionPane.showMessageDialog(null, "Error: The group name cannot be blank.");
				}
			}
		});
		
		JButton renameButton = new JButton("Rename Selected Group");
		groupPanel.add(renameButton);
		
		JButton btnDeleteSelectedGroup = new JButton("Delete Selected Group");
		groupPanel.add(btnDeleteSelectedGroup);
		btnDeleteSelectedGroup.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this group? All accounts in this group will also be deleted!");
				if(choice == 0)
				{
					Group removed = shingleton.findByName((String)groupCombo.getSelectedItem());
					groupCombo.removeItem(groupCombo.getSelectedItem());
					shingleton.removeGroup(removed);
					sqlite.removeGroupAccounts(removed);
					sqlite.removeGroup(removed);
					if(groupCombo.getItemCount() > 1)
					{
						groupCombo.setSelectedIndex(-1);
					}
					else
					{
						groupCombo.setSelectedIndex(0);
					}
					updateList();
				}
			}
		});
		
		renameButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) 
			{
				if(groupCombo.getSelectedIndex() == -1)
				{
					JOptionPane.showMessageDialog(null, "Error: Invalid Selection");
				}
				else
				{
					String name = JOptionPane.showInputDialog("Please enter a new name for the group " + groupCombo.getSelectedItem());
					String toReplace = (String) groupCombo.getSelectedItem();
					int replaceIndex = groupCombo.getSelectedIndex();
					if(name.trim().length() > 0)
					{
						for(Group group: shingleton.getGroups())
						{
							if(group.getName().equals(toReplace))
							{
								group.setName(name);;
							}
						}
						groupCombo.insertItemAt(name, replaceIndex);
						groupCombo.removeItem(toReplace);
					}
					else
					{
						JOptionPane.showMessageDialog(null, "Error: The group name cannot be blank.");						
					}
					
				}
			}			
			
		});
		
		JButton accountButton = new JButton("New Account");
		accountButton.addActionListener(new ActionListener() 
		{

			public void actionPerformed(ActionEvent e) 
			{
				if(groupCombo.getItemCount() < 1)
				{
					JOptionPane.showMessageDialog(null, "Error: A group needs to be created first.");		
				}
				else
				{
					new CreateAccountGui(gui).setVisible(true);
				}				
			}
		});
		createPanel.add(accountButton, "cell 0 2");
	}
	
	public void addToList(Account account)
	{
		listModel.addElement(account);
	}
	
	public void getGroupList(Group group)
	{
		for(Account a:group.getAccounts())
		{
			addToList(a);
		}
	}
	
	public void getAllList()
	{
		for(Group g:shingleton.getGroups())
		{
			getGroupList(g);
		}
	}
	
	public void setCombo(int index)
	{
		groupCombo.setSelectedIndex(index);		
		
	}
	
	public void setWidth()
	{
		int extraLength = 0;
		for(Group group : shingleton.getGroups())
		{
			if(group.getName().length() > extraLength)
			{
				extraLength = group.getName().length();
			}
		}
		
		setBounds(100, 100, 600+(extraLength*10), 350);
	}
	
	public void updateList()
	{
		if(groupCombo.getComponentCount()>0)
		{
			shingleton.setLastSelected(groupCombo.getSelectedIndex());
			listModel.clear();
			Group group = shingleton.findByName((String) groupCombo.getSelectedItem());
			getGroupList(group);			
			setWidth();
		}
	}
}
