import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Delete
{
    public static String deleteEntry(String table, String[] clientDataArray) {

        try (Connection conn = Database.dbConnection())
        {

            // call the get column names method and save results in a string array
            String[] columnNames = Database.getColumnNames(table);

            //Build SQL statement
            StringBuilder delete = new StringBuilder();

            // start the query
            delete.append("DELETE FROM `online_book_shop`.`" + table + "` ");

            // boolean to check that user specified column is valid
            boolean columnValid = false;

            // for each column name in the column names array, check that is is present in the table
            // this searches all columns within the specified table
            //if the column is present append to sql statement and set columnValid to true
            for(String column : columnNames)
            {
                if (column.equalsIgnoreCase(clientDataArray[3]))
                {
                    delete.append("WHERE " + column + " = ? ;");
                    columnValid = true;
                }
            }

            // if columnValid was not set to true in the for loop above, return an error statement to the client
            if(!columnValid)
            {
                return "Column name '" + clientDataArray[3] + "' not found on table " + clientDataArray[0];
            }

            // get the connection and created a prepared statement using above string
            PreparedStatement statement = conn.prepareStatement(delete.toString());

            // add the field parameters to the prepared statement (guards against SQL injection)
            // replace "_" with " " where it is found (used to indicate spaces in the same field during user entry)
            // new field data
            statement.setString(1, clientDataArray[5].replace("_", " "));

            // print the sql statement out to the server console
            System.out.println("The SQL statement is: " + delete + "\n");

            //Execute Query
            statement.execute();

            //create a string builder for saving the results in a flexible format
            StringBuilder deleteCheck = new StringBuilder();

            //add first line to the response
            deleteCheck.append("\n~~ '" +
                    clientDataArray[3].replace("_", " ").toUpperCase() + " " +
                    clientDataArray[5].replace("_", " ").toUpperCase() +
                    "' ENTRY DELETED SEARCH CHECK BELOW SHOULD RETURN NO RESULTS ~~");


            // call the searchBook method and append the response
            deleteCheck.append(Read.searchTable(clientDataArray[0], clientDataArray[5].replace("_", " ")));

            //add final line
            deleteCheck.append("~~~~~~~~~~~~~~~~~~~~");

            //search for newly entered book
            return deleteCheck.toString();

        }
        catch (SQLException e)
        {
            // get a formatted error message from the sql exception utility method
            String msg = Utils.SQLExceptionMSg(e);

            //print message to console
            System.out.println(msg);

            //return message to client
            return msg;
        }
    }
}
