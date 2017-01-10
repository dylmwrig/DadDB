import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JOptionPane;

import net.proteanit.sql.DbUtils;

public class DatabaseAccess 
{
	DatabaseAccess()
	{
		
	} //end constructor
	
	//used to show the table
	//called when "load table" "add comic" and "edit comic" are pressed
	//return a ResultSet which is to be parsed and added to the table using resultSetToTableModel
	public static ResultSet showTable()
	{
		Connection c = sqliteConnection.dbConnector();
		ResultSet rs = null;
		PreparedStatement pst = null;
		
	    try
	   	{
	   		String query = "SELECT * FROM " + Database.getTableName() + " ORDER BY " + Database.getColId() + " ASC";
	   		pst = c.prepareStatement(query);
	   		rs = pst.executeQuery();
	   	} //end try
	   	
	   	catch (Exception x)
	   	{
	   		System.out.println("Error trying to show the database");
	   		x.printStackTrace();
	   	} //end catch
	    
	    finally
	    {
	    	try
	    	{
	    		rs.close();
	    		pst.close();
	    		c.close();
	    	} //end try
	    	
	    	catch (Exception x)
	    	{
	    		System.out.println("Error trying to close everything in show table");
	    		x.printStackTrace();
	    	} //end catch
	    } //end catch
	    
	    return rs;
	} //end showTable
	
	//checks whether there is a dash in the input, used by the checkIntField. Returns true if the format is good
	public static boolean checkDash(String toCheck)
	{
		boolean dashExists = false;
		
		String [] dashCheck = toCheck.split("-");
		
		//if the input is of the format num/-/num, the result of the above split will be an array of two numbers, so check that.
		try
		{
			Integer.parseInt(dashCheck[0]);
			Integer.parseInt(dashCheck[1]);
			dashExists = true;
		} //end try
		
		catch(Exception x)
		{
			
		} //end catch
		
		return dashExists;
	} //end checkDash
	
	//checks integer fields (issue number and id) for valid integer input (I'm done messing around with jformattedtextfield tbh)
	public static boolean checkIntField(String toCheck, String id, String issue)
	{
		boolean result = true; //stores the result. Just make it false if it is false
		boolean dash = false; //checks if the format of the input is number/dash/number, as this is also valid input.
		
		int start = -1;
		int end = -1;
		
		//used to check if the id is within proper range
		PreparedStatement prep = null;
		ResultSet rs = null;
		int idCheck = 0;
		
		if (toCheck == Database.getColId())
		{
			try
			{
				Integer.parseInt(id);
			} //end try
			
			catch (Exception e)
			{
				if (checkDash(id))
				{
					dash = true;
				} //end if
				
				else
				{
					JOptionPane.showMessageDialog(null, "Please enter a valid id number", "Error", JOptionPane.ERROR_MESSAGE);
					result = false;
				} //end else
			} //end catch
			
			if (result)
			{	//choose the last item from the table so we know if the id number is within range.
				if (dash)
				{
					String[] dashSep = id.split("-");
					
					if (Integer.parseInt(dashSep[0]) < Integer.parseInt(dashSep[1]))
					{
						start = Integer.parseInt(dashSep[0]);
						end = Integer.parseInt(dashSep[1]);
					} //end if
					
					else if (Integer.parseInt(dashSep[0]) > Integer.parseInt(dashSep[1]))
					{
						start = Integer.parseInt(dashSep[1]);
						end = Integer.parseInt(dashSep[0]);
					} //end else
					
					else //the numbers on either end of the dash are equal for some reason
					{
						start = Integer.parseInt(dashSep[0]);
						end = start;
					} //end else
				} //end if
				String query = "SELECT * FROM " + Database.getTableName() + " ORDER BY " + Database.getColId() + " DESC LIMIT 1;";
				Connection con = sqliteConnection.dbConnector();
				try
				{
					prep = con.prepareStatement(query);
					rs = prep.executeQuery();
					idCheck = rs.getInt(Database.getColId());
				} //end try
				
				catch (Exception x)
				{
			   		x.printStackTrace();
				} //end catch
				
				finally
				{	
					try
					{
						prep.close();
						rs.close();
						con.close();
					} //end try
					
					catch (Exception e)
					{
						System.out.println("Failed to close the database");
					}
						
					finally
					{
						if (dash)
						{
							if ((end > idCheck) || (start < 1))
							{
								JOptionPane.showMessageDialog(null, "Please enter a valid id number", "Error", JOptionPane.ERROR_MESSAGE);
								result = false;
							} //end if
						} //end if
						
						else
						{
							if ((Integer.parseInt(id) > idCheck) || Integer.parseInt(id) < 1)
							{
								JOptionPane.showMessageDialog(null, "Please enter a valid id number", "Error", JOptionPane.ERROR_MESSAGE);
								result = false;
							} //end if
						} //end else
					} //end finally
				} //end finally
			} //end if
		} //end if
		
		//check for valid input on issue:
		//check if it is either a number or an empty string
		else if (toCheck == Database.getColIssue())
		{
			try
			{
				Integer.parseInt(issue);
			} //end try
			
			catch (Exception e)
			{
				
				if (issue.equals(""))
				{
					
				} //end if
				
				else if (checkDash(issue))
				{
					
				} //end else if
				
				else
				{
					JOptionPane.showMessageDialog(null, "Please enter a valid issue number", "Error", JOptionPane.ERROR_MESSAGE);
					result = false;	
				} //end else
			} //end catch
		} //end else if
		
		return result;
	} //end checkIntField


