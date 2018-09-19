package ClassManage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.*;

public class UserLoginWindow implements ActionListener
{
	private String ID;       //user id
	private String Password; //password
	private String UserName;
	
	public Connection con; // mysql connection var
	
	private JFrame frame = new JFrame("User Login To Account");
	
	private JPanel panel = new JPanel();
	private JPanel ImagePanel = new JPanel();
	
	private Font font = new Font("Serif",Font.BOLD,18);
	
	private JLabel userIDLabel = new JLabel("Enter Your ID : ");
	private JLabel userPassLabel = new JLabel("Enter Your Password : ");
	
	private JTextField userIDText = new JTextField("Enter ID Here");
	private JPasswordField userPassText = new JPasswordField();
	
	private JButton Login = new JButton("Login");
	private JButton clear = new JButton("CLEAR");
	
	private JLabel imageLabel = new JLabel();
	ImageIcon icon = new ImageIcon("future-bus.png");
	
	public UserLoginWindow(){}
	
	
	public void iniwindow()
	{
		GridLayout gl = new GridLayout(3,2,30,10);
		BorderLayout middleLayout = new BorderLayout();
		
		panel.setLayout(gl);
		frame.setLayout(middleLayout);
		
		userIDLabel.setFont(font);
		userPassLabel.setFont(font);
		
		Login.setBackground(Color.LIGHT_GRAY);
		clear.setBackground(Color.LIGHT_GRAY);
		
		panel.add(userIDLabel);
		panel.add(userIDText);
		panel.add(userPassLabel);
		panel.add(userPassText);
		panel.add(Login);
		panel.add(clear);
		
		panel.setBackground(Color.green);
		
		imageLabel.setPreferredSize(new Dimension(600,500));
		
		imageLabel.setIcon(icon);
		ImagePanel.add(imageLabel);
		
		frame.add(ImagePanel,middleLayout.NORTH);
		frame.add(panel,middleLayout.SOUTH);
		
		frame.setSize(800, 650);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Login.addActionListener(this);
		clear.addActionListener(this);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if(e.getSource() == Login)
		{
			
			ID = userIDText.getText();
			Password = userPassText.getText();
			getConnection();
		}
		else if(e.getSource() == clear)
		{
			userIDText.setText(null);
			userPassText.setText(null);
		}
	}
	
	public void getConnection()
	{
//		String url = "jdbc:mysql://localhost/citybusmgnt";
//		String userName = "root";
//		String pass = "";
		
		String url = "jdbc:mysql://198.54.116.34:2083/softgtyc_citybusmgnt";
		String userName = "users";
		String pass = "citybusmgnt";
		
		String query = "select * from user;";
		
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			
			con = DriverManager.getConnection(url,userName,pass);
			JOptionPane.showMessageDialog(null, "Connected to DataBase");
			
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(query);
			
			//System.out.println("--------------------");
			
			boolean key = true;
			
			while(rs.next())
			{				
				if(rs.getString(3).equals(Password) && rs.getString(1).equals(ID))
				{
					JOptionPane.showMessageDialog(null, "Login Success" + ""
							+ "\n" + "ID " + ID + "\nName : " + rs.getString(2));
					UserName = rs.getString(2);
					
					key = true;
					break;
				}
				else
				{
					key = false;
				}
			}
			
			if(key == false)
				JOptionPane.showMessageDialog(null, "Login unSuccessfull");
			else
			{
				srcDesDecide sd = new srcDesDecide(UserName,ID);
				sd.iniwindow();
			}
			
			rs.close();
		}
		catch(Exception e)
		{
			System.out.println(e.toString());
		}
	}
}
