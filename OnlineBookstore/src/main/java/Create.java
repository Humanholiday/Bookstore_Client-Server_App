import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

//THIS CLASS ADDS NEW ENTRIES TO THE DATABASE

public class Create
{
    /******** ADD A NEW ENTRY **********/

    /******** CLIENT MUST SPECIFY A VALID TABLE AND VALID COLUMN AND FIELD DATA **********/

    public static String addEntry(String table, String[] clientDataArray)
    {
        // try with resources (resources = make the database connection)
        try (Connection conn = Database.dbConnection())
        {

            /********* BUILD THE SQL QUERY *********/

            // call the get column names method and save results in a string array
            String[] columnNames = Database.getColumnNames(table);

            //Build SQL statement
            StringBuilder insert = new StringBuilder();

            // start the query
            insert.append("INSERT INTO `online_book_shop`.`" + table + "` (");

            // for each column name in the columnNames array, append to the SQL statement
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

            // for each column name in the columnNames array, append '?,' to the insert statement
            // the ? is used by the PreparedStatement to insert field variables
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


            /********* ADD PARAMETERS TO THE PREPARED STATEMENT *********/

            // get the connection and created a prepared statement using the insert string
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
                // i <= columnNames.length as there is no auto incremented id column on this table
                for(int i=1; i <= columnNames.length; i++ )
                {
                    statement.setString(i, clientDataArray[i+1].replace("_", " "));
                }
            }

            /********* PRINT AND EXECUTE SQL STATEMENT *********/

            // print the sql statement out to the server console
            System.out.println("The SQL statement is: " + insert + "\n");

            //Execute the statement
            statement.execute();


            /********* RUN A SUCCESS CHECK *********/

            // call the searchTable method using the original client data and save the result
            String successCheck = Read.searchTable(
                    clientDataArray[0], clientDataArray[3].replace("_", " "));

            // if the result is 'no results found' then the action was unsuccessful, reutrn an error message
            if(successCheck.equalsIgnoreCase("\n NO RESULTS FOUND \n"))
            {
                return "\n~~ ERROR'" +
                        clientDataArray[2].replace("_", " ").toUpperCase() + " " +
                        clientDataArray[3].replace("_", " ").toUpperCase() +
                        "' WAS NOT ADDED TO THE DATABASE, PLEASE TRY AGAIN ~~";
            }
            // if the result is some thing else, the result set was not empty and a success message including
            // entry details should be returned
            else
                {

                //create a string builder for saving the results in a flexible format
                StringBuilder checkResult = new StringBuilder();

                //add first line to the response
                checkResult.append("\n~~ '" +
                        clientDataArray[2].replace("_", " ").toUpperCase() + " " +
                        clientDataArray[3].replace("_", " ").toUpperCase() +
                        "' ADDED TO DATABASE! DETAILS BELOW ~~");

                // append the result of the searchTable method
                checkResult.append(successCheck);

                //add final line
                checkResult.append("~~~~~~~~~~~~~~~~~~~~");

                //return as a string
                return checkResult.toString();
            }

        }
        // catch exceptions and return a formatted error message
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



