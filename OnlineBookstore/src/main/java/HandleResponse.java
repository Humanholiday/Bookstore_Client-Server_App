

public class HandleResponse {

    /* SERVER RESPONSE HANDLING METHOD */
    /* TAKES THE CLIENT DATA AND RETURNS THE CORRECT RESPONSE STRING TO BE SENT TO THE CLIENT */

    public static String execute(String clientDataLine)
    {
        // convert the client string to a string array, trimming whitespace and accounting for tabs and multi-whitespace
        String[] clientDataArray = clientDataLine.trim().split("\\s+");

        /* USER MENU SELECT ACTIONS */

        // if the client data is only 1 item, do this as it is a menu select action
        if (clientDataArray.length < 2)
        {
            // call the isInteger method and if it returns true call the menuSelect method and return result
            if (isInteger(clientDataArray[0]))
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

        // assign the first 2 items in the array to relevant variables
        String tableName = clientDataArray[0];
        String command = clientDataArray[1];

        /* DATABASE TABLE ACTIONS */

        // if the first array item is a valid table name do this
        if(Database.tableNameExists(tableName.toLowerCase()))
        {
            /* ADD AN ENTRY TO THE DATABASE */

            // if the second array item is 'add' do this
            if(command.equalsIgnoreCase("add"))
            {
                try
                {
                    // call the addBook insert method and return the returned string
                    return Create.addEntry(tableName, clientDataArray);
                }
                //if the clientData is an out of bounds integer throw this exception
                catch (Exception e)
                {
                    return "Server exception: " + e.getMessage();
                }

            }
            /* SEARCH THE DATABASE  */

            // if the second array item is 'search' do this
            else if (command.equalsIgnoreCase("search"))
            {
                try
                {
                    //create a string builder for the book title
                    StringBuilder searchTermBuilder = new StringBuilder();

                    //loop through the array from the 3rd element and join the strings,
                    // separating by a single whitespace
                    for(int i=2; i<clientDataArray.length; i++)
                    {
                        searchTermBuilder.append(clientDataArray[i] + " ");
                    }

                    String searchTerm = searchTermBuilder.toString().trim();

                    //create a string builder for saving the results in a flexible format
                    StringBuilder response = new StringBuilder();

                    //add first line to the response
                    response.append("\n~~/SEARCH RESULTS FOR '" + searchTerm.toUpperCase() +
                            "' ON TABLE '" + tableName.toUpperCase() +
                            "'/~~\n");

                    // call the searchTable method and append the response
                    response.append(Read.searchTable(tableName, searchTerm));

                    // add final line to the response
                    response.append("\n~~/END OF SEARCH/~~");

                    return response.toString();
                }
                //if the clientData is an out of bounds integer throw this exception
                catch (Exception e)
                {
                    return "Server exception: " + e.getMessage();
                }

            }
            /* BOOK COMMAND IS NOT RECOGNISED */

            else
            {
                return "No action exists for command '" + clientDataArray[1] + "' on table '" + clientDataArray[0] + "'";
            }
        }
        /* TABLE DOES NOT EXIST */

        else
        {
            return "Table '" + clientDataArray[0]  + "' does not exist, please try again";
        }
    }

    // method to check that client data is an integer - for menu select actions
    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch(NumberFormatException e) {
            return false;
        } catch(NullPointerException e) {
            return false;
        }
        // only got here if we didn't return false
        return true;
    }

    public static void closeConnection()  // CONCERN - connection is not currently closed as try with resources removed
    {

    }
}
