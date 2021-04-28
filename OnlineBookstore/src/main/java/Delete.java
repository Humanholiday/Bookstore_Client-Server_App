import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

//THIS CLASS DELETES EXISTING DATABASE ENTRIES

public class Delete
{
    /******** DELETE AN ENTRY **********/


    /******** CLIENT MUST SPECIFY A VALID TABLE AND VALID EXISTING COLUMN AND FIELD DATA **********/

    public static String deleteEntry(String table, String[] clientDataArray)
    {

        // try with resources (resources = make the database connection)
        try (Connection conn = Database.dbConnection())
        {

            /********* BUILD THE SQL QUERY *********/

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


            /********* ADD PARAMETERS TO THE PREPARED STATEMENT *********/

            // get the connection and created a prepared statement using above string
            PreparedStatement statement = conn.prepareStatement(delete.toString());

            // add the field parameters to the prepared statement (guards against SQL injection)
            // replace "_" with " " where it is found (used to indicate spaces in the same field during user entry)
            // new field data
            statement.setString(1, clientDataArray[5].replace("_", " "));


            /********* PRINT AND EXECUTE SQL STATEMENT *********/

            // print the sql statement out to the server console
            System.out.println("The SQL statement is: " + delete + "\n");

            //Execute Query
            statement.execute();


            /********* RUN A SUCCESS CHECK *********/

            // call the searchTable method using the original client data and save the result
            String successCheck = Read.searchTable(
                    clientDataArray[0], clientDataArray[5].replace("_", " "));

            // if the result is 'no results found' then the action was successful, return a success message
            if(successCheck.equalsIgnoreCase("\n NO RESULTS FOUND \n"))
            {

                //return the result
                return "\n~~ SUCCESS! '" +
                        clientDataArray[3].replace("_", " ").toUpperCase() + " " +
                        clientDataArray[5].replace("_", " ").toUpperCase() +
                        "' WAS REMOVED FROM THE DATABASE ~~";
            }
            // if the result is some thing else, the result set was not empty which indicates the entry as not deleted
            // return an error message including the search result for debugging
            else
            {
                //create a string builder for saving the results in a flexible format
                StringBuilder checkResult = new StringBuilder();

                //add first line to the response
                checkResult.append("\n~~ ERROR A SEARCH FOR '" +
                        clientDataArray[3].replace("_", " ").toUpperCase() + " " +
                        clientDataArray[5].replace("_", " ").toUpperCase() +
                        "' RETURNED THE BELOW RESULT ~~");

                // append the result of the searchTable method
                checkResult.append(successCheck);

                // add a final line
                checkResult.append("\n~~ THIS INDICATES THE ENTRY WAS NOT DELETED, PLEASE TRY AGAIN ~~");

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
