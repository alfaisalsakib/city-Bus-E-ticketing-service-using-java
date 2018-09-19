package ClassManage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

public class searchBuses implements ActionListener
{
	private String userName;
	private String ID;
	private String CurrentLocation;
	private String Destination;
	private String routeNumber;
	private float totalDistance = 0;
	private String busStatus;
	private int currLocIndex = -1; //user current location index
	
	private String busID;
	private String busLicenseID;
	private String totalSeat;
	private int emptySeat;
	private String busLocation;
	private String Fare;

	private LinkedList<Integer> busIndex = new LinkedList<Integer>();
	private LinkedList<String> busIDIndex = new LinkedList<String>();
	
	String[] stoppages = null; //string array
	//String[] reverseStoppages = null;
	private LinkedList<String> reverseStoppages = new LinkedList<String>();
	
	
	private Connection con; // mysql connection var
	
	private JFrame frame = new JFrame("Available Buses");
	
	private JPanel panelUp = new JPanel();
	private JPanel panelDown = new JPanel();
	
	private Font font = new Font("Serif",Font.ITALIC,22);
	
	private JTextArea middleTxtArea = new JTextArea(16,53);
	
	private JLabel choosenBusID = new JLabel("Enter The Choosen Bus ID  : ");
	private JTextField txtBusID = new JTextField(null);
	
	private JButton select = new JButton("Select Bus");
	private JButton driverInfo = new JButton("Driver Information");
	private JButton assistantInfo = new JButton("Assistant Information");
	private JButton anotherBus = new JButton("Search for new Bus");
	
	private JScrollPane txtScrollPane = new JScrollPane(middleTxtArea);
	private Calendar calendar = Calendar.getInstance();
	
	public searchBuses(String userName,String ID,String CurrentLocation,String Destination,String routeNumber,float totalDistance
			,String busStatus,int currLocIndex,String[] stoppages) //constructor
	{
		this.userName = userName;
		this.ID = ID; 
		this.CurrentLocation = CurrentLocation;
		this.Destination = Destination;
		this.routeNumber = routeNumber;
		this.totalDistance = totalDistance;
		this.busStatus = busStatus;
		this.currLocIndex = currLocIndex;
		this.stoppages = stoppages;
	}
	
	public void iniwindow()
	{
		GridLayout gl = new GridLayout(3,2,5,5);
		BorderLayout bl = new BorderLayout();
		
		panelDown.setLayout(gl);
		frame.setLayout(bl);
			
		select.setFont(font);
		driverInfo.setFont(font);
		assistantInfo.setFont(font);
		anotherBus.setFont(font);
		choosenBusID.setFont(font);
		
		txtScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		middleTxtArea.setBackground(Color.YELLOW);		
		middleTxtArea.setEditable(false);
		middleTxtArea.setFont(font);
		panelUp.add(txtScrollPane);	
		
		middleTxtArea.append("\t\tUSER INFORMATION ");
		middleTxtArea.append("\n\t    Date                    \t:   " + calendar.getTime());
		middleTxtArea.append("\n\t    User Name               \t:   " + userName);
		middleTxtArea.append("\n\t    User ID                 \t:   " + ID);
		middleTxtArea.append("\n\t    Current Location        \t:   " + CurrentLocation);
		middleTxtArea.append("\n\t    Destination             \t:   " + Destination);
		middleTxtArea.append("\n\t    Route Number            \t:   " + routeNumber);
		middleTxtArea.append("\n\t    Total Distance          \t:   " + totalDistance);
		middleTxtArea.append("\n\t    Tour Status             \t:   " + busStatus);
		
		
		panelDown.add(choosenBusID);
		panelDown.add(txtBusID);
		panelDown.add(select);
		panelDown.add(anotherBus);
		panelDown.add(driverInfo);
		panelDown.add(assistantInfo);
		
		frame.add(panelUp,bl.NORTH);
		frame.add(panelDown, bl.SOUTH);
		
		frame.setSize(880, 650);
		//frame.setExtendedState(frame.MAXIMIZED_BOTH);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		getConnection();
		
		
		select.addActionListener(this);
		driverInfo.addActionListener(this);
		assistantInfo.addActionListener(this);
		anotherBus.addActionListener(this);
		
	}
	
