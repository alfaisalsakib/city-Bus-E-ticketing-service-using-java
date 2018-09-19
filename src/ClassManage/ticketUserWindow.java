package ClassManage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

public class ticketUserWindow implements ActionListener
{
	private String busID;
	private String userID;
	private String ticketNumber;
	private String CurrentLocation;
	private String Destination;
	private String fare;
	private String totalDistance;
	
	private Connection con;
	
	private JFrame frame = new JFrame("Ticket Confirmation And Travelling");
	
	private JPanel panelUp = new JPanel();
	private JPanel panelDown = new JPanel();
	
	private Font font = new Font("Serif",Font.ITALIC,22);
	private JTextArea middleTxtArea = new JTextArea(14,53);
	
	
	private JLabel complainLabel = new JLabel("Write Your Complain  : ");
	private JTextField txtComplain = new JTextField(null);
	
	private JLabel rateDriverLabel = new JLabel("Rate The Driver (0-5)  : ");
	private JTextField txtRateDriver = new JTextField("0");
	
	private JLabel rateAssistantLabel = new JLabel("Rate The Assistant (0-5)  : ");
	private JTextField txtRateAssistant = new JTextField("0");
	
	private JButton refresh = new JButton("Refresh");
	private JButton rateDriver = new JButton("Rate Driver");
	private JButton rateAssistant = new JButton("Rate Assistant");
	private JButton complain = new JButton("Complain");
	
	private JScrollPane txtScrollPane = new JScrollPane(middleTxtArea);
	private Calendar calendar = Calendar.getInstance();
	
	public ticketUserWindow(String busID,String CurrentLocation,String Destination,String userID,String fare,String totalDistance)
	{
		this.busID = busID;
		this.userID = userID;
		this.CurrentLocation = CurrentLocation;
		this.Destination = Destination;
		this.fare = fare;
		this.totalDistance = totalDistance;
	}
	
