import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager; //connect to the driver
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JButton;

import net.proteanit.sql.DbUtils;

   public class Database
   {
	   //my db parameters
	   private static String TABLE_NAME = "Comics";
	   
	   private static String COL_ID = "ID";
	   private static String COL_MAIN = "Main";
	   private static String COL_SERIES = "Series";
	   private static String COL_ISSUE = "Issue";
	   private static String COL_AUTHOR = "Author";
	   private static String COL_ARTIST = "Artist";
	   private static String COL_PUBLISHER = "Publisher";
	   
	   public static void main( String args[] )
	   {
		   
	   } //end main
	   
	   //database constructor
	   //in the tutorial for jtable, dude has all of the try connection stuff in a separater .java file. 
	   //probably a good idea to do so, maybe consider altering this in the future
	   public Database()
	   {
		   
	   } //end constructor
	   
	   public static void createDB()
	   {
		   Connection c = null; //null so that we have something to close at the end
		   Statement stmt = null; //sends statements to the database
		   try 
		   {
			   Class.forName("org.sqlite.JDBC"); //executes the static code in JDBC I think? Pretty standard for connecting to the jdbc driver
			   c = sqliteConnection.dbConnector();
			   c.setAutoCommit(false); //make it so each sql command isn't its own transaction so I can execute multiple in a row
			   stmt = c.createStatement();
			   String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + //create table if not exists just so we're not making it over and over again
					   "(" + COL_ID + "  INT PRIMARY KEY NOT NULL," +
					   COL_MAIN + "      TEXT," +
					   COL_SERIES + "    TEXT," +
					   COL_ISSUE + "     INT," +
					   COL_AUTHOR + "    TEXT," +
					   COL_ARTIST + "    TEXT," +
					   COL_PUBLISHER + " TEXT)";
			   stmt.executeUpdate(sql); //create that table
			   
			   c.commit();
		   } //end try
		   
		   catch (Exception e)
		   {
			   System.out.println("Error creating the database");
			   System.err.println(e.getClass().getName() + ": " + e.getMessage());
			   System.exit(0); //what is this????
		   } //end catch

		   finally
		   {
			   try
			   {
				   stmt.close();
				   c.close();
			   } //end try
			   
			   catch (Exception e)
			   {
				   System.out.println("Failed to close the database");
			   } //end catch
		   } //end finally
	   } //end createDB
	   
	   //I am making this boolean so I can return true if the addition happens successfully, false otherwise
	   public static Boolean addData(int id, String main, String series, int issue, String author, String artist, String publisher)
	   {
		   Connection c = null; 
		   Statement stmt = null; //sends statements to the database
		   try 
		   {
			   Class.forName("org.sqlite.JDBC"); //executes the static code in JDBC I think? Pretty standard for connecting to the jdbc driver
			   c = sqliteConnection.dbConnector();
			   c.setAutoCommit(false); //make it so each sql command isn't its own transaction so I can execute multiple in a row
			   
			   //add the user's input into the table
			   
			   stmt = c.createStatement();
			   String sql = "INSERT INTO " + TABLE_NAME + " (" + COL_ID + "," + COL_MAIN + "," +
					 COL_SERIES + "," + COL_ISSUE + "," + COL_AUTHOR + "," + COL_ARTIST + 
					 "," + COL_PUBLISHER + ") " + 
					 "VALUES ('" + id + "', '" + main + "', '" + series + "', '" + issue + "', '" + author + "', '" + artist + "', '" + publisher + "' );";
			   stmt.executeUpdate(sql);
			   
			   c.commit();
		   } //end try
		   
		   catch (Exception e)
		   {
			   System.err.println(e.getClass().getName() + ": " + e.getMessage());
			   System.exit(0); //what is this????
		   } //end catch

		   //this finally block is at the end of each database operation as it allows you to close the database properly
		   finally
		   {
			   try
			   {
				   stmt.close(); 
				   c.close();
			   } //end try
			   
			   catch (Exception e)
			   {
				   System.out.println("Failed to close the database");
			   } //end catch
		   } //end finally
		   
		   return true;
	   } //end addData
	   
	   public static Boolean deleteData(int choice)
	   {
		   Connection c = null; 
		   Statement stmt = null; //sends statements to the database
		   try 
		   {
			   Class.forName("org.sqlite.JDBC");
			   c = sqliteConnection.dbConnector();
			   c.setAutoCommit(false); 
			   
			   stmt = c.createStatement();
			   stmt.executeUpdate("DELETE FROM " + TABLE_NAME + " WHERE " + COL_ID + "=" + choice + ";");
			   c.commit();
		   } //end try
		   
		   catch (Exception e)
		   {
			   System.err.println(e.getClass().getName() + ": " + e.getMessage());
			   System.exit(0);
		   } //end catch
		   
		   finally
		   {
			   try
			   {
				   stmt.close();
				   c.close();
			   } //end try
			   
			   catch (Exception e)
			   {
				   System.out.println("Failed to close the database");
			   } //end catch
		   } //end finally
		   
		   return true;
	   } //end deleteData
	   
	   //allows for sorting through the database
	   //I have not looked at various standard sqlite sorting methods: however,
	   //I COULD make it so that every kind of data type for the table is passed in, and if the user chooses to sort
	   //by, say, author and publisher, just set every other column variable to null and check what's null in the beginning of the function
	   public static Boolean selectData(int choice)
	   {
		   Connection c = null; 
		   Statement stmt = null; //sends statements to the database
		   try 
		   {
			   Class.forName("org.sqlite.JDBC");
			   c = sqliteConnection.dbConnector();
			   c.setAutoCommit(false); 
			   stmt = c.createStatement();
			   
			   ResultSet rs = stmt.executeQuery( "SELECT * FROM " + TABLE_NAME + ";" );
			   while (rs.next())
			   {
				   int id = rs.getInt("id");
				   if (id == choice)
				   {
					   System.out.println(rs.getString(COL_MAIN));
				   } //end if
			   } //end while
			   
			   c.commit();
		   } //end try
		   
		   catch (Exception e)
		   {
			   System.err.println(e.getClass().getName() + ": " + e.getMessage());
			   System.exit(0);
		   } //end catch
		   
		   finally
		   {
			   try
			   {
				   stmt.close();
				   c.close();
			   } //end try
			   
			   catch (Exception e)
			   {
				   System.out.println("Failed to close the database");
			   } //end catch
		   } //end finally
		   return true;
	   } //end selectData

	   public static Boolean updateData(String col, String change, int id)
	   {
		   Connection c = null; 
		   Statement stmt = null; //sends statements to the database
		   
		   try 
		   {
			   Class.forName("org.sqlite.JDBC");
			   c = sqliteConnection.dbConnector();
			   c.setAutoCommit(false); 
			   stmt = c.createStatement();
			   
			   String sql = "UPDATE " + TABLE_NAME + " SET " + col + " = '" + change + "' WHERE " + COL_ID + "=" + id + ";";
			   stmt.executeUpdate(sql);
			   
			   c.commit();
		   } //end try

		   catch (Exception e)
		   {
			   System.err.println(e.getClass().getName() + ": " + e.getMessage());
			   System.exit(0);
		   } //end catch
		   
		   finally
		   {
			   try
			   {
				   stmt.close();
				   c.close();
			   } //end try
			   
			   catch (Exception e)
			   {
				   System.out.println("Failed to close the database");
			   } //end catch
		   } //end finally
		   
		   return true;
	   } //end updateData
	   
	   
	   //getters for table name stuff
	   public static String getTableName()
	   {
		   return TABLE_NAME;
	   } //end getter
	   
	   public static String getColId()
	   {
		   return COL_ID;
	   } //end getter
	   
	   public static String getColMain()
	   {
		   return COL_MAIN;
	   } //end getter
	   
	   public static String getColSeries()
	   {
		   return COL_SERIES;
	   } //end getter

	   public static String getColIssue()
	   {
		   return COL_ISSUE;
	   } //end getter
	   
	   public static String getColAuthor()
	   {
		   return COL_AUTHOR;
	   } //end getter

	   public static String getColArtist()
	   {
		   return COL_ARTIST;
	   } //end getter

	   public static String getColPublisher()
	   {
		   return COL_PUBLISHER;
	   } //end getter
   } //end Database