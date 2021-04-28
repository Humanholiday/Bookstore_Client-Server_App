import java.sql.SQLException;

public class Utils
{
    /***** method to return a formatted sql exception message *****/

    public static String SQLExceptionMSg (SQLException e)
    {
        // create a string builder for the error message to be sent to the client
        StringBuilder errorMessage = new StringBuilder();

        // add error message details to the stringbuilder
        errorMessage.append("An error occurred, details - " +
                "\nSQLState: " + ((SQLException) e).getSQLState() +
                "\nError Code: " + ((SQLException) e).getErrorCode() +
                "\nMessage: " + e.getMessage());

        // return the error message as a string
        return errorMessage.toString();
    }

    /***** method to check that client data is an integer - for menu select actions *****/

    public static boolean isInteger(String s)
    {
        // try to parse the data to an integer
        try
        {
            Integer.parseInt(s);
        }
        //catch exceptions if the data is not an integer format and return false
        catch(NumberFormatException e)
        {
            return false;
        }
        // catch exceptions if the data is empty and return false
        catch(NullPointerException e)
        {
            return false;
        }
        // return true if no exceptions were thrown
        return true;
    }

}