	public void iniwindow()
	{
		GridLayout gl = new GridLayout(4,3,5,5);
		BorderLayout bl = new BorderLayout();
		
		panelDown.setLayout(gl);
		frame.setLayout(bl);
		
		refresh.setFont(font);
		rateDriver.setFont(font);
		rateAssistant.setFont(font);
		complain.setFont(font);
		complainLabel.setFont(font);
		rateDriverLabel.setFont(font);
		rateAssistantLabel.setFont(font);
		
		txtScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		middleTxtArea.setBackground(Color.YELLOW);		
		middleTxtArea.setEditable(false);
		middleTxtArea.setFont(font);
		panelUp.add(txtScrollPane);	
		
		genarateTicketNumber();
		
		middleTxtArea.append("\t\tRIDING INFORMATION ");
		middleTxtArea.append("\n\t    Date                    \t:   " + calendar.getTime());
		middleTxtArea.append("\n\t    Current Location        \t:   " + CurrentLocation);
		middleTxtArea.append("\n\t    Destination             \t:   " + Destination);
		middleTxtArea.append("\n\t    Ticket Number           \t:   " + ticketNumber);
		middleTxtArea.append("\n\t    Bus Number              \t:   " + busID);
		middleTxtArea.append("\n\t    Fare                    \t:   " + fare);
		
		panelDown.add(complainLabel);
		panelDown.add(txtComplain);
		panelDown.add(complain);
		panelDown.add(rateDriverLabel);
		panelDown.add(txtRateDriver);
		panelDown.add(rateDriver);
		panelDown.add(rateAssistantLabel);
		panelDown.add(txtRateAssistant);
		panelDown.add(rateAssistant);
		panelDown.add(refresh);
		
		frame.add(panelUp,bl.NORTH);
		frame.add(panelDown, bl.SOUTH);
		
		
		frame.setSize(880, 650);
		//frame.setExtendedState(frame.MAXIMIZED_BOTH);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		getConnection();
		updateHistory();
		
		refresh.addActionListener(this);
		rateDriver.addActionListener(this);
		rateAssistant.addActionListener(this);
		complain.addActionListener(this);
		
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
			
			String query = "select current_location from bus where id = " + busID;
			
			try
			{
				Class.forName("com.mysql.jdbc.Driver");
				
				con = DriverManager.getConnection(url,userName,pass);
				Statement st = con.createStatement();
				ResultSet rs = st.executeQuery(query);
				rs.next();
				
				CurrentLocation = rs.getString(1);
				//System.out.println(rs.getString(1));
				
				if(CurrentLocation.equals(Destination))
				{
					JOptionPane.showMessageDialog(null, "You Have Arrived " + Destination);
				}
				else
				{
					middleTxtArea.append("\n\t\tRIDING INFORMATION ");
					middleTxtArea.append("\n\t    Date                    \t:   " + calendar.getTime());
					middleTxtArea.append("\n\t    Current Location        \t:   " + CurrentLocation);
					middleTxtArea.append("\n\t    Destination             \t:   " + Destination);
					
					con = DriverManager.getConnection(url,userName,pass);
					Statement st1 = con.createStatement();
					
					String UpdateQuery = "update user set CurrentLocation = '" + CurrentLocation + "' where id = " + userID;
					
					st1.executeUpdate(UpdateQuery);
					con.close();
				}
				rs.close();				
				
			}
			catch(Exception e)
			{
				System.out.println(e.toString());
			}
		}
		else if(a.getSource() == rateDriver)
		{
			String eID = null;
			String rate = null;
			String ttrip;
			float temp;
			
			String getRate = txtRateDriver.getText();
			float rating = Float.parseFloat(getRate);
			
			if(rating>=0 && rating <=5)
			{
//				String url = "jdbc:mysql://localhost/citybusmgnt";
//				String userName = "root";
//				String pass = "";
				
				String url = "jdbc:mysql://198.54.116.34/softgtyc_citybusmgnt";
				String userName = "users";
				String pass = "citybusmgnt";
				
				String query = "select e.id,e.rating, e.CountPassenger from bus b left join driver d on b.driver_id = d.id "
						+ "left join employee e on d.employee_id = e.id  where b.id = " + Integer.parseInt(busID) + ";";
				
				
				
				try
				{
					Class.forName("com.mysql.jdbc.Driver");
					
					con = DriverManager.getConnection(url,userName,pass);
					
					Statement st = con.createStatement();
					ResultSet rs = st.executeQuery(query);
					rs.next();
					
					eID = rs.getString(1);
					rate = rs.getString(2);
					ttrip = rs.getString(3);
				
					temp = ((Float.parseFloat(rate) * Float.parseFloat(ttrip)) + rating) / (Float.parseFloat(ttrip) + 1) ;
					
					//System.out.println(temp);
					String updateQuery = "update employee set rating = " + temp + ", CountPassenger = "+ (Integer.parseInt(ttrip) + 1) +" where id = " + Integer.parseInt(eID);
					st.executeUpdate(updateQuery);
					con.close();
				}
				catch(Exception e)
				{
					System.out.println(e.toString());
				}
				
				rateDriver.setEnabled(false);
			}
			else
			{
				JOptionPane.showMessageDialog(null, "Invalid Range (0~5)");
			}
			
			
		}
		else if(a.getSource() == rateAssistant)
		{
			String eID = null;
			String rate = null;
			String ttrip;
			float temp;
			
			String getRate = txtRateAssistant.getText();
			float rating = Float.parseFloat(getRate);
			
			if(rating>=0 && rating <=5)
			{
//				String url = "jdbc:mysql://localhost/citybusmgnt";
//				String userName = "root";
//				String pass = "";
				
				String url = "jdbc:mysql://198.54.116.34/softgtyc_citybusmgnt";
				String userName = "users";
				String pass = "citybusmgnt";
				
				String query = "select e.id,e.rating, e.CountPassenger from bus b left join assistant a on b.driver_id = a.id "
						+ "left join employee e on a.employee_id = e.id  where b.id = " + Integer.parseInt(busID) + ";";
				
				
				
				try
				{
					Class.forName("com.mysql.jdbc.Driver");
					
					con = DriverManager.getConnection(url,userName,pass);
					
					Statement st = con.createStatement();
					ResultSet rs = st.executeQuery(query);
					rs.next();
					
					eID = rs.getString(1);
					rate = rs.getString(2);
					ttrip = rs.getString(3);
				
					temp = ((Float.parseFloat(rate) * Float.parseFloat(ttrip)) + rating) / (Float.parseFloat(ttrip) + 1) ;
					
					//System.out.println(temp);
					String updateQuery = "update employee set rating = " + temp + ", CountPassenger = "+ (Integer.parseInt(ttrip) + 1) +" where id = " + Integer.parseInt(eID);
					st.executeUpdate(updateQuery);
					con.close();
				}
				catch(Exception e)
				{
					System.out.println(e.toString());
				}
				
				rateAssistant.setEnabled(false);
			}
			else
			{
				JOptionPane.showMessageDialog(null, "Invalid Range (0~5)");
			}
			
			
		}
		else if(a.getSource() == complain)
		{
			String getComplain = txtComplain.getText();
			
//			String url = "jdbc:mysql://localhost/citybusmgnt";
//			String userName = "root";
//			String pass = "";
			
			String url = "jdbc:mysql://198.54.116.34/softgtyc_citybusmgnt";
			String userName = "users";
			String pass = "citybusmgnt";
			
			String var = Integer.parseInt(busID) + " , '" + getComplain + "' , ' " + userID + "' ";
			String query = "insert into complain(bus_id,complain,user_id) values(" + var + ") ;";
			
			try
			{
				Class.forName("com.mysql.jdbc.Driver");
				
				con = DriverManager.getConnection(url,userName,pass);
				
				Statement st = con.createStatement();
				st.executeUpdate(query);
				//System.out.println("Data Entry Successfull");
				
				con.close();
			}
			catch(Exception e)
			{
				System.out.println(e.toString());
			}
		}
	}
	
	public void updateHistory()
	{
//		String url = "jdbc:mysql://localhost/citybusmgnt";
//		String userName = "root";
//		String pass = "";
		
		String url = "jdbc:mysql://198.54.116.34/softgtyc_citybusmgnt";
		String userName = "users";
		String pass = "citybusmgnt";

		int uID = Integer.parseInt(userID);
		int bID = Integer.parseInt(busID);
		float  tdis = Float.parseFloat(totalDistance);
		float f = Float.parseFloat(fare);
		int getID;
				
		//System.out.println(uID + "--" + bID + "--" + CurrentLocation + "--" + Destination + "--" + tdis + "--" + f);
		
		String getIDquery  = "Select count(id) from user_history"; 
		
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			
			con = DriverManager.getConnection(url,userName,pass);
			
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(getIDquery);
			rs.next();
			getID = rs.getInt(1)+1;
			
			String var = getID + ",'" + calendar.getTime() + "'," + uID + "," + bID + ",'" + CurrentLocation + "','" + Destination + "'," + tdis + "," + f;
			String query = "insert into user_history  values(" + var + ") ;";
			
			st.executeUpdate(query);
			//System.out.println("Data Entry Successfull");
			
			con.close();
		}
		catch(Exception e)
		{
			System.out.println(e.toString());
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
		
		String var = "'" + ticketNumber + "' , '" + userID + "' , ' " + busID + "' , ' " +  fare +" ' , ' " + CurrentLocation + " ' , ' " + Destination + " '";
		String query = "insert into ticket values(" + var + ") ;";
		
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			
			con = DriverManager.getConnection(url,userName,pass);
			
			Statement st = con.createStatement();
			st.executeUpdate(query);
			//System.out.println("Data Entry Successfull");
			
			con.close();
		}
		catch(Exception e)
		{
			System.out.println(e.toString());
		}
	
	}
	
	public void genarateTicketNumber()
	{
		String ticketNo = "12" + busID + userID;
		ticketNumber = ticketNo;
		//System.out.println(ticketNo);
	}
}
