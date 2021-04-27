import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

//This class queries the database and returns results
public class Read {

    /******** SEARCH THE DATABASE **********/

    /******** SEARCH A CLIENT SPECIFIED TABLE FOR A CLIENT SPECIFIED TERM **********/

    public static String searchTable(String table, String searchTerm) {

        try
        {
            // call the get column names method and save results in a string array
            String[] columnNames = Database.getColumnNames(table);

            //Build SQL statement
            StringBuilder query = new StringBuilder();

            // start the query
            query.append("SELECT * FROM " + table + " WHERE");

            // for each column name in the column names array, append to the SQL statement
            // this searches all columns within the specified table
            for(String column : columnNames)
            {
                    query.append(" " + column + " LIKE ? OR");
            }
            //delete the " OR" from the final appended statement and add ";" to the end
            query.delete(query.lastIndexOf(" "), query.lastIndexOf("R"));
            query.deleteCharAt(query.lastIndexOf("R"));
            query.append(";");


            // get the connection and created a prepared statement using above string
            PreparedStatement statement = Database.dbConnection().prepareStatement(query.toString());


            // add the search parameter to the prepared statement
            // (guards against SQL injection)
            for(int i=0; i < columnNames.length; i++ )
            {
                statement.setString(i+1, searchTerm);
            }

            // Print the statement to the console
            System.out.println(statement);

            //Execute Query and save results
            ResultSet rs = statement.executeQuery();

            //create a string builder for saving the results in a flexible format
            StringBuilder response = new StringBuilder();

            // if there are no results, add this message
            if (!rs.isBeforeFirst() )
            {
            response.append("\n NO RESULTS FOUND \n");
            }
            else
            {
                int number = 1;
                // while the result set contains data, save it to variables and insert into the response string
                while (rs.next())
                {
                    response.append("\n" + number + ".\n");

                    // for each column name in the column names array, append to the SQL statement
                    for(String column : columnNames)
                    {
                        String value = rs.getString(table + "." + column);
                        response.append(column + " - " + value + "\n");
                    }

                    number++;
                }
            }

            //convert the stringbuilder to a string and return
            return response.toString();
        }
        catch(SQLException e)
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
