package com.vypersw.passlock.core;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.FlowLayout;
import javax.swing.JButton;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;
import net.miginfocom.swing.MigLayout;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JCheckBox;
import javax.swing.JTextPane;
import java.awt.datatransfer.*;
import java.awt.Toolkit;

public class AccountGui extends JFrame {

	private JPanel contentPane;
	private JTextField accountField;
	private JTextField urlField;
	private JPasswordField passwordField;
	private JCheckBox showBox;
	private MainGUI mainGUI;
	private SQLUtility sqlite;
	private JTextField userField;
	private VyperSecurity vsec;

	/**
	 * Create the frame.
	 * @param mainGUI 
	 */
	public AccountGui(MainGUI mainGUI, Account account) {		
		sqlite = new SQLUtility();
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 600, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		this.mainGUI = mainGUI;
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Details", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		contentPane.add(panel, BorderLayout.NORTH);
		panel.setLayout(new MigLayout("", "[][grow]", "[][][][]"));
		
		JLabel accountLabel = new JLabel("Account:");
		panel.add(accountLabel, "cell 0 0,alignx trailing");
		
		accountField = new JTextField();
		accountField.setEditable(false);
		panel.add(accountField, "cell 1 0,growx");
		accountField.setColumns(10);
		accountField.setText(account.getName());
		
		JLabel lblUsername = new JLabel("Username:");
		panel.add(lblUsername, "cell 0 1,alignx trailing");
		
		userField = new JTextField();
		userField.setEditable(false);
		panel.add(userField, "cell 1 1,growx");
		userField.setColumns(10);
		userField.setText(account.getUsername());
		
		JLabel urlLabel = new JLabel("URL:");
		panel.add(urlLabel, "cell 0 2,alignx trailing");
		
		urlField = new JTextField();
		urlField.setEditable(false);
		panel.add(urlField, "cell 1 2,growx");
		urlField.setColumns(10);
		urlField.setText(account.getUrl());
		
		JLabel passField = new JLabel("Password:");
		panel.add(passField, "cell 0 3,alignx trailing");
		
		passwordField = new JPasswordField();		
		passwordField.setEchoChar('*');		
		passwordField.setEditable(false);
		passwordField.setText(account.getPassword());
		panel.add(passwordField, "flowx,cell 1 3,growx");		
		
		JButton btnGenerate = new JButton("Generate");
		btnGenerate.setEnabled(false);
		panel.add(btnGenerate, "cell 1 3");
		btnGenerate.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				int choice = JOptionPane.showConfirmDialog(null, "Warning! This will delete the current password! Continue?");
				if(choice == 0)
				{
					passwordField.setText(vsec.getInstance().generatePass());
				}				
			}
		});
		
		JCheckBox showBox = new JCheckBox("Show Password");
		panel.add(showBox, "cell 1 3");		
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "Other Information", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		contentPane.add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(new MigLayout("", "[32px][grow]", "[14px,grow]"));
		
		JLabel notesLabel = new JLabel("Notes:");
		panel_1.add(notesLabel, "cell 0 0,alignx left,aligny top");
		
		JTextPane notesPane = new JTextPane();
		notesPane.setEditable(false);
		panel_1.add(notesPane, "cell 1 0,grow");
		notesPane.setText(account.getNotes());
		
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new TitledBorder(null, "Options", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		contentPane.add(panel_2, BorderLayout.SOUTH);
		
		JButton changeButton = new JButton("Change Details");
		changeButton.setToolTipText("Click to enable changing fields.");
		panel_2.add(changeButton);
		
		JButton copyButton = new JButton("Copy password to Clipboard");
		copyButton.setToolTipText("Copies password to clipboard.");
		copyButton.setEnabled(true);
		panel_2.add(copyButton);
		
		copyButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				StringSelection selectedString = new StringSelection(account.getPassword());
				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				clipboard.setContents(selectedString, null);
			}
		});
		
		JButton deleteButton = new JButton("Delete Account");
		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this account?");
				if(choice == 0)
				{
					account.getGroup().removeAccount(account);
					sqlite.removeAccount(account);
					mainGUI.updateList();
					dispose();
				}
			}
		});
		
		
		panel_2.add(deleteButton);
				
		JButton saveButton = new JButton("Save Details");
		saveButton.setToolTipText("Details must be changeable before being able to be saved.");
		saveButton.setEnabled(false);
		panel_2.add(saveButton);
		
		changeButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				copyButton.setEnabled(false);
				changeButton.setEnabled(false);
				saveButton.setEnabled(true);
				btnGenerate.setEnabled(true);
				accountField.setEditable(true);
				userField.setEditable(true);
				urlField.setEditable(true);
				notesPane.setEditable(true);
				passwordField.setEditable(true);
			}
		});
		
		saveButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				copyButton.setEnabled(true);
				changeButton.setEnabled(true);
				saveButton.setEnabled(false);
				btnGenerate.setEnabled(false);
				accountField.setEditable(false);
				userField.setEditable(false);
				urlField.setEditable(false);
				notesPane.setEditable(false);
				passwordField.setEditable(false);
				
				//Need to find way to check for white spaces in password field! User can currently put all spaces for password!
				if(accountField.getText().trim().length()>0 && passwordField.getPassword().length>0 && userField.getText().length()>0)
				{
					account.update(accountField.getText(), userField.getText(), passwordField.getPassword(), urlField.getText(), notesPane.getText());
					sqlite.updateAccount(account);
				}
		
				else
				{
					JOptionPane.showMessageDialog(null, "Error: Name field and Password field cannot be blank.");
					accountField.setText(account.getName());
					urlField.setText(account.getUrl());
					notesPane.setText(account.getNotes());
					passwordField.setText(account.getPassword());
				}
			}
		});
		
		showBox.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) 
			{
				JCheckBox box = (JCheckBox) e.getSource();
				
				if(box.isSelected())
				{
					passwordField.setEchoChar((char) 0);
				}
				else
				{
					passwordField.setEchoChar('*');
				}
			}
		});		
	}
	
	

}
