package ClassManage;

import java.awt.BorderLayout;
import java.sql.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

import com.mysql.jdbc.Connection;

public class employeeActivityWindow implements ActionListener,ItemListener
{
	private String driverName;
	private String assistantName;
	private String busID;
	private String busStatus;
	private String routeNumber;
	
	private String userName;
	private String ticketNumber;
	private String from;
	private String to;
	private String fare;
	
	private String newLocation;
	private String newStatus;
	
	private java.sql.Connection con;
	
	private JFrame frame = new JFrame("Employee Activity");
	
	private JPanel panelUp = new JPanel();
	private JPanel panelDown = new JPanel();
	
	private Font font = new Font("Serif",Font.ITALIC,22);
	
	private JTextArea middleTxtArea = new JTextArea(16,53);
	
	private JButton refresh = new JButton("refresh");
	private JButton busHistory = new JButton("Bus History");
	private JButton updateLocation = new JButton("Update Bus Location");
	private JButton updateStatus = new JButton("Update Bus Status");
	
	private JScrollPane txtScrollPane = new JScrollPane(middleTxtArea);
	private Calendar calendar = Calendar.getInstance();
	
	private String[] roadOptions = null;
	private JComboBox LocUpdateCombo;
	private String[] statusOptions = {"Up","Down"};
	private JComboBox statusCombo = new JComboBox(statusOptions);
	
	public void RoadUpdate()
	{
//		String url = "jdbc:mysql://localhost/citybusmgnt";
//		String userName = "root";
//		String pass = "";
		
		String url = "jdbc:mysql://198.54.116.34/softgtyc_citybusmgnt";
		String userName = "users";
		String pass = "citybusmgnt";
		
		String query = "SELECT r.all_stopagges FROM route r, bus b WHERE r.id = b.route_id and b.id = " + busID + " ;";
		
		String stoppages = null;
		int sizeStoppages,countStoppages;
		
		try
		{
			Class.forName("com.mysql.jdbc.Driver");				
			con = DriverManager.getConnection(url,userName,pass);
			//JOptionPane.showMessageDialog(null, "Connected to DataBase");
			
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(query);
			
			rs.next();
			stoppages = rs.getString(1);
			rs.close();
		}
		catch(Exception e)
		{
			System.out.println(e.toString());
		}
		
		sizeStoppages = stoppages.length();
		for(int i=0;i<sizeStoppages;i++)
		{
			roadOptions = stoppages.split(" "); //entry to stoppages
		}
		
		/*
		countStoppages = roadOptions.length;
		for(int i=0;i<countStoppages;i++)
		{
			System.out.println(roadOptions[i]);
		}
		*/
		
		LocUpdateCombo = new JComboBox(roadOptions);
		
	}
	