	//used for cleaning out the database (such as for old tables)
	public static void cleanDB()
	{
		Connection c = sqliteConnection.dbConnector();
		PreparedStatement pst = null;
		try
	   	{
	   		String query = "DROP TABLE " + Database.getTableName(); //put what table you want to drop here
	   		pst = c.prepareStatement(query);
	   		pst.executeUpdate();
	   	} //end try
	   	
	   	catch (Exception x)
	   	{
	   		System.out.println("Error dropping table");
	   		x.printStackTrace();
	   	} //end catch
		
		finally
		{
			try
			{
				if (pst != null)
				{
					pst.close();
				} //end if
				
				if (c != null)
				{
					c.close();
				} //end if
			} //end try
			
			catch (Exception x)
			{
				System.out.println("Error closing everything in cleanDB");
			} //end catch
		} //end finally
	} //end cleanDB
	
	//used in the next function, checks if the two arguments passed in are equal when case, punctuation, and spaces are not a factor
	private static boolean parseCheck(String compare1, String compare2)
	{
		boolean equal = false;
		
		compare1 = compare1.toLowerCase(); //we need to do this anyway, do it before string.split() in particular 
		compare2 = compare2.toLowerCase(); //because it seems to act really strangely with capitals
		
		String delim = "[/ \\ ,.;:<>\'}{\\-!@#$%^&*()=_+]"; //add any delimiters here
		
		String[] tokens = compare1.split(delim);
		compare1 = "";
		for (int i = 0; i < tokens.length; i++)
		{
			compare1 += tokens[i];
		} //end for
		
		tokens = compare2.split(delim);
		compare2 = "";
		for (int i = 0; i < tokens.length; i++)
		{
			compare2 += tokens[i];
		} //end for
		
		if (compare1.equals(compare2)) //change boolean if they're true, otherwise do not
		{
			equal = true;
		} //end if
		
		if (equal)
		{
			System.out.println(compare1 + " and " + compare2 + " are equal");
		}
		
		return equal;
	} //end parseCheck
	
