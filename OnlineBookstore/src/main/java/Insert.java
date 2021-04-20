import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

//This class inserts a hardcoded user to the Database.
//Take care executing this twice, as it will enter in a user with the exact same properties twice.
public class Insert {

    public static String addBook(String[] clientDataArray) {

        try (Connection conn = Database.dbConnection()) {
            //Build SQL statement
            String insert = "INSERT INTO `online_book_shop`.`book`" +
                    "(`ISBN`," +
                    "`title`," +
                    "`author`," +
                    "`publisher`," +
                    "`language`," +
                    "`stock`," +
                    "`price`)" +
                    "VALUES" +
                    "(?,?,?,?,?,?,?);";

            // get the connection and created a prepared statement using above string
            PreparedStatement statement = Database.dbConnection().prepareStatement(insert);

            // add the name parameter to the prepared statement (guards against SQL injection)
            // replace "_" with " " in relevant fields
            statement.setString(1, clientDataArray[2]);
            statement.setString(2, clientDataArray[3].replace("_", " "));
            statement.setString(3, clientDataArray[4].replace("_", " "));
            statement.setString(4, clientDataArray[5].replace("_", " "));
            statement.setString(5, clientDataArray[6].replace("_", " "));
            statement.setString(6, clientDataArray[7]);
            statement.setString(7, clientDataArray[8]);

//            System.out.println("The SQL statement is: " + insert + "\n");

            //Execute Query
            statement.execute();

            //create a string builder for saving the results in a flexible format
            StringBuilder insertCheck = new StringBuilder();

            //add first line to the response
            insertCheck.append("\n~~ '" +
                    clientDataArray[3].replace("_", " ").toUpperCase() +
                    "' ADDED TO DATABASE! DETAILS BELOW ~~\n");

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
            errorMessage.append("Error, book not added " +
                    "\nError details -" +
                    "\nSQLState: " + ((SQLException) e).getSQLState() +
                    "\nError Code: " + ((SQLException) e).getErrorCode() +
                    "\nMessage: " + e.getMessage());

            // add a cause filed if it is not null
            Throwable t = e.getCause();
            while (t != null)
            {
                errorMessage.append("\nCause: " + t.toString());
            }

            // return the error message as a string
            return errorMessage.toString();
        }
    }
}