	public void updateLocation() 
	{
//		String url = "jdbc:mysql://localhost/citybusmgnt";
//		String userName = "root";
//		String pass = "";
		
		String url = "jdbc:mysql://198.54.116.34/softgtyc_citybusmgnt";
		String userName = "users";
		String pass = "citybusmgnt";
		
		String updateQuery = "update user set CurrentLocation = '" + CurrentLocation + "', Destination = '" + Destination + "' where id = " + ID;
		
		try 
		{
			Class.forName("com.mysql.jdbc.Driver");
			
			con = DriverManager.getConnection(url,userName,pass);
			
			Statement st = con.createStatement();
			st.executeUpdate(updateQuery);
			//System.out.println("Data Entry Successfull");
			
			con.close();
		}
		catch (Exception e) 
		{
			System.out.println(e.toString());
		}
	}
	
	public void updateBusSeat()
	{
//		String url = "jdbc:mysql://localhost/citybusmgnt";
//		String userName = "root";
//		String pass = "";
		
		String url = "jdbc:mysql://198.54.116.34/softgtyc_citybusmgnt";
		String userName = "users";
		String pass = "citybusmgnt";
		
		int seat,passenger;
		
		String query = "select seat_taken, passenger_count from bus where id = " + busID;
		String query2 = "select total_passenger_count from bus_history where bus_id = " + busID;
		String query3;
		
		try 
		{
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(url,userName,pass);
			
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(query);
			rs.next();
			
			seat = rs.getInt(1) + 1;
			passenger = rs.getInt(2) + 1;
			
			String query1 = "update bus set seat_taken = " + Integer.toString(seat) + ", "
					+ "passenger_count = " + Integer.toString(passenger) + " where id = " + busID; 
			st.executeUpdate(query1);
			
			ResultSet rst = st.executeQuery(query2);
			rst.next();
			query3 = "update bus_history set total_passenger_count = " + 
			Integer.toString(rst.getInt(1) + 1) + " where bus_id = " + busID;
			st.executeUpdate(query3);
			
			con.close();
		}
		catch (Exception e) 
		{
			System.out.println(e.toString());
		}
		
	}
	
