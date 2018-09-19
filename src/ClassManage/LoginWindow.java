package ClassManage;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class LoginWindow implements ActionListener
{
	private JFrame frame = new JFrame("Welcome City-Bus Service");
	
	private JPanel panelUp = new JPanel();
	private JPanel panelDown = new JPanel();
	
	private Font font = new Font("Serif",Font.BOLD,22);
	
	private JMenu menu = new JMenu("Menu");
	
	private JMenuBar mb = new JMenuBar();
	
	private JMenuItem companyLogin = new JMenuItem("Company Login");
	private JMenuItem userLogin = new JMenuItem("User Login");
	private JMenuItem employeeLogin = new JMenuItem("Employee Login");
	private JMenuItem userAccount = new JMenuItem("Create User Account");
	private JMenuItem about = new JMenuItem("About");
	private JMenuItem exit = new JMenuItem("Exit");
	
	private JLabel imageLabel = new JLabel();
	ImageIcon icon = new ImageIcon("Bus.png");
	
	public LoginWindow(){}
	
	public void iniwindow()
	{
		BorderLayout upperLayout = new BorderLayout();
		BorderLayout frameLayout = new BorderLayout();
		
		panelUp.setLayout(upperLayout);
		frame.setLayout(frameLayout);
		
		menu.setFont(font); 
		userAccount.setFont(font);
		userAccount.setBackground(Color.green);
		about.setFont(font);
		about.setBackground(Color.yellow);
		exit.setFont(font);
		exit.setBackground(Color.blue);
		
		menu.add(companyLogin);
		menu.add(userLogin);
		menu.add(employeeLogin);
				
		mb.setBackground(Color.red);
		mb.add(menu);
		mb.add(userAccount);
		mb.add(about);
		mb.add(exit);
			
		panelUp.add(mb);
		
		imageLabel.setPreferredSize(new Dimension(750,500));
		
		imageLabel.setIcon(icon);	
		panelDown.add(imageLabel);

		frame.add(panelUp,frameLayout.NORTH);
		frame.add(panelDown,frameLayout.CENTER);	
	
		frame.setSize(800, 600);
		//frame.setExtendedState(frame.MAXIMIZED_BOTH);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		companyLogin.addActionListener(this);
		userLogin.addActionListener(this);
		employeeLogin.addActionListener(this);
		userAccount.addActionListener(this);
		about.addActionListener(this);
		exit.addActionListener(this);
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource() == companyLogin)
		{
			JOptionPane.showMessageDialog(null, "Company Login");
			CompanyLoginWindow clw = new CompanyLoginWindow();
			clw.iniWindow();
		}
		else if(e.getSource() == userLogin)
		{
			JOptionPane.showMessageDialog(null, "User Login");
			UserLoginWindow ulw = new UserLoginWindow();
			ulw.iniwindow();
			//frame.dispose();
		}
		else if(e.getSource() == employeeLogin)
		{
			JOptionPane.showMessageDialog(null, "Employee Login");
			EmployeeLoginWindow elw = new EmployeeLoginWindow();
			elw.iniwindow();
		}
		else if(e.getSource() == userAccount)
		{
			JOptionPane.showMessageDialog(null, "User Account Create");
			UserAccountCreate uac =  new UserAccountCreate();
			uac.iniwindow();
					
		}
		else if(e.getSource() == about)
		{
			JOptionPane.showMessageDialog(null, "about");
		}
		else if(e.getSource() == exit)
		{
			frame.dispose();
		}		
	}
}
