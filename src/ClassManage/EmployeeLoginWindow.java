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

public class EmployeeLoginWindow implements ActionListener
{
	private String ID;       //user id
	private String Password; //password
	private String assistantName; 
	
	public Connection con; // mysql connection var
	
	private JFrame frame = new JFrame("Employee Login To Account");

	private JPanel panel = new JPanel();
	private JPanel ImagePanel = new JPanel();
	
	private Font font = new Font("Serif",Font.BOLD,18);
	
	private JLabel employeeIDLabel = new JLabel("Enter Your ID : ");
	private JLabel employeePassLabel = new JLabel("Enter Your Password : ");
	
	private JTextField employeeIDText = new JTextField("Enter ID Here");
	private JPasswordField employeePassText = new JPasswordField();
	
	private JButton Login = new JButton("Login");
	private JButton clear = new JButton("CLEAR");
	
	private JLabel imageLabel = new JLabel();
	ImageIcon icon = new ImageIcon("EmployeeBus.png");
	
	public EmployeeLoginWindow(){}
	
	public void iniwindow()
	{
		GridLayout gl = new GridLayout(3,2,30,10);
		BorderLayout middleLayout = new BorderLayout();
		
		panel.setLayout(gl);
		frame.setLayout(middleLayout);
		
		employeeIDLabel.setFont(font);
		employeePassLabel.setFont(font);
		
		panel.add(employeeIDLabel);
		panel.add(employeeIDText);
		panel.add(employeePassLabel);
		panel.add(employeePassText);
		panel.add(Login);
		panel.add(clear);
		
		imageLabel.setPreferredSize(new Dimension(750,500));
		
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
			
			ID = employeeIDText.getText();
			Password = employeePassText.getText();
			getConnection();
		}
		else if(e.getSource() == clear)
		{
			employeeIDText.setText(null);
			employeePassText.setText(null);
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
		
		String query = "select * from employee;";
		
		String assistantID;
		
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			
			con = DriverManager.getConnection(url,userName,pass);
			//JOptionPane.showMessageDialog(null, "Connected to DataBase");
			
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(query);
			
			//System.out.println("--------------------");
			
			boolean key = true;
			
			while(rs.next())
			{				
				if(rs.getString(4).equals(Password) && rs.getString(1).equals(ID))
				{
					JOptionPane.showMessageDialog(null, "Login Success" + ""
							+ "\n" + "ID : " + ID + "\nName : " + rs.getString(2));
					key = true;
					assistantName = rs.getString(2);
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
				String query1 = "select id from assistant where employee_id = " + Integer.parseInt(ID);
				ResultSet rs1 = st.executeQuery(query1);
				rs1.next();
				//System.out.println(rs1.getInt(1));
				
				//JOptionPane.showMessageDialog(null, "Login Successfull");
				employeeActivityWindow eaw = new employeeActivityWindow(Integer.toString(rs1.getInt(1)),assistantName);
				eaw.iniwindow();
			}
			
			con.close();
		}
		catch(Exception e)
		{
			System.out.println(e.toString());
		}
	}
}
