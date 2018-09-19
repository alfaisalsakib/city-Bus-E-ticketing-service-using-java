package ClassManage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class srcDesDecide implements ActionListener,ItemListener
{
	private String userName;
	private String ID;
	private String CurrentLocation;
	private String Destination;
	private String routeNumber;
	private float totalDistance = 0;
	private String busStatus;
	private int currLocIndex = -1;
	
	private int srcIndex=0,desIndex=0;
	
	
	String[] stoppages = null; //string array
	String[] distances = null;
	
	private Connection con; // mysql connection var
	
	private JFrame frame = new JFrame("Source Destination Bus Selection");
	
	private JPanel panelUp = new JPanel();
	private JPanel imagePanel = new JPanel();
	private JPanel panelDown = new JPanel();
	
	private Font font = new Font("Serif",Font.ITALIC,22);
	
	private String[] roadOptions = {"Gabtoli", "Mirpur-1", "Mirpur-10", "Kazipara", "Shewrapara",
			"Mohakhali", "Kakoli", "Gulshan-2", "Natun-Bazar", "Badda", "Rampura", "Uttara", "Airport", "Khilkhet",
			"Bashundhora-Gate", "Rampura", "Motijhil"};
	
	private JComboBox srcCombo = new JComboBox(roadOptions);
	private JComboBox desCombo = new JComboBox(roadOptions);
	
	private JLabel sourceLabel = new JLabel("Select Your Current Location ");
	private JLabel destinationLabel = new JLabel("Select Your Destination Location ");
	
	private JButton searchBus = new JButton("Search For Available Buses");
	private JButton checkHistory = new JButton("Check History");
	
	private JLabel imageLabel = new JLabel();
	ImageIcon icon = new ImageIcon("CityMap.png");
	
	public srcDesDecide(String userName,String ID)
	{
		this.userName = userName;
		this.ID = ID;
	}
	
	public void iniwindow()
	{
		GridLayout gl = new GridLayout(2,2,5,5);
		BorderLayout bl = new BorderLayout();
		
		panelUp.setLayout(gl);
		frame.setLayout(bl);
		
		sourceLabel.setFont(font);
		destinationLabel.setFont(font);
		searchBus.setFont(font);
		checkHistory.setFont(font);
		
		panelUp.setBackground(Color.green);
		srcCombo.setBackground(Color.MAGENTA);
		desCombo.setBackground(Color.cyan);
		
		searchBus.setBackground(Color.LIGHT_GRAY);
		
		panelUp.add(sourceLabel);
		panelUp.add(srcCombo);
		panelUp.add(destinationLabel);
		panelUp.add(desCombo);
		
		imageLabel.setPreferredSize(new Dimension(870,500));
		
		imageLabel.setIcon(icon);	
		imagePanel.add(imageLabel);
		
		panelDown.add(searchBus);
		panelDown.add(checkHistory);
		
		frame.add(panelUp,bl.NORTH);
		frame.add(imagePanel, bl.CENTER);
		frame.add(panelDown, bl.SOUTH);
		
		frame.setSize(880, 650);
		//frame.setExtendedState(frame.MAXIMIZED_BOTH);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		searchBus.addActionListener(this);
		srcCombo.addItemListener(this);
		desCombo.addItemListener(this);
		checkHistory.addActionListener(this);
		
	}

	@Override
	public void itemStateChanged(ItemEvent i) 
	{
		int srcIndex = -1, desIndex = -1;
		
		srcIndex = srcCombo.getSelectedIndex();
		desIndex = desCombo.getSelectedIndex();
		
		if(srcIndex == desIndex)
		{
			JOptionPane.showMessageDialog(null, "Current Location and Destination are same !!");
			return ;
		}
		else
		{
			CurrentLocation = roadOptions[srcIndex];
			Destination = roadOptions[desIndex];
		}
		
		//System.out.println(CurrentLocation + "--" + Destination);
	}

	@Override
	public void actionPerformed(ActionEvent a)
	{
		if(a.getSource() == searchBus)
		{
			//JOptionPane.showMessageDialog(null, "Search bus !!! ");
			getConnection();
			/*
			private String CurrentLocation;
			private String Destination;
			private String routeNumber;
			private float totalDistance = 0;
			private String busStatus;
			private int currLocIndex = -1;

			String[] stoppages = null; //string array
			*/
			//System.out.println(CurrentLocation + "--" + Destination);
			if(!routeNumber.equals("Invalid") && srcIndex != -1 && desIndex != -1)
			{
				//System.out.println(userName);
				searchBuses sb = new searchBuses(userName,ID,CurrentLocation,Destination,routeNumber,totalDistance
						,busStatus,currLocIndex,stoppages);
				//sb.print();
				sb.iniwindow();
			}
			else
				JOptionPane.showMessageDialog(null, "Sorry!! No Direct Route from " + CurrentLocation + ""
						+ " To " + Destination);
		}
		else if(a.getSource() == checkHistory)
		{
			System.out.println("history");
			
//			String url = "jdbc:mysql://localhost/citybusmgnt";
//			String userName = "root";
//			String pass = "";
			
			String url = "jdbc:mysql://198.54.116.34/softgtyc_citybusmgnt";
			String userName = "users";
			String pass = "citybusmgnt";
			
			String msg;
			
			String query = "select * from user_history where user_id = " + Integer.parseInt(ID) ;
			try
			{
				Class.forName("com.mysql.jdbc.Driver");
				
				con = DriverManager.getConnection(url,userName,pass);
				Statement st = con.createStatement();
				ResultSet rs = st.executeQuery(query);
				System.out.println(ID);
				while(rs.next())
				{
					msg = "Date  : " + rs.getString(2) + "\nBus ID : " + rs.getString(4) + "\nFrom : " + rs.getString(5) + 
							"\nTo   : " + rs.getString(6) + "\nDistance  : " + rs.getString(7) + "\nFare   : " + rs.getString(8);
					//System.out.println(rs.getInt(4));
					JOptionPane.showMessageDialog(null, msg);
				}
				rs.close();
			}
			catch(Exception e)
			{
				System.out.println(e.toString());
			}
		}
		
	}
	
	public void getConnection() //with data processing
	{
//		String url = "jdbc:mysql://localhost/citybusmgnt";
//		String userName = "root";
//		String pass = "";
		
		String url = "jdbc:mysql://198.54.116.34/softgtyc_citybusmgnt";
		String userName = "users";
		String pass = "citybusmgnt";
		
		String query = "select * from route;";
		
		String tempStoppages;
		String tempDistances;
		int sizeStoppages, sizeDistances , countStoppages;
		boolean key1,key2;
		
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			
			con = DriverManager.getConnection(url,userName,pass);
			//JOptionPane.showMessageDialog(null, "Connected to DataBase");
			
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(query);
			
			
			while(rs.next())
			{		
				routeNumber = null;
				countStoppages = 0;
				tempStoppages = null;
				tempDistances = null;
				key1 = false;key2 = false;
				srcIndex=0;desIndex=0;
				
				tempStoppages = rs.getString(2);
				tempDistances = rs.getString(3);
				
				sizeStoppages = tempStoppages.length();
				sizeDistances = tempDistances.length();
				
				for(int i=0;i<sizeStoppages;i++)
				{
					stoppages = tempStoppages.split(" "); //entry to stoppages
				}
				countStoppages = stoppages.length;
				
				for(int i=0;i<sizeDistances;i++)
				{
					distances = tempDistances.split(" "); //entry to distance
				}				
				
				for(int i=0;i<countStoppages;i++)
				{
					if(CurrentLocation.equals(stoppages[i]))
					{
						key1 = true;
						srcIndex = i;
					}
					else if(Destination.equals(stoppages[i]))
					{
						key2 = true;
						desIndex = i;
					}
				}
				
				if(key1 == true && key2 == true)
				{
					routeNumber = rs.getString(1);
					break;
				}
				else 
				{
					//suggested alternative route
					routeNumber = "Invalid";
					desIndex=-1;srcIndex=-1;
				}			
			}
			
			if(srcIndex < desIndex && !routeNumber.equals("Invalid"))
			{
				busStatus = "Up";
				//System.out.println("up");
			}
			else if(srcIndex > desIndex && !routeNumber.equals("Invalid"))
			{
				busStatus = "Down";
				//System.out.println("down");
			}
			
			currLocIndex = srcIndex;
			
			//System.out.println(currLocIndex);
			
			totalDistance = Math.abs(Float.parseFloat(distances[desIndex]) - Float.parseFloat(distances[srcIndex]));
			
			//JOptionPane.showMessageDialog(null, "Route Number : " + routeNumber + "\nTotal Distance"
					//+ " : " + totalDistance + "\nTour Status : " + busStatus );
			
			rs.close();
			
		}
		catch(Exception e)
		{
			JOptionPane.showMessageDialog(null, "Invalid Entry");
			//System.out.println(e.toString());
		}
	}
	
}
