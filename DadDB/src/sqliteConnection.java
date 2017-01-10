import java.sql.*;
import java.util.List;

import javax.swing.*;

//got this whole thing from that indian dude who does those tutorials tbh
//originally implemented this in code without a function but I started getting a 
//(connection variable) must be either final or effectively final error, hoping this fixes it
public class sqliteConnection 
{
	Connection conn = null;
	public static Connection dbConnector()
	{
		try
		{
		   Class.forName("org.sqlite.JDBC"); //executes the static code in JDBC I think? Pretty standard for connecting to the jdbc driver
		   Connection conn = DriverManager.getConnection("jdbc:sqlite:C:/Users/Ninjalazer/Documents/Programming/Java/DadDBPC/DadDB/dad.db");
		   //Connection conn = DriverManager.getConnection("jdbc:sqlite:C:/Users/NursingSmartz/Documents/DadDB/dad.db");
		   return conn;
		} //end try
		
		catch (Exception e)
		{
			System.out.println("Error connecting to database in sqliteConnection class");
			JOptionPane.showMessageDialog(null, e);
			return null;
		} //end catch
	} //end dbConnector
	
	//don't know why I didn't make this into a method before
	//takes a context string for error messages
	//a list of prepared statements and result sets because I don't know how many I'll need to close in each context
	//and one connection because I shouldn't have more than one connection open at a time anyway
	//should be able to set the rs and psts I want to close equal to the closed state through the list
	//but I should return a closed connection because I don't think it'll be affected otherwise.

	//okay so I wanted to turn this into a function since I have to do this all over the place anyway but I couldn't
	//figure out how to set a prepared statement equal to a null value inserted into the pst array
/*
	public static Connection closeConnections(String context, List<PreparedStatement>pst, List<ResultSet>rs, Connection c)
	{
		try
		{   //apparently, the usual order for closing stuff in JDBC is rs, pst, then con
			if (rs.size() > 0) //if there's anything in the list
			{
				for (int i = 0; i < rs.size(); i++)
				{
					if (rs.get(i) != null)
					{
						rs.get(i).close();
					} //end if
				} //end for
			} //end if
				
			if (pst.size() > 0)
			{
				for (int i = 0; i < pst.size(); i++)
				{
					if (pst.get(i) != null)
					{
						pst.get(i).close();
					} //end if
				} //end for
			} //end if
				
			if (c != null)
			{
				c.close();
			} //end if
		} //end try
		
		catch(Exception x)
		{
			System.out.println("Failed to close connections in " + context + ".");
		} //end catch
		
		return c;
	} //end closeConnections
*/
} //end sqliteConnection
