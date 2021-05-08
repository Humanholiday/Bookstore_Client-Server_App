import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

//THIS CLASS CONTAINS DATABASE UTILITY METHODS

public class UtilsDatabase
{

    /* GET THE STORED DATABASE PROPERTIES FROM THE DB.PROPERTIES FILE */

    public static Properties dbProps()
    {
        //try with resources (resources = get ~InputStream using file specified props
        try (InputStream input = new FileInputStream("src/main/resources/db.properties"))
        {

            // create a properties object
            Properties prop = new Properties();

            // load a properties file
            prop.load(input);

            // return the properties
            return prop;

        }
        // catch exceptions and print stack trace to server console
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
        return null;
    }


    /* MAKE THE DATABASE CONNECTION AND RETURN  */

    public static Connection dbConnection() throws SQLException
    {
        //Get the properties stored in the db.properties file
        Properties prop = dbProps();

        //make the connection using the properties
        Connection conn = DriverManager.getConnection(
                prop.getProperty("db.driver"),
                prop.getProperty("db.user"),
                prop.getProperty("db.password"));   // For MySQL only
        // The format is: "jdbc:mysql://hostname:port/databaseName", "username", "password"

        //return the statement
        return conn;
    }


    /* GET AN ARRAYLIST OF ALL DATABASE TABLE NAMES*/

    public static ArrayList getDatabaseTableNames()
    {
        try
        {
            // connect to the database and get its metaData
            DatabaseMetaData dbmd = UtilsDatabase.dbConnection().getMetaData();

            // define the type of table required, in ths case {"TABLE"} accesses the high level table names only
            String[] types = {"TABLE"};

            // extract the table names into a ResultSet
            ResultSet rs = dbmd.getTables(null, null, "%", types);

            // initialise and ArrayList to store the table names
            ArrayList tableNames  = new ArrayList();

            //while the ResultSet contains values, add these values to the ArrayList
            while (rs.next())
            {
                tableNames.add(rs.getString("TABLE_NAME"));
            }

            //return the ArrayList of table names
            return tableNames;
        }
        // catch exceptions and print a formatted error message
        catch (SQLException e)
        {
            // get a formatted error message from the sql exception utility method
            String msg = Utils.SQLExceptionMSg(e);

            //print message to console
            System.out.println(msg);
        }
        return null;
    }


    /* GET ALL COLUMN NAMES FROM A SPECIFIED TABLE*/

     public static String[] getColumnNames(String tableName) throws SQLException
     {

         // prepare SQL query
         String query1 = "SELECT * FROM " + tableName;

         // get the connection and created a prepared statement using above string
         PreparedStatement statement1 = dbConnection().prepareStatement(query1);

         //Execute Query and save results
         ResultSet resultSet = statement1.executeQuery();

         // get the meta data from the result set
         ResultSetMetaData metaData = resultSet.getMetaData();

         // count the number of columns deifned in the meta data
         int count = metaData.getColumnCount();

         // create a String array with the same length as the number of columns
         String columnNames[] = new String[count];

         // add the column names to the array
         for (int i = 1; i <= count; i++)
         {
             columnNames[i-1] = metaData.getColumnLabel(i);
         }

         //return the String array of column names
         return columnNames;
     }


    /* CHECK TO SEE IF THE DATABASE CONTAINS A CLIENT SPECIFIED TABLE NAME */

    public static boolean tableNameExists(String tableName)
    {
        // get the ArrayList of all current high level table names
        ArrayList tableNames = getDatabaseTableNames();

        // if the Arraylist contains the client specified table name, return true. If it does not return false
        if (tableNames.contains(tableName))
        {
            return true;
        }
        else
        {
            return false;
        }
    }


    /*  CHECK THAT USER SUBMITTED FIELDS EXIST AND ARE VALID IN THE DATABASE BEFORE PROCEEDING WITH AN UPDATE/DELETE */

    // this provides protection against injecting * into the statement and limits the amount of records to be updated
    public static boolean updateDeleteDataCheck(String table, String column, String field) throws SQLException
    {
        try
        {
            // save the query to a string variable
            String query = "SELECT * FROM " + table + " WHERE " + column + " = ?;";

            // get the connection and created a prepared statement using above string
            PreparedStatement statement = UtilsDatabase.dbConnection().prepareStatement(query);

            // set the field parameter - prepared statement guards against sql injection
            statement.setString(1, field.trim().replace("_", " "));

            //Execute Query and save results
            ResultSet rs = statement.executeQuery();

            // if the result set is empty return false as there is no database entry, if it is not empty return true
            return rs.isBeforeFirst();
        }

        //if an exception is thrown return false
        catch(SQLException e)
        {
            return false;
        }
    }
}



