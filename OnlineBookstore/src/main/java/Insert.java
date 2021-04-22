import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

//This class inserts a hardcoded user to the Database.
//Take care executing this twice, as it will enter in a user with the exact same properties twice.
public class Insert {

    public static String addEntry(String table, String[] clientDataArray) {

        try (Connection conn = Database.dbConnection()) {

            // call the get column names method and save results in a string array
            String[] columnNames = Database.getColumnNames(table);

            //Build SQL statement
            StringBuilder insert = new StringBuilder();

            // start the query
            insert.append("INSERT INTO `online_book_shop`.`" + table + "` (");

            // for each column name in the column names array, append to the SQL statement
            // this searches all columns within the specified table
            for(String column : columnNames)
            {
                //if the column is called "id" dont append as this field auto increments
                if (column.equalsIgnoreCase("id"))
                {
                    continue;
                }
                else
                {
                    insert.append("`" + column + "`,");
                }
            }
            //delete the " ," from the final appended statement
            insert.deleteCharAt(insert.lastIndexOf(","));
            insert.append(") VALUES (");
            // for each column name in the column names array, append to the SQL statement
            // this searches all columns within the specified table
            for(String column : columnNames)
            {
                //if the column is called "id" dont append as this field auto increments
                if (column.equalsIgnoreCase("id"))
                {
                    continue;
                }
                else
                {
                    insert.append("?,");
                }
            }
            //delete the " ," from the final appended statement and add ");" to the end
            insert.deleteCharAt(insert.lastIndexOf(","));
            insert.append(");");

            // get the connection and created a prepared statement using above string
            PreparedStatement statement = conn.prepareStatement(insert.toString());

            // add the field parameters to the prepared statement (guards against SQL injection)
            // replace "_" with " " where it is found (used to indicate spaces in the same field during user entry)
            // if the first column name is "id" (auto incremented value) i < columnNames.length
            if (columnNames[0].equalsIgnoreCase("id"))
            {
                // i < columnNames.length as id column is not auto populated
                for(int i=1; i < columnNames.length; i++ )
                {
                    statement.setString(i, clientDataArray[i+1].replace("_", " "));
                }
            }
            else
            {
                for(int i=1; i <= columnNames.length; i++ )
                {
                    statement.setString(i, clientDataArray[i+1].replace("_", " "));
                }
            }

            // print the sql statement out to the server console
            System.out.println("The SQL statement is: " + insert + "\n");

            //Execute Query
            statement.execute();

            //create a string builder for saving the results in a flexible format
            StringBuilder insertCheck = new StringBuilder();

            //add first line to the response
            insertCheck.append("\n~~ '" +
                    clientDataArray[2].replace("_", " ").toUpperCase() + ", " +
                    clientDataArray[3].replace("_", " ").toUpperCase() +
                    "' ADDED TO DATABASE! DETAILS BELOW ~~");

            // call the searchBook method and append the response
            insertCheck.append(Read.searchTable(clientDataArray[0], clientDataArray[3].replace("_", " ")));

            //add final line
            insertCheck.append("~~~~~~~~~~~~~~~~~~~~");

            //search for newly entered book
            return insertCheck.toString();

        }
        catch (SQLException e)
        {
            //print error message to server console
            e.printStackTrace();

            // create a string builder for the error message to be sent to the client
            StringBuilder errorMessage = new StringBuilder();

            // add error message details to the stringbuilder
            errorMessage.append("Error, entry not added " +
                    "\nError details -" +
                    "\nSQLState: " + ((SQLException) e).getSQLState() +
                    "\nError Code: " + ((SQLException) e).getErrorCode() +
                    "\nMessage: " + e.getMessage());

            // return the error message as a string
            return errorMessage.toString();
        }
    }
}



