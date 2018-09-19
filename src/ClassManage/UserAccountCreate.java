package ClassManage;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class UserAccountCreate implements ActionListener
{
	private int ID;
	private String Name;
	private String Password;
	private String Phone;
	
	private Connection con; // mysql connection var
	
	private JFrame frame = new JFrame("Create User Account");

	private JPanel EntryPanel = new JPanel();
	private JPanel ImagePanel = new JPanel();
	
	private Font font = new Font("Serif",Font.BOLD,18);
	
	private JLabel userName = new JLabel("Enter Your Name : ");
	private JLabel userPhone = new JLabel("Enter Your Phone No. : ");
	private JLabel userPassword = new JLabel("Enter Your Password : ");
	
	private JTextField userNameText = new JTextField("Enter Name Here");
	private JPasswordField userPasswordText = new JPasswordField();
	private JTextField userPhoneText = new JTextField("Enter Phone No. Here");
	
	private JButton create = new JButton("Create Account");
	private JButton clear = new JButton("CLEAR");
	
	private JLabel imageLabel = new JLabel();
	ImageIcon icon = new ImageIcon("CreateAccount.png");
	
	
	public UserAccountCreate(){}
	
	public void iniwindow()
	{
		GridLayout gl = new GridLayout(4,2,10,10);
		BorderLayout middleLayout = new BorderLayout();
		
		EntryPanel.setLayout(gl);
		frame.setLayout(middleLayout);
		
		userName.setFont(font);
		userPhone.setFont(font);
		userPassword.setFont(font);
		
		EntryPanel.add(userName);
		EntryPanel.add(userNameText);
		EntryPanel.add(userPassword);
		EntryPanel.add(userPasswordText);
		EntryPanel.add(userPhone);
		EntryPanel.add(userPhoneText);
		EntryPanel.add(create);
		EntryPanel.add(clear);
		
		imageLabel.setPreferredSize(new Dimension(550,400));
		
		imageLabel.setIcon(icon);	
		ImagePanel.add(imageLabel);
		
		frame.add(ImagePanel,middleLayout.NORTH);
		frame.add(EntryPanel,middleLayout.SOUTH);
		
		frame.setSize(750, 600);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		create.addActionListener(this);
		clear.addActionListener(this);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if(e.getSource() == create)
		{
			Name = userNameText.getText();
			Password = userPasswordText.getText();
			Phone = userPhoneText.getText();
			
			getConnection();
		}
		else if(e.getSource() == clear)
		{
			userNameText.setText(null);
			userPasswordText.setText(null);
			userPhoneText.setText(null);
		}
		
	}
		
	public void getConnection()
	{
//		String url = "jdbc:mysql://localhost/citybusmgnt";
//		String userName = "root";
//		String pass = "";
		
		String url = "jdbc:mysql://198.54.116.34/softgtyc_citybusmgnt";
		String userName = "users";
		String pass = "citybusmgnt";
		
		String var = null;	
		String query = null;
		
		String getIDquery = "Select count(ID) from user;";
		
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			
			con = DriverManager.getConnection(url,userName,pass);
			JOptionPane.showMessageDialog(null, "Connected to DataBase");
			
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(getIDquery);
			
			rs.next();
			ID = rs.getInt(1) + 1;
			
			var = Integer.toString(ID) + ",'" + Name + "','" + Password + "','" + Phone + "',"+ "0," + "0," + "0";
			query = "INSERT INTO USER VALUES(" + var + ")";
			
			st.execute(query);
			
			JOptionPane.showMessageDialog(null, "Data Entry success \nYou ID : " + ID + "\nPlease Remember it");
			
			con.close();
		}
		catch(Exception e)
		{
			System.out.println(e.toString());
		}
	}

}