	public static ResultSet sortData(String main, String series, String issue, String author, String artist, String publisher,
			 String id, boolean ascending, boolean descending, boolean mainBool, boolean seriesBool, boolean issueBool,
			boolean authorBool, boolean publisherBool, boolean idBool) throws SQLException
	{
		Boolean multFilters = false; //used for adding "AND" to my queryMod additions if there were filters coming before it
		
		int i = 0;
		
		String queryMod = " WHERE "; //queryMod adds constraints to the query if constraints are provided
		String orderString = " ORDER BY "; //same as above but for ordering
		String orderBy = "ASC"; //stores if we are ascending or descending
		
		//each list reads in the corresponding element from the table, and this value is parsed, removing spaces and punctuation and case, then compared
		List<String> mainList = new ArrayList<String>();
		List<String> seriesList = new ArrayList<String>();
		List<Integer> issueList = new ArrayList<Integer>();
		List<String> authorList = new ArrayList<String>();
		List<String> artistList = new ArrayList<String>();
		List<Integer> IDList = new ArrayList<Integer>();
		List<String> equalList = new ArrayList<String>(); //keeps track of all of the equal values
		
		Connection c = sqliteConnection.dbConnector();
		
		ResultSet rs = null;
		ResultSet rs2 = null;
		
		PreparedStatement pst = null;
		PreparedStatement pst2 = null;
/*		
		PreparedStatement pst3 = null;
		PreparedStatement pst4 = null;
		PreparedStatement pst5 = null; 
		PreparedStatement pst6 = null;
*/
		//set the prepared statement equal to a generic select (column) from (table), I'll set (column) equal to what I'm checking for in the following
		//OR you can just select all then get the correct column info using preparedset.getString(columnName)
		pst = c.prepareStatement("SELECT * FROM " + Database.getTableName());
		rs = pst.executeQuery();
		
		if (main.length() > 0) //is the text field not empty?
		{					   //if so, add the constraint to queryMod
			if (rs.next())
			{
				do
				{
					mainList.add(rs.getString(Database.getColMain()));
					if (parseCheck(mainList.get(i), main))
					{
						System.out.println("yes, this is in the db ");
						equalList.add(mainList.get(i));
					} //end if	
					
					else
					{
						System.out.println("This is not in the db.");
					}
					
					i++; //i keeps track of the index at which the matching value is at so we don't have to do unnecessary checks
				} //end do
				
				while(rs.next());
			} //end if
			i = 0;
			System.out.println("We're past the loop");
			
			while (i < equalList.size())
			{
				if (i > 0)
				{
					queryMod += "OR "; //add an or if this isn't the first time through
				} //end if
				queryMod += Database.getColMain() + " = '";
				queryMod += equalList.get(i) + "'";
				System.out.println("equalList " + equalList.get(i));
				i++;
				multFilters = true; //we only want to set this to true if we are adding something in the first place
			} //end while
			System.out.println("Past the second loop");
			queryMod += ";";
			
			System.out.println(queryMod);
		} //end if
		
		if (series.length() > 0)
		{
			if (multFilters)
			{
				queryMod += " AND";
			} //end if
			
			queryMod += " " + Database.getColSeries() + " = '" + series + "'";
			multFilters = true;
		} //end if
		
		if (issue.length() > 0)
		{
			if (checkIntField(Database.getColIssue(), id, series))
			{
				if (multFilters)
				{
					queryMod += " AND";
				} //end if
				
				queryMod += " " + Database.getColIssue() + " = '" + issue + "'";
				multFilters = true;
			} //end if
		} //end if
		
		if (author.length() > 0)
		{
			if (multFilters)
			{
				queryMod += " AND";
			} //end if
			
			queryMod += " " + Database.getColAuthor() + " = '" + author + "'";
			multFilters = true;
		} //end if
		
		if (artist.length() > 0)
		{
			if (multFilters)
			{
				queryMod += " AND";
			} //end if
			
			queryMod += " " + Database.getColArtist() + " = '" + artist + "'";
			multFilters = true;
		} //end if
		
		if (publisher.length() > 0)
		{
			if (multFilters)
			{
				queryMod += " AND";
			} //end if
			
			queryMod += " " + Database.getColPublisher() + " = '" + publisher + "'";
			multFilters = true;
		} //end if
		
		if (id.length() > 0)
		{
			if (checkIntField(Database.getColId(), id, issue))
			{
				if (multFilters)
				{
					queryMod += " AND";
				} //end if
				
				queryMod += " " + Database.getColId() + " = '" + id + "'";
				multFilters = true;
			} //end if
		} //end if
		
		if (ascending)
		{
			orderBy = "ASC";
		} //end if
		
		else if (descending)
		{
			orderBy = "DESC";
		} //end else if
		
		if (idBool)
		{
			orderString += Database.getColId() + " " + orderBy;
		} //end if
		
		else if (mainBool)
		{
			orderString += Database.getColMain() + " " + orderBy;
		} //end if
		
		else if (seriesBool)
		{
			orderString += Database.getColSeries() + " " + orderBy;
		} //end if
		
		else if (issueBool)
		{
			orderString += Database.getColIssue() + " " + orderBy;
		} //end if
		
		else if (authorBool)
		{
			orderString += Database.getColAuthor() + " " + orderBy;
		} //end if
		
		else if (authorBool)
		{
			orderString += Database.getColArtist() + " " + orderBy;
		} //end if
		
		else if (publisherBool)
		{
			orderString += Database.getColPublisher() + " " + orderBy;
		} //end if
		
		String query = "SELECT * FROM " + Database.getTableName();
		if (multFilters) //add the modifier if there are modifiers
		{
			query += queryMod; 
		} //end if
		
		if (orderString != " ORDER BY ")
		{
			query += orderString;
		} //end if
		
		try
		{
			System.out.println(queryMod);
			System.out.println(query);
			pst2 = c.prepareStatement(query);
			rs2 = pst2.executeQuery();
	   	} //end try
	   	
	   	catch (Exception x)
	   	{
	   		x.printStackTrace();
	   	} //end catch
		
		finally
		{
			try
			{
				pst.close();
				pst2.close();
				rs.close();
				rs2.close();
				c.close();
			} //end try
			
			catch (Exception e)
			{
				System.out.println("Error closing everything");
			} //end catch
		} //end finally
		
		return rs2;
	} //end sortData
	
