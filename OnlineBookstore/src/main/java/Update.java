import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

//THIS CLASS UPDATES EXISTING DATABASE ENTRIES

public class Update
{
    /******** CLIENT MUST SPECIFY A VALID TABLE AND VALID EXISTING COLUMN AND FIELD DATA **********/
    public static String updateEntry(String table, String[] clientDataArray)
    {

        // try with resources (resources = make the database connection)
        try (Connection conn = UtilsDatabase.dbConnection())
        {

            /********* BUILD THE SQL QUERY *********/

            // call the get column names method and save results in a string array
            String[] columnNames = UtilsDatabase.getColumnNames(table);

            //StringBuilder for SQL statement
            StringBuilder update = new StringBuilder();

            // start the query
            update.append("UPDATE `online_book_shop`.`" + table + "` SET ");

            // boolean to check that user specified column is valid
            boolean columnValid = false;

            // loop through each column name in the column names array
            //if the column name = client defined column name, append to sql statement and set columnValid to true
            for(String column : columnNames)
            {
                if (column.equalsIgnoreCase(clientDataArray[2]))
                {
                    update.append(column + " = ? " + "WHERE " + clientDataArray[7] + " = ? ;");
                    columnValid = true;
                }
            }

            // if columnValid was not set to true in the for loop above, return an error message
            if(!columnValid)
            {
                return "Column name '" + clientDataArray[2] + "' not found on table " + clientDataArray[0];
            }


            /********* ADD PARAMETERS TO THE PREPARED STATEMENT *********/

            // get the connection and create a prepared statement using the update string
            PreparedStatement statement = conn.prepareStatement(update.toString());

            // add the field parameters to the prepared statement (guards against SQL injection)
            // replace "_" with " " where it is found (used to indicate spaces in the same field during user entry)
            // 1. new field data
            statement.setString(1, clientDataArray[5].replace("_", " "));
            // 2. where field data
            statement.setString(2, clientDataArray[9].replace("_", " "));


            /********* PRINT AND EXECUTE SQL STATEMENT *********/

            // print the sql statement out to the server console
            System.out.println("The SQL statement is: " + update + "\n");

            //Execute Query
            statement.execute();


            /********* RUN A SUCCESS CHECK AND RETURN A RESPONSE TO THE CLIENT *********/

            // call the searchTable method using the original client data and save the result
            String successCheck = Read.searchTable(
                    clientDataArray[0], clientDataArray[5].replace("_", " "));

            // if the result is 'no results found' then the action was unsuccessful, reutrn an error message
            if(successCheck.equalsIgnoreCase("\n NO RESULTS FOUND \n"))
            {
                return "\n~~ ERROR'" +
                        clientDataArray[2].replace("_", " ").toUpperCase() + " " +
                        clientDataArray[5].replace("_", " ").toUpperCase() +
                        "' WAS NOT UPDATED, PLEASE TRY AGAIN ~~";
            }
            // if the result is some thing else, the result set was not empty and a success message including
            // entry details should be returned
            else
            {

                //create a string builder for saving the results in a flexible format
                StringBuilder checkResult = new StringBuilder();

                //add first line to the response
                checkResult.append("\n~~ '" +
                        clientDataArray[3].replace("_", " ").toUpperCase() + " TO " +
                        clientDataArray[5].replace("_", " ").toUpperCase() +
                        "' ENTRY UPDATED! NEW DETAILS BELOW ~~");

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