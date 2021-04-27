import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

//This class inserts a hardcoded user to the Database.
//Take care executing this twice, as it will enter in a user with the exact same properties twice.
public class Update {

    public static String updateEntry(String table, String[] clientDataArray) {

        try (Connection conn = Database.dbConnection()) {

            // call the get column names method and save results in a string array
            String[] columnNames = Database.getColumnNames(table);

            //Build SQL statement
            StringBuilder insert = new StringBuilder();

            // start the query
            insert.append("UPDATE `online_book_shop`.`" + table + "` SET ");

            // boolean to check that user specified column is valid
            boolean columnValid = false;

            // for each column name in the column names array, check that is is present in the table
            // this searches all columns within the specified table
                //if the column is present append to sql statement and set columnValid to true
            for(String column : columnNames)
            {
                if (column.equalsIgnoreCase(clientDataArray[2]))
                {
                    insert.append(column + " = ? " +
                            "WHERE " + clientDataArray[7] + " = ? ;");
                    columnValid = true;
                }
            }

            // if columnValid was not set to true in the for loop above, return an error statement to the client
            if(!columnValid)
            {
                return "Column name '" + clientDataArray[2] + "' not found on table " + clientDataArray[0];
            }

            // get the connection and created a prepared statement using above string
            PreparedStatement statement = conn.prepareStatement(insert.toString());

            // add the field parameters to the prepared statement (guards against SQL injection)
            // replace "_" with " " where it is found (used to indicate spaces in the same field during user entry)
            System.out.println(clientDataArray[5]);
            // new field data
            statement.setString(1, clientDataArray[5].replace("_", " "));
            // where field data
            statement.setString(2, clientDataArray[9].replace("_", " "));


            // print the sql statement out to the server console
            System.out.println("The SQL statement is: " + insert + "\n");

            //Execute Query
            statement.execute();

            //create a string builder for saving the results in a flexible format
            StringBuilder updateCheck = new StringBuilder();

            //add first line to the response
            updateCheck.append("\n~~ '" +
                    clientDataArray[2].replace("_", " ").toUpperCase() + " " +
                    clientDataArray[5].replace("_", " ").toUpperCase() +
                    "' ENTRY UPDATED! NEW DETAILS BELOW ~~");


            // call the searchBook method and append the response
            updateCheck.append(Read.searchTable(clientDataArray[0], clientDataArray[5].replace("_", " ")));

            //add final line
            updateCheck.append("~~~~~~~~~~~~~~~~~~~~");

            //search for newly entered book
            return updateCheck.toString();

        }
        catch (SQLException e)
        {
            // create a string builder for the error message to be sent to the client
            StringBuilder errorMessage = new StringBuilder();

            // add error message details to the stringbuilder
            errorMessage.append("Error, entry not added " +
                    "\nError details -" +
                    "\nSQLState: " + ((SQLException) e).getSQLState() +
                    "\nError Code: " + ((SQLException) e).getErrorCode() +
                    "\nMessage: " + e.getMessage());

            //print error message to server console
            System.out.println(errorMessage);

            // return the error message as a string
            return errorMessage.toString();
        }
    }
}