	//checks if there are any entries in the table
	public static boolean checkIfEmpty()
	{
		boolean result = true;
		
		String sql = "SELECT * FROM " + Database.getTableName() + " WHERE EXISTS (SELECT * FROM " + 
		Database.getTableName() + " WHERE " + Database.getColId() + "=" + 1;
		
		ResultSet rs = null;
		PreparedStatement pst = null;
		Connection c = sqliteConnection.dbConnector();
		
		try
		{
			System.out.println("Beginning try block");
			pst = c.prepareStatement(sql);
			System.out.println("PST is open");
			rs = pst.executeQuery();
			System.out.println("rs is open");
			if (rs != null) 
			{
				System.out.println("rs is not empty");
			    rs.first();                       
			    if (rs.getInt (0) == 0) 
			    {              
			        result = false;
			        System.out.println("The table is empty");
			    } //end if
			    
			    else
			    {
			    	System.out.println("The table is not empty");
			    }
			} //end if
			
			else
			{
				System.out.println("rs is empty");
			} 
		} //end try
		
		catch(Exception x)
		{
			System.out.println("Error checking if the table is empty");
		} //end catch
		
		try
		{
			
		} //end try
		
		catch (Exception x)
		{
			System.out.println("Error closing everything");
		} //end catch
		
		return result;
	} //end checkIfEmpty
	
	//finds the new id number after a modification to the table, such as after adding comics
	//also called when the program is launched so we know what to set id equal to without having to store this information
	//on a file or something.
	public static int newId()
	{
		int id = 1;
		
		PreparedStatement prep = null;
		ResultSet result = null;
		
		String query = "SELECT * FROM " + Database.getTableName() + " ORDER BY " + Database.getColId() + " DESC LIMIT 1;";
		Connection con = sqliteConnection.dbConnector();
		try
		{
			if (!checkIfEmpty())
			{
				prep = con.prepareStatement(query);
				result = prep.executeQuery();
				id = result.getInt(Database.getColId()) + 1;
			} //end if
			
			//do nothing if the db is empty because, in that case, id needs to be 1
		} //end try
		
		catch (Exception x)
		{
	   		x.printStackTrace();
		} //end catch
		
		finally
		{
			try
			{
				prep.close();
				result.close();
				con.close();
			} //end try
			
			catch (Exception x)
			{
				x.printStackTrace();
			} //end catch
		} //end finally
		
		return id;
	} //end newId
	
