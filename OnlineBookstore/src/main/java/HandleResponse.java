

public class HandleResponse {

    /* SERVER RESPONSE HANDLING METHOD */
    /* TAKES THE CLIENT DATA AND RETURNS THE CORRECT RESPONSE STRING TO BE SENT TO THE CLIENT */

    public static String execute(String clientDataLine)
    {
        // convert the client string to a string array, trimming whitespace and accounting for tabs and multi-whitespace
        String[] clientDataArray = clientDataLine.trim().split("\\s+");

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
                    return Insert.addBook(clientDataArray);
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

    public static void closeConnection()  // CONCERN - connection is not currently closed as try with resources removed
    {

    }
}
