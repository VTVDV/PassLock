package com.vypersw.passlock.core;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import net.miginfocom.swing.MigLayout;
import javax.swing.JCheckBox;
import javax.swing.JTextPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JPasswordField;
import javax.swing.JComboBox;

public class CreateAccountGui extends JFrame {

	private JPanel contentPane;
	private JTextField nameField;
	private JTextField urlField;
	private JPasswordField passField;
	private JPanel otherPanel;
	private JLabel noteLabel;
	private JTextPane notePane;
	private JPanel buttonPanel;
	private JButton createButton;
	private MainGUI mainGUI;
	private JCheckBox showBox;
	private JLabel groupLabel;
	private JComboBox groupCombo;
	private GroupManager shingleton;
	private SQLUtility sqlite;
	private JLabel lblUsername;
	private JTextField userField;
	private JButton btnGenerate;
	private VyperSecurity vsec;

	/**
	 * Create the frame.
	 */
	public CreateAccountGui(final MainGUI mainGUI) 
	{
		shingleton = GroupManager.getInstance();
		sqlite = new SQLUtility();
		this.mainGUI = mainGUI;
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 459, 316);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);		
		
		JPanel infoPanel = new JPanel();
		infoPanel.setBorder(new TitledBorder(null, "Information", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		contentPane.add(infoPanel, BorderLayout.NORTH);
		infoPanel.setLayout(new MigLayout("", "[][grow]", "[][][][][]"));
		
		JLabel nameLabel = new JLabel("Account Name:");
		infoPanel.add(nameLabel, "cell 0 0,alignx trailing");
		
		nameField = new JTextField();
		infoPanel.add(nameField, "cell 1 0,growx");
		nameField.setColumns(10);
		
		lblUsername = new JLabel("Username:");
		infoPanel.add(lblUsername, "cell 0 1,alignx trailing");
		
		userField = new JTextField();
		infoPanel.add(userField, "cell 1 1,growx");
		userField.setColumns(10);
		
		JLabel urlLabel = new JLabel("URL:");
		infoPanel.add(urlLabel, "cell 0 2,alignx trailing");
		
		urlField = new JTextField();
		infoPanel.add(urlField, "cell 1 2,growx");
		urlField.setColumns(10);
		
		JLabel passwordLabel = new JLabel("Password:");
		infoPanel.add(passwordLabel, "cell 0 3,alignx trailing");
		
		passField = new JPasswordField();
		infoPanel.add(passField, "flowx,cell 1 3,growx");
		passField.setColumns(10);
		
		btnGenerate = new JButton("Generate");
		infoPanel.add(btnGenerate, "cell 1 3");
		btnGenerate.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				passField.setText(vsec.getInstance().generatePass());
			}
		});
		
		showBox = new JCheckBox("Show Password");
		infoPanel.add(showBox, "cell 1 3");
		
		groupLabel = new JLabel("Group:");
		infoPanel.add(groupLabel, "cell 0 4,alignx trailing");
		
		groupCombo = new JComboBox();
		shingleton.getGroups();
		for(Group g: shingleton.getGroups())
		{
			groupCombo.addItem(g.getName());
		}
		groupCombo.setSelectedIndex(shingleton.getLastSelected());
		
		infoPanel.add(groupCombo, "cell 1 4,growx");
		showBox.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) 
			{
				JCheckBox box = (JCheckBox) e.getSource();
				
				if(box.isSelected())
				{
					passField.setEchoChar((char) 0);
				}
				else
				{
					passField.setEchoChar('*');
				}
			}
		});
		
		otherPanel = new JPanel();
		otherPanel.setBorder(new TitledBorder(null, "Other", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		contentPane.add(otherPanel, BorderLayout.CENTER);
		otherPanel.setLayout(new MigLayout("", "[32px][grow]", "[14px,grow]"));
		
		noteLabel = new JLabel("Notes:");
		otherPanel.add(noteLabel, "cell 0 0,alignx left,aligny top");
		
		notePane = new JTextPane();
		otherPanel.add(notePane, "cell 1 0,grow");
		
		buttonPanel = new JPanel();
		buttonPanel.setBorder(null);
		contentPane.add(buttonPanel, BorderLayout.SOUTH);
		
		createButton = new JButton("Create Account");
		createButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				String p = new String(passField.getPassword());
				if(nameField.getText().trim().length()>0 && p.trim().length()>0 && userField.getText().trim().length()>0)
				{
					Account account = new Account(nameField.getText(), userField.getText(), p);
					p = null;
					passField.setText(null);
					account.setUrl(urlField.getText());
					account.setNotes(notePane.getText());	
					Group group = shingleton.findByName((String)groupCombo.getSelectedItem());
					account.setGroupID(group.getId());
					group.addAccount(account);
					sqlite.createAccount(account);
					mainGUI.listModel.clear();
					mainGUI.getGroupList(group);
					mainGUI.setCombo(groupCombo.getSelectedIndex());
					dispose();					
				}
		
				else
				{
					JOptionPane.showMessageDialog(null, "Error: Name field and Password field cannot be blank.");					
				}
			}
		});
		buttonPanel.add(createButton);
	}

}