	//the "In' stands for "input" to distinguish the variables
	public static ResultSet addComic(String mainIn, String seriesIn, String issueIn, String authorIn, String artistIn, 
			String publisherIn, int id)
	{
		int issue = -1;
		int start = -1; //used if the user typed in number/dash/number for inputting multiple comics at the same time
		int end = -1;
		
		String main = "NONE";
		String series = "NONE";
		String author = "NONE";
		String artist = "NONE";
		String publisher = "NONE";
		
		boolean keepGoing = true; //waddup andy
		
		ResultSet rs = null;
		
		if (mainIn.equals(""))
		{
			main = "NONE";
		} //end if
		
		else
		{
			main = mainIn;
		} //end else
		
		if (seriesIn.equals(""))
		{
			series = "NONE";
		} //end if
		
		else
		{
			series = seriesIn;
		} //end else

		if (authorIn.equals(""))
		{
			author = "NONE";
		} //end if
		
		else
		{
			author = authorIn;
		} //end else
		
		if (artistIn.equals(""))
		{
			artist = "NONE";
		} //end if
		
		else
		{
			artist = artistIn;
		} //end else

		if (publisherIn.equals(""))
		{
			publisher = "NONE";
		} //end if
		
		else
		{
			publisher = publisherIn;
		} //end else
		
		if (checkIntField(Database.getColIssue(), Integer.toString(id), issueIn))
		{
			if (issueIn.equals(""))
			{
				issue = -1;
			} //end if
			
			//a little bit redundant calling this method after calling it within the method I just called
			//but the other solution I've thought of to show that the format is num/dash/num is to change the return value of
			//
			else if (checkDash(issueIn))
			{
				String[] splitNums = issueIn.split("-");
				if (Integer.parseInt(splitNums[0]) < Integer.parseInt(splitNums[1])) //check if the user did it in the right order
				{
					start = Integer.parseInt(splitNums[0]);
					end = Integer.parseInt(splitNums[1]);
				} //end if
				
				else
				{
					start = Integer.parseInt(splitNums[0]);
					end = Integer.parseInt(splitNums[1]);
				} //end else
				

				issue = start; //I could also just ditch start and use issue but I like using start for comparisons
				
				int reply = JOptionPane.showConfirmDialog(null, "Are you sure you want to add issues " + start + " through " + end + "?", "Are you sure?",  JOptionPane.YES_NO_OPTION);
				if (reply == JOptionPane.NO_OPTION)
				{
					JOptionPane.showMessageDialog(null, "I'll add a comic with an issue number of the first number you inputted, then.", "Cancelled operation", JOptionPane.INFORMATION_MESSAGE);
				    start = end; //this is the condition we're using in a second anyway
				} //end if
			} //end else if
			
			else
			{
				issue = Integer.parseInt(issueIn);
			} //end else
		} //end if
		
		while (keepGoing) //if we're adding multiple issues, gotta keep going
		{
			Database.addData(id, main, series, issue, author, artist, publisher);
			
			if (start == end) //this accounts for if they haven't been changed from -1, if start has iterated enough, and if the user mistakenly sets them equal on input
			{
				keepGoing = false;
			} //end if

			start++; //sentry variable
			issue++; //issue number is the only parameter that's changing in duplicate additions due to inputting an issue range
			id++; //we added a comic so up the id number
		} //end while
		
		rs = DatabaseAccess.showTable();
		
		return rs;
	} //end addComic
} //end DatabaseAccess
