import java.sql.SQLException;

public class HandleResponse {

    /* SERVER RESPONSE HANDLING METHOD */
    /* TAKES THE CLIENT DATA AND RETURNS THE CORRECT RESPONSE STRING TO BE SENT TO THE CLIENT */

    public static String execute(String clientDataLine) throws SQLException
    {

        // convert the client string to a string array, trimming whitespace and accounting for tabs and multi-whitespace
        String[] clientDataArray = clientDataLine.trim().split("\\s+");


        /********* USER MENU SELECT ACTIONS *********/

        // if the client data is only 1 item, treat it as a menu select action
        if (clientDataArray.length < 2)
        {
            // call the utility isInteger method and if it returns true call the menuSelect method and return result
            if (Utils.isInteger(clientDataArray[0]))
            {
                return Menu.menuSelect(Integer.parseInt(clientDataArray[0]));
            }
            // if the String equals 'return' then go back to the main menu
            else if (clientDataArray[0].equalsIgnoreCase("return"))
            {
                //send string to client to confirm connection
                return Menu.menu();
            }
            // for all commands that arent an integer or 'return' send an error message
            else
            {
                return "User command not recognised";
            }
        }

        // assign the first 2 items in the array to instruction variables
        String tableName = clientDataArray[0];
        String command = clientDataArray[1];


        /********* DATABASE TABLE ACTIONS *********/


        // check that the tablename is valid
        if(Database.tableNameExists(tableName.toLowerCase()))
        {

            /* ------------ ADD AN ENTRY TO THE DATABASE ------------ */

            // if the command variable is 'add' do this
            if(command.equalsIgnoreCase("add"))
            {
                try
                {
                    // call the addEntry insert method and return the result
                    return Create.addEntry(tableName, clientDataArray);
                }
                //catch exceptions and return the error
                catch (Exception e)
                {
                    return "Error - addition not processed: " + e.getMessage();
                }
            }


            /* ------------ SEARCH THE DATABASE ------------  */

            // if the second array item is 'search' do this
            else if (command.equalsIgnoreCase("search"))
            {
                try
                {
                    //create a string builder for the book title search term
                    StringBuilder searchTermBuilder = new StringBuilder();

                    //loop through the array from the 3rd element and join the strings,
                    // separating by a single whitespace
                    for(int i=2; i<clientDataArray.length; i++)
                    {
                        searchTermBuilder.append(clientDataArray[i] + " ");
                    }

                    // convert StringBuilder to a string and trim whitespace from the end
                    String searchTerm = searchTermBuilder.toString().trim();

                    //create a string builder for saving the returned result in a flexible format
                    StringBuilder response = new StringBuilder();

                    //add first line to the response
                    response.append("\n~~/SEARCH RESULTS FOR '" + searchTerm.toUpperCase() +
                            "' ON TABLE '" + tableName.toUpperCase() +
                            "'/~~\n");

                    // call the searchTable method and append the result
                    response.append(Read.searchTable(tableName, searchTerm));

                    // add final line to the response
                    response.append("\n~~/END OF SEARCH/~~");

                    // convert response to a string and return the result
                    return response.toString();
                }
                //catch exceptions and return the error
                catch (Exception e)
                {
                    return "Error - search not processed: " + e.getMessage();
                }

            }


            /* ------------ UPDATE A DATABASE ENTRY ------------ */

            // if the second array item is 'update' do this
            else if(command.equalsIgnoreCase("update"))
            {
                try
                {
                    /* Check that user submitted fields exist and are valid in the database
                    before proceeding with an update/delete.
                    This provides protection against injecting * into the statement
                    and limits the amount of records to be updated */
                    if (Database.updateDeleteDataCheck(clientDataArray[0], clientDataArray[2], clientDataArray[3])
                        && Database.updateDeleteDataCheck(clientDataArray[0], clientDataArray[7], clientDataArray[9]))
                    {
                        try
                        {
                            // call the updateEntry method and return the result
                            return Update.updateEntry(tableName, clientDataArray);
                        }
                        //catch exceptions and return the error
                        catch (Exception e)
                        {
                            return "Error - update not processed: " + e.getMessage();
                        }
                    }
                    // if the data check fails return an error message
                    else
                    {
                        return "Error, " + clientDataArray[2] + " = " + clientDataArray[3] +
                                " , " + clientDataArray[7] + " = " + clientDataArray[9] + " does not exist " +
                                "on table '" + clientDataArray[0] + "'";
                    }
                }
                //catch exceptions and return the error
                catch (Exception e)
                {
                    return "Error - update not processed: " + e.getMessage();
                }

            }

            /* ------------ DELETE A DATABASE ENTRY ------------ */

            //book delete where title = the_count_of_monte_cristo

            // if the second array item is 'delete' do this
            else if(command.equalsIgnoreCase("delete"))
            {
                try
                {
                    /* Check that user submitted fields exist and are valid in the database
                    before proceeding with an update/delete.
                    This provides protection against injecting * into the statement
                    and limits the amount of records to be updated */
                    if(Database.updateDeleteDataCheck(clientDataArray[0], clientDataArray[3], clientDataArray[5]))
                    {
                        try
                        {
                            // call the deleteEntry method and return the result
                            return Delete.deleteEntry(tableName, clientDataArray);
                        }
                        //catch exceptions and return the error
                        catch (Exception e)
                        {
                            return "Error - delete not processed: " + e.getMessage();
                        }
                    }
                    // if the data check fails return an error message
                    else
                    {
                        return "Error, '" + clientDataArray[3] + " = " + clientDataArray[5] + "' does not exist " +
                                "on table '" + clientDataArray[0] + "'";
                    }
                }
                //catch exceptions and return the error
                catch (Exception e)
                {
                    return "Error - delete not processed: " + e.getMessage();
                }
            }


            /* ------------ BOOK COMMAND IS NOT RECOGNISED ------------ */

            // if the command does not exist return an error message
            else
            {
                return "No action exists for command '" + clientDataArray[1] + "' on table '" + clientDataArray[0] + "'";
            }
        }


        /* TABLE DOES NOT EXIST */

        // if the table does not exist return an error message
        else
        {
            return "Table '" + clientDataArray[0]  + "' does not exist, please try again";
        }
    }
}