	public employeeActivityWindow(String ID,String assistantName) //assistant ID
	{
		this.assistantName = assistantName;
		
//		String url = "jdbc:mysql://localhost/citybusmgnt";
//		String userName = "root";
//		String pass = "";
		
		String url = "jdbc:mysql://198.54.116.34/softgtyc_citybusmgnt";
		String userName = "users";
		String pass = "citybusmgnt";
		
		String dID;
		
		
		String query = "select id, driver_id,route_id,bus_status from bus where assistant_id = " + Integer.parseInt(ID);
		String query1 = null;
		String query2 = null;
		
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			
			con = DriverManager.getConnection(url,userName,pass);
			//JOptionPane.showMessageDialog(null, "Connected to DataBase");
			
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(query);
			rs.next();
			busID = rs.getString(1);
			dID = rs.getString(2);
			routeNumber = rs.getString(3);
			busStatus = rs.getString(4);
			
			//System.out.println(busID + "==" + dID);
			query1 = "select employee_id from driver where id = " + dID;
			ResultSet rs1 = st.executeQuery(query1);
			rs1.next();
			
			//System.out.println(rs1.getString(1));
			String edID = rs1.getString(1);
			
			query2 = "select name from employee where id = " + edID;
			ResultSet rs2 = st.executeQuery(query2);
			rs2.next();
			//System.out.println(rs2.getString(1));
			
			driverName = rs2.getString(1);
			
			con.close();
			
			
		}
		catch(Exception e)
		{
			System.out.println(e.toString());
		}
	}
	
	public void iniwindow()
	{
		RoadUpdate();
		
		GridLayout gl = new GridLayout(3,2,5,5);
		BorderLayout bl = new BorderLayout();
		
		panelDown.setLayout(gl);
		frame.setLayout(bl);
			
		refresh.setFont(font);
		busHistory.setFont(font);
		updateLocation.setFont(font);
		updateStatus.setFont(font);
		
		txtScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		middleTxtArea.setBackground(Color.YELLOW);		
		middleTxtArea.setEditable(false);
		middleTxtArea.setFont(font);
		panelUp.add(txtScrollPane);	
		
		
		middleTxtArea.append("\t\tEMPLOYEE INFORMATION ");
		middleTxtArea.append("\n\t    Date                     \t:   " + calendar.getTime());
		middleTxtArea.append("\n\t    Driver Name              \t:   " + driverName);
		middleTxtArea.append("\n\t    Assistant Name           \t:   " + assistantName);
		middleTxtArea.append("\n\t    Route Number             \t:   " + routeNumber);
		middleTxtArea.append("\n\t    Bus Status               \t:   " + busStatus);
				
		panelDown.add(updateLocation);
		panelDown.add(LocUpdateCombo);
		panelDown.add(updateStatus);
		panelDown.add(statusCombo);
		panelDown.add(busHistory);
		panelDown.add(refresh);
		
		frame.add(panelUp,bl.NORTH);
		frame.add(panelDown, bl.SOUTH);
		
		frame.setSize(880, 650);
		//frame.setExtendedState(frame.MAXIMIZED_BOTH);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		refresh.addActionListener(this);
		updateLocation.addActionListener(this);
		updateStatus.addActionListener(this);
		busHistory.addActionListener(this);
		LocUpdateCombo.addItemListener(this);
		statusCombo.addItemListener(this);
		
	}
	
	@Override
	public void itemStateChanged(ItemEvent i) 
	{
		int srcIndex = -1,statusIndex = 0;
		
		srcIndex = LocUpdateCombo.getSelectedIndex();
		statusIndex = statusCombo.getSelectedIndex();
		
		newLocation = roadOptions[srcIndex];
		newStatus = statusOptions[statusIndex];
		
	}
	
	@Override
	public void actionPerformed(ActionEvent a) 
	{
		if(a.getSource() == refresh)
		{			
//			String url = "jdbc:mysql://localhost/citybusmgnt";
//			String userName = "root";
//			String pass = "";
			
			String url = "jdbc:mysql://198.54.116.34/softgtyc_citybusmgnt";
			String userName = "users";
			String pass = "citybusmgnt";
			
			String query = "select u.username,t.generated_code , t.fare , t.departure, t.destination from user u, ticket t "
					+ "where u.id = t.user_id and t.bus_id = " + busID + " ;";
			
			try
			{
				Class.forName("com.mysql.jdbc.Driver");				
				con = DriverManager.getConnection(url,userName,pass);
				//JOptionPane.showMessageDialog(null, "Connected to DataBase");
				
				Statement st = con.createStatement();
				ResultSet rs = st.executeQuery(query);
				
				while(rs.next())
				{
					userName = rs.getString(1);
					ticketNumber = rs.getString(2);
					fare = Float.toString(rs.getFloat(3));
					from = rs.getString(4);
					to = rs.getString(5);
								
					middleTxtArea.append("\n\t\tUSER INFORMATION ");
					middleTxtArea.append("\n\t    User Name               \t:   " + userName);
					middleTxtArea.append("\n\t    Ticket Number           \t:   " + ticketNumber);
					middleTxtArea.append("\n\t    Current Location        \t:   " + from);
					middleTxtArea.append("\n\t    Destination             \t:   " + to);
					middleTxtArea.append("\n\t    Fare                    \t:   " + fare);
				}
				
				rs.close();			
				
			}
			catch(Exception e)
			{
				System.out.println(e.toString());
			}
			
			
		}
		else if(a.getSource() == updateLocation)
		{
//			String url = "jdbc:mysql://localhost/citybusmgnt";
//			String userName = "root";
//			String pass = "";
			
			String url = "jdbc:mysql://198.54.116.34/softgtyc_citybusmgnt";
			String userName = "users";
			String pass = "citybusmgnt";
			
			int seat;
			
		//SELECT u.username from user u, user_history uh, bus b 
	//WHERE uh.bus_id = b.id and uh.user_id = u.id and uh.to = b.current_location and uh.bus_id = 1
			
			
			String query1 = "Select u.username from user u, user_history uh, bus b "
					+ "where uh.bus_id = b.id and uh.user_id = u.id and uh.to = b.current_location "
					+ "and uh.bus_id = " + busID;
			
			String query = "UPDATE bus set current_location = '" + newLocation + "' where id = " + busID;
					
			String query2 = "select seat_taken from bus where id = " + busID;
			String query3 = null;
			
			try
			{
				Class.forName("com.mysql.jdbc.Driver");				
				con = DriverManager.getConnection(url,userName,pass);
				//System.out.println("connected");
				Statement st = con.createStatement();
				st.executeUpdate(query);
				
				
				ResultSet rs = st.executeQuery(query1);
				
				while(rs.next())
				{
					JOptionPane.showMessageDialog(null, rs.getString(1) + " has Arrived the destination !!");
					
					ResultSet rst = st.executeQuery(query2);
					rst.next();
					seat = rst.getInt(1) - 1;
					
					query3 = "update bus set seat_taken = " + Integer.toString(seat) + " where id = " + busID;
					
					st.executeUpdate(query3);
				}
								
				con.close();
			}
			catch(Exception e)
			{
				System.out.println(e.toString());
			}
		}
		else if(a.getSource() == updateStatus)
		{
//			String url = "jdbc:mysql://localhost/citybusmgnt";
//			String userName = "root";
//			String pass = "";
			
			String url = "jdbc:mysql://198.54.116.34/softgtyc_citybusmgnt";
			String userName = "users";
			String pass = "citybusmgnt";
			
			int tripCount;
			
			
			String query = "UPDATE bus set bus_status = '" + newStatus + "' where id = " + busID;
			String query1 = "select trip_count_per_day from bus where id = " + busID;
			String query2 = null;
			
			String query3 = "select total_trips from bus_history where bus_id = " + busID;
			String query4;
			
			try
			{
				Class.forName("com.mysql.jdbc.Driver");				
				con = DriverManager.getConnection(url,userName,pass);
				
				Statement st = con.createStatement();
				st.executeUpdate(query);
				
				ResultSet rs = st.executeQuery(query1);
				rs.next();
				tripCount = rs.getInt(1) + 1;
				
				query2 = "update bus set trip_count_per_day = " + tripCount + " where id = " + busID;
				st.executeUpdate(query2);
				
				ResultSet rst = st.executeQuery(query3);
				rst.next();
				query4 = "update bus_history set total_trips = " + 
						Integer.toString(rst.getInt(1) + 1) + " where bus_id = " + busID;
				st.executeUpdate(query4);
				
				con.close();
			}
			catch(Exception e)
			{
				System.out.println(e.toString());
			}
		}
		else if(a.getSource() == busHistory)
		{
//			String url = "jdbc:mysql://localhost/citybusmgnt";
//			String userName = "root";
//			String pass = "";
			
			String url = "jdbc:mysql://198.54.116.34/softgtyc_citybusmgnt";
			String userName = "users";
			String pass = "citybusmgnt";
			
			//String query = "UPDATE bus set bus_status = '" + newStatus + "' where id = " + busID;
			try
			{
				
			}
			catch(Exception e)
			{
				System.out.println(e.toString());
			}
		}
		
	}

	

}