	@Override
	public void actionPerformed(ActionEvent a) 
	{
		if(a.getSource() == select)
		{
			String bID = txtBusID.getText();
			//System.out.println(bID + " -- " + busID);
			
			boolean key1 = false,key2 = false;
			
			for(int i=0;i<busIDIndex.size();i++)
			{
				if(bID.equals(busIDIndex.get(i)))
				{
					key2 = true;
				}
				else
				{
					key1 = false;
				}
			}
			
			if(key2 == true && key1 == false)
			{
				ticketUserWindow tuw = new ticketUserWindow(busID,CurrentLocation,Destination,ID,Fare,Float.toString(totalDistance));
				tuw.iniwindow();
			}
			else
				JOptionPane.showMessageDialog(null, "Please Enter Valid Bus ID");
			
			updateLocation();
			updateBusSeat();
		}
		else if(a.getSource() == driverInfo)
		{
//			String url = "jdbc:mysql://localhost/citybusmgnt";
//			String userName = "root";
//			String pass = "";
			
			String url = "jdbc:mysql://198.54.116.34/softgtyc_citybusmgnt";
			String userName = "users";
			String pass = "citybusmgnt";
			
			String BusID = txtBusID.getText();
			
			String query = "Select driver_id from bus where id = " + BusID;
			
			String driverID = null;
			
			String query1 = null;
			
			try
			{
				Class.forName("com.mysql.jdbc.Driver");
				
				con = DriverManager.getConnection(url,userName,pass);
				//JOptionPane.showMessageDialog(null, "Connected to DataBase");
				
				Statement st = con.createStatement();
				ResultSet rs = st.executeQuery(query);
				rs.next();
				driverID = rs.getString(1);
				//System.out.println(driverID);
				rs.close();
				
				query1 = "SELECT b.driver_id,d.Licence_no,e.name from bus b "
						+ "left JOIN driver d on b.driver_id = d.id "
						+ "left join employee e on d.employee_id = e.id "
						+ "WHERE b.driver_id = " + driverID + " ;";
				
				Statement st1 = con.createStatement();
				ResultSet rs1 = st1.executeQuery(query1);
				
				rs1.next();
					JOptionPane.showMessageDialog(null, "Driver ID : " + rs1.getString(1) + "\n"
							+ "Driver Name : " + rs1.getString(3) + "\n"
							+ "Driver Licence : " + rs1.getString(2));
				rs1.close();
			}
			catch(Exception e)
			{
				System.out.println(e.toString());
			}
		}
		else if(a.getSource() == assistantInfo)
		{
//			String url = "jdbc:mysql://localhost/citybusmgnt";
//			String userName = "root";
//			String pass = "";
			
			String url = "jdbc:mysql://198.54.116.34/softgtyc_citybusmgnt";
			String userName = "users";
			String pass = "citybusmgnt";
			
			String BusID = txtBusID.getText();
			
			String query = "Select driver_id from bus where id = " + BusID;
			
			String assistantID = null;
			
			String query1 = null;
			
			try
			{
				Class.forName("com.mysql.jdbc.Driver");
				
				con = DriverManager.getConnection(url,userName,pass);
				//JOptionPane.showMessageDialog(null, "Connected to DataBase");
				
				Statement st = con.createStatement();
				ResultSet rs = st.executeQuery(query);
				
				rs.next();
				assistantID = rs.getString(1);
				//System.out.println(assistantID);
				rs.close();
				
				query1 = "SELECT b.assistant_id,e.name from bus b "
						+ "left JOIN assistant a on b.driver_id = a.id "
						+ "left join employee e on a.employee_id = e.id "
						+ "WHERE b.driver_id = " + assistantID + " ;";
				
				Statement st1 = con.createStatement();
				ResultSet rs1 = st1.executeQuery(query1);
				
				rs1.next();
					JOptionPane.showMessageDialog(null, "Assistant ID : " + rs1.getString(1) + "\n"
							+ "Assistant Name : " + rs1.getString(2));
				rs1.close();
			}
			catch(Exception e)
			{
				
			}
		}
		else if(a.getSource() == anotherBus)
		{
//			String url = "jdbc:mysql://localhost/citybusmgnt";
//			String userName = "root";
//			String pass = "";
			
			String url = "jdbc:mysql://198.54.116.34/softgtyc_citybusmgnt";
			String userName = "users";
			String pass = "citybusmgnt";
			
			try
			{
				if(busStatus.equals("Up") && busIndex.size() > 0)
				{
					for(int i = busIndex.size()-1;i>=0;i--)
					{
						
						Class.forName("com.mysql.jdbc.Driver");
						
						con = DriverManager.getConnection(url,userName,pass);
						JOptionPane.showMessageDialog(null, "Connected to DataBase");
						
						Statement st1 = con.createStatement();
						ResultSet rs1 = st1.executeQuery("select * from bus where current_location = '" + stoppages[busIndex.get(i)] + "';");
						//System.out.println(stoppages[busIndex.get(i)]);	
						
						while(rs1.next())
						{		
							busID = rs1.getString(1); //
							busLicenseID = rs1.getString(3); //
							totalSeat = rs1.getString(4); //
							emptySeat = rs1.getInt(4) - rs1.getInt(5); //
							busLocation = rs1.getString(9);					
							Fare = Float.toString(getFare(busID,totalDistance)); //
							
							busIDIndex.add(busID);
							
							middleTxtArea.append("\n\t\tANOTHER NEAREST BUS INFORMATION ");
							middleTxtArea.append("\n\t    Bus ID No.              \t:   " + busID);
							middleTxtArea.append("\n\t    Bus License             \t:   " + busLicenseID);
							middleTxtArea.append("\n\t    Bus Total Seat          \t:   " + totalSeat);
							middleTxtArea.append("\n\t    Bus Empty Seat          \t:   " + emptySeat);
							middleTxtArea.append("\n\t    Bus Current Location    \t:   " + busLocation);
							middleTxtArea.append("\n	    Fare                    \t: " + Fare + " Tk");
						}
						rs1.close();
						
					}
				}
				else if(busStatus.equals("Down") && busIndex.size() > 0)
				{
					Class.forName("com.mysql.jdbc.Driver");
					
					con = DriverManager.getConnection(url,userName,pass);
					JOptionPane.showMessageDialog(null, "Connected to DataBase");
					
					Statement st1 = con.createStatement();
					ResultSet rs1 = st1.executeQuery("select * from bus where current_location = '" + reverseStoppages.get(busIndex.getLast()) + "';");
					busIndex.removeLast();
					
					while(rs1.next())
					{		
						busID = rs1.getString(1); //
						busLicenseID = rs1.getString(3); //
						totalSeat = rs1.getString(4); //
						emptySeat = rs1.getInt(4) - rs1.getInt(5); //
						busLocation = rs1.getString(9);					
						Fare = Float.toString(getFare(busID,totalDistance)); //
						
						busIDIndex.add(busID);
						
						middleTxtArea.append("\n\t\tNEAREST BUS INFORMATION ");
						middleTxtArea.append("\n\t    Bus ID No.              \t:   " + busID);
						middleTxtArea.append("\n\t    Bus License             \t:   " + busLicenseID);
						middleTxtArea.append("\n\t    Bus Total Seat          \t:   " + totalSeat);
						middleTxtArea.append("\n\t    Bus Empty Seat          \t:   " + emptySeat);
						middleTxtArea.append("\n\t    Bus Current Location    \t:   " + busLocation);
						middleTxtArea.append("\n	    Fare                    \t: " + Fare + " Tk");
					}
					
					rs1.close();
					
					
				}
				
				else
				{
					JOptionPane.showMessageDialog(null, "No Available Buses Are there right now");
				}
			}
			catch(Exception e)
			{
				System.out.println(e.toString());
			}
		}
		
	}
	
	public float getFare(String BusID,float totalDistance)
	{
//		String url = "jdbc:mysql://localhost/citybusmgnt";
//		String userName = "root";
//		String pass = "";
		
		String url = "jdbc:mysql://198.54.116.34/softgtyc_citybusmgnt";
		String userName = "users";
		String pass = "citybusmgnt";
		
		String query = "SELECT b.id,c.Fare_per_km FROM bus b "
				+ "right join company c on b.company_id = c.id;";
		
		float fare=0;
		
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			
			con = DriverManager.getConnection(url,userName,pass);
			//JOptionPane.showMessageDialog(null, "Connected to DataBase");
			
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(query);
			
			while(rs.next())
			{
				if(rs.getString(1).equals(busID))
				{
					fare = rs.getFloat(2);
				}
				
			}
		}
		catch(Exception e)
		{
			System.out.println(e.toString());
		}
		
		fare = fare * totalDistance;
		
		return fare;
		
	}
	
	public void getConnection()
	{
//		String url = "jdbc:mysql://localhost/citybusmgnt";
//		String userName = "root";
//		String pass = "";
		
		String url = "jdbc:mysql://198.54.116.34/softgtyc_citybusmgnt";
		String userName = "users";
		String pass = "citybusmgnt";
		
		String query = "select * from bus;";
		
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			
			con = DriverManager.getConnection(url,userName,pass);
			//JOptionPane.showMessageDialog(null, "Connected to DataBase");
			
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(query);
			
			for(int i=stoppages.length-1;i>=0;i--)
			{
				reverseStoppages.add(stoppages[i]);
			}
			
			while(rs.next())
			{				
				if(rs.getString(7).equals(routeNumber) && rs.getString(8).equals(busStatus))
				{
										
					busLocation = rs.getString(9);					
					
					if(busStatus.equals("Up"))
					{
						for(int i=0;i<stoppages.length;i++)
						{
							if(stoppages[i].equals(busLocation) && i<=currLocIndex)
							{
								busIndex.add(i);
							}
						}
					}
					else if(busStatus.equals("Down"))
					{
						
						for(int i=0;i<reverseStoppages.size();i++)
						{
							if(reverseStoppages.get(i).equals(CurrentLocation))
							{
								currLocIndex = i;
							}
						}
						
						//System.out.println(currLocIndex);
						//System.out.println(busLocation);
						for(int i=0;i<reverseStoppages.size();i++)
						{
							//System.out.println(reverseStoppages.get(i));
							if(reverseStoppages.get(i).equals(busLocation) && i<=currLocIndex)
							{
								//System.out.println(reverseStoppages.get(i));
								//System.out.println(busLocation);
								busIndex.add(i);
							}
						}
						
					}
										
				}
			}
			
			rs.close();
			
			//System.out.println(busIndex.size());
			
			if(busStatus.equals("Up") && busIndex.size() > 0)
			{
				//start code for Up bus Status
				int i = busIndex.size()-1;
				//System.out.println(i);
				
				Class.forName("com.mysql.jdbc.Driver");
				
				con = DriverManager.getConnection(url,userName,pass);
				JOptionPane.showMessageDialog(null, "Connected to DataBase");
				
				Statement st1 = con.createStatement();
				ResultSet rs1 = st1.executeQuery("select * from bus where current_location = '" + stoppages[busIndex.get(i)] + "';");
				busIndex.removeLast();
				//System.out.println(stoppages[busIndex.get(i)]);	
				
				while(rs1.next())
				{		
					busID = rs1.getString(1); //
					busLicenseID = rs1.getString(3); //
					totalSeat = rs1.getString(4); //
					emptySeat = rs1.getInt(4) - rs1.getInt(5); //
					busLocation = rs1.getString(9);					
					Fare = Float.toString(getFare(busID,totalDistance)); //
					
					busIDIndex.add(busID);
					
					middleTxtArea.append("\n\t\tNEAREST BUS INFORMATION ");
					middleTxtArea.append("\n\t    Bus ID No.              \t:   " + busID);
					middleTxtArea.append("\n\t    Bus License             \t:   " + busLicenseID);
					middleTxtArea.append("\n\t    Bus Total Seat          \t:   " + totalSeat);
					middleTxtArea.append("\n\t    Bus Empty Seat          \t:   " + emptySeat);
					middleTxtArea.append("\n\t    Bus Current Location    \t:   " + busLocation);
					middleTxtArea.append("\n	    Fare                    \t: " + Fare + " Tk");
				}
				rs1.close();
				// End code for Up Bus Status
				
			}
			
			//start code for down bus Status
			else if(busStatus.equals("Down") && busIndex.size() > 0)
			{
				
				
				Class.forName("com.mysql.jdbc.Driver");
				
				con = DriverManager.getConnection(url,userName,pass);
				JOptionPane.showMessageDialog(null, "Connected to DataBase");
				
				Statement st1 = con.createStatement();
				ResultSet rs1 = st1.executeQuery("select * from bus where current_location = '" + reverseStoppages.get(busIndex.getLast()) + "';");
				busIndex.removeLast();
				
				while(rs1.next())
				{		
					busID = rs1.getString(1); //
					busLicenseID = rs1.getString(3); //
					totalSeat = rs1.getString(4); //
					emptySeat = rs1.getInt(4) - rs1.getInt(5); //
					busLocation = rs1.getString(9);					
					Fare = Float.toString(getFare(busID,totalDistance)); //
					
					busIDIndex.add(busID);
					
					middleTxtArea.append("\n\t\tNEAREST BUS INFORMATION ");
					middleTxtArea.append("\n\t    Bus ID No.              \t:   " + busID);
					middleTxtArea.append("\n\t    Bus License             \t:   " + busLicenseID);
					middleTxtArea.append("\n\t    Bus Total Seat          \t:   " + totalSeat);
					middleTxtArea.append("\n\t    Bus Empty Seat          \t:   " + emptySeat);
					middleTxtArea.append("\n\t    Bus Current Location    \t:   " + busLocation);
					middleTxtArea.append("\n	    Fare                    \t: " + Fare + " Tk");
				}
				
				rs1.close();				
				
			}
			else
			{
				JOptionPane.showMessageDialog(null, "No Available Buses Are there right now");
			}
			//start code for down bus Status		
						
			con.close();
		}
		catch(Exception e)
		{
			System.out.println(e.toString());
		}
	}
	
	public void print()
	{
		System.out.println("Curr Loc : " + CurrentLocation);
		System.out.println("Destination : " + Destination);
		System.out.println("route number : " + routeNumber);
		System.out.println("totalDistance : " + totalDistance);
		System.out.println("busStatus : " + busStatus);
		System.out.println("index :  " + currLocIndex);
		
		int size = stoppages.length;
		for(int i=0;i<size;i++)
		{
			System.out.println(i + "--" + stoppages[i]);
		}
	}
	
}
